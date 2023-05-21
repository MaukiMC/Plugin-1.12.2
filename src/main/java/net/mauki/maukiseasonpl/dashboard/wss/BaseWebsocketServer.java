package net.mauki.maukiseasonpl.dashboard.wss;

import net.mauki.maukiseasonpl.caches.handler.Cache;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.AuthenticateWSS;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.OnlinePlayersWSS;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSAPIEndpoint;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSRequestInformation;
import net.mauki.maukiseasonpl.entities.InternalPlayer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Management of the entire API
 */
public class BaseWebsocketServer extends WebSocketServer {

    private final int port;
    private final HashMap<String, WSSAPIEndpoint> endpoints = new HashMap<>();

    private final Collection<String> authentication_required = new ArrayList<>();
    private final Cache<String, InternalPlayer> sessions = new Cache<>();

    public BaseWebsocketServer(String host, int port) {
        super(new InetSocketAddress(host, port));
        this.port = port;

        endpoints.put("authenticate", new AuthenticateWSS());
        endpoints.put("online-players", new OnlinePlayersWSS());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String ip = conn.getRemoteSocketAddress().getHostName();
        Boot.getLOGGER().info(ip + " established a wss-connection");
        conn.send(new JSONObject()
                .put("code", 401)
                .put("message", "Authentication required").toString());
        authentication_required.add(ip);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        JSONObject request;
        try {
            request = new JSONObject(message);
        } catch(Exception ex) {
            conn.send(new JSONObject()
                    .put("code", 400)
                    .put("message", "Invalid syntax").toString());
            return;
        }
        if(!request.has("action")) {
            conn.send(new JSONObject()
                    .put("code", 400)
                    .put("message", "Invalid syntax").toString());
            return;
        }
        String action = request.getString("action");
        WSSAPIEndpoint endpoint;
        if((endpoint = endpoints.getOrDefault(action, null)) == null) {
            conn.send(new JSONObject()
                    .put("code", 400)
                    .put("message", "Invalid action").toString());
            return;
        }
        if(authentication_required.contains(conn.getRemoteSocketAddress().getHostName()) && !action.equalsIgnoreCase("authenticate")) {
            conn.send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Authentication required").toString());
            return;
        }
        request.remove("action");
        if(!WSSAPIEndpoint.JSONStructure.validate(new WSSAPIEndpoint.JSONStructure(request), endpoint.structure())) {
            conn.send(new JSONObject()
                    .put("code", 400)
                    .put("message", "Invalid syntax").toString());
            return;
        }
        try {
            endpoint.handle(new WSSRequestInformation() {
                @Override
                public JSONObject getRequestBody() {
                    return request;
                }

                @Override
                public WebSocket getWebsocket() {
                    return conn;
                }
            });
        } catch(Exception ex) {
            conn.send(new JSONObject()
                    .put("code", 500)
                    .put("message", "Internal Server Error").toString());
            ex.printStackTrace();
        }

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onStart() {
        Boot.getLOGGER().info("Websocket started with port" + this.port);
    }

    public Cache<String, InternalPlayer> getSessions() {
        return sessions;
    }

    public Collection<String> getAuthenticationRequired() {
        return authentication_required;
    }

    public HashMap<String, WSSAPIEndpoint> getEndpoints() {
        return endpoints;
    }

    @Override
    public int getPort() {
        return port;
    }
}

package net.mauki.maukiseasonpl.dashboard.wss.endpoints;

import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSAPIEndpoint;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSRequestInformation;
import net.mauki.maukiseasonpl.entities.InternalPlayer;
import org.json.JSONObject;
import org.json.simple.JSONArray;

/**
 * WSS-Endpoint for authentication with the API
 */
public class AuthenticateWSS implements WSSAPIEndpoint {

    /**
     * The code that will execute when the endpoint is called
     * @param wssRequestInformation The information of the request
     * @throws Exception Will be thrown when there was an error
     */
    @Override
    public void handle(WSSRequestInformation wssRequestInformation) throws Exception {
        if(!Boot.getWss().getAuthenticationRequired().contains(wssRequestInformation.getWebsocket().getRemoteSocketAddress().getHostName())) {
            wssRequestInformation.getWebsocket().send(new JSONObject()
                    .put("code", 400)
                    .put("message", "Bad request").toString());
            return;
        }
        String access_token = wssRequestInformation.getRequestBody().getString("access_token");
        InternalPlayer internalPlayer = InternalPlayer.getFromAccessToken(access_token);
        if(internalPlayer == null) {
            wssRequestInformation.getWebsocket().send(new JSONObject()
                    .put("code", 401)
                    .put("message", "Invalid Token").toString());
        }
        Caches.internalPlayerCache.addOrUpdate(internalPlayer.getPlayer().getUniqueId(), internalPlayer);
        Boot.getWss().getAuthenticationRequired().remove(wssRequestInformation.getWebsocket().getRemoteSocketAddress().getHostName());
        Boot.getWss().getSessions().addOrUpdate(wssRequestInformation.getWebsocket().getRemoteSocketAddress().getHostName(), internalPlayer);
        wssRequestInformation.getWebsocket().send(new JSONObject()
                .put("code", 200)
                .put("message", "Access granted")
                .put("requester", internalPlayer.toJSON()).toString());
    }

    /**
     * The structure of the request body
     * @return The {@link JSONStructure} object
     */
    @Override
    public JSONStructure structure() {
        return new JSONStructure()
                .put("access_token", String.class);
    }
}

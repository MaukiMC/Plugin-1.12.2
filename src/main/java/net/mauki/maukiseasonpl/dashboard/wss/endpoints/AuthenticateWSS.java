package net.mauki.maukiseasonpl.dashboard.wss.endpoints;

import net.mauki.maukiseasonpl.caches.Caches;
import net.mauki.maukiseasonpl.core.Boot;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSAPIEndpoint;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSRequestInformation;
import net.mauki.maukiseasonpl.entities.InternalPlayer;
import org.json.JSONObject;

public class AuthenticateWSS implements WSSAPIEndpoint {

    @Override
    public void handle(WSSRequestInformation wssRequestInformation) throws Exception {
        if(!Boot.getWSS().getAuthenticationRequired().contains(wssRequestInformation.getWebsocket().getRemoteSocketAddress().getHostName())) {
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
        Boot.getWSS().getAuthenticationRequired().remove(wssRequestInformation.getWebsocket().getRemoteSocketAddress().getHostName());
        Boot.getWSS().getSessions().addOrUpdate(wssRequestInformation.getWebsocket().getRemoteSocketAddress().getHostName(), internalPlayer);
    }

    @Override
    public JSONStructure structure() {
        return new JSONStructure()
                .put("access_token", String.class);
    }
}
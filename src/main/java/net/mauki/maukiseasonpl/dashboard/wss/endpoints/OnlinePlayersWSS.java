package net.mauki.maukiseasonpl.dashboard.wss.endpoints;

import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSAPIEndpoint;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSRequestInformation;

public class OnlinePlayersWSS implements WSSAPIEndpoint {

    @Override
    public void handle(WSSRequestInformation wssRequestInformation) {

    }

    @Override
    public JSONStructure structure() {
        return null;
    }

}

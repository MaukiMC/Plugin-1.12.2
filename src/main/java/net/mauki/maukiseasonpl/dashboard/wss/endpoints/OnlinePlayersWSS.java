package net.mauki.maukiseasonpl.dashboard.wss.endpoints;

import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSAPIEndpoint;
import net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler.WSSRequestInformation;

/**
 * WSS-Endpoint to get all currently online players
 */
public class OnlinePlayersWSS implements WSSAPIEndpoint {

    /**
     * The code that will execute when the endpoint is called
     * @param wssRequestInformation The information of the request
     * @throws Exception Will be thrown when there was an error
     */
    @Override
    public void handle(WSSRequestInformation wssRequestInformation) throws Exception {

    }

    /**
     * The structure of the request body
     * @return The {@link JSONStructure} object
     */
    @Override
    public JSONStructure structure() {
        return null;
    }

}

package net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler;

import org.java_websocket.WebSocket;
import org.json.JSONObject;

/**
 * Interface for request information
 */
public interface WSSRequestInformation {

    /**
     * Method to get all important information given by the requester as {@link JSONObject}
     * @return The {@link JSONObject}
     */
    JSONObject getRequestBody();

    /**
     * All websocket information
     * @return The information
     */
    WebSocket getWebsocket();

}

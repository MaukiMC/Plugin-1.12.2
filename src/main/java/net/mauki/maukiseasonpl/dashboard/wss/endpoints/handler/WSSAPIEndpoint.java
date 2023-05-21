package net.mauki.maukiseasonpl.dashboard.wss.endpoints.handler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Interface for Endpoints
 */
public interface WSSAPIEndpoint {

    /**
     * The code that will execute when the endpoint is called
     * @param wssRequestInformation The information of the request
     */
    void handle(WSSRequestInformation wssRequestInformation) throws Exception;

    JSONStructure structure();

    /**
     * Class for the structure of a request body
     */
    class JSONStructure {

        /**
         * {@link HashMap} for all components of the request body
         */
        private final HashMap<String, Class<?>> structure = new HashMap<>();

        /**
         * Initialise a {@link JSONStructure} object and automatically parse a {@link JSONObject} into it
         * @param jsonObject The {@link JSONObject} you want to parse
         */
        public JSONStructure(JSONObject jsonObject) {
            jsonObject.keySet().forEach(key -> {
                structure.put(key, jsonObject.get(key).getClass());
            });
        }

        /**
         * Initialise an empty {@link JSONStructure} object
         */
        public JSONStructure() {

        }

        /**
         * Add a part of the structure
         * @param s The key of the part
         * @param o The value of the part
         * @return The current {@link JSONStructure} object
         */
        public JSONStructure put(String s, Class<?> o) {
            structure.put(s, o);
            return this;
        }

        /**
         * Check if the structure of two {@link JSONStructure} objects are equal
         * @param jsonStructure1 The first {@link JSONStructure}
         * @param jsonStructure2 The second {@link JSONStructure}
         * @return If the two {@link JSONStructure}s are equal
         */
        public static boolean validate(JSONStructure jsonStructure1, JSONStructure jsonStructure2) {
            int elements = 0;
            AtomicReference<Boolean> equal = new AtomicReference<>(true);
            for (Map.Entry<String, Class<?>> entry : jsonStructure1.structure.entrySet()) {
                elements++;
                String key = entry.getKey();
                Class<?> type = entry.getValue();
                if(jsonStructure2.structure.get(key) != type) {
                    equal.set(false);
                    break;
                }
            }
            if(elements != jsonStructure1.structure.size()) equal.set(false);
            return equal.get();
        }

    }

}

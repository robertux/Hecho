package org.robertux.data.model;

import com.google.gson.JsonObject;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by robertux on 9/16/17.
 */
@Data
public class JsonResponse {
    private int code;
    private String reason;
    private JsonObject content;

    public JsonResponse() {
        this.code = 0;
        this.reason = "OK";
        this.content = new JsonObject();
    }

    public JsonResponse(int code, String reason) {
        this.code = code;
        this.reason = reason;
        this.content = new JsonObject();
    }

    public JsonResponse(int code, String reason, JsonObject content) {
        this(code, reason);
        this.content = content;
    }

    public static JsonResponse fromError(int errorCode) {
        Logger logger = LogManager.getLogger(JsonResponse.class);
        JsonResponse r = new JsonResponse(999, "Error general");
        Properties props = new Properties();

        try {
            props.load(JsonResponse.class.getResourceAsStream("/errorMessages.properties"));
            if (props.getProperty(String.valueOf(errorCode)) != null) {
                r.setCode(errorCode);
                r.setReason(props.getProperty(String.valueOf(errorCode)));
            } else {
                r.setCode(999);
                r.setReason("Error desconocido");
            }
        } catch (NullPointerException | IOException e) {
            logger.error("Error tratando de cargar los mensajes de error: " + e.getMessage(), e);
        }

        return r;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("code", code);
        obj.addProperty("reason", reason);
        obj.add("content", content);

        return obj;
    }
}

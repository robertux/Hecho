package org.robertux.data;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.model.JsonResponse;

import javax.servlet.http.HttpSession;

/**
 * Created by robertux on 11/6/17.
 */
public abstract class CloudSyncProvider {
    protected Logger logger = LogManager.getLogger(this.getClass());

    public abstract String getName();

    public abstract String getLogoUrl();

    public abstract String getSyncUrl(HttpSession session);

    public abstract JsonResponse sync(String sessionId, String code);

    public JsonObject toJson(HttpSession session) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", this.getName());
        obj.addProperty("logoUrl", this.getLogoUrl());
        obj.addProperty("syncUrl", this.getSyncUrl(session));

        return obj;
    }
}

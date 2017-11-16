package org.robertux.data.syncProviders;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;

/**
 * Created by robertux on 11/6/17.
 */
public abstract class CloudSyncProvider {
    protected Logger logger = LogManager.getLogger(this.getClass());

    public abstract String getName();

    public abstract String getLogoUrl();

    public abstract CloudSyncSessionData createSessionData();

    public abstract String getSyncUrl(HttpSession session, CloudSyncSessionData sessionData);

    public abstract JsonResponse sync(Request req, String code, CloudSyncSessionData sessionData);

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", this.getName());
        obj.addProperty("logoUrl", this.getLogoUrl());

        return obj;
    }
}
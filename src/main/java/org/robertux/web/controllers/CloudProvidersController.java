package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.ConnectionManager;
import org.robertux.data.model.JsonResponse;
import org.robertux.data.syncProviders.*;
import spark.Request;
import spark.Session;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertux on 11/7/17.
 */
public class CloudProvidersController {
    public static final String SYNC_SESSION = "syncSession";
    public static final String SELECTED_PROVIDER = "provider";

    private static Map<String, CloudSyncProvider> dataSyncProviders = new HashMap<>();

    static {
        dataSyncProviders.put(DropboxProvider.PROVIDER_NAME, new DropboxProvider());
        dataSyncProviders.put(GDriveProvider.PROVIDER_NAME, new GDriveProvider());
        dataSyncProviders.put(ICloudProvider.PROVIDER_NAME, new ICloudProvider());
    }

    private Logger logger = LogManager.getLogger(this.getClass());

    public CloudSyncProvider getProvider(String name) {
        return dataSyncProviders.get(name);
    }

    public JsonResponse getProviders() {
        JsonResponse resp = new JsonResponse();
        JsonArray arr = new JsonArray();
        for (CloudSyncProvider prov : dataSyncProviders.values()) {
            arr.add(prov.toJson());
        }

        resp.getContent().add("providers", arr);
        return resp;
    }

    public boolean isInSync(Session session) {
        return session.attribute(SYNC_SESSION) != null &&
                ((CloudSyncSessionData) session.attribute(SYNC_SESSION)).isInSync();
    }

    public String getSyncUrl(String providerName, Session session) {
        if (!dataSyncProviders.containsKey(providerName)) {
            return JsonResponse.fromCode(1201).toUrlParams("/");
        }

        session.attribute(SELECTED_PROVIDER, providerName);

        if (session.attribute(CloudProvidersController.SYNC_SESSION) == null) {
            session.attribute(CloudProvidersController.SYNC_SESSION, dataSyncProviders.get(providerName).createSessionData());
        }

        return dataSyncProviders.get(providerName).getSyncUrl(session.raw(), session.attribute(SYNC_SESSION));
    }

    public JsonResponse sync(String providerName, Request req, String code, Session session) {
        if (!dataSyncProviders.containsKey(providerName)) {
            return JsonResponse.fromCode(1201);
        }

        if (req.session().attribute(SYNC_SESSION) == null) {
            return JsonResponse.fromCode(2013);
        }

        return dataSyncProviders.get(providerName).sync(req, code, req.session().attribute(SYNC_SESSION));
    }

    public JsonResponse save(String providerName, Request req) {
        String dbPath;

        try {
            dbPath = ConnectionManager.getDatabasePath(req.session().id());
            return dataSyncProviders.get(providerName).save(new FileInputStream(dbPath), ConnectionManager.DATABASE_NAME, req.session().attribute(SYNC_SESSION));

        } catch (IOException e) {
            this.logger.error("Error tratando de guardar la información en la nube: " + e.getMessage(), e);
            return JsonResponse.fromCode(1206);
        }
    }

    public JsonResponse load(String providerName, Request req) {
        String dbPath;

        try {
            dbPath = ConnectionManager.getDatabasePath(req.session().id());
            return dataSyncProviders.get(providerName).load(new FileOutputStream(dbPath), ConnectionManager.DATABASE_NAME, req.session().attribute(SYNC_SESSION));

        } catch (IOException e) {
            this.logger.error("Error tratando de cargar datos de la nube: " + e.getMessage(), e);
            return JsonResponse.fromCode(1207);
        }
    }
}

package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.ConnetcionManager;
import org.robertux.data.model.JsonResponse;
import org.robertux.data.syncProviders.*;
import spark.Request;
import spark.Session;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertux on 11/7/17.
 */
public class CloudProvidersController {
    public static final String SYNC_SESSION = "syncSession";
    public static final String SYNCED_FLAG = "synced";
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

    public JsonResponse getProviders(String providerNameFilter) {
        JsonResponse resp = new JsonResponse();
        JsonArray arr = new JsonArray();
        for (CloudSyncProvider prov : dataSyncProviders.values()) {
            if (providerNameFilter == null || providerNameFilter.trim().length() == 0 || prov.getName().equals(providerNameFilter)) {
                arr.add(prov.toJson());
            }
        }

        resp.getContent().add("providers", arr);
        return resp;
    }

    public String getSyncUrl(String providerName, Session session) {
        if (!dataSyncProviders.containsKey(providerName)) {
            return JsonResponse.fromCode(1201).toUrlParams("/");
        }

        return dataSyncProviders.get(providerName).getSyncUrl(session.raw(), session.attribute(SYNC_SESSION));
    }

    public JsonResponse sync(String providerName, Request req, String code, CloudSyncSessionData sessionData) {
        if (!dataSyncProviders.containsKey(providerName)) {
            return JsonResponse.fromCode(1201);
        }

        JsonResponse result = dataSyncProviders.get(providerName).sync(req, code, sessionData);
        if (result.getCode() != 0) {
            return result;
        }

        boolean firstSync = (req.session() == null || req.session().isNew() || req.session().attribute(SYNCED_FLAG) == null);
        this.logger.debug("firstSync? {}", firstSync);
        if (firstSync) {
            try {
                String dbPath = ConnetcionManager.getDatabasePath(req.session().id());
                dataSyncProviders.get(providerName).save(new FileInputStream(dbPath), ConnetcionManager.DATABASE_NAME, req.session().attribute(SYNC_SESSION));
                req.session().attribute(SYNCED_FLAG, true);
            } catch (IOException e) {
                this.logger.error("Error tratando de guardar la información en la nube: " + e.getMessage(), e);
                return JsonResponse.fromCode(1206);
            }
        } else {
            return JsonResponse.OK;
        }

        return JsonResponse.OK;
    }
}

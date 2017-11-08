package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.robertux.data.CloudSyncProvider;
import org.robertux.data.DropboxProvider;
import org.robertux.data.GDriveProvider;
import org.robertux.data.model.JsonResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertux on 11/7/17.
 */
public class CloudProvidersController {
    private static Map<String, CloudSyncProvider> dataSyncProviders = new HashMap<>();

    static {
        dataSyncProviders.put("dropbox", new DropboxProvider());
        dataSyncProviders.put("gdrive", new GDriveProvider());
    }

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
}
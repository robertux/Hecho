package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.robertux.data.model.JsonResponse;
import org.robertux.data.syncProviders.CloudSyncProvider;
import org.robertux.data.syncProviders.DropboxProvider;
import org.robertux.data.syncProviders.GDriveProvider;
import org.robertux.data.syncProviders.ICloudProvider;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by robertux on 11/7/17.
 */
public class CloudProvidersController {
    private static Map<String, CloudSyncProvider> dataSyncProviders = new HashMap<>();

    static {
        dataSyncProviders.put(DropboxProvider.PROVIDER_NAME, new DropboxProvider());
        dataSyncProviders.put(GDriveProvider.PROVIDER_NAME, new GDriveProvider());
        dataSyncProviders.put(ICloudProvider.PROVIDER_NAME, new ICloudProvider());
    }

    public CloudSyncProvider getProvider(String name) {
        return dataSyncProviders.get(name);
    }

    public JsonResponse getProviders(HttpSession session) {
        JsonResponse resp = new JsonResponse();
        JsonArray arr = new JsonArray();
        for (CloudSyncProvider prov : dataSyncProviders.values()) {
            arr.add(prov.toJson(session));
        }

        resp.getContent().add("providers", arr);
        return resp;
    }
}

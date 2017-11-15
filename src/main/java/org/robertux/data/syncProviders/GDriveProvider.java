package org.robertux.data.syncProviders;

import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;

/**
 * Created by robertux on 11/7/17.
 */
public class GDriveProvider extends CloudSyncProvider {
    public static final String PROVIDER_NAME = "Google Drive";
    public static final String LOGO_URL = "/img/providers/Drive Lockup - with gray Google.png";

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getLogoUrl() {
        return LOGO_URL;
    }

    @Override
    public String getSyncUrl(HttpSession session) {
        return "";
    }

    @Override
    public JsonResponse sync(Request req, String code) {
        return new JsonResponse();
    }
}

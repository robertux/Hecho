package org.robertux.data;

import org.robertux.data.model.JsonResponse;

/**
 * Created by robertux on 11/7/17.
 */
public class GDriveProvider extends CloudSyncProvider {
    private static final String PROVIDER_NAME = "Google Drive";
    private static final String LOGO_URL = "/img/providers/Drive Lockup - with gray Google.png";

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getLogoUrl() {
        return LOGO_URL;
    }

    @Override
    public String getSyncUrl() {
        return "";
    }

    @Override
    public JsonResponse sync(String sessionId, String code) {
        return new JsonResponse();
    }
}

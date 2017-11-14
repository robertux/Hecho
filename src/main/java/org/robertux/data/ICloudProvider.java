package org.robertux.data;

import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;

/**
 * Created by robertux on 11/13/17.
 */
public class ICloudProvider extends CloudSyncProvider {
    private static final String PROVIDER_NAME = "Apple iCloud";
    private static final String LOGO_URL = "/img/providers/iCloud-Drive.png";

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

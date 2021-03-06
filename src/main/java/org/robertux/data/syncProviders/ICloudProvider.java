package org.robertux.data.syncProviders;

import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by robertux on 11/13/17.
 */
public class ICloudProvider extends CloudSyncProvider {
    public static final String PROVIDER_NAME = "icloud";
    public static final String PROVIDER_DISPLAY_NAME = "Apple iCloud";
    public static final String LOGO_URL = "/img/providers/iCloud-Drive.png";

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getDisplayName() {
        return PROVIDER_DISPLAY_NAME;
    }

    @Override
    public String getLogoUrl() {
        return LOGO_URL;
    }

    @Override
    public CloudSyncSessionData createSessionData() {
        return null;
    }

    @Override
    public String getSyncUrl(HttpSession session, CloudSyncSessionData sessionData) {
        return "";
    }

    @Override
    public JsonResponse sync(Request req, String code, CloudSyncSessionData sessionData) {
        return new JsonResponse();
    }

    @Override
    public JsonResponse save(InputStream dbContent, String dbName, CloudSyncSessionData sessionData) {
        return null;
    }

    @Override
    public JsonResponse load(OutputStream dbContent, String dbName, CloudSyncSessionData sessionData) {
        return null;
    }
}

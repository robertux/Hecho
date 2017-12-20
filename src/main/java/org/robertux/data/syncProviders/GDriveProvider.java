package org.robertux.data.syncProviders;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by robertux on 11/7/17.
 */
public class GDriveProvider extends CloudSyncProvider {
    public static final String PROVIDER_NAME = "gdrive";
    public static final String PROVIDER_DISPLAY_NAME = "Google Drive";
    public static final String LOGO_URL = "/img/providers/Drive Lockup - with gray Google.png";

    private static final String REDIRECT_URL = "https://hecho.herokuapp.com/api/providers/gdrive/auth";

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
        return new GoogleBrowserClientRequestUrl("696464267146-tik0vr1bnql2hdqppo0k73ennnj4819o.apps.googleusercontent.com",
                REDIRECT_URL, Arrays.asList("https://www.googleapis.com/auth/drive", "https://www.googleapis.com/auth/drive.file")).build();
    }

    @Override
    public JsonResponse sync(Request req, String code, CloudSyncSessionData sessionData) {
        return new JsonResponse();
    }

    @Override
    public JsonResponse save(InputStream dbContent, String dbName, CloudSyncSessionData sessionData) {
        if (sessionData == null || !sessionData.isInSync()) {
            return JsonResponse.fromCode(1205);
        }
        return new JsonResponse(0, "Datos guardados exitosamente");
    }

    @Override
    public JsonResponse load(OutputStream dbContent, String dbName, CloudSyncSessionData sessionData) {
        if (sessionData == null || !sessionData.isInSync()) {
            return JsonResponse.fromCode(1205);
        }
        return new JsonResponse(0, "Datos cargados exitosamente");
    }
}

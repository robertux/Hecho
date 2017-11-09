package org.robertux.data;

import com.dropbox.core.*;
import org.robertux.data.model.JsonResponse;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by robertux on 11/6/17.
 */
public class DropboxProvider extends CloudSyncProvider {
    private static final String PROVIDER_NAME = "Dropbox";
    private static final String LOGO_URL = "/img/providers/Dropbox.png";
    private static final String REDIRECT_URL = "https://hecho.herokuapp.com/api/dropbox/save";
    private static final String URL = "https://www.dropbox.com/oauth2/authorize?client_id=" + System.getenv("DROPBOX_API_KEY") + "&redirect_uri=" + REDIRECT_URL + "&response_type=code";

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
        return URL;
    }

    @Override
    public JsonResponse sync(String sessionId, String code) {
        JsonResponse ok = new JsonResponse();

        try {
            DbxRequestConfig requestConfig = new DbxRequestConfig(DropboxClient.CLIENT_IDENTIFIER);
            DbxAppInfo appInfo = new DbxAppInfo(System.getenv("DROPBOX_API_KEY"), System.getenv("DROPBOX_API_SECRET"));
            DbxWebAuth auth = new DbxWebAuth(requestConfig, appInfo);

            DbxAuthFinish authFinish = auth.finishFromCode(code);
            DropboxClient client = new DropboxClient(requestConfig, authFinish.getAccessToken());

            client.saveFile(new FileInputStream(ConnetcionManager.getDatabasePath(sessionId)), ConnetcionManager.DATABASE_NAME);
        } catch (IOException | DbxException | NullPointerException e) {
            logger.error("Error tratando de guardar la base de datos en Dropbox: " + e.getMessage(), e);
            return JsonResponse.fromError(1202);
        }

        return ok;
    }
}

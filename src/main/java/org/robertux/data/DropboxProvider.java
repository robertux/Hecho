package org.robertux.data;

import com.dropbox.core.*;
import org.robertux.data.model.JsonResponse;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by robertux on 11/6/17.
 */
public class DropboxProvider extends CloudSyncProvider {
    private static final String PROVIDER_NAME = "Dropbox";
    private static final String LOGO_URL = "/img/providers/Dropbox.png";
    private static final String REDIRECT_URL = "https://hecho.herokuapp.com/providers/dropbox/auth";
    private static final String URL = "https://www.dropbox.com/oauth2/authorize?client_id=" + System.getenv("DROPBOX_API_KEY") + "&redirect_uri=" + REDIRECT_URL + "&response_type=token";
    private HttpSession session;

    private DbxRequestConfig requestConfig;
    private DbxAppInfo appInfo;
    private DbxWebAuth auth;

    public DropboxProvider() {
        this.requestConfig = new DbxRequestConfig(DropboxClient.CLIENT_IDENTIFIER);
        this.appInfo = new DbxAppInfo(System.getenv("DROPBOX_API_KEY"), System.getenv("DROPBOX_API_SECRET"));
        this.auth = new DbxWebAuth(requestConfig, appInfo);
        this.session = session;
    }


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
        String sessionKey = "dropbox-auth-csrf-token";
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(this.session, sessionKey);
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URL, csrfTokenStore)
                .build();

        return this.auth.authorize(authRequest);
    }

    @Override
    public JsonResponse sync(String sessionId, String accessToken) {
        JsonResponse ok = new JsonResponse();

        try {
            DbxRequestConfig requestConfig = new DbxRequestConfig(DropboxClient.CLIENT_IDENTIFIER);
            DropboxClient client = new DropboxClient(requestConfig, accessToken);

            client.saveFile(new FileInputStream(ConnetcionManager.getDatabasePath(sessionId)), ConnetcionManager.DATABASE_NAME);
        } catch (IOException | DbxException | NullPointerException e) {
            logger.error("Error tratando de guardar la base de datos en Dropbox: " + e.getMessage(), e);
            return JsonResponse.fromError(1202);
        }

        return ok;
    }
}

package org.robertux.data;

import com.dropbox.core.*;
import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;

/**
 * Created by robertux on 11/6/17.
 */
public class DropboxProvider extends CloudSyncProvider {
    private static final String PROVIDER_NAME = "Dropbox";
    private static final String LOGO_URL = "/img/providers/Dropbox.png";
    private static final String REDIRECT_URL = "https://hecho.herokuapp.com/providers/dropbox/auth";
    private static final String SESSION_KEY = "dropbox-auth-csrf-token";

    private DbxRequestConfig requestConfig;
    private DbxAppInfo appInfo;
    private DbxWebAuth auth;

    public DropboxProvider() {
        this.requestConfig = new DbxRequestConfig(DropboxClient.CLIENT_IDENTIFIER);
        this.appInfo = new DbxAppInfo(System.getenv("DROPBOX_API_KEY"), System.getenv("DROPBOX_API_SECRET"));
        this.auth = new DbxWebAuth(requestConfig, appInfo);
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getLogoUrl() {
        return LOGO_URL;
    }

    /**
     * Basado en: https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/com/dropbox/core/DbxWebAuth.html
     */
    @Override
    public String getSyncUrl(HttpSession session) {
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, SESSION_KEY);
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URL, csrfTokenStore)
                .build();

        return this.auth.authorize(authRequest);
    }

    /**
     * Basado en: https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/com/dropbox/core/DbxWebAuth.html
     *
     * @param req
     * @param code
     * @return
     */
    @Override
    public JsonResponse sync(Request req, String code) {
        JsonResponse ok = new JsonResponse(0, "Sincronización realizada");

        try {
            DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(req.session().raw(), SESSION_KEY);
            DbxAuthFinish authFinish = auth.finishFromRedirect(REDIRECT_URL, csrfTokenStore, req.raw().getParameterMap());

            DropboxClient client = new DropboxClient(requestConfig, authFinish.getAccessToken());
            client.saveFile(new FileInputStream(ConnetcionManager.getDatabasePath(req.session().id())), ConnetcionManager.DATABASE_NAME);
        } catch (Exception e) {
            logger.error("Error tratando de guardar la base de datos en Dropbox: " + e.getMessage(), e);
            return JsonResponse.fromError(1202);
        }

        return ok;
    }
}

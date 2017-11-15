package org.robertux.data.syncProviders;

import com.dropbox.core.*;
import org.apache.logging.log4j.LogManager;
import org.robertux.data.DropboxClient;
import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;

/**
 * Created by robertux on 11/6/17.
 */
public class DropboxProvider extends CloudSyncProvider {
    public static final String PROVIDER_NAME = "Dropbox";
    public static final String LOGO_URL = "/img/providers/Dropbox.png";

    private static final String REDIRECT_URL = "https://hecho.herokuapp.com/providers/dropbox/auth";
    private static final String SESSION_KEY = "dropbox-auth-csrf-token";

    private DbxRequestConfig requestConfig;
    private DbxAppInfo appInfo;
    private DbxWebAuth auth;
    private DropboxClient client;

    public DropboxProvider() {
        this.logger = LogManager.getLogger(this.getClass());
        try {
            this.requestConfig = new DbxRequestConfig(DropboxClient.CLIENT_IDENTIFIER);
            this.appInfo = new DbxAppInfo(System.getenv("DROPBOX_API_KEY"), System.getenv("DROPBOX_API_SECRET"));
            this.auth = new DbxWebAuth(requestConfig, appInfo);
        } catch (Exception e) {
            logger.error("Error tratando de inicializar la clase DropboxProvider: " + e.getMessage(), e);
        }
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getLogoUrl() {
        return LOGO_URL;
    }

    public DropboxClient getClient() {
        return this.client;
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
        JsonResponse ok = new JsonResponse(0, "Sincronizacion realizada");
        boolean firstSync = (req.session() == null || req.session().isNew() || req.session().attribute("synced") == null);

        try {
            DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(req.session().raw(), SESSION_KEY);
            DbxAuthFinish authFinish = auth.finishFromRedirect(REDIRECT_URL, csrfTokenStore, req.raw().getParameterMap());

            this.client = new DropboxClient(requestConfig, authFinish.getAccessToken());

            //String dbPath = ConnetcionManager.getDatabasePath(req.session().id());
            //client.saveFile(new FileInputStream(dbPath), ConnetcionManager.DATABASE_NAME);
        } catch (Exception e) {
            logger.error("Error tratando de guardar la base de datos en Dropbox: " + e.getMessage(), e);
            return JsonResponse.fromCode(1202);
        }

        return ok;
    }
}

package org.robertux.data.syncProviders;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxStandardSessionStore;
import com.dropbox.core.DbxWebAuth;
import org.robertux.data.model.JsonResponse;
import spark.Request;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by robertux on 11/6/17.
 */
public class DropboxProvider extends CloudSyncProvider {
    public static final String PROVIDER_NAME = "Dropbox";
    public static final String LOGO_URL = "/img/providers/Dropbox.png";

    private static final String REDIRECT_URL = "https://hecho.herokuapp.com/api/providers/dropbox/auth";
    private static final String SESSION_KEY = "dropbox-auth-csrf-token";

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getLogoUrl() {
        return LOGO_URL;
    }

    @Override
    public CloudSyncSessionData createSessionData() {
        return new DropboxSessionData();
    }

    /**
     * Basado en: https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/com/dropbox/core/DbxWebAuth.html
     */
    @Override
    public String getSyncUrl(HttpSession session, CloudSyncSessionData sessionData) {
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, SESSION_KEY);
        DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                .withRedirectUri(REDIRECT_URL, csrfTokenStore)
                .build();

        return ((DropboxSessionData) sessionData).getAuth().authorize(authRequest);
    }

    /**
     * Basado en: https://dropbox.github.io/dropbox-sdk-java/api-docs/v2.1.x/com/dropbox/core/DbxWebAuth.html
     *
     * @param req
     * @param code
     * @return
     */
    @Override
    public JsonResponse sync(Request req, String code, CloudSyncSessionData sessionData) {
        JsonResponse ok = new JsonResponse(0, "Sincronizacion realizada");
        boolean firstSync = (req.session() == null || req.session().isNew() || req.session().attribute("synced") == null);

        try {
            DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(req.session().raw(), SESSION_KEY);
            DbxAuthFinish authFinish = ((DropboxSessionData) sessionData).getAuth().finishFromRedirect(REDIRECT_URL, csrfTokenStore, req.raw().getParameterMap());
            ((DropboxSessionData) sessionData).setAccessToken(authFinish.getAccessToken());
            sessionData.setInSync(true);

            //this.client = new DropboxClient(requestConfig, authFinish.getAccessToken());
            //String dbPath = ConnetcionManager.getDatabasePath(req.session().id());
            //client.saveFile(new FileInputStream(dbPath), ConnetcionManager.DATABASE_NAME);
        } catch (Exception e) {
            sessionData.setInSync(false);
            logger.error("Error tratando de guardar la base de datos en Dropbox: " + e.getMessage(), e);
            return JsonResponse.fromCode(1202);
        }

        return ok;
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

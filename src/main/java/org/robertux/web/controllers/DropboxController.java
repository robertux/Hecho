package org.robertux.web.controllers;

import com.dropbox.core.DbxException;
import org.robertux.data.ConnetcionManager;
import org.robertux.data.DropboxClient;
import org.robertux.data.model.JsonResponse;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by robertux on 11/6/17.
 */
public class DropboxController extends DataSyncController {
    private static final String APP_ID = "0n9qkarqmul4ub";
    private static final String LOGO_URL = "/img/providers/Dropbox.png";
    private static final String REDIRECT_URL = "https://hecho.herokuapp.com/login/";
    private static final String URL = "https://www.dropbox.com/oauth2/authorize?client_id=" + APP_ID + "&redirect_uri=" + REDIRECT_URL + "&response_type=token";

    @Override
    public String getLogoUrl() {
        return "";
    }

    @Override
    public String getSyncUrl() {
        return URL;
    }

    @Override
    public JsonResponse sync(String sessionId, String token) {
        JsonResponse ok = new JsonResponse();
        DropboxClient client = new DropboxClient(token);
        try {
            client.saveFile(new FileInputStream(ConnetcionManager.getDatabasePath(sessionId)), ConnetcionManager.DATABASE_NAME);
        } catch (IOException | DbxException e) {
            logger.error("Error tratando de guardar la base de datos en Dropbox: " + e.getMessage(), e);
            return JsonResponse.fromError(1202);
        }

        return ok;
    }
}

package org.robertux.data.syncProviders;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import org.robertux.data.DropboxClient;

/**
 * Created by robertux on 11/15/17.
 */
public class DropboxSessionData extends CloudSyncSessionData {
    private DbxRequestConfig requestConfig;
    private DbxAppInfo appInfo;
    private DbxWebAuth auth;
    private DropboxClient client;
    private String accessToken;

    public DropboxSessionData() {
        try {
            this.requestConfig = new DbxRequestConfig(DropboxClient.CLIENT_IDENTIFIER);
            this.appInfo = new DbxAppInfo(System.getenv("DROPBOX_API_KEY"), System.getenv("DROPBOX_API_SECRET"));
            this.auth = new DbxWebAuth(requestConfig, appInfo);
            this.initialized = true;
        } catch (Exception e) {
            logger.error("Error tratando de inicializar la clase DropboxProvider: " + e.getMessage(), e);
            this.initialized = false;
        }
    }

    public DbxRequestConfig getRequestConfig() {
        return requestConfig;
    }

    public DbxWebAuth getAuth() {
        return auth;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

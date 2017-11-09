package org.robertux.data;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.users.FullAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.model.DropboxAccount;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/8/17.
 */
public class DropboxClient {
    public static final String CLIENT_IDENTIFIER = "HECHO/1.0";

    private DbxClientV2 client;
    private Logger logger;

    public DropboxClient(DbxRequestConfig requestConfig, String accessToken) {
        logger = LogManager.getLogger(this.getClass());
        this.connect(requestConfig, accessToken);
    }

    public void connect(DbxRequestConfig requestConfig, String accessToken) {
        this.client = new DbxClientV2(requestConfig, accessToken);
    }

    public DropboxAccount getAccountInfo() throws DbxException {
        FullAccount acc = this.client.users().getCurrentAccount();
        return new DropboxAccount(acc.getAccountId(), acc.getName().getDisplayName(), acc.getEmail(), acc.getProfilePhotoUrl());
    }

    public String getAccountName() throws DbxException {
        FullAccount account = this.client.users().getCurrentAccount();
        return account.getName().getDisplayName();
    }

    public List<String> getFileNames(String path) throws DbxException {
        ListFolderResult result = client.files().listFolder(path);
        List<String> fileNames = new ArrayList<>();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                fileNames.add(metadata.getName());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        return fileNames;
    }

    public void loadFile(OutputStream stream, String fileName) throws DbxException, IOException {
        this.logger.debug("Cargando archivo {}", fileName);
        DbxDownloader<FileMetadata> downloader = client.files().download("/" + fileName);
        downloader.download(stream);
    }

    public void saveFile(InputStream stream, String fileName) throws IOException, DbxException {
        this.logger.debug("Guardando archivo {}", fileName);
        FileMetadata metadata = client.files().uploadBuilder("/" + fileName)
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(stream);
    }
}

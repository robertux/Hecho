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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/8/17.
 */
public class DropboxClient {
    public static final String CLIENT_IDENTIFIER = "HECHO/1";
    public static final String ACCESS_TOKEN = "EvAnuSj8cuMAAAAAAAABfu09nbbb8Ey3DYQlw83oUgw1XPUVqs3QZoL8JNQwjmCO";

    private DbxClientV2 client;
    private Logger logger;

    public DropboxClient() {
        logger = LogManager.getLogger(this.getClass());
        this.connect();
    }

    public void connect() {
        DbxRequestConfig config = new DbxRequestConfig(CLIENT_IDENTIFIER);
        this.client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    public String getAccountInfo() throws DbxException {
        FullAccount account = this.client.users().getCurrentAccount();
        return "ID: " + account.getAccountId() + " name: " + account.getName() + " email: " + account.getEmail();
    }

    public List<String> getFileNames(String path) throws DbxException {
        ListFolderResult result = client.files().listFolder(path);
        List<String> fileNames = new ArrayList<>();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                fileNames.add(metadata.getPathLower() + "/" + metadata.getName());
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        return fileNames;
    }

    public void loadFile(OutputStream stream, String fileName) throws DbxException, IOException {
        DbxDownloader<FileMetadata> downloader = client.files().download("/" + fileName);
        downloader.download(stream);
    }

    public void saveFile(InputStream stream, String fileName) throws IOException, DbxException {
        FileMetadata metadata = client.files().uploadBuilder("/" + fileName)
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(stream);
    }
}

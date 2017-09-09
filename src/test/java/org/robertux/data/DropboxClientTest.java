package org.robertux.data;

import com.dropbox.core.DbxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by robertux on 9/8/17.
 */
public class DropboxClientTest {
    private DropboxClient client;
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        this.client = new DropboxClient();
        this.logger = LogManager.getLogger(this.getClass());
    }

    @Test
    public void getAccountInfo() throws Exception {
        this.logger.info("AccountInfo: {}", client.getAccountInfo());
    }

    @Test
    public void getFileNames() throws Exception {
        this.logger.info("FileNames: {}", Arrays.toString(client.getFileNames("").toArray()));
    }

    @Test
    public void addFile() throws IOException, DbxException {
        client.saveFile(new ByteArrayInputStream("TEST".getBytes()), "testFile.txt");
    }

    @Test
    public void getFile() throws IOException, DbxException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        client.loadFile(oStream, "testFile.txt");
        this.logger.info("Stream: {}", new String(oStream.toByteArray()));
    }

}
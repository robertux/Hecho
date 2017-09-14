package org.robertux.data;

import com.dropbox.core.DbxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        String accInfo = client.getAccountInfo().toString();
        this.logger.info("AccountInfo: {}", accInfo);
        Assert.assertNotNull("Información de la cuenta no debe ser nula", accInfo);
    }

    @Test
    public void getFileNames() throws Exception {
        this.logger.info("FileNames: {}", Arrays.toString(client.getFileNames("").toArray()));
        Assert.assertTrue("Archivo hecho.db debe estar cargado en Dropbox", client.getFileNames("").contains("hecho.db"));
    }

    @Test
    public void addFile() throws IOException, DbxException {
        client.saveFile(ConnetcionManager.class.getResourceAsStream("/hecho.db"), "hecho.db");
        Assert.assertTrue("Archivo hecho.db debe estar cargado en Dropbox", client.getFileNames("").contains("hecho.db"));
    }

    @Test
    public void getFile() throws IOException, DbxException {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        client.loadFile(oStream, "hecho.db");

        Assert.assertNotEquals("Tamaño del stream debe ser mayor que cero", 0, oStream.size());
    }

}
package org.robertux.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

/**
 * Created by robertux on 9/9/17.
 */
public class ConnetcionManagerTest {
    private Logger logger;
    private Connection cn;

    @Before
    public void setUp() throws Exception {
        this.logger = LogManager.getLogger(this.getClass());
    }

    @Test
    public void getConnection() throws Exception {
        String dbPath = ConnetcionManager.getDatabasePath("_");
        this.logger.debug("dbPath: {}", dbPath);
        Assert.assertTrue("Ruta debe apuntar a carpeta temporal del sistema", dbPath.contains(System.getProperty("java.io.tmpdir")));

        Connection cn = ConnetcionManager.getConnection("_");
        Assert.assertNotNull("Conexión no debe ser nula", cn);
        Assert.assertFalse("Conexión no debe estar cerrada", cn.isClosed());
    }

    @After
    public void tearDown() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }
}
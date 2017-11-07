package org.robertux.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by robertux on 9/9/17.
 */
public class ConnetcionManager {
    public static final String DATABASE_NAME = "hecho.db";
    public static Logger logger = LogManager.getLogger(ConnetcionManager.class);

    public static Connection getConnection(String sessionId) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.sqlite.JDBC");
        String dbPath = getDatabasePath(sessionId);

        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public static String getDatabasePath(String sessionId) throws IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (!tmpDir.endsWith(File.separator)) {
            tmpDir += File.separator;
        }

        String dbPath = tmpDir + sessionId + File.separator + DATABASE_NAME;
        File fPath = new File(dbPath);

        if (!fPath.exists()) {
            logger.debug("Archivo en ruta {} no existe. Creando a partir de recurso interno...", dbPath);
            InputStream iStream = ConnetcionManager.class.getResourceAsStream("/hecho.db");
            Files.copy(iStream, fPath.toPath());
        }

        logger.debug("Devolviendo ruta {}", dbPath);
        return dbPath;
    }

    public static DSLContext getContext(String sessionId) throws SQLException, IOException, ClassNotFoundException {
        return DSL.using(ConnetcionManager.getConnection(sessionId), SQLDialect.SQLITE);
    }

    public static DSLContext getContext(Connection cn) throws SQLException, IOException, ClassNotFoundException {
        return DSL.using(cn, SQLDialect.SQLITE);
    }
}

package org.robertux.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;

/**
 * Created by robertux on 9/9/17.
 */
public class Startup {
    private static Logger logger;

    public static void main(String[] args) {
        configureServer();
    }

    public static void configureServer() {
        logger = LogManager.getLogger(Startup.class);
        port(8082);
        staticFiles.location("/web");
        staticFiles.expireTime(600L);

        init();
    }
}

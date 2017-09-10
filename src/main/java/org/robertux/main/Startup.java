package org.robertux.main;

import com.dropbox.core.DbxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.DropboxClient;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by robertux on 9/9/17.
 */
public class Startup {
    private static Logger logger;

    public static void main(String[] args) {
        init();

        DropboxClient dbClient = new DropboxClient();
        Map<String, String> data = new HashMap<>();
        try {
            data.put("name", dbClient.getAccountInfo());
        } catch (DbxException e) {
            logger.error("Error obteniendo datos de la cuenta de dropbox: " + e.getMessage(), e);
        }

        get("/hello", (rq, rs) -> new ModelAndView(data, "hello"), new ThymeleafTemplateEngine());
    }

    public static void init() {
        logger = LogManager.getLogger(Startup.class);
        port(8080);
        staticFileLocation("/web");
        staticFiles.expireTime(600L);

    }
}

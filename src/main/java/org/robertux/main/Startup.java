package org.robertux.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.web.controllers.CategoriesController;
import org.robertux.web.controllers.TasksController;

import static spark.Spark.*;

/**
 * Created by robertux on 9/9/17.
 */
public class Startup {
    private static Logger logger;

    public static void main(String[] args) {
        configureServer();
        configureRoutes();
    }

    public static void configureServer() {
        logger = LogManager.getLogger(Startup.class);
        port(8082);
        staticFiles.location("/web");
        staticFiles.expireTime(600L);

        init();
    }

    public static void configureRoutes() {
        get("/categories", (req, resp) -> {
            resp.type("application/json");
            return new CategoriesController().getCategories().toJson();
        });

        get("/categories/:categoryId/tasks", (req, resp) -> {
            resp.type("application/json");
            return new TasksController().getTasks(Integer.parseInt(req.params(":categoryId"))).toJson();
        });
    }
}

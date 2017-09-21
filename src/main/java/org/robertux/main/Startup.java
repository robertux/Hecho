package org.robertux.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.jooq.tables.records.CategoryRecord;
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

    public static void configureFilters() {
        before("/categories/*", (req, resp) -> {

        });

        before("/tasks/*", (req, resp) -> {

        });

        after("/categories/*", (req, resp) -> {
            resp.type("application/json");
        });

        after("/tasks/*", (req, resp) -> {
            resp.type("application/json");
        });
    }

    public static void configureRoutes() {

        get("/categories", (req, resp) -> {
            return new CategoriesController().get().toJson();
        });

        post("/categories/:categoryName", (req, resp) -> {
            return new CategoriesController().add(new CategoryRecord(-1, req.params(":categoryName")));
        });

        put("/categories/:categoryId/:categoryName", (req, resp) -> {
            return new CategoriesController().edit(new CategoryRecord(Integer.parseInt(req.params(":categoryId")), req.params(":categoryName")));
        });

        delete("/categories/:categoryId", (req, resp) -> {
            return new CategoriesController().delete(new CategoryRecord(Integer.parseInt(req.params(":categoryId")), ""));
        });

        get("/categories/:categoryId/tasks", (req, resp) -> {
            return new TasksController().getTasks(Integer.parseInt(req.params(":categoryId"))).toJson();
        });


    }
}

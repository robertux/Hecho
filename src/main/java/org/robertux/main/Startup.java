package org.robertux.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.jooq.tables.records.CategoryRecord;
import org.robertux.web.controllers.CategoriesController;
import org.robertux.web.controllers.TasksController;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 * Created by robertux on 9/9/17.
 */
public class Startup {
    private static Logger logger;

    public static void main(String[] args) {
        configureServer();
        configureFilters();
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
        before("/api/*", (req, resp) -> {
            logRequest(req);
        });

        after("/api/*", (req, resp) -> {
            resp.type("application/json");
            logResponse(resp);
        });
    }

    public static void configureRoutes() {

        get("/api/categories", (req, resp) -> {
            return new CategoriesController().get().toJson();
        });

        post("/api/categories/:categoryName", (req, resp) -> {
            return new CategoriesController().add(new CategoryRecord(-1, req.params(":categoryName"))).toJson();
        });

        put("/api/categories/:categoryId/:categoryName", (req, resp) -> {
            return new CategoriesController().edit(new CategoryRecord(Integer.parseInt(req.params(":categoryId")), req.params(":categoryName"))).toJson();
        });

        delete("/api/categories/:categoryId", (req, resp) -> {
            return new CategoriesController().delete(new CategoryRecord(Integer.parseInt(req.params(":categoryId")), "")).toJson();
        });

        get("/api/categories/:categoryId/tasks", (req, resp) -> {
            return new TasksController().getTasks(Integer.parseInt(req.params(":categoryId"))).toJson();
        });


    }

    protected static void logRequest(Request req) {
        logger.debug("Request received: " + req.requestMethod() + " " + req.url() + " - " + req.body());
    }

    protected static void logResponse(Response resp) {
        logger.debug("Response sent: " + resp.status() + " " + resp.type() + " " + resp.body());
    }
}

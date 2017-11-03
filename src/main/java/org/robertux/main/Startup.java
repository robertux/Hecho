package org.robertux.main;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.jooq.tables.Task;
import org.robertux.data.jooq.tables.records.CategoryRecord;
import org.robertux.data.jooq.tables.records.TaskRecord;
import org.robertux.web.controllers.CategoriesController;
import org.robertux.web.controllers.TasksController;
import spark.Request;
import spark.Response;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        get("/api/categories/:categoryId/tasks/:sortBy", (req, resp) -> {
            return new TasksController().getTasks(Integer.parseInt(req.params(":categoryId")), Integer.parseInt(req.params(":sortBy"))).toJson();
        });

        post("/api/categories/:categoryId/tasks/:taskName", (req, resp) -> {
            return new TasksController().add(new TaskRecord(req.params(":taskName"), Integer.parseInt(req.params(":categoryId")))).toJson();
        });

        put("/api/categories/:categoryId/tasks/:taskId", (req, resp) -> {
            TasksController controller = new TasksController();
            TaskRecord task = controller.getRepo().getTask(Integer.parseInt(req.params(":taskId")));
            Map<String, String> params = getBodyParams(req.body());

            task.set(Task.TASK.DESCRIPTION, params.getOrDefault("description", task.getDescription()));
            task.set(Task.TASK.PRIORITY, Integer.parseInt(params.getOrDefault("priority", String.valueOf(task.getPriority()))));
            task.set(Task.TASK.STATUS, params.getOrDefault("status", task.getStatus()));

            return controller.edit(task).toJson();
        });

        delete("/api/categories/:categoryId/doneTasks/", (req, resp) -> {
            return new TasksController().deleteDoneTasks().toJson();
        });

        delete("/api/categories/:categoryId/tasks/:taskId", (req, resp) -> {
            return new TasksController().delete(new TaskRecord(Integer.parseInt(req.params(":taskId")))).toJson();
        });
    }

    protected static void logRequest(Request req) {
        logger.debug("Request received: " + req.requestMethod() + " " + req.url() + " - " + req.body());
    }

    protected static void logResponse(Response resp) {
        logger.debug("Response sent: " + resp.status() + " " + resp.type() + " " + resp.body());
    }

    protected static Map<String, String> getBodyParams(String requestBody) {
        List<NameValuePair> URLparams = URLEncodedUtils.parse(requestBody, Charset.forName("UTF-8"));
        Map<String, String> params = new HashMap<>(0);
        for (NameValuePair param : URLparams) {
            params.put(param.getName(), param.getValue());
        }

        return params;
    }
}

package org.robertux.main;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.jooq.tables.Task;
import org.robertux.data.jooq.tables.records.CategoryRecord;
import org.robertux.data.jooq.tables.records.TaskRecord;
import org.robertux.data.model.JsonResponse;
import org.robertux.web.controllers.CategoriesController;
import org.robertux.web.controllers.CloudProvidersController;
import org.robertux.web.controllers.TasksController;
import spark.Request;
import spark.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

/**
 * Created by robertux on 9/9/17.
 */
public class Startup {
    private static final DateFormat inputDFmt = new SimpleDateFormat("yyyy/MM/dd");
    private static Logger logger;

    public static void main(String[] args) {
        configureServer(getEnvironmentPort());
        configureFilters();
        configureRoutes();
        configureExceptions();
    }

    public static void configureServer(int port) {
        logger = LogManager.getLogger(Startup.class);
        port(port);
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
        get("/", (req, resp) -> {
            return getFileContent("/web/home.html");
        });

        get("/categories/", (req, resp) -> getFileContent("/web/categories.html"));

        get("/providers/", (req, resp) -> {
            CloudProvidersController controller = new CloudProvidersController();

            if (req.session().attribute(CloudProvidersController.SELECTED_PROVIDER) != null && controller.isInSync(req.session())) {
                resp.redirect(controller.save(req.session().attribute(CloudProvidersController.SELECTED_PROVIDER), req).toUrlParams("/"));
                return null;
            } else {
                return getFileContent("/web/chooseProvider.html");
            }
        });

        get("/api/providers", (req, resp) -> new CloudProvidersController().getProviders().toJson());

        get("/api/providers/:syncProvider/sync", (req, resp) -> {
            CloudProvidersController controller = new CloudProvidersController();

            if (controller.isInSync(req.session())) {
                resp.redirect(controller.save(req.params(":syncProvider"), req).toUrlParams("/"));
            } else {
                String syncUrl = controller.getSyncUrl(req.params(":syncProvider"), req.session());
                logger.debug("Redireccionando a URL {} del proveedor {}", syncUrl, req.params(":syncProvider"));
                resp.redirect(syncUrl);
            }
            return null;
        });

        get("/api/providers/:syncProvider/auth", (req, resp) -> {
            CloudProvidersController controller = new CloudProvidersController();
            Map<String, String> params = getBodyParams(req.body());

            boolean isFirstSync = !controller.isInSync(req.session());

            JsonResponse result = controller.sync(req.params(":syncProvider"), req, params.get("token"), req.session());
            if (result.getCode() != 0) {
                resp.redirect(result.toUrlParams("/"));
                return null;
            }

            if (isFirstSync) {
                result = controller.load(req.params(":syncProvider"), req);
            } else {
                result = controller.save(req.params(":syncProvider"), req);
            }

            logger.debug("Redireccionando a URL {} del proveedor {}", result.toUrlParams("/"), req.params(":syncProvider"));
            resp.redirect(result.toUrlParams("/"));
            return null;
        });

        get("/api/categories", (req, resp) -> {
            return new CategoriesController(req.session().id()).get().toJson();
        });

        post("/api/categories/:categoryName", (req, resp) -> {
            return new CategoriesController(req.session().id()).add(new CategoryRecord(-1, req.params(":categoryName"))).toJson();
        });

        put("/api/categories/:categoryId/:categoryName", (req, resp) -> {
            return new CategoriesController(req.session().id()).edit(new CategoryRecord(Integer.parseInt(req.params(":categoryId")), req.params(":categoryName"))).toJson();
        });

        delete("/api/categories/:categoryId", (req, resp) -> {
            return new CategoriesController(req.session().id()).delete(new CategoryRecord(Integer.parseInt(req.params(":categoryId")), "")).toJson();
        });

        get("/api/categories/:categoryId/tasks/:sortBy", (req, resp) -> {
            return new TasksController(req.session().id()).getTasks(Integer.parseInt(req.params(":categoryId")), Integer.parseInt(req.params(":sortBy"))).toJson();
        });

        post("/api/categories/:categoryId/tasks/:taskName", (req, resp) -> {
            return new TasksController(req.session().id()).add(new TaskRecord(req.params(":taskName"), Integer.parseInt(req.params(":categoryId")))).toJson();
        });

        put("/api/categories/:categoryId/tasks/:taskId", (req, resp) -> {
            TasksController controller = new TasksController(req.session().id());
            TaskRecord task = controller.getRepo().getTask(Integer.parseInt(req.params(":taskId")));
            Map<String, String> params = getBodyParams(req.body());

            task.set(Task.TASK.DESCRIPTION, params.getOrDefault("description", task.getDescription()));
            task.set(Task.TASK.PRIORITY, Integer.parseInt(params.getOrDefault("priority", String.valueOf(task.getPriority()))));
            task.set(Task.TASK.STATUS, params.getOrDefault("status", task.getStatus()));

            String taskTime = params.getOrDefault("date", null);
            if (taskTime != null) {
                try {
                    task.set(Task.TASK.TIME, taskTime.isEmpty() ? null : new BigDecimal(inputDFmt.parse(taskTime).getTime()));
                } catch (ParseException e) {
                    logger.error("No se pudo formatear la fecha " + taskTime, e);
                }
            }

            return controller.edit(task).toJson();
        });

        delete("/api/categories/:categoryId/doneTasks/", (req, resp) -> {
            return new TasksController(req.session().id()).deleteDoneTasks().toJson();
        });

        delete("/api/categories/:categoryId/tasks/:taskId", (req, resp) -> {
            return new TasksController(req.session().id()).delete(new TaskRecord(Integer.parseInt(req.params(":taskId")))).toJson();
        });
    }

    public static void configureExceptions() {
        exception(IOException.class, (e, req, resp) -> {
            logger.error("Excepción ocurrida durante la ejecución de una ruta: " + e.getMessage(), e);
        });

        internalServerError((req, res) -> {
            if (req.url().contains("/api/")) {
                res.type("application/json");
                return JsonResponse.fromCode(1000).toJson();
            } else {
                res.redirect(JsonResponse.fromCode(1000).toUrlParams("/"));
                return null;
            }
        });
    }

    protected static void logRequest(Request req) {
        logger.debug("Request received: " + req.requestMethod() + " " + req.url() + " - " + req.body());
    }

    protected static void logResponse(Response resp) {
        //logger.debug("Response sent: " + resp.status() + " " + resp.type() + " " + resp.body());
    }

    protected static Map<String, String> getBodyParams(String requestBody) {
        List<NameValuePair> URLparams = URLEncodedUtils.parse(requestBody, StandardCharsets.UTF_8);
        Map<String, String> params = new HashMap<>(0);
        for (NameValuePair param : URLparams) {
            params.put(param.getName(), param.getValue());
        }

        return params;
    }

    /**
     * Configuración para leer el valor del puerto de una variable de entorno (Ej. Heroku)
     * http://sparkjava.com/tutorials/heroku
     */
    public static int getEnvironmentPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8082; //return default port if env-port isn't set (i.e. on localhost)
    }

    private static String getFileContent(String fileName) {
        try {
            return new BufferedReader(new InputStreamReader(Startup.class.getResourceAsStream(fileName)))
                    .lines().collect(Collectors.joining("\n"));

        } catch (Exception e) {
            logger.error("Excepción tratando de obtener el contenido del archivo " + fileName, e);
        }
        return null;
    }
}

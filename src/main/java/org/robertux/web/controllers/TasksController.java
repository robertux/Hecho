package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.ConnetcionManager;
import org.robertux.data.TasksRepository;
import org.robertux.data.jooq.tables.Task;
import org.robertux.data.jooq.tables.records.TaskRecord;
import org.robertux.data.model.JsonResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/16/17.
 */
public class TasksController {
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_PRIORITY = 2;
    private TasksRepository repo;
    private Logger logger;

    public TasksController(String sessionId) throws IOException {
        this.logger = LogManager.getLogger(this.getClass());
        this.repo = new TasksRepository(ConnetcionManager.getDatabasePath(sessionId));
    }

    public TasksRepository getRepo() {
        return repo;
    }

    public JsonResponse getTasks(int categoryId, int sortMethod) {
        JsonArray arr = new JsonArray();
        JsonResponse jresp = new JsonResponse();
        List<TaskRecord> tasks = new ArrayList<>(0);

        switch (sortMethod) {
            case SORT_BY_DATE:
                tasks.addAll(repo.getTasks(Task.TASK.TIME, categoryId));
                break;
            case SORT_BY_PRIORITY:
                tasks.addAll(repo.getTasks(Task.TASK.PRIORITY, categoryId));
                break;
        }

        for (TaskRecord task : tasks) {
            arr.add(task.toJson());
        }

        jresp.getContent().add("tasks", arr);
        this.logger.debug("Devolviendo tareas " + jresp.toString());
        return jresp;
    }

    public JsonResponse get(int taskId) {
        JsonResponse resp = new JsonResponse();
        TaskRecord task = this.repo.getTask(taskId);

        if (task != null) {
            resp.getContent().add("task", task.toJson());
        } else {
            resp = JsonResponse.fromCode(1104);
        }

        return resp;
    }

    public JsonResponse add(TaskRecord task) {
        JsonResponse resp = JsonResponse.OK;
        this.logger.debug("Agregando tarea " + task);

        if (this.repo.addTask(task) == 0) {
            resp = JsonResponse.fromCode(1101);
        }

        return resp;
    }

    public JsonResponse edit(TaskRecord task) {
        JsonResponse resp = JsonResponse.OK;
        this.logger.debug("Actualizando tarea " + task);

        if (this.repo.updateTask(task) == 0) {
            resp = JsonResponse.fromCode(1102);
        }

        return resp;
    }

    public JsonResponse delete(TaskRecord task) {
        JsonResponse resp = JsonResponse.OK;
        this.logger.debug("Eliminando tarea " + task);

        if (this.repo.deleteTask(task) == 0) {
            resp = JsonResponse.fromCode(1103);
        }

        return resp;
    }

    public JsonResponse deleteDoneTasks() {
        JsonResponse resp = JsonResponse.OK;
        this.logger.debug("Eliminando tareas finalizadas");

        if (this.repo.deleteDoneTasks() == 0) {
            resp = JsonResponse.fromCode(1105);
        }

        return resp;
    }
}

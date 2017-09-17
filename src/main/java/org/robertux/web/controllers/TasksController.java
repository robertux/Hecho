package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.robertux.data.jooq.tables.records.TaskRecord;
import org.robertux.data.model.JsonResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/16/17.
 */
public class TasksController {

    public JsonResponse getTasks(int categoryId) {
        List<TaskRecord> tasks = new ArrayList<>(0);
        JsonArray arr = new JsonArray();
        JsonResponse jresp = new JsonResponse();

        for (TaskRecord task : tasks) {
            arr.add(task.toJson());
        }

        jresp.getContent().add("tasks", arr);
        return jresp;
    }
}

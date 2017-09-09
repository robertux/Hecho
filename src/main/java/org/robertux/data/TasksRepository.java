package org.robertux.data;

import org.robertux.data.model.Category;
import org.robertux.data.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/8/17.
 */
public class TasksRepository {

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>(0);

        return categories;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(0);

        return tasks;
    }
}

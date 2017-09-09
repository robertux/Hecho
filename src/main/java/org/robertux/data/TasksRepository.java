package org.robertux.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.robertux.data.jooq.tables.Category;
import org.robertux.data.jooq.tables.Task;
import org.robertux.data.jooq.tables.records.CategoryRecord;
import org.robertux.data.jooq.tables.records.TaskRecord;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/8/17.
 */
public class TasksRepository {
    private Logger logger;

    public TasksRepository() {
        this.logger = LogManager.getLogger(this.getClass());
    }

    public List<CategoryRecord> getCategories() {
        List<CategoryRecord> categories = new ArrayList<>(0);

        try (Connection cn = ConnetcionManager.getConnection()) {
            DSLContext context = ConnetcionManager.getContext(cn);
            categories.addAll(context.selectFrom(Category.CATEGORY).fetch());

        } catch (SQLException | IOException | ClassNotFoundException ex) {
            this.logger.error("Error tratando de obtener las categorías: " + ex.getMessage(), ex);
        }

        return categories;
    }

    public List<TaskRecord> getTasks(TableField<TaskRecord, ?> orderBy) {
        List<TaskRecord> tasks = new ArrayList<>(0);

        try (Connection cn = ConnetcionManager.getConnection()) {
            DSLContext context = ConnetcionManager.getContext(cn);
            tasks.addAll(context.selectFrom(Task.TASK).orderBy(orderBy).fetch());

        } catch (SQLException | IOException | ClassNotFoundException ex) {
            this.logger.error("Error tratando de obtener las categorías: " + ex.getMessage(), ex);
        }

        return tasks;
    }

    public List<TaskRecord> getTasks() {
        return getTasks(Task.TASK.ID);
    }
}

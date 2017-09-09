package org.robertux.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.TableField;
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

    public List<TaskRecord> getTasks(TableField<TaskRecord, ?> orderBy, CategoryRecord cat) {
        List<TaskRecord> tasks = new ArrayList<>(0);

        try (Connection cn = ConnetcionManager.getConnection()) {
            DSLContext context = ConnetcionManager.getContext(cn);
            tasks.addAll(context.selectFrom(Task.TASK).where(Task.TASK.CATEGORYID.eq(cat.getId())).orderBy(orderBy).fetch());

        } catch (SQLException | IOException | ClassNotFoundException ex) {
            this.logger.error("Error tratando de obtener las categorías: " + ex.getMessage(), ex);
        }

        return tasks;
    }

    public List<TaskRecord> getTasks(CategoryRecord cat) {
        return getTasks(Task.TASK.ID, cat);
    }

    public int addTask(TaskRecord task) {
        try (Connection cn = ConnetcionManager.getConnection()) {
            DSLContext context = ConnetcionManager.getContext(cn);
            return context.insertInto(Task.TASK)
                    .set(Task.TASK.DESCRIPTION, task.getDescription())
                    .set(Task.TASK.TIME, task.getTime())
                    .set(Task.TASK.CATEGORYID, task.getCategoryid())
                    .set(Task.TASK.STATUS, task.getStatus())
                    .set(Task.TASK.PRIORITY, task.getPriority()).execute();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            this.logger.error("Error tratando de agregar la tarea: " + e.getMessage(), e);
        }

        return 0;
    }

    public int updateTask(TaskRecord task) {
        return task.update();
    }

    public int deleteTask(TaskRecord task) {
        return task.delete();
    }
}

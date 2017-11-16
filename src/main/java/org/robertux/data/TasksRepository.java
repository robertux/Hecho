package org.robertux.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.robertux.data.jooq.tables.Task;
import org.robertux.data.jooq.tables.records.TaskRecord;
import org.robertux.data.model.Status;

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
    private String dbPath;

    public TasksRepository(String dbPath) {
        this.logger = LogManager.getLogger(this.getClass());
        this.dbPath = dbPath;
    }

    public List<TaskRecord> getTasks(TableField<TaskRecord, ?> orderBy) {
        List<TaskRecord> tasks = new ArrayList<>(0);

        try (Connection cn = ConnectionManager.getConnection(dbPath)) {
            DSLContext context = ConnectionManager.getContext(cn);
            tasks.addAll(context.selectFrom(Task.TASK).orderBy(orderBy).fetch());

        } catch (SQLException | IOException | ClassNotFoundException ex) {
            this.logger.error("Error tratando de obtener las tareas: " + ex.getMessage(), ex);
        }

        return tasks;
    }

    public List<TaskRecord> getTasks() {
        return getTasks(Task.TASK.ID);
    }

    public List<TaskRecord> getTasks(TableField<TaskRecord, ?> orderBy, int categoryId) {
        List<TaskRecord> tasks = new ArrayList<>(0);

        try (Connection cn = ConnectionManager.getConnection(dbPath)) {
            DSLContext context = ConnectionManager.getContext(cn);
            tasks.addAll(context.selectFrom(Task.TASK).where(Task.TASK.CATEGORYID.eq(categoryId)).orderBy(Task.TASK.STATUS.desc(), orderBy.desc()).fetch());

        } catch (SQLException | IOException | ClassNotFoundException ex) {
            this.logger.error("Error tratando de obtener las tareas: " + ex.getMessage(), ex);
        }

        return tasks;
    }

    public List<TaskRecord> getTasks(int categoryId) {
        return getTasks(Task.TASK.ID, categoryId);
    }

    public TaskRecord getTask(int taskId) {
        TaskRecord task = null;

        try (Connection cn = ConnectionManager.getConnection(dbPath)) {
            DSLContext context = ConnectionManager.getContext(cn);
            task = context.selectFrom(Task.TASK).where(Task.TASK.ID.eq(taskId)).fetchOne();
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            this.logger.error("Error tratando de obtener la tarea con ID " + taskId + " +: " + ex.getMessage(), ex);
        }

        return task;
    }

    public int addTask(TaskRecord task) {
        try (Connection cn = ConnectionManager.getConnection(dbPath)) {
            DSLContext context = ConnectionManager.getContext(cn);
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
        try (Connection cn = ConnectionManager.getConnection(dbPath)) {
            DSLContext context = ConnectionManager.getContext(cn);
            return context.update(Task.TASK)
                    .set(Task.TASK.DESCRIPTION, task.getDescription())
                    .set(Task.TASK.TIME, task.getTime())
                    .set(Task.TASK.CATEGORYID, task.getCategoryid())
                    .set(Task.TASK.STATUS, task.getStatus())
                    .set(Task.TASK.PRIORITY, task.getPriority())
                    .where(Task.TASK.ID.eq(task.getId())).execute();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            this.logger.error("Error tratando de modificar la tarea: " + e.getMessage(), e);
        }

        return 0;
    }

    public int deleteTask(TaskRecord task) {
        try (Connection cn = ConnectionManager.getConnection(dbPath)) {
            DSLContext context = ConnectionManager.getContext(cn);
            return context.deleteFrom(Task.TASK)
                    .where(Task.TASK.ID.eq(task.getId())).execute();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            this.logger.error("Error tratando de eliminar la tarea: " + e.getMessage(), e);
        }

        return 0;
    }

    public int deleteDoneTasks() {
        try (Connection cn = ConnectionManager.getConnection(dbPath)) {
            DSLContext context = ConnectionManager.getContext(cn);
            return context.deleteFrom(Task.TASK)
                    .where(Task.TASK.STATUS.eq(Status.DONE.getValue())).execute();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            this.logger.error("Error tratando de eliminar la tarea: " + e.getMessage(), e);
        }

        return 0;
    }
}

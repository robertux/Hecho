package org.robertux.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robertux.data.jooq.tables.records.TaskRecord;
import org.robertux.data.model.Priority;
import org.robertux.data.model.Status;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by robertux on 9/9/17.
 */
public class TasksRepositoryTest {
    private TasksRepository repo;

    @Before
    public void setUp() throws Exception {
        this.repo = new TasksRepository(ConnectionManager.getDatabasePath("_"));
    }

    @Test
    public void test() {
        TaskRecord task = new TaskRecord(0, "TaskTest", new BigDecimal(new Date().getTime()), 0, Status.PENDING.getValue(), Priority.NORMAL.getValue());
        Assert.assertEquals("La tarea debe ser agregada a la base de datos", 1, repo.addTask(task));

        Assert.assertTrue("La tarea con nombre 'TaskTest' debe existir en la base de datos", repo.getTasks().stream().filter(t -> t.getDescription().equals("TaskTest")).count() > 0);

        task = repo.getTasks().stream().filter(t -> t.getDescription().equals("TaskTest")).findFirst().get();

        task.setDescription("TaskTest2");
        repo.updateTask(task);

        Assert.assertTrue("La tarea con nombre 'TaskTest2' debe existir en la base de datos", repo.getTasks().stream().filter(t -> t.getDescription().equals("TaskTest2")).count() > 0);

        repo.deleteTask(task);

        Assert.assertTrue("La tarea con nombre 'TaskTest2' debe estar eliminada de la base de datos", repo.getTasks().stream().filter(t -> t.getDescription().equals("TaskTest2")).count() == 0);
    }

}
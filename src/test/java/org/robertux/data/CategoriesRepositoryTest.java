package org.robertux.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robertux.data.jooq.tables.records.CategoryRecord;

/**
 * Created by robertux on 9/9/17.
 */
public class CategoriesRepositoryTest {
    private CategoriesRepository repo;

    @Before
    public void setUp() throws Exception {
        repo = new CategoriesRepository(ConnectionManager.getDatabasePath("_"));
    }

    @Test
    public void test() throws Exception {
        CategoryRecord cat = new CategoryRecord(0, "CategoriaTest");
        Assert.assertEquals("La categoría debe ser agregada a la base de datos", 1, repo.addCategory(cat));

        Assert.assertTrue("La categoría con nombre 'CategoriaTest' debe existir en la base de datos", repo.getCategories().stream().filter(c -> c.getName().equals("CategoriaTest")).count() > 0);

        cat = repo.getCategories().stream().filter(c -> c.getName().equals("CategoriaTest")).findFirst().get();

        cat.setName("CategoriaTest2");
        repo.updateCategory(cat);

        Assert.assertTrue("La categoría con nombre 'CategoriaTest2' debe existir en la base de datos", repo.getCategories().stream().filter(c -> c.getName().equals("CategoriaTest2")).count() > 0);

        repo.deleteCategory(cat);

        Assert.assertTrue("La categoría con nombre 'CategoriaTest2' debe estar eliminada de la base de datos", repo.getCategories().stream().filter(c -> c.getName().equals("CategoriaTest2")).count() == 0);
    }
}
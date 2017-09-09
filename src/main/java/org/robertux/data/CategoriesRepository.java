package org.robertux.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.DSLContext;
import org.robertux.data.jooq.tables.Category;
import org.robertux.data.jooq.tables.records.CategoryRecord;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/9/17.
 */
public class CategoriesRepository {
    private Logger logger;

    public CategoriesRepository() {
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

    public int addCategory(CategoryRecord cat) {
        try (Connection cn = ConnetcionManager.getConnection()) {
            DSLContext context = ConnetcionManager.getContext(cn);
            return context.insertInto(Category.CATEGORY)
                    .set(Category.CATEGORY.NAME, cat.getName()).execute();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            this.logger.error("Error tratando de agregar la categoría: " + e.getMessage(), e);
        }

        return 0;
    }

    public int updateCategory(CategoryRecord cat) {
        try (Connection cn = ConnetcionManager.getConnection()) {
            DSLContext context = ConnetcionManager.getContext(cn);
            return context.update(Category.CATEGORY)
                    .set(Category.CATEGORY.NAME, cat.getName())
                    .where(Category.CATEGORY.ID.eq(cat.getId())).execute();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            this.logger.error("Error tratando de modificar la categoría: " + e.getMessage(), e);
        }

        return 0;
    }

    public int deleteCategory(CategoryRecord cat) {
        try (Connection cn = ConnetcionManager.getConnection()) {
            DSLContext context = ConnetcionManager.getContext(cn);
            return context.deleteFrom(Category.CATEGORY)
                    .where(Category.CATEGORY.ID.eq(cat.getId())).execute();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            this.logger.error("Error tratando de eliminar la categoría: " + e.getMessage(), e);
        }

        return 0;
    }
}

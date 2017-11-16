package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robertux.data.CategoriesRepository;
import org.robertux.data.jooq.tables.records.CategoryRecord;
import org.robertux.data.model.JsonResponse;

import java.util.List;

/**
 * Created by robertux on 9/16/17.
 */
public class CategoriesController {
    private CategoriesRepository repo;
    private Logger logger;

    public CategoriesController(String sessionId) {
        this.logger = LogManager.getLogger(this.getClass());
        this.repo = new CategoriesRepository(sessionId);
    }

    public JsonResponse get() {
        List<CategoryRecord> categories = this.repo.getCategories();
        JsonArray arr = new JsonArray();
        JsonResponse jresp = new JsonResponse();

        if (categories.size() == 0) {
            CategoryRecord cat = new CategoryRecord(1, "General");
            this.repo.addCategory(cat);
            categories.add(cat);
        }

        for (CategoryRecord cat : categories) {
            arr.add(cat.toJson());
        }
        jresp.getContent().add("categories", arr);
        this.logger.debug("Retornando categorías " + jresp.toString());
        return jresp;
    }

    public JsonResponse add(CategoryRecord cat) {
        JsonResponse resp = JsonResponse.OK;
        this.logger.debug("Agregando categoría " + cat);

        if (this.repo.addCategory(cat) == 0) {
            resp = JsonResponse.fromCode(1001);
        }

        return resp;
    }

    public JsonResponse edit(CategoryRecord cat) {
        JsonResponse resp = JsonResponse.OK;
        this.logger.debug("Actualizando categoría " + cat);

        if (this.repo.updateCategory(cat) == 0) {
            resp = JsonResponse.fromCode(1002);
        }

        return resp;
    }

    public JsonResponse delete(CategoryRecord cat) {
        JsonResponse resp = JsonResponse.OK;
        this.logger.debug("Eliminando categoría " + cat);

        if (this.repo.deleteCategory(cat) == 0) {
            resp = JsonResponse.fromCode(1003);
        }

        return resp;
    }
}

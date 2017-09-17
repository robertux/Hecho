package org.robertux.web.controllers;

import com.google.gson.JsonArray;
import org.robertux.data.jooq.tables.records.CategoryRecord;
import org.robertux.data.model.JsonResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertux on 9/16/17.
 */
public class CategoriesController {

    public JsonResponse getCategories() {
        List<CategoryRecord> categories = new ArrayList<>(0);
        JsonArray arr = new JsonArray();
        JsonResponse jresp = new JsonResponse();

        for (CategoryRecord cat : categories) {
            arr.add(cat.toString());
        }
        jresp.getContent().add("categories", arr);
        return jresp;
    }
}

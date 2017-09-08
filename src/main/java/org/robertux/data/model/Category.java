package org.robertux.data.model;

import lombok.Data;

/**
 * Created by robertux on 9/8/17.
 */
@Data
public class Category implements Comparable<Category> {
    public static final Category GENERAL = new Category(0l, "GENERAL");

    private long id;
    private String name;

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public int compareTo(Category o) {
        return this.name.compareTo(o.getName());
    }
}

package org.robertux.data.model;

import lombok.Data;

/**
 * Created by robertux on 9/8/17.
 */
@Data
public class Status implements Comparable<Status> {
    public static final Status PENDING = new Status("P");
    public static final Status IN_PROGRESS = new Status("I");
    public static final Status DONE = new Status("D");

    private String value;

    public Status(String value) {
        this.value = value;
    }

    public int compareTo(Status o) {
        return this.value.compareTo(o.getValue());
    }
}

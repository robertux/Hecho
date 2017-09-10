package org.robertux.data.model;

/**
 * Created by robertux on 9/8/17.
 */
public enum Status {
    PENDING("P"),
    IN_PROGRESS("I"),
    DONE("D");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

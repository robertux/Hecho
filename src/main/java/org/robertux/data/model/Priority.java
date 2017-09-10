package org.robertux.data.model;

/**
 * Created by robertux on 9/9/17.
 */
public enum Priority {
    NORMAL(0),
    LOW(-1),
    HIGH(1);

    private int value;

    Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

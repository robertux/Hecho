package org.robertux.data.model;

/**
 * Created by robertux on 9/9/17.
 */
public class Priority implements Comparable<Priority> {
    public static final Priority NORMAL = new Priority(0);
    public static final Priority LOW = new Priority(-1);
    public static final Priority HIGH = new Priority(1);
    private int value;

    public Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Priority o) {
        return new Integer(this.value).compareTo(o.getValue());
    }
}

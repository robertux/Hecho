package org.robertux.data.model;

import lombok.Data;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by robertux on 9/8/17.
 */
@Data
public class Task implements Comparable<Task> {
    private long id;
    private String description;
    private Date time;
    private Category category;
    private Status status = Status.PENDING;

    public int compareTo(Task o) {
        return new Long(this.id).compareTo(o.getId());
    }
}

class ByTimeComparator implements Comparator<Task> {
    public int compare(Task o1, Task o2) {
        return o1.getTime().compareTo(o2.getTime());
    }
}

class ByCategoryComparator implements Comparator<Task> {
    public int compare(Task o1, Task o2) {
        return o1.getCategory().compareTo(o2.getCategory());
    }
}

class ByStatusComparator implements Comparator<Task> {
    public int compare(Task o1, Task o2) {
        return o1.getStatus().compareTo(o2.getStatus());
    }
}
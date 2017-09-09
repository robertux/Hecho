/*
 * This file is generated by jOOQ.
*/
package org.robertux.data.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;
import org.robertux.data.jooq.tables.Task;

import javax.annotation.Generated;
import java.math.BigDecimal;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.9.1"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class TaskRecord extends UpdatableRecordImpl<TaskRecord> implements Record6<Integer, String, BigDecimal, Integer, String, Integer> {

    private static final long serialVersionUID = 1241988124;

    /**
     * Create a detached TaskRecord
     */
    public TaskRecord() {
        super(Task.TASK);
    }

    /**
     * Create a detached, initialised TaskRecord
     */
    public TaskRecord(Integer id, String description, BigDecimal time, Integer categoryid, String status, Integer priority) {
        super(Task.TASK);

        set(0, id);
        set(1, description);
        set(2, time);
        set(3, categoryid);
        set(4, status);
        set(5, priority);
    }

    /**
     * Getter for <code>Task.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>Task.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>Task.description</code>.
     */
    public String getDescription() {
        return (String) get(1);
    }

    /**
     * Setter for <code>Task.description</code>.
     */
    public void setDescription(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>Task.time</code>.
     */
    public BigDecimal getTime() {
        return (BigDecimal) get(2);
    }

    /**
     * Setter for <code>Task.time</code>.
     */
    public void setTime(BigDecimal value) {
        set(2, value);
    }

    /**
     * Getter for <code>Task.categoryId</code>.
     */
    public Integer getCategoryid() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>Task.categoryId</code>.
     */
    public void setCategoryid(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>Task.status</code>.
     */
    public String getStatus() {
        return (String) get(4);
    }

    /**
     * Setter for <code>Task.status</code>.
     */
    public void setStatus(String value) {
        set(4, value);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>Task.priority</code>.
     */
    public Integer getPriority() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>Task.priority</code>.
     */
    public void setPriority(Integer value) {
        set(5, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, BigDecimal, Integer, String, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, BigDecimal, Integer, String, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Task.TASK.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Task.TASK.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<BigDecimal> field3() {
        return Task.TASK.TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field4() {
        return Task.TASK.CATEGORYID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Task.TASK.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return Task.TASK.PRIORITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal value3() {
        return getTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value4() {
        return getCategoryid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getPriority();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskRecord value2(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskRecord value3(BigDecimal value) {
        setTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskRecord value4(Integer value) {
        setCategoryid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskRecord value5(String value) {
        setStatus(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskRecord value6(Integer value) {
        setPriority(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskRecord values(Integer value1, String value2, BigDecimal value3, Integer value4, String value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }
}

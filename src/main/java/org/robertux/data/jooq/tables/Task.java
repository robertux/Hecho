/*
 * This file is generated by jOOQ.
*/
package org.robertux.data.jooq.tables;


import org.jooq.*;
import org.jooq.impl.TableImpl;
import org.robertux.data.jooq.DefaultSchema;
import org.robertux.data.jooq.Keys;
import org.robertux.data.jooq.tables.records.TaskRecord;

import javax.annotation.Generated;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


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
public class Task extends TableImpl<TaskRecord> {

    /**
     * The reference instance of <code>Task</code>
     */
    public static final Task TASK = new Task();
    private static final long serialVersionUID = -460837304;
    /**
     * The column <code>Task.id</code>.
     */
    public final TableField<TaskRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");
    /**
     * The column <code>Task.description</code>.
     */
    public final TableField<TaskRecord, String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");
    /**
     * The column <code>Task.time</code>.
     */
    public final TableField<TaskRecord, BigDecimal> TIME = createField("time", org.jooq.impl.SQLDataType.NUMERIC, this, "");
    /**
     * The column <code>Task.categoryId</code>.
     */
    public final TableField<TaskRecord, Integer> CATEGORYID = createField("categoryId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");
    /**
     * The column <code>Task.status</code>.
     */
    public final TableField<TaskRecord, String> STATUS = createField("status", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");
    /**
     * The column <code>Task.priority</code>.
     */
    public final TableField<TaskRecord, Integer> PRIORITY = createField("priority", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>Task</code> table reference
     */
    public Task() {
        this("Task", null);
    }

    /**
     * Create an aliased <code>Task</code> table reference
     */
    public Task(String alias) {
        this(alias, TASK);
    }

    private Task(String alias, Table<TaskRecord> aliased) {
        this(alias, aliased, null);
    }

    private Task(String alias, Table<TaskRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TaskRecord> getRecordType() {
        return TaskRecord.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<TaskRecord> getPrimaryKey() {
        return Keys.PK_TASK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<TaskRecord>> getKeys() {
        return Arrays.<UniqueKey<TaskRecord>>asList(Keys.PK_TASK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task as(String alias) {
        return new Task(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Task rename(String name) {
        return new Task(name, null);
    }
}
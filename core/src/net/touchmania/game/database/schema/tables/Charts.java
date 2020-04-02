/*
 * This file is generated by jOOQ.
*/
package net.touchmania.game.database.schema.tables;


import net.touchmania.game.database.schema.DefaultSchema;
import net.touchmania.game.database.schema.Keys;
import net.touchmania.game.database.schema.tables.records.ChartsRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Charts extends TableImpl<ChartsRecord> {

    private static final long serialVersionUID = 1629770488;

    /**
     * The reference instance of <code>charts</code>
     */
    public static final Charts CHARTS = new Charts();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ChartsRecord> getRecordType() {
        return ChartsRecord.class;
    }

    /**
     * The column <code>charts.id</code>.
     */
    public final TableField<ChartsRecord, String> ID = createField("id", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>charts.song_id</code>.
     */
    public final TableField<ChartsRecord, Integer> SONG_ID = createField("song_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>charts.hash</code>.
     */
    public final TableField<ChartsRecord, String> HASH = createField("hash", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>charts.difficulty_class</code>.
     */
    public final TableField<ChartsRecord, String> DIFFICULTY_CLASS = createField("difficulty_class", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>charts.difficulty_meter</code>.
     */
    public final TableField<ChartsRecord, Integer> DIFFICULTY_METER = createField("difficulty_meter", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>charts.display_bpm</code>.
     */
    public final TableField<ChartsRecord, String> DISPLAY_BPM = createField("display_bpm", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>charts.name</code>.
     */
    public final TableField<ChartsRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>charts.description</code>.
     */
    public final TableField<ChartsRecord, String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>charts.credit</code>.
     */
    public final TableField<ChartsRecord, String> CREDIT = createField("credit", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * Create a <code>charts</code> table reference
     */
    public Charts() {
        this("charts", null);
    }

    /**
     * Create an aliased <code>charts</code> table reference
     */
    public Charts(String alias) {
        this(alias, CHARTS);
    }

    private Charts(String alias, Table<ChartsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Charts(String alias, Table<ChartsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
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
    public UniqueKey<ChartsRecord> getPrimaryKey() {
        return Keys.PK_CHARTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ChartsRecord>> getKeys() {
        return Arrays.<UniqueKey<ChartsRecord>>asList(Keys.PK_CHARTS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ChartsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ChartsRecord, ?>>asList(Keys.FK_CHARTS_SONGS_1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Charts as(String alias) {
        return new Charts(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Charts rename(String name) {
        return new Charts(name, null);
    }
}

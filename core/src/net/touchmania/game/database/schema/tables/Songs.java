/*
 * This file is generated by jOOQ.
*/
package net.touchmania.game.database.schema.tables;


import net.touchmania.game.database.schema.DefaultSchema;
import net.touchmania.game.database.schema.Keys;
import net.touchmania.game.database.schema.tables.records.SongsRecord;
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
public class Songs extends TableImpl<SongsRecord> {

    private static final long serialVersionUID = 438780546;

    /**
     * The reference instance of <code>songs</code>
     */
    public static final Songs SONGS = new Songs();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SongsRecord> getRecordType() {
        return SongsRecord.class;
    }

    /**
     * The column <code>songs.id</code>.
     */
    public final TableField<SongsRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>songs.pack</code>.
     */
    public final TableField<SongsRecord, String> PACK = createField("pack", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>songs.directory</code>.
     */
    public final TableField<SongsRecord, String> DIRECTORY = createField("directory", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>songs.hash</code>.
     */
    public final TableField<SongsRecord, String> HASH = createField("hash", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>songs.sim_path</code>.
     */
    public final TableField<SongsRecord, String> SIM_PATH = createField("sim_path", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>songs.format</code>.
     */
    public final TableField<SongsRecord, String> FORMAT = createField("format", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>songs.title</code>.
     */
    public final TableField<SongsRecord, String> TITLE = createField("title", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>songs.subtitle</code>.
     */
    public final TableField<SongsRecord, String> SUBTITLE = createField("subtitle", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>songs.artist</code>.
     */
    public final TableField<SongsRecord, String> ARTIST = createField("artist", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>songs.genre</code>.
     */
    public final TableField<SongsRecord, String> GENRE = createField("genre", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>songs.banner_path</code>.
     */
    public final TableField<SongsRecord, String> BANNER_PATH = createField("banner_path", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>songs.background_path</code>.
     */
    public final TableField<SongsRecord, String> BACKGROUND_PATH = createField("background_path", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>songs.album</code>.
     */
    public final TableField<SongsRecord, String> ALBUM = createField("album", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>songs.music_path</code>.
     */
    public final TableField<SongsRecord, String> MUSIC_PATH = createField("music_path", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>songs.sample_start</code>.
     */
    public final TableField<SongsRecord, Float> SAMPLE_START = createField("sample_start", org.jooq.impl.SQLDataType.REAL.defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.REAL)), this, "");

    /**
     * The column <code>songs.sample_length</code>.
     */
    public final TableField<SongsRecord, Float> SAMPLE_LENGTH = createField("sample_length", org.jooq.impl.SQLDataType.REAL, this, "");

    /**
     * Create a <code>songs</code> table reference
     */
    public Songs() {
        this("songs", null);
    }

    /**
     * Create an aliased <code>songs</code> table reference
     */
    public Songs(String alias) {
        this(alias, SONGS);
    }

    private Songs(String alias, Table<SongsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Songs(String alias, Table<SongsRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<SongsRecord> getPrimaryKey() {
        return Keys.PK_SONGS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SongsRecord>> getKeys() {
        return Arrays.<UniqueKey<SongsRecord>>asList(Keys.PK_SONGS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Songs as(String alias) {
        return new Songs(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Songs rename(String name) {
        return new Songs(name, null);
    }
}

/*
 * This file is generated by jOOQ.
 */
package de.w3is.recipes.infra.persistence.generated.tables;


import de.w3is.recipes.infra.persistence.generated.Indexes;
import de.w3is.recipes.infra.persistence.generated.Keys;
import de.w3is.recipes.infra.persistence.generated.Public;
import de.w3is.recipes.infra.persistence.generated.tables.records.ImagesRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Images extends TableImpl<ImagesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.images</code>
     */
    public static final Images IMAGES = new Images();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ImagesRecord> getRecordType() {
        return ImagesRecord.class;
    }

    /**
     * The column <code>public.images.id</code>.
     */
    public final TableField<ImagesRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.images.image_id</code>.
     */
    public final TableField<ImagesRecord, String> IMAGE_ID = createField(DSL.name("image_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.images.data</code>.
     */
    public final TableField<ImagesRecord, byte[]> DATA = createField(DSL.name("data"), SQLDataType.BLOB.nullable(false), this, "");

    /**
     * The column <code>public.images.thumbnail</code>.
     */
    public final TableField<ImagesRecord, byte[]> THUMBNAIL = createField(DSL.name("thumbnail"), SQLDataType.BLOB.nullable(false), this, "");

    private Images(Name alias, Table<ImagesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Images(Name alias, Table<ImagesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.images</code> table reference
     */
    public Images(String alias) {
        this(DSL.name(alias), IMAGES);
    }

    /**
     * Create an aliased <code>public.images</code> table reference
     */
    public Images(Name alias) {
        this(alias, IMAGES);
    }

    /**
     * Create a <code>public.images</code> table reference
     */
    public Images() {
        this(DSL.name("images"), null);
    }

    public <O extends Record> Images(Table<O> child, ForeignKey<O, ImagesRecord> key) {
        super(child, key, IMAGES);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.IMAGE_ID_IDX);
    }

    @Override
    public Identity<ImagesRecord, Integer> getIdentity() {
        return (Identity<ImagesRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<ImagesRecord> getPrimaryKey() {
        return Keys.IMAGES_PKEY;
    }

    @Override
    public List<UniqueKey<ImagesRecord>> getKeys() {
        return Arrays.<UniqueKey<ImagesRecord>>asList(Keys.IMAGES_PKEY, Keys.IMAGES_IMAGE_ID_KEY);
    }

    @Override
    public Images as(String alias) {
        return new Images(DSL.name(alias), this);
    }

    @Override
    public Images as(Name alias) {
        return new Images(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Images rename(String name) {
        return new Images(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Images rename(Name name) {
        return new Images(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, byte[], byte[]> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
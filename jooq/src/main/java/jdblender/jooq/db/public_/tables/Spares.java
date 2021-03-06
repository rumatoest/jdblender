/**
 * This class is generated by jOOQ
 */
package jdblender.jooq.db.public_.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import jdblender.jooq.db.public_.Keys;
import jdblender.jooq.db.public_.Public;
import jdblender.jooq.db.public_.tables.records.SparesRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.5"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Spares extends TableImpl<SparesRecord> {

    private static final long serialVersionUID = -798921292;

    /**
     * The reference instance of <code>PUBLIC.SPARES</code>
     */
    public static final Spares SPARES = new Spares();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SparesRecord> getRecordType() {
        return SparesRecord.class;
    }

    /**
     * The column <code>PUBLIC.SPARES.ID</code>.
     */
    public final TableField<SparesRecord, Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.SPARES.BRAND_ID</code>.
     */
    public final TableField<SparesRecord, Long> BRAND_ID = createField("BRAND_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.SPARES.NAME</code>.
     */
    public final TableField<SparesRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(16), this, "");

    /**
     * The column <code>PUBLIC.SPARES.LABEL</code>.
     */
    public final TableField<SparesRecord, String> LABEL = createField("LABEL", org.jooq.impl.SQLDataType.CHAR.length(2), this, "");

    /**
     * The column <code>PUBLIC.SPARES.FLAG</code>.
     */
    public final TableField<SparesRecord, Boolean> FLAG = createField("FLAG", org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>PUBLIC.SPARES.NUM</code>.
     */
    public final TableField<SparesRecord, Integer> NUM = createField("NUM", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>PUBLIC.SPARES</code> table reference
     */
    public Spares() {
        this("SPARES", null);
    }

    /**
     * Create an aliased <code>PUBLIC.SPARES</code> table reference
     */
    public Spares(String alias) {
        this(alias, SPARES);
    }

    private Spares(String alias, Table<SparesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Spares(String alias, Table<SparesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<SparesRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_92;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<SparesRecord>> getKeys() {
        return Arrays.<UniqueKey<SparesRecord>>asList(Keys.CONSTRAINT_92);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<SparesRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<SparesRecord, ?>>asList(Keys.CONSTRAINT_922);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Spares as(String alias) {
        return new Spares(alias, this);
    }

    /**
     * Rename this table
     */
    public Spares rename(String name) {
        return new Spares(name, null);
    }
}

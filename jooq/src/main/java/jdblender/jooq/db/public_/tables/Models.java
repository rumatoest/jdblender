/**
 * This class is generated by jOOQ
 */
package jdblender.jooq.db.public_.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import jdblender.jooq.db.public_.Keys;
import jdblender.jooq.db.public_.Public;
import jdblender.jooq.db.public_.tables.records.ModelsRecord;

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
public class Models extends TableImpl<ModelsRecord> {

    private static final long serialVersionUID = 333296511;

    /**
     * The reference instance of <code>PUBLIC.MODELS</code>
     */
    public static final Models MODELS = new Models();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ModelsRecord> getRecordType() {
        return ModelsRecord.class;
    }

    /**
     * The column <code>PUBLIC.MODELS.ID</code>.
     */
    public final TableField<ModelsRecord, Long> ID = createField("ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.MODELS.SERIES_ID</code>.
     */
    public final TableField<ModelsRecord, Long> SERIES_ID = createField("SERIES_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.MODELS.NAME</code>.
     */
    public final TableField<ModelsRecord, String> NAME = createField("NAME", org.jooq.impl.SQLDataType.VARCHAR.length(16), this, "");

    /**
     * Create a <code>PUBLIC.MODELS</code> table reference
     */
    public Models() {
        this("MODELS", null);
    }

    /**
     * Create an aliased <code>PUBLIC.MODELS</code> table reference
     */
    public Models(String alias) {
        this(alias, MODELS);
    }

    private Models(String alias, Table<ModelsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Models(String alias, Table<ModelsRecord> aliased, Field<?>[] parameters) {
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
    public UniqueKey<ModelsRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ModelsRecord>> getKeys() {
        return Arrays.<UniqueKey<ModelsRecord>>asList(Keys.CONSTRAINT_8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ModelsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ModelsRecord, ?>>asList(Keys.CONSTRAINT_87);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Models as(String alias) {
        return new Models(alias, this);
    }

    /**
     * Rename this table
     */
    public Models rename(String name) {
        return new Models(name, null);
    }
}

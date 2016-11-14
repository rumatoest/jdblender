/**
 * This class is generated by jOOQ
 */
package jdblender.jooq.db.public_.tables.records;


import javax.annotation.Generated;

import jdblender.jooq.db.public_.tables.Spares;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


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
public class SparesRecord extends UpdatableRecordImpl<SparesRecord> implements Record6<Long, Long, String, String, Boolean, Integer> {

    private static final long serialVersionUID = -560466889;

    /**
     * Setter for <code>PUBLIC.SPARES.ID</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.SPARES.ID</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>PUBLIC.SPARES.BRAND_ID</code>.
     */
    public void setBrandId(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.SPARES.BRAND_ID</code>.
     */
    public Long getBrandId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>PUBLIC.SPARES.NAME</code>.
     */
    public void setName(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.SPARES.NAME</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>PUBLIC.SPARES.LABEL</code>.
     */
    public void setLabel(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.SPARES.LABEL</code>.
     */
    public String getLabel() {
        return (String) get(3);
    }

    /**
     * Setter for <code>PUBLIC.SPARES.FLAG</code>.
     */
    public void setFlag(Boolean value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.SPARES.FLAG</code>.
     */
    public Boolean getFlag() {
        return (Boolean) get(4);
    }

    /**
     * Setter for <code>PUBLIC.SPARES.NUM</code>.
     */
    public void setNum(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>PUBLIC.SPARES.NUM</code>.
     */
    public Integer getNum() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, Long, String, String, Boolean, Integer> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, Long, String, String, Boolean, Integer> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Spares.SPARES.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Spares.SPARES.BRAND_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Spares.SPARES.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Spares.SPARES.LABEL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field5() {
        return Spares.SPARES.FLAG;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field6() {
        return Spares.SPARES.NUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getBrandId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getLabel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value5() {
        return getFlag();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value6() {
        return getNum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SparesRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SparesRecord value2(Long value) {
        setBrandId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SparesRecord value3(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SparesRecord value4(String value) {
        setLabel(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SparesRecord value5(Boolean value) {
        setFlag(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SparesRecord value6(Integer value) {
        setNum(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SparesRecord values(Long value1, Long value2, String value3, String value4, Boolean value5, Integer value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SparesRecord
     */
    public SparesRecord() {
        super(Spares.SPARES);
    }

    /**
     * Create a detached, initialised SparesRecord
     */
    public SparesRecord(Long id, Long brandId, String name, String label, Boolean flag, Integer num) {
        super(Spares.SPARES);

        set(0, id);
        set(1, brandId);
        set(2, name);
        set(3, label);
        set(4, flag);
        set(5, num);
    }
}

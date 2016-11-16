package jdblender.cayenne.model.auto;

import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

import jdblender.cayenne.model.Brands;
import jdblender.cayenne.model.Models;

/**
 * Class _Series was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Series extends CayenneDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "ID";

    public static final Property<Long> BRAND_ID = new Property<Long>("brandId");
    public static final Property<Long> ID = new Property<Long>("id");
    public static final Property<String> NAME = new Property<String>("name");
    public static final Property<Brands> BRAND = new Property<Brands>("brand");
    public static final Property<List<Models>> MODELSS = new Property<List<Models>>("modelss");

    public void setBrandId(long brandId) {
        writeProperty("brandId", brandId);
    }
    public long getBrandId() {
        Object value = readProperty("brandId");
        return (value != null) ? (Long) value : 0;
    }

    public void setId(long id) {
        writeProperty("id", id);
    }
    public long getId() {
        Object value = readProperty("id");
        return (value != null) ? (Long) value : 0;
    }

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
    }

    public void setBrand(Brands brand) {
        setToOneTarget("brand", brand, true);
    }

    public Brands getBrand() {
        return (Brands)readProperty("brand");
    }


    public void addToModelss(Models obj) {
        addToManyTarget("modelss", obj, true);
    }
    public void removeFromModelss(Models obj) {
        removeToManyTarget("modelss", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Models> getModelss() {
        return (List<Models>)readProperty("modelss");
    }


}
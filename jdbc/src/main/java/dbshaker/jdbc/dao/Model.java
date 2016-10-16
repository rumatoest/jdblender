package dbshaker.jdbc.dao;

import dbshaker.core.domain.SpareObj;
import java.util.Collection;

public class Model implements dbshaker.core.domain.Model, dbshaker.core.domain.ModelObj {

    private long id;

    private long seriesId;

    private String name;

    private Series series;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(long seriesId) {
        this.seriesId = seriesId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    @Override
    public Collection<SpareObj> getSpares() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

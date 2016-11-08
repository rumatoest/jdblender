package jdblender.mybatis.dao;

import java.util.Set;

public class Model implements jdblender.core.domain.Model, jdblender.core.domain.ModelObj {

    private long id;

    private long seriesId;

    private String name;

    private Series series;

    private Set<Spare> spares;

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
    public Set<Spare> getSpares() {
        return spares;
    }

    public void setSpares(Set<Spare> spares) {
        this.spares = spares;
    }
}

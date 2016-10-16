package dbshaker.hibernate.model;

import dbshaker.core.domain.SeriesObj;
import dbshaker.core.domain.SpareObj;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "models")
public class Model implements dbshaker.core.domain.Model, dbshaker.core.domain.ModelObj {

    public Model() {
    }

    public Model(long id, Series series, String name) {
        this.id = id;
        this.series = series;
        this.name = name;
    }

    @Id
    @Column(name = "id")
    private long id;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;

    @Override
    public Series getSeries() {
        return series;
    }

    @Override
    public long getSeriesId() {
        return series.getId();
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    @Column(name = "name")
    private String name;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<SpareObj> getSpares() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

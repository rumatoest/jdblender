package jdblender.sdata.model;

import java.util.ArrayList;
import jdblender.core.domain.SpareObj;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "models")
public class Model implements jdblender.core.domain.Model, jdblender.core.domain.ModelObj {

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "spare_to_model",
        joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "spare_id", referencedColumnName = "id")
    )
    private List<Spare> spares;

    public void setSpares(List<Spare> spares) {
        this.spares = spares;
    }

    @Override
    public List<Spare> getSpares() {
        if (spares == null) {
            return new ArrayList<>();
        }
        return spares;
    }
}

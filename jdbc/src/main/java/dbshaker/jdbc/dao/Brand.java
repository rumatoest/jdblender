package dbshaker.jdbc.dao;

public class Brand implements dbshaker.core.domain.Brand {

    private long id;

    private String name;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

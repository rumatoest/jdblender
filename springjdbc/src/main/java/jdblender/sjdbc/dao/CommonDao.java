package jdblender.sjdbc.dao;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public abstract class CommonDao {

    @Autowired
    DataSource ds;

    JdbcTemplate tpl;

    @PostConstruct
    public void init() {
        tpl = new JdbcTemplate(ds, true);
    }

    JdbcTemplate template() {
        return tpl;
    }
}

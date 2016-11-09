package jdblender.sjdbc;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("jdblender.sjdbc")
public class Config {

    @Bean
    JdbcDataSource dataSourceH2() {
        return new JdbcDataSource();

    }

}

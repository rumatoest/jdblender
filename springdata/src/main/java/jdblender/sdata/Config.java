package jdblender.sdata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan("jdblender.sdata")
@EnableJpaRepositories(basePackages = "jdblender.sdata.dao")
@EnableTransactionManagement
public class Config {

    @Bean
    DataSource dataSourceH2() {
        return H2DS.get();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setShowSql(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("jdblender.sdata.model");
        factory.setDataSource(dataSourceH2());
        Map<String, Object> props = factory.getJpaPropertyMap();
        //props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.hbm2ddl.auto", "validate");
        //props.put("hibernate.jdbc.batch_size", "100");
        //props.put("hibernate.cache.use_second_level_cache", "false");
        //props.put("hibernate.order_inserts", "1");
        //props.put("hibernate.order_updates", "1");
        
        factory.setJpaPropertyMap(props);

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setDataSource(dataSourceH2());
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

}

package com.example.main_service.config;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "BasicDataSourceConfiguration",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.example.main_service.repository"}
)
public class BasicDataSourceConfiguration {

    public Map<String, String> basicJpaProperties() {
        Map<String, String> basicJpaProperties = new HashMap<>();
        basicJpaProperties.put("hibernate.hbm2ddl.auto", "update");
        basicJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        basicJpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        basicJpaProperties.put("hibernate.transaction.jta.platform", "com.atomikos.icatch.jta.hibernate4.AtomikosPlatform");
        basicJpaProperties.put("javax.persistence.transactionType", "JTA");
        return basicJpaProperties;
    }

    @Bean(name = "basicEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder basicEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), basicJpaProperties(), null);
    }

    @Bean(name = "BasicDataSourceConfiguration")
    public LocalContainerEntityManagerFactoryBean basicEntityManager(
            @Qualifier("basicEntityManagerFactoryBuilder") EntityManagerFactoryBuilder basicEntityManagerFactoryBuilder,
            @Qualifier("basicDataSource") DataSource postgresDataSource
    ) {
        return basicEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("com.example.main_service.model")
                .persistenceUnit("postgres")
                .properties(basicJpaProperties())
                .jta(true)
                .build();
    }

    @Bean("basicDataSourceProperties")
    @ConfigurationProperties("spring.datasource.basic")
    public DataSourceProperties basicDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean("basicDataSource")
    public DataSource basicDataSource(@Qualifier("basicDataSourceProperties") DataSourceProperties basicDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(basicDataSourceProperties.getUrl());
        ds.setUser(basicDataSourceProperties.getUsername());
        ds.setPassword(basicDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setUniqueResourceName("xa_basic");
        return xaDataSource;
    }
}

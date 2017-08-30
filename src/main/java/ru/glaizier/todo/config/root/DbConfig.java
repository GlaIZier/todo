package ru.glaizier.todo.config.root;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "ru.glaizier.todo.persistence")
@EnableTransactionManagement
// Todo create postgres docker prod datasource
public class DbConfig {

    @Bean
    @Profile("default")
    public DataSource localDataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(false)
                .setName("MemoryTaskDb")
                .setType(HSQL)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("sql/hsql/schema.sql")
//                .addScript("sql/populate.sql")
                .build();
    }

    @Bean
    @Profile("default")
    public JpaVendorAdapter localJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.HSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");
        return adapter;
    }

    @Bean("entityManagerFactory")
    @Profile("default")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(localDataSource());
        factoryBean.setJpaVendorAdapter(localJpaVendorAdapter());
        factoryBean.setPackagesToScan("ru.glaizier.todo.model.domain");
        return factoryBean;
    }

    // uncomment to select from embedded db in console
//    @PostConstruct
//    public void startDBManager() {
//        //hsqldb
//        DatabaseManagerSwing.main(new String[]{"--url", "jdbc:hsqldb:mem:MemoryTaskDb", "--user", "sa", "--password", ""});
//    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        DriverManagerDataSource dataSourceConfig = new DriverManagerDataSource();
        dataSourceConfig.setDriverClassName("org.postgresql.Driver");

        dataSourceConfig.setUrl("jdbc:postgresql://localhost:5432/tododb");
        dataSourceConfig.setUsername("todoer");
        dataSourceConfig.setPassword("password");

        return dataSourceConfig;
    }

    @Bean
    @Profile("prod")
    public JpaVendorAdapter prodJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
        return adapter;
    }

    @Bean("entityManagerFactory")
    @Profile("prod")
    public LocalContainerEntityManagerFactoryBean prodEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(prodDataSource());
        factoryBean.setJpaVendorAdapter(prodJpaVendorAdapter());
        factoryBean.setPackagesToScan("ru.glaizier.todo.model.domain");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

}

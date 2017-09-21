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
@Profile({"default", "prod"})
// Todo create postgres docker prod datasource
public class DbConfig {

    @Bean
    @Profile("default")
//    @Profile("prod")
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
//    @Profile("prod")
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
//    @Profile("prod")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(localDataSource());
        factoryBean.setJpaVendorAdapter(localJpaVendorAdapter());
        factoryBean.setPackagesToScan("ru.glaizier.todo.model.domain");
        return factoryBean;
    }

    // uncomment to select from embedded db in console
//    @PostConstruct
//    @Profile("default")
//    public void startDBManager() {
//        //hsqldb
//        DatabaseManagerSwing.main(new String[]{"--url", "jdbc:hsqldb:mem:MemoryTaskDb", "--user", "sa", "--password", ""});
//    }

    @Bean
    @Profile("prod")
//    @Profile("default")
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
//    @Profile("default")
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
//    @Profile("default")
    public LocalContainerEntityManagerFactoryBean prodEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(prodDataSource());
        factoryBean.setJpaVendorAdapter(prodJpaVendorAdapter());
        factoryBean.setPackagesToScan("ru.glaizier.todo.model.domain");
        return factoryBean;
    }

    @Bean
    @Profile({"default", "prod"})
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

}

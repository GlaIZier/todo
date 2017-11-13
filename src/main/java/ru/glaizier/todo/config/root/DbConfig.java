package ru.glaizier.todo.config.root;

import java.lang.invoke.MethodHandles;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ru.glaizier.todo.properties.PropertiesService;

@Configuration
@EnableJpaRepositories(basePackages = "ru.glaizier.todo.persistence")
@EnableTransactionManagement
@Profile({"default", "prod"})
public class DbConfig {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private PropertiesService propertiesService;

    @Autowired
    public DbConfig(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @Bean
    @Profile("default")
    public DataSource localDataSource() {
        log.info("Using default, hsql, local persistence implementation...");
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
//    @Profile("default")
//    public void startDBManager() {
//        //hsqldb
//        DatabaseManagerSwing.main(new String[]{"--url", "jdbc:hsqldb:mem:MemoryTaskDb", "--user", "sa", "--password", ""});
//    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        log.info("Using prod, external, postgres persistence implementation...");
        // We use here Hikari cp but also we could use Postgres cp, container (Tomcat) cp or some other external cp like c3p0
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(propertiesService.getDbUrl());
        config.setUsername(propertiesService.getDbLogin());
        config.setPassword(propertiesService.getDbPassword());
        config.setDriverClassName(propertiesService.getDbDriver());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("maximumPoolSize", String.valueOf(propertiesService.getDbMaxPoolSize()));
        return new HikariDataSource(config);

//        DriverManagerDataSource dataSourceConfig = new DriverManagerDataSource();
//        dataSourceConfig.setDriverClassName("org.postgresql.Driver");
//        dataSourceConfig.setUrl("jdbc:postgresql://localhost:5432/tododb");
//        dataSourceConfig.setUsername("todoer");
//        dataSourceConfig.setPassword("password");
//        return dataSourceConfig;
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
    @Profile({"default", "prod"})
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

}

package com.java.auth;

import com.java.util.HashUtil;
import com.java.util.StringUtil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

import static com.java.constant.Constants.AES_SECRET;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "${datasource.auth.repository.packageScan}",
        entityManagerFactoryRef = "authEntityManager", transactionManagerRef = "authTransactionManager")
@NoArgsConstructor
public class AuthDataSourceConfig {

    /**
     * The driver.
     */
    @Value("${datasource.auth.driver-class-name}")
    private String driver;

    /**
     * The url.
     */
    @Value("${datasource.auth.url}")
    private String url;

    /**
     * The username.
     */
    @Value("${datasource.auth.username}")
    private String username;

    /**
     * The password.
     */
    @Value("${datasource.auth.password}")
    private String password;

    /**
     * The package scan.
     */
    @Value("${datasource.auth.packageScan}")
    private String packageScan;

    @Value("${datasource.auth.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    @Value("${datasource.auth.hibernate.dialect:}")
    private String hibernateDialect;

    @Bean
    public LocalContainerEntityManagerFactoryBean authEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(userDataSource());
        em.setPackagesToScan(packageScan);

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", hibernateDdlAuto);
        properties.put("hibernate.dialect", StringUtil.isEmpty(hibernateDialect)
                ? "org.hibernate.dialect.MySQL5InnoDBDialect"
                : hibernateDialect);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource userDataSource() {
        DriverManagerDataSource dataSource
                = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(HashUtil.aesDecrypt(password, AES_SECRET));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager authTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                authEntityManager().getObject());
        return transactionManager;
    }
}

package jforgame.admin.database;

import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "configEntityManagerFactory",
	transactionManagerRef = "configDbTransactionManager", 
	basePackages = {"jforgame.admin" })
public class StaticDbConfig {

	@Autowired
	@Qualifier("configDataSource")
	private DataSource configDataSource;

	@Primary
	@Bean(name = "configEntityManager")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return configEntityManagerFactory(builder).getObject().createEntityManager();
	}

	@Primary
	@Bean(name = "configEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean configEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(configDataSource).properties(getVendorProperties(configDataSource))
				.packages("jforgame.admin") // 设置实体类所在位置
				.persistenceUnit("primaryPersistenceUnit").build();
	}

	@Autowired
	private JpaProperties jpaProperties;

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getProperties();
	}

	@Primary
	@Bean(name = "configDbTransactionManager")
	public PlatformTransactionManager configDbTransactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(Objects.requireNonNull(configEntityManagerFactory(builder).getObject()));
	}

}
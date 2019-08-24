package cn.com.boco.dss.database;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = JpaTowerConfig.C_ENTITYMANAGERFACTORY_NAME,transactionManagerRef=JpaTowerConfig.C_TRANSACTIONMANAGER_NAME,basePackages = { "cn.com.boco.dss.subject.towerqs" })
public class JpaTowerConfig implements JpaConfig {

	/**
	 * 定义数据源名称
	 */
	public static final String C_DATASOURCE_NAME = DataSourceTowerConfig.C_DATASOURCE_NAME;

	/**
	 * 定义entityManagerFactory实体类工厂名称
	 */
	public static final String C_ENTITYMANAGERFACTORY_NAME = "towerEntityManagerFactory";

	/**
	 * 定义transactionManager事务名称
	 */
	public static final String C_TRANSACTIONMANAGER_NAME = "towerTransactionManager";

	/**
	 * 定义entityManager实体类管理器名称
	 */
	public static final String C_ENTITYMANAGER_NAME = "entityManagerTower";

	/**
	 * 定义persistenceUnit名称
	 */
	public static final String C_PERSISTENCEUNIT = "towerPersistenceUnit";

	/**
	 * 定义data jpa扫描的domain包路径(可以采用通配符,可以为数组类型)
	 */
	protected static final String[] C_PACKAGE_TO_SCAN = { "cn.com.boco.dss.subject.towerqs" };

	@Autowired
	@Qualifier(C_DATASOURCE_NAME)
	private DataSource ds;

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private HibernateProperties hibernateProperties;

	@Override
	@Bean(name = C_ENTITYMANAGER_NAME)
	public EntityManager getEntityManager(EntityManagerFactoryBuilder builder) {
		return getEntityManagerFactory(builder).getObject().createEntityManager();
	}

	@Override
	@Bean(name = C_ENTITYMANAGERFACTORY_NAME)
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(ds).properties(getVendorProperties(ds)).packages(C_PACKAGE_TO_SCAN) // 设置实体类包路径
				.persistenceUnit(C_PERSISTENCEUNIT).build();
	}

	@Override
	@Bean(name = C_TRANSACTIONMANAGER_NAME)
	public PlatformTransactionManager getTransactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(getEntityManagerFactory(builder).getObject());
	}

	@Override
	public Map<String, Object> getVendorProperties(DataSource dataSource) {
		return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());
	}

}

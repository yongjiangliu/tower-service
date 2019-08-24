package cn.com.boco.dss.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceTowerConfig implements DataSourceConfig {

	/**
	 * 定义application.properties配置文件里面数据源Key的前缀
	 */
	public static final String C_DATASOURCE_CON_PREFIX_KEY = "spring.db.towerqs";

	/**
	 * 定义数据源的名称
	 */
	public static final String C_DATASOURCE_NAME = "dataSourceTower";

	/**
	 * 定义数据库的jdbcTemplate
	 */
	public static final String C_JDBCTEMPLATE_NAME = "jdbcTemplateTower";

	@Override
	@Bean(name = C_DATASOURCE_NAME)
	@Qualifier(C_DATASOURCE_NAME)
	@ConfigurationProperties(prefix = C_DATASOURCE_CON_PREFIX_KEY)
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Override
	@Bean(name = C_JDBCTEMPLATE_NAME)
	public JdbcTemplate jdbcTemplate(@Qualifier(C_DATASOURCE_NAME) DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}

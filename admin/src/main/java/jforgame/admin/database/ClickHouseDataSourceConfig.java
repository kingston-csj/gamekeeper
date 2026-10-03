package jforgame.admin.database;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "spring.datasource.clickhouse", name = "enabled", havingValue = "true")
public class ClickHouseDataSourceConfig {

        @Bean(name = "clickHouseDataSource")
        @Qualifier("clickHouseDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.clickhouse")
        public DataSource clickHouseDataSource() {
                return DataSourceBuilder.create().build();
        }

        @Bean(name = "clickHouseJdbcTemplate")
        public JdbcTemplate clickHouseJdbcTemplate(@Qualifier("clickHouseDataSource") DataSource clickHouseDataSource) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(clickHouseDataSource);
                jdbcTemplate.setResultsMapCaseInsensitive(true);
                return jdbcTemplate;
        }

        @Bean(name = "clickHouseQueryTemplate")
        public ClickHouseQueryTemplate clickHouseQueryTemplate(
                        @Qualifier("clickHouseJdbcTemplate") JdbcTemplate clickHouseJdbcTemplate) {
                return new ClickHouseQueryTemplate(clickHouseJdbcTemplate);
        }
}

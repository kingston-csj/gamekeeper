package jforgame.admin.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

/**
 * 只暴露查询能力，避免在业务里直接对 ClickHouse 执行写操作。
 */
public class ClickHouseQueryTemplate {

        private final JdbcTemplate jdbcTemplate;

        public ClickHouseQueryTemplate(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        public List<Map<String, Object>> queryForList(String sql) {
                return jdbcTemplate.queryForList(sql);
        }

        public List<Map<String, Object>> queryForList(String sql, Object... args) {
                return jdbcTemplate.queryForList(sql, args);
        }

        public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
                return jdbcTemplate.query(sql, rowMapper);
        }

        public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
                return jdbcTemplate.query(sql, rowMapper, args);
        }

        public <T> T queryForObject(String sql, Class<T> requiredType) {
                return jdbcTemplate.queryForObject(sql, requiredType);
        }

        public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
                return jdbcTemplate.queryForObject(sql, requiredType, args);
        }

        public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
                return jdbcTemplate.queryForObject(sql, rowMapper);
        }

        public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
                return jdbcTemplate.queryForObject(sql, rowMapper, args);
        }

        public int update(String sql, Object... args) {
                return jdbcTemplate.update(sql, args);
        }
}

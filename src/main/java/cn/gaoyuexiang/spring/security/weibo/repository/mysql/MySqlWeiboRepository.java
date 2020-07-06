package cn.gaoyuexiang.spring.security.weibo.repository.mysql;

import cn.gaoyuexiang.spring.security.weibo.model.Weibo;
import cn.gaoyuexiang.spring.security.weibo.repository.WeiboRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MySqlWeiboRepository implements WeiboRepository {

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  public MySqlWeiboRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public Weibo save(Weibo weibo) {
    try {
      String json = objectMapper.writeValueAsString(weibo);
      jdbcTemplate.update(
          "insert into weibo(id, json_content) value (?, ?) on duplicate key update json_content = ?",
          weibo.getId(),
          json,
          json);
      return weibo;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Weibo findBy(String id) {
    return jdbcTemplate.queryForObject(
        "select json_content from weibo where id = ?;",
        new Object[] {id},
        (RowMapper<? extends Weibo>)
            (rs, idx) -> {
              try {
                return objectMapper.readValue(rs.getString("json_content"), Weibo.class);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });
  }
}

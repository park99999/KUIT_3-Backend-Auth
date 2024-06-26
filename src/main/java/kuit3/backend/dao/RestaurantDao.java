package kuit3.backend.dao;

import kuit3.backend.dto.restaurant.GetRestaurant;
import kuit3.backend.dto.restaurant.GetcategoryResponse;
import kuit3.backend.dto.restaurant.PostRestaurantRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Repository
public class RestaurantDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RestaurantDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<GetcategoryResponse> getCategory(String category) {
        String sql = "select * from restaurants where category=:category";

        Map<String, Object> param = Map.of(
                "category", "%" + category + "%");

        return jdbcTemplate.query(sql, param,
                (rs, rowNum) -> new GetcategoryResponse(
                        rs.getString("category"),
                        rs.getString("storeName"))
        );
    }
    public List<GetRestaurant> getRestaurants(String restaurantname, String category, String status) {
        String sql = "select * from restaurants where restaurantname like :restaurantname and category like :category and status=:status";
;       Map<String, Object> param = Map.of(
                "restaurantname", "%" + restaurantname + "%",
                "category", "%" + category + "%",
                "status", status);
        return jdbcTemplate.query(sql,param,
                (rs, rowNum) -> new GetRestaurant(
                        rs.getString("restaurantname"),
                        rs.getString("category"),
                        rs.getLong("min_order_amount"))
        );
    }
    public long createRestaurant(PostRestaurantRequest postRestaurantRequest){
        String sql = "insert into restaurants(restaurantname,category,min_order_amount) values(:restaurantname,:category,:min_order_amount)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(postRestaurantRequest);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql,param,keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
    public boolean hasDuplicateRestaurantName(String restaurantname) {
        String sql = "select exists(select restaurantId from restaurants where restaurantname=:restaurantname)";
        Map<String, Object> param = Map.of("restaurantname", restaurantname);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, boolean.class));
    }

    public int modifyRestaurantName(long restaurantId, String restaurantname) {
        String sql = "update restaurants set restaurantname=:restaurantname where restaurantId=:restaurantId";
        Map<String, Object> param = Map.of(
                "restaurantname", restaurantname,
                "restaurantId", restaurantId);
        return jdbcTemplate.update(sql, param);
    }
}

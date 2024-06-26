package kuit3.backend.dao;

import kuit3.backend.dto.user.GetUserResponse;
import kuit3.backend.dto.user.PostUserRequest;
import kuit3.backend.dto.user.PutUserInfoRequest;
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
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public boolean hasDuplicateEmail(String email) {
        String sql = "select exists(select email from user where email=:email and status in ('active', 'dormant'))";
        Map<String, Object> param = Map.of("email", email);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, boolean.class));
    }

    public boolean hasDuplicateNickName(String nickname) {
        String sql = "select exists(select email from user where username=:nickname and status in ('active', 'dormant'))";
        Map<String, Object> param = Map.of("nickname", nickname);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, boolean.class));
    }
    public boolean hasAlreadyDormant(long userId) {
        String sql = "select exists(select email from user where userid=:userId and status = 'dormant')";
        Map<String, Object> param = Map.of("userId", userId);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, boolean.class));
    }
    public long createUser(PostUserRequest postUserRequest) {
        String sql = "insert into user(email, password, username, phone) " +
                "values(:email, :password, :nickname,:phoneNumber)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(postUserRequest);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, param, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int modifyUserStatus_dormant(long userId) {
        String sql = "update user set status=:status where userid=:user_id";
        Map<String, Object> param = Map.of(
                "status", "dormant",
                "user_id", userId);
        return jdbcTemplate.update(sql, param);
    }

    public int modifyUserStatus_deleted(long userId) {
        String sql = "update user set status=:status where userid=:user_id";
        Map<String, Object> param = Map.of(
                "status", "deleted",
                "user_id", userId);
        return jdbcTemplate.update(sql, param);
    }

    public int modifyNickname(long userId, String nickname) {
        String sql = "update user set username=:nickname where userid=:user_id";
        Map<String, Object> param = Map.of(
                "nickname", nickname,
                "user_id", userId);
        return jdbcTemplate.update(sql, param);
    }
    public int modifyUserInfo(long userId, PutUserInfoRequest putUserInfoRequest) {
        String nickname = putUserInfoRequest.getNickname();
        String password = putUserInfoRequest.getPassword();
        String email = putUserInfoRequest.getEmail();
        String phone = putUserInfoRequest.getPhoneNumber();
        String sql = "update user set username=:nickname,password=:password,email=:email,phone=:phonewhere userid=:user_id";
        Map<String, Object> param = Map.of(
                "nickname", nickname,
                "password",password,
                "email",email,
                "phone",phone,
                "user_id", userId);
        return jdbcTemplate.update(sql, param);
    }

    public List<GetUserResponse> getUsers(String nickname, String email, String status, long last_id) {
        String sql = "select email, phone, username, status from user " +
                "where username like :username and email like :email and status=:status and userid > :last_id LIMIT 2";

        Map<String, Object> param = Map.of(
                "username", "%" + nickname + "%",
                "email", "%" + email + "%",
                "status", status,
                "last_id",last_id);

        return jdbcTemplate.query(sql, param,
                (rs, rowNum) -> new GetUserResponse(
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("status"))
        );
    }

    public long getUserIdByEmail(String email) {
        String sql = "select userid from user where email=:email and status='active'";
        Map<String, Object> param = Map.of("email", email);
        return jdbcTemplate.queryForObject(sql, param, long.class);
    }

    public String getPasswordByUserId(long userId) {
        String sql = "select password from user where userid=:user_id and status='active'";
        Map<String, Object> param = Map.of("user_id", userId);
        return jdbcTemplate.queryForObject(sql, param, String.class);
    }



}
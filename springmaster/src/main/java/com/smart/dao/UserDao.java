package com.smart.dao;

import com.smart.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String GET_MATCH_COUNT_SQL = "select count(*) from t_user where user_name = ? and password = ?";
    private final static String FIND_USER_BY_USERNAME_SQL="select * from t_user where user_name=?";
    private final static String UPDATE_LOGIN_LOG_INFO_SQL="update t_user set " +
            " last_visit=?,last_ip=?,credits=? where user_id=?";
    public int getMatchCount(String userName,String password){
        return jdbcTemplate.queryForObject(GET_MATCH_COUNT_SQL,new Object[]{userName,password},Integer.class);
    }

    public User findUserByUserName(final String userName){
       final User user=new User();
        jdbcTemplate.query(FIND_USER_BY_USERNAME_SQL, new Object[]{userName}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {

                user.setUserName(userName);
                user.setUserId(resultSet.getInt("user_id"));
                user.setCredits(resultSet.getInt("credits"));
            }
        });
        return user;
    }

    public void updateLoginLogInfo(User user){
        jdbcTemplate.update(UPDATE_LOGIN_LOG_INFO_SQL,new Object[]{user.getLastVisit(),user.getLastIp(),user.getCredits(),user.getUserId()});
    }
}

package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserDaoImpl implements UserDao {

    //是对数据库的操作在jdbc的封装,处理了资源的建立和释放(不需要我们管理连接了)
   private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public User findUserByName(String username) {
        User user = null;
      try{     //1 定义sql
          String sql = "select * from tab_user where username = ?";
          //2 执行
          user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),username);
      }catch (EmptyResultDataAccessException e){
          return null;
      }
        return user;
    }

    @Override
    public void save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code)VALUES(?,?,?,?,?,?,?,?,?)";
        template.update(sql,user.getUsername(),
                             user.getPassword(),
                              user.getName(),
                              user.getBirthday(),
                                user.getSex(),
                              user.getTelephone(),
                               user.getEmail(),
                               user.getStatus(),
                                user.getCode());
    }

    //根据激活码查询用户
    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    //修改激活状态
    @Override
    public void updateStatus(User user) {
          String sql = "update tab_user set status = 'Y' where uid = ?";
          template.update(sql,user.getUid());
    }

    //根据用户名和密码查询
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user = null;

        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (Exception e) {

        }
        return user;
    }

    //根据用户id查询商品id
    @Override
    public List<Map<String,Object>> findRidByUid(int uid) {

        List<Map<String,Object>> ridList = null;
        try{
            String sql = "select rid from tab_favorite where uid = ?";
            ridList = template.queryForList(sql,uid);
        }catch (Exception e){

        }
        return ridList;
    }

    //根据路线id查询路线
    @Override
    public Route findRouteByRid(int rid) {
        System.out.println("ccc="+rid);
        Route route = null;
        try {
            String sql = "select * from tab_route where rid = ?";
             route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        }catch (Exception e){

        }
        return route;
    }


}

package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
     User findUserByName(String username);
     void save(User user);

    User findByCode(String code);

    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);

    List<Map<String,Object>> findRidByUid(int uid);

    Route findRouteByRid(int rid);
}

package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
//import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDaoImpl userDao = new UserDaoImpl();
    @Override
    public boolean regist(User user) {
        //1 根据用户名查询用户
        User u = userDao.findUserByName(user.getUsername());
        if(u != null){
            //用户名存在，注册失败
            return false;
        }
        //2 保存用户信息
        //2.1 设置激活码 唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2 设置激活状态
        user.setStatus("N");
        userDao.save(user);
        //激活邮件发送，邮件正文
        String content="<a href='http://localhost:8080/user/active?code="+user.getCode()+"'>点击激活【旅游网站】</a>";
        MailUtils.sendMail(user.getEmail(),content,"邮件激活");
        return true;
    }

    //激活方法
    @Override
    public boolean active(String code) {
      User user = userDao.findByCode(code);
      if(user != null){
          userDao.updateStatus(user);
          return true;
      }else {
          return false;
      }
    }
     //登录方法
    @Override
    public User login(User user) {

        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    //用户收藏夹
    @Override
    public List<Route> findFavoriteByUid(int uid) {
        //1 根据uid查询rid
       List<Map<String,Object>> ridList = userDao.findRidByUid(uid);
        System.out.println(ridList);
        List<Route> routeList = new ArrayList<>();
       // 2 根据rid集合查询route集合
       for(int i = 0;i < ridList.size();i++){
           Map<String, Object> stringObjectMap = ridList.get(i);
           int rid = (int)stringObjectMap.get("rid");
           Route route = userDao.findRouteByRid(rid);
           routeList.add(route);
        }
        System.out.println(routeList);
        return routeList;
    }

}

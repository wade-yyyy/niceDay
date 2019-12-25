package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    UserService service = new UserServiceImpl();

    //注册
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //0 验证码校验
        String check = request.getParameter("check");//用户输入的验证码
        //0.1 从session获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//保证验证码只能用一次
        //0.2 比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码验证失败
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //1 获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2 封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3 调用service方法，完成注册
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        //4 响应结果
        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("用户名已存在");
        }
        //5 info对象序列化json
        /*ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);*/
        writeValueAsString(info);
        //6 将json数据返回客户端，并设置content-type
        /*response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);*/
        writeValue(info,response);
    }

    //登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //0 验证码校验
        String check = request.getParameter("login_check");//用户输入的验证码
        //0.1 从session获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//保证验证码只能用一次
        //0.2 比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码验证失败
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        //1 获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //2 封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3 调用service查询

        User u = service.login(user);
        ResultInfo info = new ResultInfo();
        //4 判断用户是否为空
        if (u == null) {
            //用户名或者密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或者密码错误");
        }
        //5 判断用户是否激活
        if (u != null && !"Y".equals(u.getStatus())) {
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活后登录");
        }
        //6 判断登陆成功
        if (u != null && "Y".equals(u.getStatus())) {
            //登录成功标记
            request.getSession().setAttribute("user", u);
            //登录成功
            info.setFlag(true);
        }
        //响应数据
      /*  ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), info);*/
      writeValue(info,response);
    }

    //获取session用户来返回用户名
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session获取user
        Object user = request.getSession().getAttribute("user");
        //将用户写回到客户端
        /*ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), user);*/
       writeValue(user,response);
    }

    //退出
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 销毁session
        request.getSession().invalidate();
        //2 页面跳转
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 获取激活码code
        String code = request.getParameter("code");
        if (code != null) {
            //2 调用serice完成激活
            boolean flag = service.active(code);
            //3 判断标记
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功，请<a href='http://localhost:8080/login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败，请联系管理员";

            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    //判断用户是否登录
    public void isLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out =response.getWriter();
            out.println("<script language='javascript'>alert('您尚未登录，请登录后进行此操作');" +
                    "window.location = 'http://localhost:8080/login.html'</script>");

        }else {
            response.sendRedirect(request.getContextPath() + "/myfavorite.html");
        }
    }
    //查找登录用户的收藏
    public List<Route> findFavoriteByUid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        int uid = user.getUid();
        List<Route> routeList = service.findFavoriteByUid(uid);
        writeValue(routeList,response);
        return routeList;
    }
}
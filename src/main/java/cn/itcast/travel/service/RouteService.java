package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteService {
    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname);

    Route findOne(String rid);

    List<Route> favoriteRank();

    List<Route> getPopularity();

    List<Route> getNewest();

    List<Route> getTheme();
}

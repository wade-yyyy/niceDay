package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

import java.util.List;
import java.util.Map;

public interface FavoriteDao {
    Favorite findByRidAndUid(int rid, int uid);

    int findCountByRid(int rid);

    void add(int rid, int uid);
}

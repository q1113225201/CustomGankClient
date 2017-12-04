package com.sjl.platform.base.db;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

/**
 * DBManager
 *
 * @author SJL
 * @date 2017/12/4
 */

public class DBManager {
    private static final String TAG = "DBManager";
    private static DBManager dbManager;
    private static FinalDb finalDb;

    public static DBManager getInstance() {
        if (dbManager == null) {
            synchronized (TAG) {
                if (dbManager == null) {
                    dbManager = new DBManager();
                }
            }
        }
        return dbManager;
    }

    public static void init(FinalDb finalDb) {
        DBManager.finalDb = finalDb;
    }

    /**
     * 获取列表
     * @param clazz
     * @param where
     * @param orderBy
     * @param <T>
     * @return
     */
    public <T> List<T> getList(Class<T> clazz, String where, String orderBy) {
        List<T> list = finalDb.findAllByWhere(clazz, where, orderBy);
        list = list == null ? new ArrayList<T>() : list;
        return list;
    }

    /**
     * 保存数据
     * @param model
     * @param <T>
     */
    public <T> void save(T model) {
        finalDb.save(model);
    }

    /**
     * 保存列表
     * @param list
     * @param <T>
     */
    public <T> void save(List<T> list) {
        for (T item : list) {
            save(item);
        }
    }

    /**
     * 获取最后一条数据
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getLastModel(Class<T> clazz) {
        List<T> list = getList(clazz, "", "");
        return list.size() > 0 ? list.get(list.size() - 1) : null;
    }
}

package com.sjl.gank.mvp.view;

import com.sjl.gank.bean.GankDataResult;
import com.sjl.platform.base.MvpView;

import java.util.List;

/**
 * SortListMvpView
 *
 * @author 林zero
 * @date 2018/3/4
 */

public interface SortListMvpView extends MvpView {
    void setSortList(List<GankDataResult> list,int page);
}

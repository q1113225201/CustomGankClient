package com.sjl.gank.mvp.view;

import com.sjl.gank.bean.GankDataResult;
import com.sjl.platform.base.MvpView;

import java.util.List;

/**
 * GankDetailMvpView
 *
 * @author 林zero
 * @date 2018/3/4
 */

public interface GankDetailMvpView extends MvpView {
    /**
     * 设置详情
     */
    void setGankDetail(List<GankDataResult> list);
}

package com.sjl.gankapp.mvp.view;

import com.sjl.gankapp.model.pojo.AboutInfo;
import com.sjl.platform.base.MvpView;

/**
 * AboutMvpView
 *
 * @author 林zero
 * @date 2018/3/4
 */

public interface AboutMvpView extends MvpView {
    void onGetAboutInfo(boolean isSuccess, AboutInfo aboutInfo);
}

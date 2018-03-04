package com.sjl.gank.mvp.presenter;

import com.sjl.gank.bean.GankData;
import com.sjl.gank.config.GankConfig;
import com.sjl.gank.http.ServiceClient;
import com.sjl.gank.mvp.view.SortListMvpView;
import com.sjl.gank.mvp.view.SortMvpView;
import com.sjl.platform.base.BasePresenter;
import com.sjl.platform.util.LogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * SortListPresenter
 *
 * @author 林zero
 * @date 2018/3/4
 */

public class SortListPresenter extends BasePresenter<SortListMvpView> {
    /**
     * 获取数据
     */
    public void getSortList(String sort, final int page){
        if(page==1) {
            getMvpView().autoProgress(true);
        }
        ServiceClient.getGankAPI().getSortDataByPages(sort, GankConfig.PAGE_SIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GankData>() {
                    @Override
                    public void accept(GankData gankData) throws Exception {
                        getMvpView().autoProgress(false);
                        getMvpView().setSortList(gankData.getResults(),page);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().autoProgress(false);
                    }
                });
    }
}

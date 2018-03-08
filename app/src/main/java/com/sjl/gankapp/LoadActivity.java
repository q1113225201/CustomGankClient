package com.sjl.gankapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.sjl.gank.ui.activity.GankMainActivity;
import com.sjl.gankapp.mvp.presenter.LoadPresenter;
import com.sjl.gankapp.mvp.view.LoadMvpView;
import com.sjl.platform.base.BaseActivity;
import com.sjl.platform.util.PermisstionUtil;

import butterknife.BindView;

public class LoadActivity extends BaseActivity<LoadMvpView, LoadPresenter> implements LoadMvpView {
    @BindView(R.id.tvVersion)
    TextView tvVersion;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_load;
    }

    @Override
    protected void initView() {
        tvVersion.setText(BuildConfig.VERSION_NAME);
        PermisstionUtil.requestPermissions(mContext, PermisstionUtil.STORAGE, 100, "正在请求读写权限", new PermisstionUtil.OnPermissionResult() {
            @Override
            public void granted(int requestCode) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext, GankMainActivity.class));
                        finish();
                    }
                }, 2000);
            }

            @Override
            public void denied(int requestCode) {

            }
        });
    }

    @Override
    protected LoadMvpView obtainMvpView() {
        return this;
    }

    @Override
    protected LoadPresenter obtainPresenter() {
        mPresenter = new LoadPresenter();
        return (LoadPresenter) mPresenter;
    }

    @Override
    public void onClick(View v) {

    }
}

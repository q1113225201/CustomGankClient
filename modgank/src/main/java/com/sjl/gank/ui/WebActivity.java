package com.sjl.gank.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sjl.gank.R;
import com.sjl.platform.base.BaseActivity;
import com.sjl.platform.util.ShareUtil;

/**
 * 网页
 *
 * @author SJL
 * @date 2017/12/14
 */
public class WebActivity extends BaseActivity {
    private static final String TAG = "WebActivity";
    public static final String TITLE = "title";
    public static final String URL = "url";
    private String title;
    private String url;

    public static Intent newIntent(Context context, String title, String link) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, link);
        return intent;
    }

    private void parseIntent() {
        title = getIntent().getStringExtra(TITLE);
        url = getIntent().getStringExtra(URL);
    }

    private Toolbar toolBar;
    private TextView tvTtile;
    private ProgressBar pbProgress;
    private LinearLayout llWebView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        parseIntent();
        initView();
    }

    private void initView() {
        initToolBar();
        tvTtile = findViewById(R.id.tvTitle);
        pbProgress = findViewById(R.id.pbProgress);
        llWebView = findViewById(R.id.llWebView);
        initWebView();

        setTitle(title);
        tvTtile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tvTtile.requestFocus();
                }
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tvTtile.setText(title);
    }

    private void initToolBar() {
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuRefresh) {
                    webView.reload();
                } else if (id == R.id.menuCopy) {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("text", webView.getUrl()));
                    toast(getString(R.string.gank_copy_success));
                } else if (id == R.id.menuShare) {
                    ShareUtil.shareMsg(mContext, webView.getUrl());
                }
                return false;
            }
        });
    }

    private void initWebView() {
        webView = new WebView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pbProgress.setProgress(newProgress);
                pbProgress.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl() != null) {
                    view.loadUrl(request.getUrl().toString(), request.getRequestHeaders());
                }
                return true;
            }
        });
        initWebViewSetting(webView);
        webView.loadUrl(url);
        llWebView.addView(webView);
    }

    // 缓存路径
    private final String appCachePath = "/webcache";

    /**
     * 初始化webview设置
     */
    private void initWebViewSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //设置缩放
        webSettings.setSupportZoom(false);
        //设置允许访问文件
        webSettings.setAllowFileAccess(true);
        //设置保存表单数据
        webSettings.setSaveFormData(true);
        //设置是否使用viewport
        webSettings.setUseWideViewPort(true);
        //设置WebView标准字体库字体，默认"sans-serif"
        webSettings.setStandardFontFamily("sans-serif");
        //设置WebView固定的字体库字体，默认"monospace"
        webSettings.setFixedFontFamily("monospace");
        //设置WebView是否以http、https方式访问从网络加载图片资源，默认false
        webSettings.setBlockNetworkImage(false);
        //设置WebView是否从网络加载资源，Application需要设置访问网络权限，否则报异常
        webSettings.setBlockNetworkLoads(false);
        //设置WebView是否允许执行JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置WebView运行中的脚本可以是否访问任何原始起点内容，默认true
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        //设置Application缓存API是否开启，默认false
        webSettings.setAppCacheEnabled(true);
        //设置当前Application缓存文件路径
        webSettings.setAppCachePath(appCachePath);
        //设置是否开启数据库存储API权限
        webSettings.setDatabaseEnabled(true);
        //设置是否开启DOM存储API权限
        webSettings.setDomStorageEnabled(true);
        //设置是否开启定位功能，默认true，开启定位
        webSettings.setGeolocationEnabled(true);
        //设置脚本是否允许自动打开弹窗，默认false，不允许
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        //设置WebView加载页面文本内容的编码，默认“UTF-8”。
        webSettings.setDefaultTextEncodingName("UTF-8");
        // 设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        //设置WebView是否自动加载图片
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
        //硬件加速器的使用
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }
}

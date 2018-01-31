package com.sjl.gank.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sjl.gank.R;
import com.sjl.gank.bean.GankData;
import com.sjl.gank.bean.GankDataResult;
import com.sjl.gank.config.GankConfig;
import com.sjl.gank.service.ServiceClient;
import com.sjl.gank.util.GankUtil;
import com.sjl.platform.base.BaseActivity;
import com.sjl.platform.base.adapter.CommonRVAdapter;
import com.sjl.platform.util.LogUtil;
import com.sjl.platform.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
/**
 * 分类列表页面
 * 
 * @author SJL
 * @date 2017/12/14
 */
public class SortListActivity extends BaseActivity{
    private static final String TAG = "SortListActivity";
    private static final String SORT = "sort";
    private String sort;

    public static Intent newIntent(Context context, String sort) {
        Intent intent = new Intent(context, SortListActivity.class);
        intent.putExtra(SORT, sort);
        return intent;
    }

    private void parseIntent() {
        sort = getIntent().getStringExtra(SORT);
    }

    private Toolbar toolBar;
    private SwipeRefreshLayout srl;
    private RecyclerView rvSort;
    private CommonRVAdapter<GankDataResult> adapter;
    private List<GankDataResult> gankDataResultList;

    private int currentPage = 1;
    private final static int LOADING = 1;
    private final static int NOLOAD = 2;
    private final static int LOAD_NO_MORE = 3;
    private int loadState = NOLOAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_list);

        parseIntent();
        initView();
    }


    private void initView() {
        initToolBar();
        srl = findViewById(R.id.srl);
        rvSort = findViewById(R.id.rvSort);

        srl.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.i(TAG, "onRefresh");
                currentPage = 1;
                loadState = NOLOAD;
                getSortList(currentPage, sort);
            }
        });

        initSortList();
        getSortList(currentPage, sort);
    }

    private void initToolBar() {
        toolBar = findViewById(R.id.toolBar);
        toolBar.setTitle(sort);
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
                sort = item.getTitle().toString();
                toolBar.setTitle(sort);
                currentPage = 1;
                adapter.removeAll();
                getSortList(currentPage, sort);
                return false;
            }
        });
    }

    private void initSortList() {
        gankDataResultList = new ArrayList<>();
        adapter = new CommonRVAdapter<GankDataResult>(mContext, gankDataResultList, R.layout.item_sort, R.layout.item_sort_empty) {
            @Override
            protected void onBindNullViewHolder(RecyclerView.Adapter adapter, RVViewHolder viewHolder, int position, GankDataResult item, List<GankDataResult> list) {

            }

            @Override
            protected void onBindViewHolder(RecyclerView.Adapter adapter, RVViewHolder viewHolder, int position, final GankDataResult item, List<GankDataResult> list) {
                if (GankConfig.WELFARE.equalsIgnoreCase(sort)) {
                    viewHolder.findViewById(R.id.llItemType).setVisibility(View.VISIBLE);
                    viewHolder.findViewById(R.id.llItemContent).setVisibility(View.GONE);
                    ((TextView) viewHolder.findViewById(R.id.tvItemType)).setText(GankUtil.parseDate(item.getPublishedAt()));
                    viewHolder.findViewById(R.id.llItemImg).setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(item.getUrl()).centerCrop().error(R.drawable.error).into(((ImageView) viewHolder.findViewById(R.id.ivItemImg)));
                } else {
                    viewHolder.findViewById(R.id.llItemType).setVisibility(GankConfig.ALL.equalsIgnoreCase(sort) ? View.VISIBLE : View.GONE);
                    viewHolder.findViewById(R.id.llItemContent).setVisibility(View.VISIBLE);
                    ((TextView) viewHolder.findViewById(R.id.tvItemType)).setText(item.getType());
                    ((TextView) viewHolder.findViewById(R.id.tvItemDesc)).setText(item.getDesc());
                    ((TextView) viewHolder.findViewById(R.id.tvItemTimeAuth)).setText(String.format("%s\n%s", GankUtil.parseDate(item.getPublishedAt()), item.getWho()));
                    if (item.getImages() == null || item.getImages().size() == 0) {
                        viewHolder.findViewById(R.id.llItemImg).setVisibility(View.GONE);
                    } else {
                        viewHolder.findViewById(R.id.llItemImg).setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(item.getImages().get(0)).error(R.drawable.error).into(((ImageView) viewHolder.findViewById(R.id.ivItemImg)));
                    }
                    if (GankConfig.WELFARE.equalsIgnoreCase(item.getType())) {
                        viewHolder.findViewById(R.id.llItemContent).setVisibility(View.GONE);
                        viewHolder.findViewById(R.id.llItemImg).setVisibility(View.VISIBLE);
                        Glide.with(mContext).load(item.getUrl()).centerCrop().error(R.drawable.error).into(((ImageView) viewHolder.findViewById(R.id.ivItemImg)));
                    }
                }
                viewHolder.findViewById(R.id.cvItemSort).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(GankConfig.WELFARE.equalsIgnoreCase(item.getType())){
                            startActivity(ImageActivity.newIntent(mContext,item.getUrl()));
                        }else{
                            startActivity(WebActivity.newIntent(mContext,item.getDesc(),item.getUrl()));
                        }
                    }
                });
            }
        };
        rvSort.setAdapter(adapter);
        rvSort.setLayoutManager(new LinearLayoutManager(this));
        rvSort.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition() >= adapter.getItemCount() - GankConfig.PAGE_SIZE / 2 && loadState == NOLOAD) {
                    getSortList(currentPage, sort);
                }
            }
        });
    }

    private synchronized void getSortList(int page, String sort) {
        if (loadState == LOAD_NO_MORE && currentPage != 1) {
            ToastUtil.showToast(mContext,getString(R.string.gank_no_more_data));
            return;
        }
        if (loadState == NOLOAD) {
            loadState = LOADING;
        } else {
            return;
        }
        if(currentPage==1&&!srl.isRefreshing()){
            srl.setRefreshing(true);
        }
        ServiceClient.getGankAPI().getSortDataByPages(sort, GankConfig.PAGE_SIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GankData>() {
                    @Override
                    public void accept(GankData gankData) throws Exception {
                        LogUtil.i(TAG, gankData.toString());
                        if (gankData.getResults().size() == GankConfig.PAGE_SIZE) {
                            currentPage++;
                            loadState = NOLOAD;
                        } else {
                            loadState = LOAD_NO_MORE;
                        }
                        adapter.addList(gankData.getResults());
                        srl.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(TAG, "accept---:" + throwable.getMessage());
                        loadState = NOLOAD;
                        srl.setRefreshing(false);
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort,menu);
        return true;
    }
}
package com.xuxu.datatool.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.xuxu.datatool.Model.GeekNews;
import com.xuxu.datatool.R;
import com.xuxu.datatool.adpter.RecyclerAdapterGeek;
import com.xuxu.datatool.data.DataBaseTool_CNew;
import com.xuxu.datatool.data.DataBasetool;
import com.xuxu.datatool.utils.GetData;
import com.xuxu.datatool.utils.MyReceiver;
import com.xuxu.datatool.utils.NetWorkUtil;
import com.xuxu.datatool.utils.NetworkReceiver;
import com.xuxu.datatool.utils.ShareUtil;
import com.xuxu.datatool.widget.LoadFinishCallBack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/7/23.
 * 此Fragment显示引发的几个改进点：
 * 1、需要显示多个头条，建立数据库，存入，
 * 2、解决本地数据与网络获取数据冲突问题，获取的数据首先存入，显示统一获取
 * 3、出现存入数据重复问题，存入之前先查询（利用常量IsHased）,并且只需查询前8个及以上Cloumn，分页加载
 * 4、数据顺序错误
 */
public class GetDataFromWebFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerAdapterGeek recyclerAdapterGeek;
    final String CSDNURL = "http://www.csdn.net/";
    private DataBasetool dbt;
    private DataBaseTool_CNew dataBaseTool_cNew;
    final String CONTENTURL = "http://www.superqq.com/blog/2015/07/28/four-create-uiimage-method/";
    private SwipeRefreshLayout swipeRefreshLayout;
    List<Map<String, Object>> data;
    int sum = 0;
    int times = 1;
    private List<GeekNews> mGeekNewsList;
    private List<GeekNews> geekNewsList = null;
    private LoadFinishCallBack mLoadFinisCallBack;
    private LinearLayoutManager linearLayoutManager;
    private boolean mIsRefreshing = false;
    //    private ProgressDialog pd;
    private Dialog pd;
    private int firstSum;
    private boolean hasIt = false;
    private boolean isVisible;
    private NetworkReceiver networkReceiver;
    private IntentFilter counterActionFilter;
    protected ActionBar mActionBar;
    private int retry = 0;

    public GetDataFromWebFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;

            if (NetWorkUtil.isNetWorkConnected(getActivity()) && isVisible && !hasIt) {
//                Log.e("加载了第1页数据", "");
                swipeHandler.sendEmptyMessageDelayed(0, 100);
            } else {
                networkReceiver=new NetworkReceiver(){
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        super.onReceive(context, intent);
                        if ((activeInfo!=null&&activeInfo.isAvailable()&&!hasIt)||(wifiInfo.isConnected()&&!hasIt)) {
                            Toast.makeText(app.getContext(), "网路好了耶，重新获取数据！", Toast.LENGTH_SHORT).show();
                            hasIt = true;
                            swipeHandler.sendEmptyMessageDelayed(0, 100);
                        }
                    }
                };
                counterActionFilter = new IntentFilter();
                counterActionFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                getActivity().registerReceiver(networkReceiver,counterActionFilter);
            }
        } else {
            isVisible = false;
        }
    }

    private android.os.Handler swipeHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            pd = createLoadingDialog(getActivity(), "正在加载中");
            pd.show();
            new FirstAsyncTask().execute(String.valueOf(0));//更新过程实际上是先把数据下载然后存入数据库，统一取出
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.getdatafromwebfragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
//        new TabAsyncTask().execute(CSDNURL);//更新过程实际上是先把数据下载然后存入数据库，统一取出
        //解决了需要拼接两个List的复杂问题，但是增加了数据库操作，多次查询
//        String reStart =getArguments().getString("APPReStart");
        mGeekNewsList = new ArrayList<>();
        initView();
        //此处为data为空值，由于是子线程，可能这个值并没有来得及改变
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    /**
     * 处理联网结果，显示在listview
     *
     * @return
     * @author Lai Huan
     * @created 2013-6-20
     */
    private void initData(boolean loading) {
        if (mGeekNewsList != null) {
            if (!loading) {
                recyclerAdapterGeek = new RecyclerAdapterGeek(getActivity(), mGeekNewsList);
                recyclerView.setAdapter(recyclerAdapterGeek);
                hasIt = true;
                if (pd != null) {
                    pd.dismiss();
                }
            } else {
                recyclerAdapterGeek.notifyDataSetChanged();
            }
        }
    }


    private void initView() {
//        data = dbt.selectDB9(0);

        recyclerView = (RecyclerView) view.findViewById(R.id.two_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mCurrentState = RecyclerView.SCROLL_STATE_IDLE;
            private int lastdy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCurrentState == RecyclerView.SCROLL_STATE_DRAGGING || mCurrentState == RecyclerView.SCROLL_STATE_SETTLING) {

                    if (dy < 0) {//向下滑动

                        //可以不处理，在SwipeRefreshLayout的onRefreshListener中实现下拉刷新
                    } else {//向上滑动
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof LinearLayoutManager) {
                            int lastitem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                            if (recyclerView.getAdapter().getItemCount() == lastitem + 1) {
                                swipeRefreshLayout.setRefreshing(true);
                                swipeRefreshLayout.setEnabled(false);
                                new TabAsyncTask().execute();
                            }
                        }
                    }

                    lastdy = dy;
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mCurrentState = newState;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById
                (R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //为ArrayList<FreshNews>中添加数据
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                if(!hasIt){
                   new FirstAsyncTask().execute(String.valueOf(0));
                }else{
                    new FirstAsyncTask().execute(String.valueOf(1));
                }

            }
        });
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActionBar = ((AppCompatActivity) activity).getSupportActionBar();
//        mActionBar.setTitle("IT前沿");
        View customView = LayoutInflater.from(activity).inflate(R.layout.toolbarlittle, null);
        mActionBar.setCustomView(customView);
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(app.getContext(), "....", Toast.LENGTH_SHORT).show();
                recyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(networkReceiver!=null){
            getActivity().unregisterReceiver(networkReceiver);
        }
        super.onDestroy();
    }

    private ShareActionProvider mShareActionProvider;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
//        MenuItem item = menu.findItem(R.id.action_share);
//
//        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

    }
//        SearchManager searchManager =
//                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        MenuItem searchItem = menu.findItem(R.id.ab_search);
//        //searchItem.expandActionView();
////        searchView.setSubmitButtonEnabled(true);
//        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        ComponentName componentName = getActivity().getComponentName();
//
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(componentName));
//        searchView.setQueryHint(getString(R.string.search_note));
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                return true;
//            }
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                data = dbt.selectDBAll();//从数据库查询。
//                //关闭输入法
//                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                Log.e("搜索输入", s);
//                List<Map<String, Object>> dataFiltered = new ArrayList<Map<String, Object>>();
//                for (Map<String, Object> map : data) {
//                    if (map.get("jk_title").toString().contains(s)) {
//                        dataFiltered.add(map);
//                    }
//                }
//                Log.e("搜索输出", dataFiltered + "");
//                data.clear();
//                data.addAll(dataFiltered);
//                Log.e("搜索输出data", data + "");
////                initData();
//                return true;
//            }
//
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return true;
//            }
//        });
//        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//
//                return true;
//            }
//
//            //searchview关闭时调用，显示全部信息
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                data = dbt.selectDB10();
////                initData();//单这句话没有作用
//                return true;
//            }
//        });
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
//                dbt.delete("重构真的能提高代码质量吗？");
//                dbt.deleteAll();
//                dataBaseTool_cNew.clear();
                GetData.clearData(getActivity());
                Toast.makeText(app.getContext(), "没缓存数据咯^~^!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
//                    String imgPath = ShareUtil.saveToSD(getActivity());
////                    Log.e("最后要分享的图片路径",imgPath);
                ShareUtil.shareText(getActivity(), "重邮极客客户端下载http://zhushou.360.cn/detail/index/soft_id/3075459?recrefer=SE_D_%E9%87%8D%E9%82%AE#prev");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //还没有找到在其他类里面调用AsyncTask的方法！！！暂且在本类中进行操作，下拉刷新可以再次调用TabAsyncTask实现
// 页面的刷新
    class TabAsyncTask extends AsyncTask<String, Void, List<GeekNews>> {
        @Override
        protected List<GeekNews> doInBackground(String... strings) {
            dbt = new DataBasetool(getActivity());
            mIsRefreshing = true;
            //增加此判断，解决没有网络连接，程序终止问题

            if (NetWorkUtil.isNetWorkConnected(getActivity())) {
                if (geekNewsList != null) {
                    geekNewsList.clear();
                }
                int hasUpdate = mGeekNewsList.get(mGeekNewsList.size() - 1).getJk_type();
//                Log.e("第一次更新的最后一篇的网址标示", hasUpdate + "");
                sum = hasUpdate - 1;
                times++;
                geekNewsList = GetData.getGeekList(sum, 8);
                for (int j = 0; j < geekNewsList.size(); j++) {
                    GeekNews geekNewsCut = geekNewsList.get(j);
                    if (geekNewsCut.getJk_title().contains("已删除") || geekNewsCut.getJk_title().contains("待审核")) {
                        geekNewsList.remove(geekNewsCut);
                    }
                }
            }
            return geekNewsList;
        }

        @Override
        protected void onPostExecute(List<GeekNews> maps) {
            super.onPostExecute(maps);
            if (NetWorkUtil.isNetWorkConnected(getActivity())) {
                mGeekNewsList.addAll(maps);
                pd.dismiss();
                initData(true);
                mIsRefreshing = false;
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                }
            } else {
                Toast.makeText(app.getContext(), "无网络连接，请联网！", Toast.LENGTH_SHORT).show();
                if (swipeRefreshLayout.isRefreshing()) {
                    if (swipeRefreshLayout!=null) {
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setEnabled(true);
                    }
                }
            }
        }
    }

    class FirstAsyncTask extends AsyncTask<String, Void, List<GeekNews>> {
        int re = 0;

        @Override
        protected List<GeekNews> doInBackground(String... strings) {
            re = Integer.parseInt(strings[0]);
            times = 1;
            List<GeekNews> FirstList = null;
            if (NetWorkUtil.isNetWorkConnected(getActivity())) {
                FirstList = GetData.getGeekFirst();
            }
            return FirstList;
        }

        @Override
        protected void onPostExecute(List<GeekNews> geekNewses) {
            super.onPostExecute(geekNewses);
            if (NetWorkUtil.isNetWorkConnected(getActivity())) {
                if (re == 0) {
                    if (geekNewses != null) {
                        if (mGeekNewsList != null) {
                            mGeekNewsList.clear();
                        }
                        retry = 0;
                        firstSum = geekNewses.size();
//                    linearLayout.setVisibility(View.INVISIBLE);
//                    circleProgressView.stopSpinning();
                        mGeekNewsList.addAll(geekNewses);
                        if (pd != null) {
                            pd.dismiss();
                        }
                    } else {
                        Message message = new Message();
                        message.what = re;
                        firstHandler.sendMessageDelayed(message, 100);
                    }
                } else if (re == 1 && firstSum > 0) {
//                    Log.e("第一次加载数量",firstSum+"");
                    if (geekNewses != null) {
                        retry = 0;
                        int j = geekNewses.get(geekNewses.size() - 1).getJk_type();
                        int k = mGeekNewsList.get(firstSum - 1).getJk_type();
                        int y = j - k;
                        List<GeekNews> old = new ArrayList<>();
                        List<GeekNews> load = new ArrayList<>();
                        for (int m = firstSum; m < mGeekNewsList.size(); m++) {
                            load.add(mGeekNewsList.get(m));
                        }
                        for (int n = firstSum - y; n <= firstSum; n++) {
                            if (n == firstSum) {
                            } else {
                                old.add(mGeekNewsList.get(n));
                                //                            Log.e("需要另外取出的旧数据", mGeekNewsList.get(n).getJk_title());
                            }
                        }
                        if (mGeekNewsList != null) {
                            mGeekNewsList.clear();
                        }
                        mGeekNewsList.addAll(geekNewses);
                        mGeekNewsList.addAll(old);
                        mGeekNewsList.addAll(load);
                    } else {
                        Message message = new Message();
                        message.what = re;
                        firstHandler.sendMessageDelayed(message, 500);
                    }
                }
                initData(false);
                mIsRefreshing = false;
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                }
            } else {
                if (pd != null) {
                    pd.dismiss();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                }
//                pd = ProgressDialog.show(getActivity(),"网络连接错误！","请联网！");
//                finishHandler.sendEmptyMessageDelayed(0,1000);
                Toast.makeText(app.getContext(), "无网络连接，请联网！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private android.os.Handler firstHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what >= 0) {
                if (retry <= 7) {
                    retry++;
                    Toast.makeText(app.getContext(), "正在重新刷新IT前沿~~", Toast.LENGTH_SHORT).show();
                    new FirstAsyncTask().execute(String.valueOf(msg.what));
                } else {
                    Toast.makeText(app.getContext(), "重连失败，请检查网络！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(v);// 设置布局
//        , new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT)
        return loadingDialog;

    }
}



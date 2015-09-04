package com.xuxu.datatool.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.xuxu.datatool.Model.CNews;
import com.xuxu.datatool.Model.CNewsDetails;
import com.xuxu.datatool.Model.News;
import com.xuxu.datatool.Model.NewsDto;
import com.xuxu.datatool.Model.NewsPicture;
import com.xuxu.datatool.R;
import com.xuxu.datatool.adpter.ListAdpter;
import com.xuxu.datatool.adpter.RecyclerAdapter1;
import com.xuxu.datatool.adpter.RecyclerAdapter2;
import com.xuxu.datatool.data.DataBaseTool_CNew;
import com.xuxu.datatool.data.DataBasetool;
import com.xuxu.datatool.utils.GetData;
import com.xuxu.datatool.utils.NetWorkUtil;
import com.xuxu.datatool.utils.ShareUtil;
import com.xuxu.datatool.widget.AutoScrollTextView;
import com.xuxu.datatool.widget.CircleProgressView;
import com.xuxu.datatool.widget.RefreshLayout;
import com.xuxu.datatool.widget.UserListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/8/6.
 */
public class CQUPTNews extends Fragment {
    private ListView listView;
    private RefreshLayout cSwipeRefreshLayout;
    private View view;
    private View view1;
    private ListAdpter listAdpter;
    private DataBasetool dbt;
    private int page = 1;
    private static final String TAG = "CQUPT";
    private ViewPager mBanner;
    private BannerAdapter mBannerAdapter;
    private ImageView[] mIndicators;
    private Timer mTimer = new Timer();
    private boolean hasFirst=false;

    private int mBannerPosition = 0;
    private final int FAKE_BANNER_SIZE = 100;
    private  int DEFAULT_BANNER_SIZE = 5;
    private boolean mIsUserTouched = false;
    private List<NewsPicture> newsPictures;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private AutoScrollTextView autoScrollTextView;
    private View view2;
    private TextView test;
    private String testString;
    private DataBaseTool_CNew dataBaseTool_cNew;
    private List<CNews> data_new;
    private ProgressDialog pd;
    private CircleProgressView circleProgressView;
    private LinearLayout linearLayout;
    private List<CNews> SumList;
    private boolean hasLoad = false;
    private boolean isVisible = false;
    private ProgressWheel progressWheel;
    private int toast = 1;
    private int retry = 0;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;

            if (NetWorkUtil.isNetWorkConnected(getActivity()) && isVisible && !hasLoad) {
                cSwipeRefreshLayout.setEnabled(false);
                showProgressWheel(true);
                initBannerData();
//                Log.e("加载了第二页数据","");
                new NewsPictureAsyncTask().execute();
                new CQUPTAsyncTask().execute(String.valueOf(1));
                hasLoad = true;
            }

        } else {
            isVisible = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dataBaseTool_cNew = new DataBaseTool_CNew(getActivity());
        SumList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cquptnews, container, false);
        LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view2 = lif.inflate(R.layout.header, new ListView(getActivity()), false);//找到布局文件，并且把布
        // 局属性转换成listview形式
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//         pd = ProgressDialog.show(getActivity(), "首次加载时间较长！", "让子弹飞一会儿！");
//        pd = ProgressDialog.show(getActivity(), "connecting", "正在连接");
        autoScrollTextView = (AutoScrollTextView) view.findViewById(R.id.TextViewNotice);
        autoScrollTextView.init(getActivity().getWindowManager());
        autoScrollTextView.startScroll();
        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        int loadingResource = R.drawable.img1;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .build();
        initCircle();
        initView();
        initViewPager();


        mTimer.schedule(mTimerTask, 5000, 5000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                pd.dismiss();
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_look) {
            try {
                String imgPath = ShareUtil.saveToSD(getActivity());
                Toast.makeText(app.getContext(), "截图完成", Toast.LENGTH_SHORT).show();
//                Log.e("最后要分享的图片路径", imgPath);
                ShareUtil.sharePicture(getActivity(), imgPath, "天气");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //    public int getColorPrimary(){
//        TypedValue typedValue = new  TypedValue();
//        app.getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
//        return typedValue.data;
//    }
    private void showProgressWheel(boolean visible) {
        progressWheel.setBarColor(getResources().getColor(R.color.dark_red));
        if (visible) {
            if (!progressWheel.isSpinning())
                progressWheel.spin();
        } else {
            progressWheel.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressWheel.isSpinning()) {
                        progressWheel.stopSpinning();
                    }
                }
            }, 500);
        }
    }

    /*
    * 设置提示点*/
    private void setIndicator(int position) {
        position %= DEFAULT_BANNER_SIZE;
        for (ImageView indicator : mIndicators) {
            indicator.setImageResource(R.mipmap.indicator_unchecked);
        }
        mIndicators[position].setImageResource(R.mipmap.indicator_checked);
    }

    private void initCircle() {
        linearLayout = (LinearLayout) view.findViewById(R.id.progressLayout);
//        circleProgressView = (CircleProgressView) view.findViewById(R.id.circleView);
//        circleProgressView.setMaxValue(100);
//        circleProgressView.setValue(0);
//        circleProgressView.setSpinningBarLength(200);
//        circleProgressView.setValueAnimated(24);

////        show unit
//        circleProgressView.setUnit("%");
//        circleProgressView.setShowUnit(true);

//        text sizes
//        circleProgressView.setTextSize(20); // text size set, auto text size off
//        circleProgressView.setUnitSize(15); // if i set the text size i also have to set the unit size
//
//        circleProgressView.setAutoTextSize(true);
//        circleProgressView.spin();
    }

    private void initView() {
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel);
//        data_new = dataBaseTool_cNew.selectAll();
        listView = (ListView) view.findViewById(R.id.listView_cqupt);
        listView.setDividerHeight(0);

        listView.addHeaderView(view2, null, false);
//        if (data_new != null) {
//            listAdpter = new ListAdpter(getActivity(), data_new);
//            listView.setAdapter(listAdpter);
//        }
//        cRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        cRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cSwipeRefreshLayout = (RefreshLayout) view.findViewById(R.id.swipe_refresh_cqupt);
        cSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        cSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetWorkUtil.isNetWorkConnected(getActivity())) {
                    new CQUPTAsyncTask().execute(String.valueOf(1));
                    new NewsPictureAsyncTask().execute();
                } else {
                    Toast.makeText(app.getContext(), "无网络连接", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        cSwipeRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
//            @Override
//            public void onLoad() {
//
//            }
//        });
//        cSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources()
//                        .getDisplayMetrics()));
        cSwipeRefreshLayout.setEnabled(false);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                View firstView = absListView.getChildAt(i);
                if (i == 0 && (firstView == null || firstView.getTop() == 0)) {
                    cSwipeRefreshLayout.setEnabled(true);
                } else {
                    cSwipeRefreshLayout.setEnabled(false);
                }
                if (absListView.getAdapter() != null) {
                    if (absListView.getLastVisiblePosition() + 5 == (absListView.getAdapter().getCount())
                            && !cSwipeRefreshLayout.isRefreshing()) {
                        page++;
                        cSwipeRefreshLayout.setEnabled(false);
                        cSwipeRefreshLayout.setRefreshing(true);//是swipeRefreshLayout失效，避免重复联网获取数据,并且得在获取数据之前设置
                        new CQUPTAsyncTask().execute(String.valueOf(page));
                    }
                }
            }
        });
    }

    private void initViewPager() {
        view1 = getActivity().getLayoutInflater().inflate(R.layout.header, null);
//        view1.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 500));

        mIndicators = new ImageView[]{
                (ImageView) view2.findViewById(R.id.indicator1),
                (ImageView) view2.findViewById(R.id.indicator2),
                (ImageView) view2.findViewById(R.id.indicator3),
                (ImageView) view2.findViewById(R.id.indicator4),
                (ImageView) view2.findViewById(R.id.indicator5)
        };

        mBanner = (ViewPager) view2.findViewById(R.id.banner);
//        mBanner.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 500));
        mBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                    cSwipeRefreshLayout.setEnabled(false);
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                    cSwipeRefreshLayout.setEnabled(true);
                }
                return false;
            }

        });

        mBanner.setOnPageChangeListener(mBannerAdapter);
    }

    private void initBannerData() {
        mBannerAdapter = new BannerAdapter(getActivity(), newsPictures);
        mBanner.setAdapter(mBannerAdapter);
    }

    class NewsPictureAsyncTask extends AsyncTask<String, Void, List<NewsPicture>> {

        @Override
        protected List<NewsPicture> doInBackground(String... strings) {
            List<NewsPicture> list_img = null;
            //增加此判断，解决没有网络连接，程序终止问题
            if (NetWorkUtil.isNetWorkConnected(app.getContext())) {
                list_img = GetData.getNewsPicture();
//                Log.e("list_img,抓取的viewpager", list_img + "");
            }
            return list_img;
        }

        @Override
        protected void onPostExecute(List<NewsPicture> newsPictures) {
            super.onPostExecute(newsPictures);
            if (newsPictures == null) {
                Message message = new Message();
                message.what = 1;
                reHandler.sendMessageDelayed(message, 1000);
            }
            mBannerAdapter = new BannerAdapter(getActivity(), newsPictures);
            if (newsPictures != null) {
                for (int i = newsPictures.size(); i < 5; i++) {
                    mIndicators[i].setVisibility(View.INVISIBLE);
                }
            }
//            FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                    FrameLayout.LayoutParams.WRAP_CONTENT);
//            mBanner.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, 500)); //
            mBanner.setAdapter(mBannerAdapter);
            mBanner.setOnPageChangeListener(mBannerAdapter);
        }
    }

    private Handler reHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                Log.e("重加载次数", retry + "");
                new NewsPictureAsyncTask().execute();
                retry++;
            }
        }
    };

    class CQUPTAsyncTask extends AsyncTask<String, Void, List<CNews>> {
        int pageLittle = 0;
        List<CNews> moreList = new ArrayList<>();
        @Override
        protected List<CNews> doInBackground(String... strings) {
            boolean isHaved = false;
            pageLittle = Integer.parseInt(strings[0]);
//            dbt = new DataBasetool(getActivity());
            List<CNews> list_c = new ArrayList<CNews>();
            List<CNews> list_c_select = new ArrayList<CNews>();
            List<CNewsDetails> list_img = new ArrayList<CNewsDetails>();
            //增加此判断，解决没有网络连接，程序终止问题
            if (NetWorkUtil.isNetWorkConnected(getActivity())) {
//                GetData.getNewCquptData(1);
                list_c = GetData.getNewCquptData(pageLittle);
                if (list_c != null) {
//                        Log.e("每次获取的新闻列表数据总数", list_c.size()+"");
                }
//                if (list_c != null) {
//                    list_c_select = dataBaseTool_cNew.selectAll();
//                    for (int index = list_c.size() - 1; index >= 0; index--) {//逆序插入,注意减1
//                        CNews cNews = list_c.get(index);
//                        if (list_c_select != null) {
//                            for (CNews cNews_select : list_c_select) {
//                                if (cNews_select.getcTitle().equals(cNews.getcTitle())) {
//                                    isHaved = true;
//                                }
//                            }
//                            if (cNews != null && !isHaved) {
//                                dataBaseTool_cNew.addToNews(cNews);
////                                Log.e("成功插入news一条数据", "cNews.getcTitle()");
//                                isHaved = false;
//                            }
//                        }
//                    }
//                    //第一次安装程序或清除缓存后没有数据
//                    if (list_c_select == null) {
//                        for (int j = list_c.size() - 1; j >= 0; j--) {
//                            dataBaseTool_cNew.addToNews(list_c.get(j));
////                            Log.e("插入news一条数据", "list_c.get(j).getcTitle()");
//                        }
//                    }
//                }
            }

            return list_c;
        }

        @Override
        protected void onPostExecute(List<CNews> maps) {
            super.onPostExecute(maps);
            if (maps != null) {
                toast = 0;
                if (pageLittle == 1) {
                    if(SumList!=null){
                        for(int i=maps.size();i<SumList.size();i++) {
                            moreList.add(SumList.get(i));
                        }
                        SumList.clear();
                    }
                    SumList.addAll(maps);
                    SumList.addAll(moreList);
                    listAdpter = new ListAdpter(getActivity(), SumList);
                    listView.setAdapter(listAdpter);
                    if(linearLayout.getVisibility()==View.VISIBLE){
                        Animation animation = AnimationUtils.loadAnimation(linearLayout.getContext(), R
                                .anim.backgroud_slide_out);
                        linearLayout.startAnimation(animation);
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                } else {
                    SumList.addAll(maps);
                    listAdpter.notifyDataSetChanged();
                }
                cSwipeRefreshLayout.setEnabled(true);
//                linearLayout.setVisibility(View.INVISIBLE);
                showProgressWheel(false);
//                    circleProgressView.stopSpinning();
                if (cSwipeRefreshLayout.isRefreshing()) {
                    cSwipeRefreshLayout.setRefreshing(false);
                }
            } else {
                Message message = new Message();
                message.what = pageLittle;
                againHandler.sendMessageDelayed(message, 100);
            }
        }
    }

    private android.os.Handler againHandler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (toast <= 7) {
                if (toast == 2 || toast == 5) {
                    Toast.makeText(app.getContext(), "正在重新获取重邮动态！", Toast.LENGTH_SHORT).show();
                }
                toast++;
                new CQUPTAsyncTask().execute(String.valueOf(msg.what));
            } else {
                Toast.makeText(app.getContext(), "重连失败，请检查网络！", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!mIsUserTouched) {
                mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
//                Log.d(TAG, "tname:" + Thread.currentThread().getName());
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mBanner.setCurrentItem(mBannerPosition);
            }
        }
    };
    public class BannerAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        private LayoutInflater mInflater;
        private List<NewsPicture> newsPictures;
        private String getUrl;

        public BannerAdapter(Context context, List<NewsPicture> newsPictures) {
            mInflater = LayoutInflater.from(context);
            this.newsPictures = newsPictures;
        }

        @Override
        public int getCount() {
//此处返回值一定要尽量大，不然会出现滑动问题
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (newsPictures != null) {
                DEFAULT_BANNER_SIZE=newsPictures.size();
            }
            position %= DEFAULT_BANNER_SIZE;
            View view = mInflater.inflate(R.layout.item, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            if (newsPictures != null) {
                imageLoader.displayImage(newsPictures.get(position).getImageUrl(), imageView, options);
            } else {
//                Toast.makeText(getActivity(), "学校主页抽风了！得不到照片", Toast.LENGTH_SHORT).show();
            }
            final int pos = position;
            final int finalPosition = position;
            view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
//                                            Toast.makeText(getActivity(), "click banner item :" + pos, Toast.LENGTH_SHORT).show();
//                                            Log.e("uri", newsPictures.get(finalPosition).getNewsUrl().toString());

//                    final ProgressDialog pd = ProgressDialog.show(getActivity(), "connecting", "正在连接");
                                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                            if (getUrl != null) {
                                            }
                                            Bundle bundle = new Bundle();
                                            bundle.putString("getUrl", getUrl);
                                            bundle.putString("url", newsPictures.get(finalPosition).getNewsUrl().toString());
                                            getActivity().overridePendingTransition(0, R.anim.base_slide_right_in);
                                            intent.putExtras(bundle);
                                            //获取网址错误或不规范（不是http开头）
                                            // 导致无法启动
                                            getActivity().startActivity(intent);
                                        }
                                    }
            );
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = mBanner.getCurrentItem();
            if (position == 0) {
                position = DEFAULT_BANNER_SIZE;
                mBanner.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = DEFAULT_BANNER_SIZE - 1;
                mBanner.setCurrentItem(position, false);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mBannerPosition = position;
            setIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}


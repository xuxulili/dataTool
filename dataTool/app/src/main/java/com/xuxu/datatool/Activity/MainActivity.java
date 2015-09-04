package com.xuxu.datatool.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xuxu.datatool.R;
import com.xuxu.datatool.adpter.TabFragmentAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.xuxu.datatool.data.NotesDB;
import com.xuxu.datatool.utils.ChineseUtil;
import com.xuxu.datatool.utils.MyReceiver;
import com.xuxu.datatool.utils.NetWorkUtil;

/*
*
Widget Name	Description
android.support.design.widget.TextInputLayout	强大带提示的MD风格的EditText
android.support.design.widget.FloatingActionButton	MD风格的圆形按钮，来自于ImageView
android.support.design.widget.Snackbar	类似Toast，添加了简单的单个Action
android.support.design.widget.TabLayout	选项卡
android.support.design.widget.NavigationView	DrawerLayout的SlideMenu
android.support.design.widget.CoordinatorLayout	超级FrameLayout
android.support.design.widget.AppBarLayout	MD风格的滑动Layout
android.support.design.widget.CollapsingToolbarLayout	可折叠MD风格ToolbarLayout*/
//调用系统方法 @android:drawable/ic_menu_send
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    private android.support.v7.app.ActionBarDrawerToggle mActionBarDrawerToggle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> tabList;
    private List<Fragment> fragmentList;
    final String CSDNURL = "http://www.csdn.net/";
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private MaterialDialog noNetWorkDialog;
    private MaterialDialog noNetWorkDialog1;
    private MaterialDialog noNetWorkDialog2;
    private MaterialDialog noNetWorkDialog3;

    private Context context;
    private SQLiteDatabase dbReader;
    private AlarmManager aManager;
    private PendingIntent pi;
    //标识是否点击过一次back退出
    private boolean mIsExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.addActivity(this);
        /*1、Style（风格）
        Layout（布局）
        Activity（代码）
        如果你想要通过toolbar.setTitle(“主标题”);设置Toolbar的标题，你必须在调用它之前调用如下代码：
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        //第一视图为CoordinatorLayout导致navigation无法显示
        ChineseUtil.getUrl("丰城", this);
        initToolBar();
        initNavigationView();
        initTabLayout();
        initFloatingButton();
        initDialog();
        initViewPager();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

    /*初始化Viewpager*/
    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        if (viewPager != null) {
            setupViewpager(viewPager);
        }
    }

    private void initNavigationView() {
        mNavigationView = (NavigationView) findViewById(R.id.id_nv_menu);
        if (mNavigationView != null) {
            setupContentDrawer(mNavigationView);
        }
        //为setNavigationIcon设置动作
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolbar.setTitle(getString(R.string.title));
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowHomeEnabled(true);   //使左上角图标是否显示，如果设成false，则没有程序
            // 图标，仅仅就个标题，否则，显示应用程序图标，对应id为android.R.id.home，
            actionBar.setDisplayShowCustomEnabled(true);  // 使自定义的普通View能在title栏显示，
            // 即actionBar.setCustomView能起作用
//            actionBar.setDisplayShowTitleEnabled(true);   //对应ActionBar.DISPLAY_SHOW_TITLE。
            actionBar.setLogo(R.drawable.ic_discuss);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            LinkedList<Integer> linkedList = new LinkedList<>();

        }

    }

    private void initDialog() {
        if (!NetWorkUtil.isNetWorkConnected(this)) {

            if (noNetWorkDialog == null) {
                noNetWorkDialog = new MaterialDialog.Builder(MainActivity.this)
                        .title("无网络连接")
                        .content("去开启网络?")
                        .positiveText("是")
                        .negativeText("否")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                Intent intent = new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(intent);
                            }
                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                finish();
                            }
                        })
                        .cancelable(false)//这句话决定返回键是否能退出dialog
                        .build();
            }
            if (!noNetWorkDialog.isShowing()) {
                noNetWorkDialog.show();
            }
        }
    }

    private void initTabLayout() {

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabList = new ArrayList<>();
        tabList.add(getString(R.string.jike_topTitle));
        tabList.add(getString(R.string.universityTop));

        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(0)));//添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(tabList.get(1)));
    }

    private void initFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "再次点击查询天气", Snackbar.LENGTH_SHORT)
                        .setAction("click", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (NetWorkUtil.isNetWorkConnected(MainActivity.this)) {
                                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);

                                    startActivity(intent);
                                    //必须在startactity之后调用，第一个为打开activity的动画，第二个为退出所打开activity的动画
                                    overridePendingTransition(R.anim.base_slide_right_in, 0);
                                } else {
                                    Toast.makeText(MainActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
            }
        });
    }

    private void setupViewpager(ViewPager viewPager) {
        /*
        * 总结：首先在每个fragment初始化并且recycleView适配，然后把每个fragment加入list再为Viewpager适配,
        * 并把tablayout和Viewpager关联*/
        fragmentList = new ArrayList<>();
        GetDataFromWebFragment f1 = new GetDataFromWebFragment();
        Bundle bundle1 = new Bundle();
        f1.setArguments(bundle1);
        fragmentList.add(f1);
        CQUPTNews f2 = new CQUPTNews();
        Bundle bundle2 = new Bundle();
        f2.setArguments(bundle2);
        fragmentList.add(f2);

        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), fragmentList, tabList);
        viewPager.setAdapter(fragmentAdapter);//给ViewPager设置适配器
//        viewPager.setOffscreenPageLimit(0);//是viewPager只加载一个缓存页面增加至2个Fragment

        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);//给Tabs设置适配器
    }

    //响应导航页事件
    private void setupContentDrawer(final NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.job:
                                if (noNetWorkDialog2 == null) {
                                    noNetWorkDialog2 = new MaterialDialog.Builder(MainActivity.this)
                                            .negativeText("返回")
                                            .positiveText("查看就业网")
                                            .content("开发中，敬请期待！")
                                            .callback(new MaterialDialog.ButtonCallback() {
                                                @Override
                                                public void onPositive(MaterialDialog dialog) {
                                                    if (NetWorkUtil.isNetWorkConnected(MainActivity.this)) {
                                                        noNetWorkDialog2.dismiss();
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://job.cqupt.edu.cn"));

                                                        startActivity(intent);
                                                        //必须在startactity之后调用，第一个为打开activity的动画，第二个为退出所打开activity的动画
                                                        overridePendingTransition(R.anim.base_slide_right_in, 0);
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onNegative(MaterialDialog dialog) {
                                                    noNetWorkDialog2.dismiss();
                                                }
                                            })
                                            .cancelable(true)
                                            .build();
                                }
                                if (!noNetWorkDialog2.isShowing()) {
                                    noNetWorkDialog2.show();
                                }
                                menuItem.setChecked(false);
                                break;
                            case R.id.about:
                                if (noNetWorkDialog1 == null) {
                                    noNetWorkDialog1 = new MaterialDialog.Builder(MainActivity.this)
                                            .title("重邮极客 Version1.1")
                                            .content("关注重邮动态，分享IT资讯!\n反馈地址:chongyoujike@163.com,谢谢!此外非常感谢极客头条的IT数据!\n开发者:xuxu")
                                            .negativeText("返回")
                                            .callback(new MaterialDialog.ButtonCallback() {
                                                @Override
                                                public void onNegative(MaterialDialog dialog) {
                                                    dialog.dismiss();
                                                    super.onNegative(dialog);
                                                }
                                            })
                                            .cancelable(true).build();
                                }
                                if (!noNetWorkDialog1.isShowing()) {
                                    noNetWorkDialog1.show();
                                }
                                menuItem.setChecked(false);
                                break;
                            case R.id.useBook:
                                if (noNetWorkDialog3 == null) {
                                    noNetWorkDialog3 = new MaterialDialog.Builder(MainActivity.this)
                                            .title("应用手册")
                                            .positiveText("返回")
                                            .content("1、新闻内容丰富多样，如果您喜欢哪篇文章，可以点击" +
                                                            "右上角分享按钮，把文章保存至本地记事本QQ微信等等，方便电脑端阅读；也可以点击查看原文查看原网页；" + "\n"
                                                            + "2、点击右下方的按钮，您可以查看本地天气，也可在上方编辑框输入城市名，" +
                                                            "查询其他城市天气，这时您可以点击分享按钮，会自动为您截取当前屏幕，随意分享图片；" + "\n"
                                                            + "3、重邮主页不稳定，有时无法获取数据，不过应用能自动重新获取，" +
                                                            "请耐心等待，肯定能够获取相关新闻；" + "\n" + "4、点击顶部的中间位置，IT前沿可以自动返回顶部。"
                                            )
                                            .callback(new MaterialDialog.ButtonCallback() {
                                                @Override
                                                public void onPositive(MaterialDialog dialog) {
                                                    noNetWorkDialog3.dismiss();
                                                }

                                                @Override
                                                public void onNegative(MaterialDialog dialog) {
                                                }
                                            })
                                            .cancelable(true)
                                            .build();
                                }
                                if (!noNetWorkDialog3.isShowing()) {
                                    noNetWorkDialog3.show();
                                }
                                menuItem.setChecked(false);
                                break;
                            case R.id.notify:
                                setAlarm(mNavigationView);
//                                setAlarmOne();
//                                aManager.cancel(pi);//用于取消闹钟
                                break;
                            case R.id.exit:
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.drawable.ic_input_delete) {
            //打开抽屉侧滑菜单
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("退出"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
//        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if ((System.currentTimeMillis() - exitTime) > 2000) {
//                        exitTime = System.currentTimeMillis();//返回当前时间至毫秒
//                    } else {
                    dialog.dismiss();
                    moveTaskToBack(false);
//                finish();
//                    }

                }
                return true;
            }
        });
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                finish();
//                Toast.makeText(app.getContext(), "确认" + which, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(app.getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("后台运行", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                moveTaskToBack(false);//后台运行实现
//                Toast.makeText(app.getContext(), "后台运行" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showExitDialog();
                Toast.makeText(app.getContext(), "再按一次后台运行！", Toast.LENGTH_SHORT).show();
//                Toast.makeText(app.getContext(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
            }
//            } else {
//                moveTaskToBack(false);
////                finish();
//            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private void setAlarmOne() {
        AlarmManager alarmManagerOne = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intentOne = new Intent();
        intentOne.setAction("alarmOne");
        PendingIntent piOne = PendingIntent.getBroadcast(MainActivity.this, 0, intentOne, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendarOne = Calendar.getInstance();
        calendarOne.setTimeInMillis(System.currentTimeMillis());
        calendarOne.set(Calendar.HOUR_OF_DAY, 9);
        calendarOne.set(Calendar.MINUTE, 40);
        calendarOne.set(Calendar.SECOND, 0);
        alarmManagerOne.setRepeating(AlarmManager.RTC_WAKEUP, calendarOne.getTimeInMillis(), AlarmManager.INTERVAL_DAY, piOne);
        Log.e("闹钟一设置成功", "");
    }

    private void setAlarm(final NavigationView navigationView) {
        Calendar cal = Calendar.getInstance();
        Log.d("", "click the time button to set time");
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                Log.e("设置的时间", String.valueOf(hourOfDay));
                Log.e("设置的时间", String.valueOf(minute));
                aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                Intent intent = new Intent();
                intent.setAction("com.test.BC_ACTION");
                intent.putExtra("msg", "该去开会啦！");
                pi = PendingIntent.getBroadcast(MainActivity.this, 0,
                        intent, 0);
                Calendar calendar = Calendar.getInstance();
//                c.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);//分钟数，月份会有偏移
//                aManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), pi);
                aManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
                Snackbar.make(navigationView, "后台推送设置成功，时间为" + hourOfDay + ":" + minute, Snackbar.LENGTH_SHORT).show();
            }
        }, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false);
        dialog.show();
    }
}

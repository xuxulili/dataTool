package com.xuxu.datatool.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.xuxu.datatool.R;

import java.util.Random;

public class WelcomeActivity extends Activity {

    ImageView mBackgroundImage;
    TextView mTitleText;
    TextView mVersionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        App app = (App) getApplication();//获取应用程序全局的实例引用
//        app.activities.add(this);    //把当前Activity放入集合中
        setContentView(R.layout.activity_welcome);
        ActivityManager.addActivity(this);
        View decorView = getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        //隐藏导航栏
        newUiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(newUiOptions);

        mBackgroundImage = (ImageView) findViewById(R.id.image_background);
        Random random = new Random();
//        int num = random.nextInt(4);
        int drawable = R.drawable.pic_background_1;
//        switch (num ){
//            case 0:
//                break;
//            case 1:
//                drawable = R.drawable.pic_background_2;
//                break;
//            case 2:
//                drawable = R.drawable.pic_background_3;
//                break;
//        }
        mBackgroundImage.setImageDrawable(getResources().getDrawable(drawable));
        final Animation animImage = AnimationUtils.loadAnimation(this, R.anim.image_welcome);
        mBackgroundImage.startAnimation(animImage);
        animImage.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束时打开首页
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mTitleText = (TextView) findViewById(R.id.title_text);
        mVersionText = (TextView) findViewById(R.id.version_text);
//        mVersionText.setText("版本：1.0.0");
    }

    @Override
    public void finish() {
        mBackgroundImage.destroyDrawingCache();
        super.finish();
    }
}

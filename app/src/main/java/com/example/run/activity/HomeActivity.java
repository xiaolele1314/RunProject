package com.example.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.adapter.HomeBannerAdapter;
import com.example.run.bomb.MyUser;
import com.example.run.manager.UserManage;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.xuexiang.xui.widget.banner.recycler.BannerLayout;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.util.V;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvNickName;
    private TextView tvMoney;
    private TextView tvId;
    private RadiusImageView ivPhoto;
    private BannerLayout bannerLayout;
    private TextView tvUp;

    //底部选单
    private View bottomSheet;
    private BottomSheetBehavior<View> behavior;

    //选单的选项view
    private LinearLayout linTake;
    private ImageView ivClose;

    //中间的横向滑动view，bannerLayout内部实现RecyclerView
    private HomeBannerAdapter adapter;
    private List<Integer> list;

    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        initView();
        initData();
    }

    @Override
    public void init() {
        list = new ArrayList<>();
        intent = new Intent();
    }

    @Override
    public void initView() {
        tvNickName = findViewById(R.id.tv_nickName_home);
        tvId = findViewById(R.id.tv_money_home);
        tvMoney = findViewById(R.id.tv_money_home);
        tvUp = findViewById(R.id.tv_up_home);
        ivPhoto = findViewById(R.id.iv_photo_home);
        bannerLayout = findViewById(R.id.banner_home);

        //获取behavior
        bottomSheet = findViewById(R.id.nes_home);
        behavior = BottomSheetBehavior.from(bottomSheet);

        //隐藏底部选单
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //获取选单选项
        linTake = bottomSheet.findViewById(R.id.lin_take_home);
        ivClose = bottomSheet.findViewById(R.id.iv_close_home);

        //设置点击事件
        tvUp.setOnClickListener(this);
        linTake.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void initData() {
        MyUser user = UserManage.getInstance().getUser();
        tvNickName.setText(user.getName());
        tvMoney.setText(String.valueOf(user.getMoney()));

        //为bannerLayout的RecyclerView设置适配器
        list.add(R.drawable.ima_51);
        list.add(R.drawable.ima_51);
        list.add(R.drawable.ima_51);
        adapter = new HomeBannerAdapter(list);
        bannerLayout.setAdapter(adapter);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_up_home:
                if(behavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.iv_close_home:
                if(behavior.getState() != BottomSheetBehavior.STATE_HIDDEN){
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case R.id.lin_take_home:
                //跳转地图信息界面
                intent.setClass(HomeActivity.this,TakeActivity.class);
                startActivity(intent);
                break;

        }
    }
}

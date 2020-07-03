package com.example.run.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.adapter.OrderViewPagerAdapter;
import com.example.run.fragment.CollectFragment;
import com.example.run.fragment.TakeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity {

    private ImageView ivClose;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OrderViewPagerAdapter adapter;
    private List<Fragment> list;

    private TakeFragment takeFragment;
    private CollectFragment collectFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
        initView();
        initData();
        initEvent();
    }

    @Override
    public void init() {
        takeFragment = new TakeFragment();
        collectFragment = new CollectFragment();
        list = new ArrayList<>();
        list.add(takeFragment);
        list.add(collectFragment);
        adapter = new OrderViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,list);
    }

    @Override
    public void initView() {
        ivClose = findViewById(R.id.iv_close_order);
        tabLayout = findViewById(R.id.tab_order);
        viewPager = findViewById(R.id.viewPager_order);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}

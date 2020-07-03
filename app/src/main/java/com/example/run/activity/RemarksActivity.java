package com.example.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.run.BaseActivity;
import com.example.run.R;

public class RemarksActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivClose;
    private EditText etContent;
    private TextView tvComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);

        initView();
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        ivClose = findViewById(R.id.iv_close_remarks);
        etContent = findViewById(R.id.et_remarks);
        tvComplete = findViewById(R.id.tv_complete_remarks);

        ivClose.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_remarks:
                finish();
                break;
            case R.id.tv_complete_remarks:
                complete();
                break;
        }
    }

    private void complete(){
        Intent intent = new Intent();
        intent.putExtra("content",etContent.getText().toString().trim());
        setResult(RESULT_OK,intent);
        finish();
    }
}

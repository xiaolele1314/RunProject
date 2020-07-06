package com.example.run.activity;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.bomb.MyUser;
import com.example.run.manager.UserManage;
import com.example.run.utils.PhotoUtils;
import com.example.run.widget.RoundImageView;

import java.io.FileNotFoundException;

import static android.provider.MediaStore.EXTRA_OUTPUT;

public class PersonMsgActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_portrait;
    private RoundImageView ri_portrait;
    private Uri imageUri;
    private String imagePath;
    private PopupWindow popupWindow;
    private PhotoUtils photoUtils = new PhotoUtils();
    private static final int TAKE_PHOTO = 1;
    private static final int FROM_ALBUMS = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_msg);

        initView();

    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        ri_portrait = (RoundImageView) findViewById(R.id.ri_portrait);
        ll_portrait = (LinearLayout) findViewById(R.id.ll_portrait);

        ll_portrait.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    //展示修改头像的选择框，并设置选择框的监听器
    private void show_popup_windows(){
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select,null);
        if(popupWindow==null){
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //设置监听器
        TextView take_photo =  (TextView) layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = (TextView)  layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //拍照按钮监听
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow != null && popupWindow.isShowing()) {
                    imageUri = photoUtils.take_photo_util(PersonMsgActivity.this, "com.example.run.fileprovider", "output_image.jpg");
                    //调用相机，拍摄结果会存到imageUri也就是outputImage中
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                    //去除选择框
                    popupWindow.dismiss();
                }
            }
        });
        //相册按钮监听
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请权限
                if(ContextCompat.checkSelfPermission(PersonMsgActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PersonMsgActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //打开相册
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, FROM_ALBUMS);
                }
                //去除选择框
                popupWindow.dismiss();
            }
        });
        //取消按钮监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_portrait:
                Log.e("PersonMsgActivity","click ll_portrait");
                show_popup_windows();
                break;

        }
    }

    //处理拍摄照片回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照得到图片
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的图片展示并更新数据库
                        Bitmap bitmap = BitmapFactory.decodeStream((getContentResolver().openInputStream(imageUri)));
                        ri_portrait.setImageBitmap(bitmap);
                        UserManage.getInstance().getUser().setPortrait(photoUtils.bitmap2byte(bitmap));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //从相册中选择图片
            case FROM_ALBUMS:
                if (resultCode == RESULT_OK) {
                    //判断手机版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        imagePath = photoUtils.handleImageOnKitKat(this, data);
                    } else {
                        imagePath = photoUtils.handleImageBeforeKitKat(this, data);
                    }
                }
                if (imagePath != null) {
                    //将拍摄的图片展示并更新数据库
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    ri_portrait.setImageBitmap(bitmap);
                    //添加到MyUser类中
                    //UserManage.getInstance().getUser().setPortrait(photoUtils.bitmap2byte(bitmap));
                } else {
                    Log.d("food", "没有找到图片");
                }
                break;

            //如果是编辑名字，则修改展示
            /**
             case EDIT_NAME:
             if(resultCode == RESULT_OK){
             ig_name.getContentEdt().setText(loginUser.getName());
             }
             break;
             **/
            default:
                break;
        }
    }
}

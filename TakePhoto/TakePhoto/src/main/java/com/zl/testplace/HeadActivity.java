package com.zl.testplace;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zl.testplace.bean.CropBean;
import com.zl.testplace.constant.Constant;
import com.zl.testplace.utils.CameraUtils;
import com.zl.testplace.utils.ScreenUtils;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 改变头像
 *
 * @author zl
 * @date 2017/9/12 1:01
 */
public class HeadActivity extends BaseActivity implements View.OnClickListener {

    private final int REQUEST_CODE_ALBUM = 101;//相册回调
    private final int REQUEST_CODE_CAMER = 102;//相机回调
    private final int REQUEST_CODE_CROP = 103;//裁剪回调

    private ImageView ivHead;//头像
    private Button btnChange;//改变头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        init();
    }

    private void init() {
        ivHead = (ImageView) findViewById(R.id.iv_head);
        btnChange = (Button) findViewById(R.id.btn_change);
        btnChange.setOnClickListener(this);
    }

    /**
     * 跳转裁剪
     *
     * @param data
     */
    private void startCrop(Uri data) {
        CropBean albumCropBean = new CropBean();
        albumCropBean.dataUri = data;
        albumCropBean.outputX = ScreenUtils.dp2Px(55);
        albumCropBean.outputY = ScreenUtils.dp2Px(55);
        albumCropBean.caculateAspect();
        albumCropBean.isReturnData = false;
        albumCropBean.saveUri = Uri.fromFile(CameraUtils.headPortraitFile);
        //跳转裁剪
        CameraUtils.startCrop(this, albumCropBean, REQUEST_CODE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {//相册
            case REQUEST_CODE_ALBUM:
                startCrop(data.getData());
                break;
            case REQUEST_CODE_CAMER://相机
                startCrop(Uri.fromFile(CameraUtils.headPortraitFile));
                break;
            case REQUEST_CODE_CROP://裁剪完成
                Bitmap cropBitmap = BitmapFactory.decodeFile(CameraUtils.headPortraitFile.getPath());
                ivHead.setImageBitmap(cropBitmap);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change://改变头像
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("请选择图片获取方式");
                builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openPremissionAblum();
                    }
                });
                builder.setNeutralButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openPremissionCamera();
                    }
                });
                builder.create().show();
                break;
        }
    }

    //@AfterPermissionGranted：权限授权回调，当用户在授权之后，会回调带有AfterPermissionGranted对应权限的方法
    @AfterPermissionGranted(Constant.PREMISSION_CAMERA)
    public void openPremissionCamera() {
        if (CameraUtils.isOpenPremission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            CameraUtils.startCamer(HeadActivity.this, CameraUtils.headPortraitFile, REQUEST_CODE_CAMER);
        } else {
            EasyPermissions.requestPermissions(HeadActivity.this, "您需要打开拍照权限以及读取相册权限", Constant.PREMISSION_CAMERA, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @AfterPermissionGranted(Constant.PREMISSION_WRITE_EXTERNAL_STORAGE)
    public void openPremissionAblum() {
        if (CameraUtils.isOpenPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            CameraUtils.startAlbum(HeadActivity.this, REQUEST_CODE_ALBUM);
        } else {
            EasyPermissions.requestPermissions(HeadActivity.this, "您需要打开读取相册权限", Constant.PREMISSION_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

}

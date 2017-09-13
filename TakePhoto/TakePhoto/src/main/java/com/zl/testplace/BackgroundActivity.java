package com.zl.testplace;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zl.testplace.constant.Constant;
import com.zl.testplace.utils.ImageUtils;
import com.zl.testplace.utils.PictureUtil;
import com.zl.testplace.utils.UriUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 切换背景
 *
 * @author zl
 * @date 2017/9/12 1:06
 */
public class BackgroundActivity extends BaseActivity implements View.OnClickListener {
    private final int REQUEST_CODE_ALBUM = 101;//相册回调
    private final int REQUEST_CODE_CAMER = 102;//相机回调
    private ImageView ivBg;//背景
    private Button btnChange;//更改背景图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        init();
    }

    private void init() {
        ivBg = (ImageView) findViewById(R.id.iv_bg);
        btnChange = (Button) findViewById(R.id.btn_change);
        btnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change://切换背景
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
        if (ImageUtils.isOpenPremission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ImageUtils.startCamer(BackgroundActivity.this, ImageUtils.bgFile, REQUEST_CODE_CAMER);
        } else {
            EasyPermissions.requestPermissions(BackgroundActivity.this, "您需要打开拍照权限以及读取相册权限", Constant.PREMISSION_CAMERA, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @AfterPermissionGranted(Constant.PREMISSION_WRITE_EXTERNAL_STORAGE)
    public void openPremissionAblum() {
        if (ImageUtils.isOpenPremission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ImageUtils.startAlbum(BackgroundActivity.this, REQUEST_CODE_ALBUM);
        } else {
            EasyPermissions.requestPermissions(BackgroundActivity.this, "您需要打开读取相册权限", Constant.PREMISSION_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_ALBUM://相册
                Uri dataUri = data.getData();
                Log.i("mengyuanuri", "相册uri:" + dataUri.getScheme() + ":" + dataUri.getSchemeSpecificPart());
                //压缩图片
                Bitmap bitmapAlbum = PictureUtil.getSmallBitmap(UriUtil.getPath(dataUri), 720, 1080);
                //保存图片到根目录的AAA文件夹
                String saveDir = Environment.getExternalStorageDirectory() + File.separator + "AAA";
                File file = new File(saveDir);
                if (!file.exists()) {
                    file.mkdir();
                }
                SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");//20170913141830
                String filePath = saveDir + File.separator + time.format(new Date()) + ".jpg";
                com.blankj.utilcode.util.ImageUtils.save(bitmapAlbum, filePath, Bitmap.CompressFormat.JPEG);
//                Bitmap bitmapAlbum = BitmapFactory.decodeFile(UriUtil.getPath(dataUri));
                ivBg.setImageBitmap(bitmapAlbum);
                break;
            case REQUEST_CODE_CAMER://相机
                File bgPath = ImageUtils.bgFile;
                Log.i("mengyuanuri", "回调的Path:" + bgPath.getPath());
                Bitmap bitmapCamer = BitmapFactory.decodeFile(bgPath.getPath());
                ivBg.setImageBitmap(bitmapCamer);
                break;
        }
    }

//    public static String getAllTime() {
//        return sdfAllTime.format(new Date());
//    }
}

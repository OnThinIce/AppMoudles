package com.zl.testplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 我的相机相册
 *
 * @author zl
 * @date 2017/9/12 0:55
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private Button btnHead;//头像选择
    private Button btnBackground;//背景图选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnHead = (Button) findViewById(R.id.btnHead);
        btnBackground = (Button) findViewById(R.id.btnBackground);
        btnHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HeadActivity.class);
                startActivity(intent);
            }
        });
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BackgroundActivity.class);
                startActivity(intent);
            }
        });
    }
}

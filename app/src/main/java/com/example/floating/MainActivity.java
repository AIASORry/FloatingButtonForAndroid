package com.example.floating;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1001;
    private static final String FLOAT_TAG = "AssistiveTouchTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShow = findViewById(R.id.btn_show_float);
        btnShow.setOnClickListener(v -> requestAndShowFloat());
    }

    private void requestAndShowFloat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);

        } else {
            showFloatingButton(this);

            Toast.makeText(this,
                    "Vui lòng bật Accessibility Service cho ứng dụng!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && Settings.canDrawOverlays(this)) {

                showFloatingButton(this);

            } else {
                Toast.makeText(this,
                        "Bạn đã từ chối quyền vẽ trên màn hình.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showFloatingButton(Context context) {

        EasyFloat.with(context)
                .setTag(FLOAT_TAG)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setLayout(R.layout.float_layout_button)
                .setLocation(-1, -1)
                .setDragEnable(true)
                .registerCallbacks(new OnFloatCallbacks() {

                    @Override
                    public void createdResult(boolean isCreated, String msg, View view) {
                        if (isCreated) {

                            View btn = view.findViewById(R.id.float_button_id);

                            btn.setOnClickListener(v -> {
                                MyAssistiveTouchService.performSystemAction(
                                        context,
                                        MyAssistiveTouchService.ACTION_HOME
                                );

                                Toast.makeText(context,
                                        "Đã gửi lệnh HOME",
                                        Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void show(View view) {}

                    @Override
                    public void hide(View view) {}

                    @Override
                    public void dismiss() {}

                    @Override
                    public void touchEvent(View view, MotionEvent event) {}

                    @Override
                    public void drag(View view, MotionEvent event) {}

                    @Override
                    public void dragEnd(View view) {}
                })
                .show();
    }

}

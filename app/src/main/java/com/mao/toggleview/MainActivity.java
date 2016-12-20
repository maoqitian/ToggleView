package com.mao.toggleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mao.toggleview.view.MyToggleView;

/**
 * 完全自定义的开关控件
 */
public class MainActivity extends AppCompatActivity {

    private MyToggleView mytoggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mytoggle= (MyToggleView) findViewById(R.id.mytoggle);

        /*//设置控件背景
        mytoggle.setSwitchBackgroundResource(R.mipmap.switch_background);
        //设置滑块按钮的背景
        mytoggle.setSlideButtonBackground(R.mipmap.slide_button);
        //设置开关的状态
        mytoggle.setSwitchState(true);*/

        mytoggle.setOnOnSwitchStateUpdateListener(new MyToggleView.OnSwitchStateUpdateListener() {
            @Override
           public void onStateUpdate(boolean state) {
                if(state){
                    Toast.makeText(getApplicationContext(),"开关打开了",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"开关关闭了",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

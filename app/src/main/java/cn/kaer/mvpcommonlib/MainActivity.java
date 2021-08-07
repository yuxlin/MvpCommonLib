package cn.kaer.mvpcommonlib;

import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        String url = "ftp://192.168.3.35/test";
        url = AppConfig.SPEED_TEST_ADDRESS;
        SpeedTestManager.get().startSpeedTestActivity(this,url , "123", "威海", 10, 10, "");
        finish();*/


    }
}
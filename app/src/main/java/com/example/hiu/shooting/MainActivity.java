package com.example.hiu.shooting;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
    /* Activityが始めて生成された時に呼び出される */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(new MySurfaceView(this)); //ほとんどの処理はMySurfaceViewまかせ
    }
}

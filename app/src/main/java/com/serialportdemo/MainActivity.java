package com.serialportdemo;
/**
 *Android NDK 开发（五）AndroidStudio 2.2 NDK的开发环境搭建
 * http://blog.csdn.net/u011974987/article/details/52888864
 *
 *
 * 一天掌握Android JNI本地编程 快速入门
 * https://www.cnblogs.com/rocomp/p/4892866.html
 * */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
       // System.loadLibrary("serialport-test");
       // System.loadLibrary("passdata");
        System.loadLibrary("stest");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        String msg = "==abc";
        //tv.setText(serilaTest()+"\r\nsayHelloInC   "+ sayHelloInC(msg,msg.length()));//+"\r\n js:"+serilaJs()
        String msg1="jiangliqiang_nihao";
        stringFromJNI();
        tv.setText(getStringFromC(msg)+"\r\nsayHelloInC   "+ decode(msg,msg.length())+"\r\n  addstr   "+encode(msg1,msg1.length()));
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native String decode(String msg ,int length);
    public native String encode(String msg ,int length);
    public native String getStringFromC(String msg);
}

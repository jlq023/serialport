package com.serialportdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiang on 2017/12/28.
 */

public class SerialportTestAct extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.act_serialport);
        log= (TextView) findViewById(R.id.log);
        sendMsg = (Button) findViewById(R.id.sendMsg);
        sendMsg.setOnClickListener(this);
        serMsg = (EditText) findViewById(R.id.serMsg);
        initSerialPort();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendMsg:
                String msg = serMsg.getText().toString()+"\r\n";
                if(msg!=null&&!msg.equals("")){
                    byte [] buff = msg.getBytes();
                    try {
                        mOutputStream.write(buff,0,buff.length);
                        Logger.d(TAG+"msg 输出完成");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logger.e(TAG+e.getMessage());
                    }
                }
        }
    }

    private void initSerialPort(){
        try {
            serialPort = new SerialPort(SERIALPORT_NO3,BAUDRATE);
            mInputStream = serialPort.getInputStream();
            mOutputStream = serialPort.getOutputStream();
            new Thread(new ReadSerialPortMsgThread()).start();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(TAG+"打开串口失败");
        }
    }

    private class ReadSerialPortMsgThread implements  Runnable{
        @Override
        public void run() {
            int size;
            byte buff[] = new byte[1024];
           final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            while (true){
               try {
                   if(mInputStream==null){
                       return;
                   }
                   size = mInputStream.read(buff);
                   if(size<=0){
                       continue;
                   }
                   final String message = new String(buff,0,size);
                   Logger.d(TAG+"接收到串口回调  "+message);
                   seriapPortMsg.append(message);
                   if(buff[size - 1] == '\n'){
                       log.post(new Runnable() {
                           @Override
                           public void run() {
                               log.setText(sdf.format(new Date())+"接收到串口发送的指令  "+message);
                           }
                       });
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }finally {
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }
    }

    private StringBuffer seriapPortMsg = new StringBuffer();
    private final String TAG="SerialportTestAct:";
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private TextView log;
    private EditText serMsg;
    private Button sendMsg;
    private SerialPort serialPort;
    private final String SERIALPORT_NO3 = "/dev/ttyS3";//串口号3
    private final int BAUDRATE=115200;//波特率
    private Context mContext;
}

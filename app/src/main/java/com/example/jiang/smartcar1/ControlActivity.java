package com.example.jiang.smartcar1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiang.service.DrawImp;
import com.jiang.service.DrawService;

import java.io.IOException;

import static com.example.jiang.smartcar1.ClientActivity.output;

public class ControlActivity extends AppCompatActivity {
    private DrawImp myDraw;
    //定义控件
    public static ImageView imageView;
    public static int currentSpeed;
    public static int currentangle;
    private EditText editSpeed;
    private EditText editangle;
    //显示当前状态的控件
    private TextView currents;
    private TextView currenta;
    private int speedDistance;
    private int angleDistance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        //初始化速度
        currentSpeed=0;
        //初始化速度间隔控件
        editSpeed= (EditText) findViewById(R.id.speed);
        //给速度设置0为初值
//        editSpeed.setText(0);

        //同理进行角度的初始化
        currentangle=1500;
        editangle= (EditText) findViewById(R.id.angle);
        //editangle.setText(1500);
        //初始化当前状态插件
        imageView= (ImageView) findViewById(R.id.image);
        currenta = (TextView) findViewById(R.id.currentA);
        currenta.setText("1500");
        currents = (TextView) findViewById(R.id.currentS);
        currents.setText("0");
        //设置默认初值
        editSpeed.setText("100");
        editangle.setText("100");
        //初始化时进行画图的配置
        DrawInit();
    }

    public void DrawInit(){
        //在control页面开启的时候就开启服务，并且绑定服务,直接绑定自动开启
        Intent intent=new Intent(ControlActivity.this, DrawService.class);
        bindService(intent,new MyServiceConnection(),BIND_AUTO_CREATE);
        //在此页面启动的时候直接就调用画图程序


    }
    public void startdraw(View view ){
        myDraw.drawLine();
    }

    public void speedreset(View v){

        //进行速度复位的响应
        currentSpeed =0;
        sendSpeedMessage(currentSpeed);
        //在这里进行当前速度的改变
        currents.setText(currentSpeed+"");

    }

    //绑定时用到的类
    public class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myDraw= (DrawImp) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myDraw=null;
        }
    }

    public void anglereset(View v){
        //进行角度复位的响应
        currentangle=1500;
        sendAngleMessage(currentangle);
        //在这里进行当前角度的改变
        currenta.setText(currentangle+"");

    }


    //点击速度加的响应
    public void speedup(View v)
    {
        //每次点击，随时得到速度的间隔
        speedDistance=getSpeedDistance();
        int newspeed=currentSpeed+speedDistance;
        if(newspeed<=5000) {
            //当改变后的速度在范围内的时候进行修改
            currentSpeed = newspeed;
            sendSpeedMessage(currentSpeed);
            //在这里进行当前速度的改变
            currents.setText(currentSpeed+"");
        }
    }

    //点击速度减的响应
    public void speeddown(View v){
        //每次点击，随时得到速度的间隔
        speedDistance=getSpeedDistance();
        int newspeed=currentSpeed-speedDistance;
        if(newspeed>=0) {
            currentSpeed =newspeed;
            sendSpeedMessage(currentSpeed);
            //在这里进行当前速度的改变
            currents.setText(currentSpeed+"");
        }
        else{
            currentSpeed =0;
            sendSpeedMessage(currentSpeed);
            //在这里进行当前速度的改变
            currents.setText(currentSpeed+"");

        }

    }

    //点击角度加的响应
    public void angleup(View v)
    {
        //每次点击，随时得到速度的间隔
        angleDistance=getAngleDistance();
        int newangle=currentangle+angleDistance;
        if(newangle<=1700) {
            //当改变后的速度在范围内的时候进行修改
            currentangle = newangle;
            sendAngleMessage(currentangle);
            //在这里进行当前角度的改变
            currenta.setText(currentangle+"");
        }
    }
    //点击角度减的响应
    public void angledown(View v)
    {
        //每次点击，随时得到速度的间隔
        angleDistance=getAngleDistance();
        int newangle=currentangle-angleDistance;
        if(newangle>=1300) {
            //当改变后的速度在范围内的时候进行修改
            currentangle = newangle;
            sendAngleMessage(currentangle);
            //在这里进行当前角度的改变
            currenta.setText(currentangle+"");
        }
    }




    //获取速度间隔值
    public int getSpeedDistance(){
        return  Integer.parseInt(editSpeed.getText().toString());

    }
    //获取角度间隔值
    public int getAngleDistance(){

        return  Integer.parseInt(editangle.getText().toString());

    }
/*
    //发送速度信息
    private void sendSpeedMessage(int temp) {
        //将当前速度改变为16字节
//        String sixteennumber=Integer.toHexString(temp);
        String sixteennumber=intToHexString(temp,2);
        output.write("5"+sixteennumber+"YES");
    }
    */
    //修改后的按照字节传输的发送速度
    private void sendSpeedMessage(int temp){
        String sixteennumber=intToHexString(temp,2);
        //要传输的字符串数组
        String newString="35"+sixteennumber+"594553";
        //直接写入
        try {
            output.write(HexString2Bytes(newString));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//16机制转字节
    private byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 | _b1);
        return ret;
    }


    //发送角度信息
    private void sendAngleMessage(int temp){
        //等待实现
        String sixteennumber=intToHexString(temp,2);
        //要传输的字符串数组
        String newString="34"+sixteennumber+"594553";
        //直接写入
        try {
            output.write(HexString2Bytes(newString));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //int转换为固定位数的16进制数
    private static String intToHexString(int a,int len){
        len<<=1;
        String hexString = Integer.toHexString(a);
        int b = len -hexString.length();
        if(b>0){
            for(int i=0;i<b;i++)  {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }



}

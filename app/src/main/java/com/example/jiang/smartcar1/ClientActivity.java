package com.example.jiang.smartcar1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class ClientActivity extends Activity{

    private Button btnConnect;
    private EditText edit_ip;
    private EditText edit_port;

//    private Button btnSend;

//    private EditText editSend;

    private Socket socket;

    public static OutputStream output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

//      Toast.makeText(ClientActivity.this, this.getIntent().getStringExtra("message"), Toast.LENGTH_LONG).show();

        initView();

        btnConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //先进行端口连接的部分
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        firstinitClientSocket();
                    }
                }).start();
            //此处可以加一个延时，以使得连接成功
                if(socket!=null) {
                    //在此处实现跳转到下一个控制页面
                    Intent intent = new Intent();
                    intent.setClass(ClientActivity.this, ControlActivity.class);
                    startActivity(intent);
                }else{
                      Toast.makeText(ClientActivity.this, "服务器连接不成功", Toast.LENGTH_LONG).show();
                }
            }

        });

    }


    private void initView() {
        btnConnect = (Button) findViewById(R.id.btnConnect);
         edit_ip = (EditText) ClientActivity.this.findViewById(R.id.edit_hostIp);
         edit_port = (EditText) ClientActivity.this.findViewById(R.id.edit_hostPort);
        //给端口号和ip设置处置
        edit_ip.setText("192.168.4.1");
        edit_port.setText("8888");
//        btnSend = (Button) findViewById(R.id.btnSend);
//        editSend = (EditText) findViewById(R.id.editSend);
//
//        btnSend.setEnabled(false);
//        editSend.setEnabled(false);
    }

//    private void sendMessage(String str) {
//        output.println(str);
//    }

    public void closeSocket() {
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            //handleException(e, "close Exception: ");
            e.printStackTrace();
        }
    }

    public void firstinitClientSocket(){
//        Message message=new Message();
        try {
            System.out.println("开始初始化");
            //new
//            EditText edit_ip = (EditText) ClientActivity.this.findViewById(R.id.edit_hostIp);
//            EditText edit_port = (EditText) ClientActivity.this.findViewById(R.id.edit_hostPort);
//            //给端口号和ip设置处置
//            edit_ip.setText("192.168.4.1");
//            edit_port.setText("8888");
            String ip = edit_ip.getText().toString().trim();
            String port = edit_port.getText().toString().trim();


            //System.out.println("ip是+"+ip);
            //System.out.println("端口是+"+port);
            socket = new Socket(ip,Integer.parseInt(port));
            //发送信息给UI

//            message.obj=true;
//            handler.sendMessage(message);
//            isSucceed=true;
            //get output Stream
            output = socket.getOutputStream();
//          output = new PrintStream(socket.getOutputStream(),true);



        } catch (UnknownHostException e) {
           // Toast.makeText(ClientActivity.this, "请检查端口号是否为服务器IP", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
          //  Toast.makeText(ClientActivity.this, "服务器未开启", Toast.LENGTH_LONG).show();
           // e.printStackTrace();
        }

    }



}
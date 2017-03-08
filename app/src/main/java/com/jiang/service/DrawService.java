package com.jiang.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.jiang.smartcar1.ControlActivity;
//导入接受流
import static com.example.jiang.smartcar1.ClientActivity.intput;



//整个画图，这里实现
public class DrawService extends Service {

    //实现10个大小的byte数组转化10个大小的int数组
    public int[] bytetoint(byte[] bytes){

        int[] myint=new int[10];
        for(int i=0;i<10;i++){
            myint[i]=bytes[i]&0xff;
        }
        return myint;

    }

    //定义handler
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //处理信息
            if(msg.what==1){
                Bitmap bitmap= (Bitmap) msg.obj;
                ControlActivity.imageView.setImageBitmap(bitmap);
            }
        }
    };
    public DrawService() {
    }
    //我们需要在service中定义一个内线
    private class MyAgent extends Binder implements DrawImp{


        @Override
        public void drawLine() {
            //在此处实现画直线的逻辑
            drawLineInService();

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new MyAgent();
    }

    public void finallyDraw(int[] xpoints){

        //这里的图片大小不重要，image已经控制大小了
        Bitmap baseBitmap = Bitmap.createBitmap(310, 150, Bitmap.Config.ARGB_8888);
        // 创建一张画布
        Canvas canvas = new Canvas(baseBitmap);
        // 画布背景为灰色
        canvas.drawColor(Color.BLACK);
        // 创建画笔
        Paint paint = new Paint();
        // 画笔颜色为红色
        paint.setColor(Color.WHITE);
        // 宽度5个像素
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        //直接开画就行了
        int[] ypoints=new int[]{0,30,60,90,119,0,30,60,90,119};
        //画第一条线
        Path path1 = new Path();//
        path1.moveTo(xpoints[0], ypoints[0]);//绘画基点
        for(int i=1;i<5;i++)
        path1.lineTo(xpoints[i], ypoints[i]);
       //画第二条线
        Path path2 = new Path();//
        path2.moveTo(xpoints[5], ypoints[5]);//绘画基点
        for(int i=6;i<10;i++)
            path2.lineTo(xpoints[i], ypoints[i]);
        //画
        canvas.drawPath(path1, paint);
        canvas.drawPath(path2, paint);



//        canvas.drawLine(0,0,55,55,paint);
        // 先将灰色背景画上
       // canvas.drawBitmap(baseBitmap, new Matrix(), paint);
        //在子线程里修改UI，行不通,使用handler
        Message message = new Message();
        message.obj = baseBitmap;
        message.what = 1;
        handler.sendMessage(message);
    }

    public void drawLineInService(){
        //在service中真正执行接受socket画图的逻辑
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println("进入到了while循环");
                    //进行接受字节的统计
                    int len=0;
                    byte[] buff=new byte[10];
                    try {
                         len = intput.read(buff,0,10);
                        System.out.println("从input中接收到的byte数组大小是:"+len);
                        if(len==10){
                            //说明接收到了十个数据
                            //可以绘图了
                            //String s = buff.toString();
//                            System.out.println("接收到的数据是:"+Byte.toString(buff[0]));
                            //然后将这十个数据转换为大小为10的int数组
                            int[] tenNumber=bytetoint(buff);
//                            for(int j=0;j<10;j++) System.out.println(tenNumber[j]);
                            //然后组织十个点开始画图
                            finallyDraw(tenNumber);

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    /*
                        //接收到数据的时候才进行图片的重绘


                            //在循环线程里，时刻接受socket内容
                            //画图在线程里实现
                            //这里的图片大小不重要，image已经控制大小了
                            Bitmap baseBitmap = Bitmap.createBitmap(550, 201, Bitmap.Config.ARGB_8888);
                            // 创建一张画布
                            Canvas canvas = new Canvas(baseBitmap);
                            // 画布背景为灰色
                            canvas.drawColor(Color.BLACK);
                            // 创建画笔
                            Paint paint = new Paint();
                            // 画笔颜色为红色
                            paint.setColor(Color.RED);
                            // 宽度5个像素
                            paint.setStrokeWidth(5);
                            // 先将灰色背景画上
                            canvas.drawBitmap(baseBitmap, new Matrix(), paint);
                            //在子线程里修改UI，行不通,使用handler
                            Message message = new Message();
                            message.obj = baseBitmap;
                            message.what = 1;
                            handler.sendMessage(message);
                    */

                    try {
                        //3秒接受一次
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }
}

package com.jiang.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Binder;
import android.os.IBinder;
import com.example.jiang.smartcar1.ControlActivity;




//整个画图，这里实现
public class DrawService extends Service {
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



    public void drawLineInService(){
        //在service中真正执行接受socket画图的逻辑
        //画图在线程里实现
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
        ControlActivity.imageView.setImageBitmap(baseBitmap);

    }
}

package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class heroView extends View {
    static int pos;
    public heroView(Context context) {
        super(context);
        pos=0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect(pos,0,100+pos,100,paint);
    }

    // 좌표 잡는 부분
    void leftmove() {
        if(pos>0) {
            pos-=10;
        }
        invalidate();
    }

    void rightmove() {
        if(pos<1000) {
            pos+=10;
        }
        invalidate();
    }
}


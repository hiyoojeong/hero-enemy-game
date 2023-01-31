package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Vector;

public class enemyView extends View {
    int pos;
    static int num, maxnum, millis;
    static Vector<myRect> enemys;

    public enemyView(Context context) {
        super(context);
        pos = 0;
        num = 0;
        maxnum = 10;
        millis = 100;
        enemys = new Vector<myRect>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor((Color.RED));
        for (int i = 0; i < enemys.size(); i++) {
            myRect r = enemys.get(i);
            canvas.drawRect(r.left, r.top, r.right, r.bottom, paint);
        }
    }

    void makeenemy() {
        // enemy를 하나하나 만들어내는 스레드
        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (num < maxnum) {
                        int left = (int) (Math.random() * 900);
                        myRect r = new myRect(left, 0, 100 + left, 100, num);
                        num++;
                        // r.start()를 하면 run()이 계속 수행된다.
                        r.start();
                        enemys.add(r);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        t1.start();

        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    invalidate();
                }
            }
        };
        t.start();
    }
}

// 만들어진 enemy를 내려오게 하는(좌표값을 바꾸는) 스레드
class myRect extends Thread {
    int left, top, right, bottom, pos, idx;
    static int score;

    myRect(int left, int top, int right, int bottom, int num) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.pos = 10;
        this.idx = num;
    }

    @Override
    public void run() {
        while (true) {
            movedown();
            try {
                sleep(enemyView.millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 적이 끝까지 내려왔을 때, 피하기 성공
            // Runnable 객체를 post 요청으로 전달하여, 계속해서 점수를 변경
            if (bottom > 1380) {
                MainActivity.scoretxt.post(new Runnable() {
                    @Override
                    public void run() {
                        // 적을 피했는지 판정
                        if ((left < heroView.pos && right > heroView.pos) || (left < (heroView.pos + 100) && right > (heroView.pos + 100))) {
                            score = 0;
                        } else {
                            score += 10;
                        }

                        // score 100 단위로 최대 적의 수를 1 증가
                        if (score != 0 && score % 100 == 0) {
                            enemyView.maxnum++;
                        }

                        // score 500 단위로 스피드 증가
                        if (score != 0 && score % 500 == 0 && enemyView.millis > 20) {
                            enemyView.millis -= 20;
                        }

                        MainActivity.scoretxt.setText("score: " + score);
                    }
                });
                enemyView.enemys.remove(0);
                enemyView.num--;
                break;
            }
        }
    }

    void movedown() {
        top += pos;
        bottom += pos;
    }
}

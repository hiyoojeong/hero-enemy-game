package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    static TextView scoretxt;
    TextView starttxt;
    Button leftbtn, rightbtn;
    LinearLayout enemyview, heroview;
    heroView hero;
    enemyView enemy;
    int dir = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 연결작업
        scoretxt = (TextView) findViewById(R.id.scoretxt);
        starttxt = (TextView) findViewById(R.id.starttxt);
        leftbtn = (Button) findViewById(R.id.leftbtn);
        rightbtn = (Button) findViewById(R.id.rightbtn);
        enemyview = (LinearLayout) findViewById(R.id.enemyview);
        heroview = (LinearLayout) findViewById(R.id.heroview);

        // 메인 클래스에는 스레드가 하나 밖에 없다.
        heromove();
        starttxt.setOnClickListener(e -> {
            init();
        });
    }

    // 초기화
    void init() {
        starttxt.setVisibility(View.INVISIBLE);
        hero = new heroView(getApplicationContext());
        enemy = new enemyView(getApplicationContext());
        enemy.makeenemy();
        heroview.addView(hero);
        enemyview.addView(enemy);
        rightbtn.setVisibility(View.VISIBLE);
        leftbtn.setVisibility(View.VISIBLE);
    }

    void heromove() {
        // Runnable: 스레드를 관리하는 인터페이스
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(dir==1) {
                    hero.leftmove();
                } else {
                    hero.rightmove();
                }
                // post 재요청: 메인스레드에게 작업할 것을 재요청
                // 만약에 이렇게 안하고, 스레드를 새로 하나 만들어버리면,
                // 메인스레드는 잠깐 왔다 가는게 아니라 계속 와있게 되니까 메인스레드가 작업하던 UI 작업에 문제가 생긴다.
                handler.postDelayed(this, 10);
            }
        };

        rightbtn.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                // post 요청: 메인스레드에게 작업할 것을 요청, 메인스레드는 잠깐 와서 도와준다.
                handler.post(runnable);
                return false;
            }
        });

        rightbtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    dir = 0;
                    hero.rightmove();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    // 스레드 죽이는 작업 (버튼을 떼었을 때)
                    handler.removeCallbacks(runnable);
                }
                return false;
            }
        });

        leftbtn.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                handler.post(runnable);
                return false;
            }
        });

        leftbtn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    dir = 1;
                    hero.leftmove();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacks(runnable);
                }
                return false;
            }
        });

    }






}
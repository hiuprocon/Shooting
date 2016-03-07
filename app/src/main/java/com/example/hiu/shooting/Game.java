package com.example.hiu.shooting;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * ゲーム全体を司るクラス。ゲーム内の多数のオブジェクトを管理する。
 * これに加えて、GUIとは独立したゲームフィールドを定義しており、
 * 画面の大きさや解像度などと無関係にゲーム内のオブジェクトの
 * 座標などを指定できるようにしている。このゲームフィールドは
 * 右がX軸の正の方向で、上がY軸の正の方向となっており中心が
 * 原点となるように定義される。
 */
public class Game {
    public final int FPS = 60;//1秒間の描画回数
    public final float WIDTH=16; //ゲームフィールドの幅
    public final float HEIGHT=20f; //ゲームフィールドの高さ
    public double time = 0.0;//ゲーム内の仮想の時間(秒)
    int pWidth=-1; //MySurfaceViewの幅(pixel)
    int pHeight=-1; //MySurfaceViewの高さ(pixel)
    public float dpu=1f; //ゲームフィールドでの単位長さあたりのピクセル数

    boolean dirTouched=false;//方向入力がされてるかどうか
    float dirX,dirY;//方向
    boolean bombTouched=false;//ボムが押されてるかどうか

    Resources res; //画像を生成する時などに必要な物
    Paint paint = new Paint();//描画する時に必要
    Context con; //WAVの読み込みなどに必要

    private ArrayList<GObject> objects = new ArrayList<GObject>();
    private ArrayList<GObject> objTmp = new ArrayList<GObject>();
    private SoundPool sounds;
    public int S1,S2,S3,S4;

    Player player; //プレイヤー
    Boss boss; //ボス

    public Game(MySurfaceView msv,Context con) {
        this.con = con;
        this.res = con.getResources();
        sounds = new SoundPool(3,AudioManager.STREAM_MUSIC,0);//3は同時再生数
        S1 = sounds.load(con,R.raw.pyoro07,1);
        S2 = sounds.load(con,R.raw.pyoro31,1);
        S3 = sounds.load(con,R.raw.don01,1);
        S4 = sounds.load(con,R.raw.don03,1);
        player = new Player(this);
        player.setLoc(0,-5);
        add(player);
        boss = new Boss(this);
        boss.setLoc(0,5);
        add(boss);
    }

    /* Surfaceのサイズなどが変更された時に呼び出される */
    public void initOrChangeSize(int w,int h) {
        pWidth = w;
        pHeight = h;
        dpu = Math.max(pWidth/WIDTH,pHeight/HEIGHT);
        for (GObject o : objects)
            o.initOrChangeSize();
    }

    public void add(GObject o) {
        objects.add(o);
    }
    public void del(GObject o) {
        objects.remove(o);
    }
    public void draw(Canvas c) {
        //各オブジェクトの処理を呼び出す
        objTmp.clear();
        objTmp.addAll(objects);
        for (GObject o : objTmp) {
            o.exec();
        }

        //当り判定
        objTmp.clear();
        objTmp.addAll(objects);
        for (int i=0;i<objTmp.size();i++) {
            for (int j=i+1;j<objTmp.size();j++) {
                GObject o1 = objTmp.get(i);
                GObject o2 = objTmp.get(j);
                float l = (o1.x-o2.x)*(o1.x-o2.x)+(o1.y-o2.y)*(o1.y-o2.y);
                l = (float)Math.sqrt(l);
                if (o1.r+o2.r>l) {
                    o1.collision(o2);
                    o2.collision(o1);
                }
            }
        }

        //描画
        objTmp.clear();
        objTmp.addAll(objects);
        for (GObject o : objTmp) {
            o.draw(c);
        }
    }
    public void playSound(int id) {
        sounds.play(id, 1f, 1f, 0, 0, 1f);
    }
}

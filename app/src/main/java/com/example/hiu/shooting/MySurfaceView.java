package com.example.hiu.shooting;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.content.Context;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{

    SurfaceHolder holder;//SurfaceViewの描画に必要なオジェクト
    boolean isAttached;//MySurfaceViewがActivityに登録されたかどうか
    Game game; //シューティングゲームプログラム本体

    //以下タッチパネル関係の情報
    int dirCenterX; //方向ポインタの中心X
    int dirCenterY; //方向ポインタの中心Y
    int dirRadius; //方向ポインタの半径
    int bombCenterX; //ボムボタンの中心X
    int bombCenterY; //ボムボタンの中心Y
    int bombRadius; //ボムボタンの半径
    int dirPointerID;
    int bombPointerID;

    /* コンストラクタ(初期化) */
    public MySurfaceView(Context context) {
        super(context);
        game = new Game(this,context);
        this.getHolder().addCallback(this);
        Log.d("debug","MySurfaceViewが作られた！");//デバッグ情報の書き出し方
    }

    /* このSurfaceが生成された時に呼ばれる */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        this.holder = holder;
        isAttached = true;
        new Thread(this).start();
    }

    /* Surfaceのサイズなどが変更された時に呼び出される */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        game.initOrChangeSize(width,height);
        int u = (int)game.dpu;
        dirCenterX = 3*u; dirCenterY = height - 3*u; dirRadius = (int)(1*u);
        bombCenterX = width-3*u; bombCenterY = height - 3*u; bombRadius = (int)(1*u);
    }

    /* このSurfaceが破棄される時に呼ばれる */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        isAttached = false;
    }

    /* 画面がタッチされた時の処理 */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        int index = e.getActionIndex();
        int pointerID = e.getPointerId(index);
        int x = (int)e.getX(index);
        int y = (int)e.getY(index);
        switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (Math.abs(x-dirCenterX)<dirRadius && Math.abs(y-dirCenterY)<50) {
                    dirPointerID = pointerID;
                    game.dirTouched = true;
                } else if (Math.abs(x-bombCenterX)<dirRadius && Math.abs(y-bombCenterY)<50) {
                    bombPointerID = pointerID;
                    game.bombTouched = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (pointerID==dirPointerID) {
                    game.dirTouched = false;
                    dirPointerID = -1;
                } else if (pointerID==bombPointerID) {
                    game.bombTouched = false;
                    bombPointerID = -1;
                } else {
                    game.dirTouched = game.bombTouched = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointerID==dirPointerID) {
                    game.dirX = (x - dirCenterX)/game.dpu;
                    game.dirY = (dirCenterY - y)/game.dpu;
                }
                break;
            case MotionEvent.ACTION_OUTSIDE: break;
        }
        if(x>10000000.0)performClick();//ワーニング対策(気にしなくてよし)
        return true;
    }

    @Override //ワーニング対策(気にしなくてよし)
    public boolean performClick() {return super.performClick();}

    /* メインの処理。1秒にFPS回(60)回くりかえす */
    @Override
    public void run() {
        while (isAttached) {
            try{Thread.sleep(1000/game.FPS);}catch(Exception e){;}
            game.time += 1.0/game.FPS;
            Canvas canvas = holder.lockCanvas();//描画開始時の約束
            if (canvas==null) continue;//これがないと正常修了できない

            //以下描画
            canvas.drawColor(0,PorterDuff.Mode.CLEAR);
            game.paint.setColor(Color.WHITE);
            canvas.drawCircle(dirCenterX,dirCenterY,dirRadius,game.paint);//方向ポインタの○
            canvas.drawCircle(bombCenterX,bombCenterY,bombRadius,game.paint);//ボムボタンの○
            game.draw(canvas);
            canvas.drawText("BOSS:"+game.boss.life, 10, 10, game.paint);
            holder.unlockCanvasAndPost(canvas);//描画終了時の約束
        }
    }
}

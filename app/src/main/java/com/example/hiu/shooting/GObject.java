package com.example.hiu.shooting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

/**
 * ゲームに出てくる全ての物体のスーパークラス。
 */
public abstract class GObject {
    protected Game game;
    private final Bitmap bmpOrg;//オリジナル画像
    private Bitmap bmp;//リサイズ画像
    private float pw,ph;//物体の幅と高さ(pixel,リサイズ後の画像で)
    private float w,h;//物体の幅と高さ(ゲーム座標で)
    protected float x,y;//物体のゲーム座標における位置
    float r;//当り判定用の半径

    public GObject(Game game,int resID,float w,float h,float r) {
        this.game = game;
        this.w = w;
        this.h = h;
        this.r = r;
        bmpOrg = BitmapFactory.decodeResource(game.res,resID);
        Log.d("debug","GAHA:new GObject:"+this.getClass().getName());
        if (game.pWidth!=-1)
            initOrChangeSize();
    }

    public void initOrChangeSize() {
        bmp = Bitmap.createScaledBitmap(bmpOrg,(int)(w*game.dpu),(int)(h*game.dpu),false);
        pw = bmp.getWidth();
        ph = bmp.getHeight();
    }

    public final void draw(Canvas c) {
        float fx =  x*game.dpu+game.pWidth/2 -pw/2;
        float fy = -y*game.dpu+game.pHeight/2 -ph/2;
        c.drawBitmap(bmp,fx,fy, game.paint);
    }

    public abstract void exec();

    public void delMyself() {
        game.del(this);
    }
    public void add(GObject o) {
        game.add(o);
    }
    public void del(GObject o) {
        game.del(o);
    }
    public void setLoc(float x,float y) {
        this.x = x; this.y = y;
    }
    public void addLoc(float x,float y) {
        this.x += x; this.y += y;
    }
    public void collision(GObject o) {
        ;
    }
}

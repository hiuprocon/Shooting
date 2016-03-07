package com.example.hiu.shooting;

/**
 * ラスボス。
 * もともと100x150ピクセル。
 */
public class Boss extends GObject {
    int life=100;
    private int bulletCount=0;
    public Boss(Game game) {
        super(game,R.mipmap.enemy,2,3,1);
    }

    @Override
    public void exec() {
        x = (float)(5*Math.sin(game.time));
        y = (float)(2*Math.sin(2*game.time)+5);
        bulletCount++;
        if (bulletCount%100==0) {
            Bullet2 b = new Bullet2(game);
            b.setLoc(x,y);
            add(b);
        }
    }

    @Override
    public void collision(GObject o) {
        if (o instanceof Bullet) {
            game.playSound(game.S3);
            life--;
        } else if (o instanceof Bomb) {
            game.playSound(game.S4);
            life=life-10;
        }
    }
}

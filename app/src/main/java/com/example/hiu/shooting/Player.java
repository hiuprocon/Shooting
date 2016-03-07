package com.example.hiu.shooting;

/**
 * プレイヤー。
 * もともと60x60ピクセル。
 */
public class Player extends GObject {
    int bulletCount=0;
    boolean oldBombTouched = false;
    public Player(Game game) {
        super(game,R.mipmap.space_fighter,1,1,0.5f);
    }

    @Override
    public void exec() {
        //戦闘機の座標計算
        if (game.dirTouched==true) {
            float dx = 5*game.dirX/game.FPS;
            float dy = 5*game.dirY/game.FPS;
            dx = dx>0.1f?0.1f:(dx<-0.1f?-0.1f:dx);//スピード制限(x)
            dy = dy>0.1f?0.1f:(dy<-0.1f?-0.1f:dy);//スピード制限(y)
            x += dx; y += dy;
            if (x<-game.WIDTH/2) x = -game.WIDTH/2;//はみ出し防止
            if (x> game.WIDTH/2) x =  game.WIDTH/2;//はみ出し防止
            if (y<-game.HEIGHT/2) y = -game.HEIGHT/2;//はみ出し防止
            if (y> game.HEIGHT/2) y =  game.HEIGHT/2;//はみ出し防止
        }

        if (bulletCount%10==0) {
            Bullet b = new Bullet(game);
            b.setLoc(x,y);
            add(b);
        }
        if (oldBombTouched==false && game.bombTouched==true) {
            Bomb b = new Bomb(game);
            b.setLoc(x,y);
            add(b);
        }
        bulletCount++;
        oldBombTouched = game.bombTouched;
    }
}

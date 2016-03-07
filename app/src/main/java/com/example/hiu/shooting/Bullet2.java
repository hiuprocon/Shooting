package com.example.hiu.shooting;

public class Bullet2 extends GObject {
    public Bullet2(Game game) {
        super(game,R.mipmap.bullet2,0.5f,0.5f,0.25f);
    }

    @Override
    public void exec() {
        y -= 0.1;
        if (y<-game.HEIGHT/2) delMyself();
    }
}

package com.example.hiu.shooting;

public class Bullet extends GObject {
    public Bullet(Game game) {
        super(game,R.mipmap.bullet1,0.5f,0.5f,0.25f);
        game.playSound(game.S1);
    }

    @Override
    public void exec() {
        addLoc(0,0.1f);
        if (y>game.HEIGHT/2) delMyself();
    }
}

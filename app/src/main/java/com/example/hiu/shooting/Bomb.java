package com.example.hiu.shooting;

public class Bomb extends GObject {
    public Bomb(Game game) {
        super(game,R.mipmap.bomb1,1.0f,1.0f,0.5f);
        game.playSound(game.S2);
    }

    @Override
    public void exec() {
        addLoc(0,0.05f);
        if (y>game.HEIGHT/2) delMyself();
    }
}

package com.company;

public class ShipBody extends Entity {
    //The ship exists in this game
    private Game game;

    // now we must create a ship entity

    public ShipBody(Game game, String ref, int x, int y) {
        super(ref, x, y);

        this.game = game;
    }
    // If the ship hasnt moved in a while then we must make it do so

    public void move(long deltaL) {
        //if we reach max of left movement then stop

        if ((horSpee < 0) && (x < 10)) {
            return;
        }

        if ((horSpee > 0) && (x > 750)) {
            return;
        }
        super.move(deltaL);
    }

    public void collidedWith(Entity opposition) {

        //if player hit an alien then the player is dead and we must display the gg screen

        if (opposition instanceof AliBody) {
            game.deathGG();
        }
    }
}
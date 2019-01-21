package com.company;

public class AliBody extends Entity {

    // the speed at which the alien will move horizontally
    private double oppSpee = 60;
    // where is the entity
    private Game game;

    // now we must create the alien entity

    public AliBody(Game game, String ref, int x , int y ) {

        super(ref, x, y);

        this.game = game;
        horSpee = -oppSpee;
    }

    // this alien should be moved based on time, its not manual

    public void move (long deltaL) {
        // now we must code for screen bounds

        if ((horSpee < 0) && (x < 10)) {
            game.updateLogic();

        }

        if ((horSpee > 0) && (x > 750)) {
            game.updateLogic();

        }

        // and now we may continue
        super.move(deltaL);
    }

    public void doLogic() {
        horSpee = -horSpee;
        y += 10;

        // if the alien gets to the bottom of the screen then the player is gone

        if (y > 570) {
            game.deathGG();
        }
    }
    // now we must note that a collision has been made
    public void collidedWith (Entity other) {
        //handled in other class
    }
}

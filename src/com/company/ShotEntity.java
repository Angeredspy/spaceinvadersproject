package com.company;

public class ShotEntity extends Entity {
    // Shot speed
    private double shotSpeed = -1000;

    private Game game;

    private boolean hitConfirm = false;

    public ShotEntity(Game game, String sprite, int x, int y) {
        super(sprite, x, y);

        this.game = game;

        verSpee = shotSpeed;
    }

    public void move(long delta) {

        super.move(delta);

        //if the shot goes off the screen, let it go

        if (y < -100) {
            game.removeEntity(this);
        }
    }

        public void collidedWith (Entity o) {

            if (hitConfirm) {
                return;
            }

            if (o instanceof AliBody) {

                game.removeEntity(this);
                game.removeEntity(o);

                game.notifyMobSmoked();
                hitConfirm = true;
            }
        }
    }



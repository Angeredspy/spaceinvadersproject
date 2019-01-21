package com.company;

import java.awt.Graphics;
import java.awt.Rectangle;

// The entity class is representitive of any element that appears within the game
// Doubles are used for positions

public abstract class Entity {
    // X location of the entity/image
    protected double x;
    // y location of the entity / image
    protected double y;
    // Image sprite
    protected Sprite sprite;
    // The horizontal speed of the entity in pixels per second (PPS)
    protected double horSpee;
    // The vertical speed of the entity in PPS
    protected double verSpee;
    // Here we will be creating the hitbox for the user entity
    protected Rectangle player = new Rectangle();
    // Now were going to create the rectangle hitbox for the enemy alien
    private Rectangle enemy = new Rectangle();

    // Now the entity needs to be constructed based on the actual image and location
    public Entity(String ref, int x, int y) {

        this.sprite = SpriteBox.get().getSprite(ref);
        // entitys initial x location
        this.x = x;
        // entitys initial y location
        this.y = y;
    }
    // As time passes, the entity should move itself
    public void move(long deltaL) {
        x += (deltaL * horSpee) / 1000;
        y += (deltaL * verSpee) / 1000;
    }
    // Now we have to set the horizontal speed of the entity

    public void setHorizontalMovement (double horSpee) {
        this.horSpee = horSpee;
    }

    public void setVerticalMovement (double verSpee) {
        this.verSpee = verSpee;
    }
    // now we need to get the speeds that we previously set
    public double getHorizontalMovement () {
       return horSpee;
    }
    public double getVerticalMovement () {
       return verSpee;
    }
    // Next we will draw the entity to the graphics
    public void draw (Graphics g) {
        sprite.draw(g,(int) x, (int) y);
    }
    // now we need to do the logic associated with the entity
    public void doLogic() {
    }
    // get the X location of the entity
    public int getX() {
        return (int) x;
    }
    // now we need to get the Y location of the entity
    public int getY() {
        return (int) y;
    }
    // How do we know whether or not the hitboxes have hit? We must check
    public boolean hittingHitboxes (Entity o) {
        player.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        enemy.setBounds((int) o.x, (int) o.y, o.sprite.getWidth(), o.sprite.getHeight());
        return player.intersects(enemy);
    }
    // This goes to show that two hitboxes have collided
    public abstract void collidedWith(Entity o);



}

package com.company;

import java.awt.Graphics;
import java.awt.Image;

public class Sprite {

    // image must be drawn
    private Image image;

    // now we must create a new sprite based on the image

    public Sprite(Image image) {
        this.image = image;
    }

    // Now we need dimensions, so we must get the width of the drawn sprite
    // the width will be returned as an int
    public int getWidth() {
        return image.getWidth(null);
    }
    // Next we get height and return height
    public int getHeight() {
        return image.getHeight(null);
    }
    // Next we will draw the sprite to the graphics
    public void draw (Graphics g, int x, int y) {
        g.drawImage(image,x,y,null);
    }
}

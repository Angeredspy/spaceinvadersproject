package com.company;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

// This class will be the games main resource manager
public class SpriteBox {
    // We will create one instance of the class
    private static SpriteBox single = new SpriteBox();
    // next we will grab the instance created and return it
    public static SpriteBox get() {
        return single;
    }
    // The cashed hashed map
    private HashMap sprites = new HashMap ();

    // Next we actually have to pull a sprite that has been stored from the store
    public Sprite getSprite (String r) {

        // The sprite may already be within the cache
        if (sprites.get(r) != null) {
            return (Sprite) sprites.get(r);
        }

        // however it may also not be, if this is the case then

        BufferedImage sourceImage = null;
        // This try block will catch any errors that we encounter and will produce error messages
        try {
            URL url = this.getClass().getClassLoader().getResource(r);
            if (url == null) {
                fail("cant find reference: " + r);
            }
            // Next in order to read the image, we use ImageIO

            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            fail("Failed to load: " + r);
        }

        // Next we create an image of the proper size to store the sprite in
        GraphicsConfiguration graphConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = graphConfig.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

        // Now we must draw the image to the accelerated image
        image.getGraphics().drawImage(sourceImage, 0, 0, null);
        // And now finally we can create a sprite which is then added to cache and returned
        Sprite sprite = new Sprite(image);
        sprites.put(r, sprite);

        return sprite;
    }

    // Now we need to be ready to handle any failures that occur with resource loading
    private void fail(String message) {
        // If an error occurs print the message parameter defined by its argument and exit
        System.err.println(message);
        System.exit(0);
    }

}

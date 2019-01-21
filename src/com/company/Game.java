package com.company;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {

    // Variable Declerations

    private BufferStrategy strat;
    // This is true if the game is currently active
    private boolean activeGame = true;
    // Create and initialize an array of variable size that will be used to store the entities used
    private ArrayList entities = new ArrayList();
    // Taking into account that things must be removed from the game, we need to store these
    private ArrayList removedEnts = new ArrayList();
    // This will be the players main body
    private Entity ship;
    // movement speed
    private double movementSpeed = 350;
    // number of aliens left on the screen
    private int baddiesLeft;
    // The time at which a shot was last fired
    private long lastFire = 0;
    // The interval between the shots (ms)
    private long firingInterval = 200;

    // Message to be displayed
    private String message = "";
    // Boolean thats true if we're waiting for a key to be pressed
    private boolean waitingForKey = true;
    // If the a key is pressed this is true, but normally its default state must be false
    private boolean aKey = false;
    // We will do the same for the right key here (using w a s d format)
    private boolean dKey = false;
    // Using the arrow keys
    private boolean rightKey = false;
    // Using the arrow key format
    private boolean leftKey = false;
    // Now we need to see if a shot has been fired
    private boolean shotFired = false;
    // Look into this one right here
    private boolean logicRequiredForTheLoop = false;

    //Method to actually construct the game
    public Game() {

        // This creates the page that the game will be displayed on
        JFrame page = new JFrame("Welcome to Space Invaders!");
        // Now we need the page to hold the content within
        JPanel panel = (JPanel) page.getContentPane();
        // Setting the size of the page and assigning dimensions
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setLayout(null);

        // JFrame creates the framed page, yet JPanel allows buttons and other things to be
        // Placed within certain bounds within the page

        // Next, we need to determine the space where we can place things (buttons graphics etc)
        setBounds(0, 0, 800, 600);
        panel.add(this);

        // Ignores paint messages from the Operating System
        setIgnoreRepaint(true);

        // Now the page needs to be made visible, since this is not automatically done
        // This will size the frame
        page.pack();
        page.setBackground(Color.black);
        // Prevents resizing of the frame if false (notice that you cant full screen)
        page.setResizable(false);
        // Make the page visible
        page.setVisible(true);

        page.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

        });

        // How do we know when a key has been pressed? we have to add something to listen
        addKeyListener(new KeyInputHandler());

        // Requesting focus now to set the given component to a focused state
        requestFocus();

        // create the buffering strategy which will allow the abstract window toolkit (AWT)
        // to handle graphics

        createBufferStrategy(2);
        strat = getBufferStrategy();

        initEntities();
    }

    // Now were going to create a method that starts a new game
    private void startGame() {
        // If any old entities persist then clear them out and initialize a new set
        entities.clear();
        initEntities();

        //Get rid of old keyboard inputs / settings
        aKey = false;
        dKey = false;
        shotFired = false;
    }

    //now we need to initialize the starting state of the entities

    public void initEntities () {

        // We will create the player body (ship) and place it in the middle of our page

        ship = new ShipBody(this, "sprites/ship.gif", 370, 550);
        entities.add(ship);

        // Now we will create the enemies (aliens) evenly spaced
        baddiesLeft = 0;
        for (int row = 0; row < 5; row++) {
            for (int x = 0; x < 12; x++) {
                Entity alien = new AliBody(this, "sprites/alien.gif", 100 + (x * 50), (50) + row * 30);
                entities.add(alien);
                baddiesLeft++;
            }
        }
    }
    // With all thats been done, we want to notify the game logic to run

    public void updateLogic() {

        logicRequiredForTheLoop = true;
    }

    public void removeEntity (Entity entity) {
        removedEnts.add(entity);
    }
    // acknowledgement of player death
    public void deathGG() {

        message = "You fought well!";
        waitingForKey = true;
    }

    public void winGG() {
        message = "The galaxy fears you!";
        waitingForKey = true;
    }
    public void notifyMobSmoked() {

        baddiesLeft--;

        if (baddiesLeft == 0) {
            winGG();
        }


        // The less aliens there are, the faster they need to go; this can be covered with a for loop

        for (int i = 0; i<entities.size(); i++) {
            Entity entity = (Entity) entities.get(i);

            if (entity instanceof AliBody) {
                // now we speed up these instances by 6

                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.06);
            }
        }
    }

    // Shoot your shot

    public void checkForShoot() {

        if (System.currentTimeMillis() - lastFire < firingInterval) {

            return;
        }

        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this, "sprites/shot.gif", ship.getX() + 10, ship.getY() - 30);
        entities.add(shot);
    }

    // Now we can begin to create the main game loop
    // This runs for as long as a game instance is in play
    // here we move the game entities

    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();

        while (activeGame) {


            // now we will loop until the game is done

            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // now we need to get the graphics context

            Graphics2D g = (Graphics2D) strat.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 800, 600);
            // cycle round asking each to move itself

            if (!waitingForKey) {
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);

                    entity.move(delta);
                }
            }

            // Cycle round drawing the entities that are in the game

            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.draw(g);
            }

            // now we will code for the collisions within the main game

            for (int p = 0; p < entities.size(); p++) {

                for (int s = p + 1; s < entities.size(); s++) {
                    Entity player = (Entity) entities.get(p);
                    Entity enemy = (Entity) entities.get(s);

                    if (player.hittingHitboxes(enemy)) {
                        player.collidedWith(enemy);
                        enemy.collidedWith(player);
                    }
                }
            }
            entities.removeAll(removedEnts);
            removedEnts.clear();

            if (logicRequiredForTheLoop) {
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.doLogic();
                }

                logicRequiredForTheLoop = false;
            }

            if (waitingForKey) {
                g.setColor(Color.white);
                g.drawString(message, (800 - g.getFontMetrics().stringWidth(message)) / 2, 250);
                g.drawString("Ready? Press something!", (800 - g.getFontMetrics().stringWidth("Ready? Press Something!")) / 2, 300);
            }

            // Now that all the drawings have been completed, the graphics can be freed up

            g.dispose();
            strat.show();

            // now we need to detect ship movement,
            // this corresponds to whether or now the W or D keys are pressed

            ship.setHorizontalMovement(0);

            if ((aKey) && (!dKey)) {
                ship.setHorizontalMovement(movementSpeed);
            } else if ((dKey) && (!aKey)) {
                ship.setHorizontalMovement(-movementSpeed);
            } else if ((rightKey) && (!leftKey)) {
                ship.setHorizontalMovement(movementSpeed);
            } else if ((leftKey) && (!rightKey)) {
                ship.setHorizontalMovement(-movementSpeed);
            }

            // if the player presses fire then fire
            if (shotFired) {
                checkForShoot();
            }

            // we need to implement a delay after every shot

            try {
                Thread.sleep(4);
            } catch (Exception e) {}
        }
    }

    // Now we need to create a class that handles keyboard input (such as a and d)

    private class KeyInputHandler extends KeyAdapter {
        // the number of key presses we've had while waiting for a key to be pressed
        private int pressCount = 1;

        //Notify the abstract window toolkit that the key has been pressed.
        public void keyPressed(KeyEvent e) {
            if (waitingForKey) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                dKey = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftKey = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                aKey = true;
            }
            if  (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightKey = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                shotFired = true;
            }
        }
        // A key pressed does not equal a key released, therefore

        public void keyReleased(KeyEvent e) {

            if (waitingForKey) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                dKey = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftKey = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                aKey = false;
            }
            if  (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightKey = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                shotFired = false;
            }
        }

        // notification from AWT that a key has been typed

        public void keyTyped(KeyEvent e) {

            if (waitingForKey) {
                if (pressCount == 1) {
                    waitingForKey = false;
                    startGame();
                    // reset the counter
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }
            // if esc is hit then the game should terminate and exit
            // key character for the esc key
            if (e.getKeyChar() == 27) {
                System.exit(0);
            }
        }
    }

    // Everything in the game will start here,
    // an instance of the game loop and display will be started here

    public static void main(String[] args) {

       Game game = new Game();
       game.gameLoop();
    }


}

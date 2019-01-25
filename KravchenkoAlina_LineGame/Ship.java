import greenfoot.*;
import java.util.List;
import java.awt.Color;
import java.awt.Font;

/**
 * The Ship class directs the player's ship movements and records spacebar activity,
 * as well as detects intersection with blocks.
 */
public class Ship extends Actor
{
    //VELOCITY
    // velocity of how quickly ship moves to bottom of screen (idle movement)
    private double velocityIdle = -3;
    // Velocity of ship's thrust to the other side
    private double velocityThrust = 35;
    // Angle with which the ship will fly upon thrust
    private int angleThrust = 20;

    // POSITION
    // double coordinate value for more precise calculations
    private double posX;
    private double posY;

    // starting amount of the explosion animation images
    private int explosionImageCount = 1;
    // total amount of explosion animation images
    private int explosionImageTotal = 46;

    // Direction of flight
    private Direction direction;

    // State of flight
    private State state;

    //key info
    String key;

    public Ship()
    {
        // reduce ship image to half its size
        GreenfootImage image = getImage();
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);

        // Initial state of the flight is idle
        state = State.idle;

        // Initial direction of the ship is "up"
        direction = Direction.up;

        // Turn the ship to face upward
        turn(-90);
    }

    // act out each frame
    public void act() 
    {
        // check the state of flight
        switch (state) {
            //if ship is idle
            case idle:
            //update location
            setIdleLocation();
            //check if state still should be 'idle'
            checkIdleState();
            break;

            //if ship is at 'thrust'
            case thrust:
            //update location
            setThrustLocation();
            //check if state still should be 'thrust'
            checkThrustState();
            break;

            //if ship is at 'slowdown'
            case slowdown:
            //update location
            setSlowdownLocation();
            //check if state should still be 'slowdown'
            checkSlowdownState();
            break;

            //if ship is 'dead'
            case dead:
            //draw explosion
            explode();
            break;
        }

    }

    // This method is called by the Greenfoot system when this actor has been inserted into the world.
    protected void addedToWorld(World world) {
        // set precise double position to the initial position upon adding the ship to world
        posX = getX();
        posY = getY();
    }

    // Set the new location of the ship when idle
    private void setIdleLocation() {
        // Calculate the new Y position. It cannot be less that 20 pixels above the screen
        posY = Math.min(getWorld().getHeight()-20, posY - velocityIdle);

        // Turn the ship towards the new location
        turnTowards((int)posX, 0);
        // Set the new location
        setLocation((int)posX, (int)posY);

        // Change the state of the ship to "explode" if it reached bottom of the screen
        if (posY == getWorld().getHeight()-20) {
            state = State.dead;
        }
    }

    // Verify if the state should stay "idle" or become "thrust"
    private void checkIdleState() {
        key = Greenfoot.getKey();

        //if space is pressed
        if (key != null && key.equals("space")) {
            // change the state to "thrust"
            state = State.thrust;

            // Set the direction of the "thrust" flight depending on the current position of the ship
            if (posX <= leftBorder()) {
                direction = Direction.right;
            }
            else if (posX >= rightBorder()) {
                direction = Direction.left;
            }
        }
    }

    // Set the new location of the ship if its state is "thrust"
    public void setThrustLocation() {
        // Calculate horizontal velocity
        double velocityX = velocityThrust*Math.cos(Math.toRadians(angleThrust));

        // Calculate the new X coordinate 
        // If the ship is about to cross the centre, put it in the centre.
        if (direction == Direction.right) {
            posX = Math.min(posX + velocityX, centre());
        } 
        else if (direction == Direction.left) {
            posX = Math.max(posX - velocityX, centre());
        }

        // Calculate vertical velocity
        double velocityY = velocityThrust*Math.sin(Math.toRadians(angleThrust));
        // Calculate the new Y coordinate. It cannot be less than zero.
        posY = Math.max(0, posY - velocityY);

        // Turn the ship towards the new location
        turnTowards((int)posX, (int)posY);
        // Set the new location
        setLocation((int)posX, (int)posY);
    }

    // Verify if the state should stay "thrust" or become "slowdown"
    private void checkThrustState() {
        if (posX == centre()) {
            // If the ship has reached the centre, change the state to "slowdown"
            state = State.slowdown;

            // Find the block that the ship has intersected
            Block b = ((Space)getWorld()).getFirstBlockAt(getX(), getY());
            //if intersected with block
            if (b != null) {
                //act out the block's effects on the ship
                b.onHit(this);
            }
        }
    }

    // Set the new location of the ship if its state is "slowdown"
    private void setSlowdownLocation() {
        // Ratio with which the ship should slow down
        double slowdownRatio = 0;

        //if going right
        if (direction == Direction.right) {
            /*
             * Slowdown ratio gets smaller as the ship comes closer to the right border.
             * 30 is the minimum distance the ship will be from the border position before it just moves there 
             */
            slowdownRatio = (rightBorder() - posX + 30)/(rightBorder() - centre());
        } 
        else if (direction == Direction.left)
        {
            /*
             * Slowdown ratio gets smaller as the ship comes closer to the left border.
             * 30 is the minimum distance the ship will be from the border position before it just moves there 
             */
            slowdownRatio = - (posX - leftBorder() + 30)/(centre() - leftBorder());
        }

        // Calculate horizontal velocity
        double velocityX = velocityThrust*Math.cos(Math.toRadians(angleThrust));
        // New X coordinate increases slower as the ship comes closer to the border, due to slowdownRatio
        posX = posX + velocityX*slowdownRatio;

        // Calculate vertical velocity
        double velocityY = velocityThrust*Math.sin(Math.toRadians(angleThrust));
        // Vertical velocity cannot be less than zero
        posY = Math.max(0, posY - velocityY);

        // Turn the ship towards the new location
        turnTowards((int)posX, (int)posY);
        // Set the new location
        setLocation((int)posX, (int)posY);
    }

    // Verify if the state should stay "slowdown" or go to "thrust"
    private void checkSlowdownState() {
        key = Greenfoot.getKey();

        // If space was pressed
        if (key != null && key.equals("space")) {
            // Change the state to "thrust"
            state = State.thrust;

            // Change the directon to the opposite
            if (posX < centre()) {
                direction = Direction.right;
            }
            else if (posX > centre()) {
                direction = Direction.left;
            }
        }
        // If space was not pressed
        else {
            // If the ship arrived to the border, change its state to "idle"
            boolean arrivedToRight = direction == Direction.right && posX >= rightBorder();
            boolean arrivedToLeft = direction == Direction.left && posX <= leftBorder();
            //change state to 'idle' when arrived
            if (arrivedToRight || arrivedToLeft) {
                state = State.idle;
                direction = Direction.up;
            }
        }
    }

    // When the state is "explode", draw explosion animation
    private void explode() {
        // If no explosion animation has been shown yet
        if (explosionImageCount == 1) {
            // Play explosion sound over the theme music
            GreenfootSound sound = new GreenfootSound("Explosion.wav");
            sound.play();            
        }

        // If not all animation has been drawn yet
        if (explosionImageCount <= explosionImageTotal) {
            // Change the ship image to the explosion image with index = explosionImageCount
            this.setImage(new GreenfootImage("frame_"+explosionImageCount+"_delay-0.07s.png"));

            explosionImageCount++;
        }
        // If all images have been shown
        else {
            //show the score after exiting Space
            Intro.scoreShown(Space.getScore());

            // Stop the theme music when the game is over
            getWorld().stopped();

            //clear the background
            getWorld().removeObjects(getWorld().getObjects(null));

            //set the world to Intro again
            Greenfoot.setWorld(new Intro());
        }
    }

    //return instance of this class
    public Ship getObject() {
        return this;
    }

    //return the direction of ship
    public Direction getDirection() {
        return direction;
    }

    //set direction of ship
    public void setDirection(Direction d) {
        direction = d;
    }

    //get state of flight
    public State getState() {
        return state;
    }

    //set state of flight
    public void setState(State s) {
        state = s;
    }

    //return x value of the leftmost possible position of ship
    private double leftBorder() {
        return getWorld().getWidth()/10.0;
    }

    //return x value of the rightmost possible position of ship
    private double rightBorder() {
        return getWorld().getWidth()*9.0/10.0;
    }

    //return x value of the centre of screen
    private int centre() {
        return getWorld().getWidth()/2;
    }

}

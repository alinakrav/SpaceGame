import greenfoot.*;
import java.util.List;
import java.awt.Color;
import java.awt.Font;

/** Line Breaking Game by Alina Kravchenko
 * Mr. Cohen P3 ICS3U1
 * June 15th, 2017
 * 
 * Sound Effects credited to audioblocks.com
 * Animations credited to giphy.com
 * 
 * To play the game, press the spacebar to launch the ship from side to side.
 * Try to hit the white blocks to break them.
 * Hit the grey blocks to bounce off and gain some motion.
 * Hitting the red blocks breaks the ship.
 * You have to stay in view, or else the ship breaks down.
 * You can keep a streak if you break the line each consecutive thrust (3 times and up = breaking streak)
 * A streak over 5 makes you gain double points for each line break (as long as the streak is running)
 * You can see your highscore per program execution (everything resets once program is stopped) in the menu.
 */
public class Space extends World
{
    private static int score = 0;

    private static int scoreStreak = 0;
    private GreenfootSound sound;
    private Block block;
    private Ship ship = new Ship();

    //World constructor
    public Space()
    {    
        // Create a new world with 400x600 cells with a cell size of 1x1 pixels.
        super(400, 600, 1); 

        // play theme music on loop
        startMusic();

        //put spaceship at 10% of screen width and 60% of screen height
        addObject(ship, getWidth()/10, (int)(getHeight()*2/5.0));

        //set score to 0
        score = 0;

        // Fill space with blocks
        initBlocks();
    }

    //act out each frame
    public void act()
    {
        // Display the current game score (and streaks)
        updateScore();
        checkStreak();

        // Generate new blocks
        addBlock();
    }

    // Make the initial blocks for the start of the game
    private void initBlocks() {
        int startY = 0;

        //while the Y indicator is still within bounds
        while (startY < getHeight()) {
            //remaining space is between the Y indicator and the bottom of screen
            int spaceLeft = getHeight() - 1 - startY;

            //generate random height for the block
            int height = Block.generateHeight();
            //if block height can fit in the space left
            if (height > spaceLeft * 2) {
                height = spaceLeft * 2;
            }
            //make a block while skipping its growth stage
            Block b = Block.createBlock(height, true);
            //the location of the next block that will be instantly grown
            int blockMiddleY = startY + b.getImage().getHeight() / 2;
            //add this instant block to the appropriate location
            addObject(b, getWidth()/2, blockMiddleY);

            //increase the next location for the next instant block	
            startY += b.getImage().getHeight();
        }
    }

    // This method is called when the execution has started.
    public void startMusic() {
        // play theme music on loop
        sound = new GreenfootSound("Theme.mp3");
        //sound.playLoop();
    }

    // This method is called when the execution has stopped.
    public void stopped() {
        // Stop the theme music when the game is over
        if (sound != null) {
            sound.stop();
            sound = null;
        }
    }

    public Ship getShip() {
        return ship;
    }

    // Increase the game score
    public void increaseScore() {
        score++;
    }

    //increase score streak by 1
    public void setStreak() {
        scoreStreak++;
        if(scoreStreak > 4)
            score++;
    }

    public void resetStreak() {
        scoreStreak = 0;
    }

    public int getStreak(){
        return scoreStreak;
    }

    private void addBlock()
    {
        // If there is no block at the top of the screen
        if(getFirstBlockAt(getWidth()/2, 0) == null) {
            //add a new block
            int height = Block.generateHeight();
            Block b = Block.createBlock(height, false);

            addObject(b, getWidth()/2, 0);
        }
    }

    // Find the first block that contains the given point
    public Block getFirstBlockAt(int x, int y) {
        // Get all objects of type Block or inherited from Block
        List<Block> blocks = getObjects(Block.class);

        //for each Block object
        for (Block b : blocks) {
            //if block contains certain coordinate
            if (b.containsPoint(x, y)) 
            //return that block
                return b;
        }

        //return null if no blocks found
        return null;
    }

    //print the current score
    private void updateScore() {
        getBackground().setColor(Color.BLACK);
        getBackground().fillRect(getWidth()-100, getHeight()-81, 100, 30);
        getBackground().setColor(Color.WHITE);
        getBackground().drawString("Score: " + score, getWidth() - 87, getHeight()-68);

        //update highscore
        Highscore.setHighscore(Math.max(Highscore.getHighscore(), score));
    }

    public static int getScore()
    {
        return score;
    }

    //print the streak number if streak is present
    private void checkStreak() {
        //clear streak text
        getBackground().setColor(Color.BLACK);
        getBackground().fillRect(getWidth()-111, getHeight()- 120, 100, 30);
        getBackground().setColor(Color.WHITE);
        //print streak if it exists
        if(scoreStreak > 2) {
            getBackground().drawString(scoreStreak + "   STREAK", getWidth()-90, getHeight()-100);
        }
    }
}

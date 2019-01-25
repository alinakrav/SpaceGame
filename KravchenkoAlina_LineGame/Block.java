import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
import java.util.List;

/**
 * Base class for blocks.
 * Contains functionality common for all types of blocks.
 * Type-specific functionality is defined in child classes.
 */
public class Block extends Actor
{
    // Block width is the same for all blocks
    static protected int width = 10;

    // Min and max heights for a block
    private static int minHeight = 40;
    private static int maxHeight = 180;

    //previous block type
    private static String lastBlock = "x";

    // Velocity with which the block moves down
    static protected int velocity = 5;

    // Block height after it has fully grown
    protected int finalHeight;
    // Block color
    protected Color color;

    //Block constrctor
    public Block(Color _color, int _finalHeight, boolean _skipGrowth)
    {
        //set block object height and colour 
        finalHeight = _finalHeight;
        color = _color;

        //if the block doesn't need to grow gradually
        if (_skipGrowth) {
            //make it its final height
            rescale(width, finalHeight);
        }
        else {
            // When the block is just created, its height is only one pixel
            rescale(width, velocity);
        }
    }

    // Act method for the block.
    public void act() 
    {
        // Move the block through the screen
        moveBlock();
    }

    //create a block while skipping the growth stage
    public static Block createBlock(int height, boolean skipGrowth) {
        Block block;
        // Generate a random number between 0 and 100, 
        int typeSelector = Greenfoot.getRandomNumber(100);
        //40% of random numbers make a white block
        if (typeSelector < 40) {

            block = new WhiteBlock(height, skipGrowth);

            /*              
            //if the last block wasn't white
            if(!lastBlock.equals("w"))
            {
            block = new WhiteBlock(height, skipGrowth);
            lastBlock = "w";
            }
            //if last block was white, make it grey instead of white this time
            else
            {
            block = new GrayBlock(height, skipGrowth);
            lastBlock = "g";
            }
             */
        }
        //30% make a grey block
        else if (typeSelector < 70) {

            block = new GrayBlock(height, skipGrowth);

            /*                
            //if the last block wasn't grey
            if(!lastBlock.equals("g"))
            {
            block = new GrayBlock(height, skipGrowth);
            lastBlock = "g";
            }
            //if last block was grey, make it red instead of grey this time
            else
            {
            block = new RedBlock(height, skipGrowth);
            lastBlock = "r";
            }
             */
        }
        //30% make a red red block
        else {

            block = new RedBlock(height, skipGrowth);

            /*
            //if the last block wasn't red
            if(!lastBlock.equals("r"))
            {
            block = new RedBlock(height, skipGrowth);
            lastBlock = "r";
            }
            //if last block was red, make it white instead of re this time
            else
            {
            block = new WhiteBlock(height, skipGrowth);
            lastBlock = "w";
            }
             */
        }

        return block;
    }

    //generate random height for block
    public static int generateHeight() {
        return Block.minHeight + Greenfoot.getRandomNumber(Block.maxHeight - Block.minHeight + 1);
    }

    // Find the first block that contains the given point
    public Block getFirstBlockAt(int x, int y) {
        // Get all objects of type Block or inherited from Block
        List<Block> blocks = getWorld().getObjects(Block.class);

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

    // Move the block through the screen
    protected void moveBlock()
    {
        GreenfootImage image = getImage();

        // If block is located at the top and has not grown full size yet, then increase its height
        if (getY() == 0 && image.getHeight() < finalHeight) {
            rescale(width, image.getHeight() + velocity * 2);
        }
        // If block is full size and did not approach the bottom, then move it down
        else if (getY() < getWorld().getHeight() - 1) {
            setLocation(getX(), getY() + velocity);
        }
        // If block reached the bottom and has not shrunk to height = 1, then decrease its height
        else if (getImage().getHeight() > 1) {
            rescale(width, Math.max(1, image.getHeight() - velocity * 2));
        }
        // If block has shrunk to height = 1, then remove it
        else {
            getWorld().removeObject(this);
        }
    }

    // Detect if the block contains the specified coordinates
    public boolean containsPoint(int x, int y) {
        //left edge of block x coordinate
        double x1 = getX()-getImage().getWidth()/2.0;
        //right edge of block
        double x2 = getX()+getImage().getWidth()/2;
        //upper edge of block
        double y1 = getY()-getImage().getHeight()/2.0;
        //lower edge of block
        double y2 = getY()+getImage().getHeight()/2;

        //is a certain x value between the left and right edges?
        boolean containsX = x >= x1 && x <= x2;
        //is a certain y value between the upper and lower edges?
        boolean containsY = y >= y1 && y <= y2;
        //return true if both x and y are between all the edges
        return (containsX && containsY);
    }

    // Set the block's new size and redraw it
    protected void rescale(int newWidth, int newHeight) {
        GreenfootImage image = getImage();
        image.scale(newWidth, newHeight);
        setImage(image);

        // Fill the block with its color
        image.setColor(color);
        image.fill();
        // Draw a frame around the block
        // Comment two lines below if you don't want the frame around each block
        //image.setColor(Color.orange);
        //image.drawRect(0, 0, image.getWidth()-1, image.getHeight()-1);
    }

    // What should the block do when it is intersected by the ship.
    // This method is implemented differently in each child block class (RedBlock, WhiteBlock, ...).
    protected void onHit(Ship sh) {
    }
}

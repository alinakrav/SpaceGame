import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.awt.Color;
import java.awt.Font;

/**
 * RedBlock is a child class of class Block.
 * It inherits most of its functionality from class Block, and only defines what is different.
 * If the ship hits the WhiteBlock, the lower part of the block disappears, along with all lower blocks.
 */
public class WhiteBlock extends Block
{   
    public WhiteBlock(int h, boolean skipGrowth) {
        super(Color.white, h, skipGrowth);
    }

    // What should the block do when it is intercepted by the ship.
    protected void onHit(Ship sh) {
        Space space = (Space)getWorld();

        // Increase Score
        space.increaseScore();
        //if one block is broken more than once
        space.setStreak();

        // Play collision sound over the theme music
        GreenfootSound sound = new GreenfootSound("Woosh.wav");
        sound.play();

        // The block should be cut off at the point of intersection by the ship.
        // The top of the block should stay where it is, whereas the bottom should disappear.

        // Calculate the new height of the top of block and its new central point (location).
        int shipCentralY = sh.getY();
        int blockHeight = getImage().getHeight();
        int blockStartY = getY() - blockHeight / 2;
        int blockEndY = getY() + blockHeight / 2;

        int blockNewHeight = Math.max(1, shipCentralY - blockStartY); // Height cannot be less than 1
        int blockNewCentralY = blockStartY + blockNewHeight / 2;

        // Change the block's height and redraw it
        rescale(getImage().getWidth(), blockNewHeight);
        // Set the new location (central point) of the block
        setLocation(getX(), blockNewCentralY);

        // Remove all blocks below this block
        List<Block> blocks = space.getObjects(Block.class);
        for (Block b : blocks) {
            if (b.getY() > blockEndY) {
                space.removeObject(b);
            }
        }
    }
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;

/**
 * RedBlock is a child class of class Block.
 * It inherits most of its functionality from class Block, and only defines what is different.
 * If the ship hits the RedBlock the game will finish.
 */
public class RedBlock extends Block
{
    public RedBlock(int h, boolean skipGrowth) {
        super(Color.red, h, skipGrowth);
    }

    // What should the block do when it is intercepted by the ship.
    protected void onHit(Ship sh) {
        // Update ship's state to "Dead" which will eventually finish the game
        sh.setState(State.dead);
    }
}

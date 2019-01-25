import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;

/**
 * RedBlock is a child class of class Block.
 * It inherits most of its functionality from class Block, and only defines what is different.
 * If the ship hits the GrayBlock it starts flying in the opposite direction.
 */
public class GrayBlock extends Block
{
    public GrayBlock(int h, boolean skipGrowth) {
        super(Color.gray, h, skipGrowth);
    }

    // What should the block do when it is intercepted by the ship.
    protected void onHit(Ship sh) {
        Space space = (Space)getWorld();

        // Play collision sound over the theme music
        GreenfootSound sound = new GreenfootSound("Bounce.wav");
        sound.play();
        sound.setVolume(100);
        //sound.play();

        //reset streak number
        space.resetStreak();

        // Change the direction of the ship.
        // Ship's state (thrust) is not changing.
        if (sh.getDirection() == Direction.left) {
            sh.setDirection(Direction.right);
        }
        else if (sh.getDirection() == Direction.right) {
            sh.setDirection(Direction.left);
        }
    }
}

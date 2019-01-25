import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Font;
import java.awt.Color;

/**
 * The button that leads to the highscore screen when pressed.
 */
public class Highscore extends Actor
{
    private static int highscore = 0;

    public void act() 
    {
        if(Greenfoot.mouseClicked(this))
        {
            //clear all the buttons
            getImage().clear();
            getWorld().removeObjects(getWorld().getObjects(Instructions.class));
            getWorld().removeObjects(getWorld().getObjects(Start.class));

            //set image to highscore image
            getWorld().setBackground("highscoreScreen.jpg");
            //add Back button
            getWorld().addObject(new Back(), getWorld().getWidth()/2, 520);

            //set the font and write the highscore
            getWorld().getBackground().setFont(new java.awt.Font("Abadi MT Condensed Light", java.awt.Font.PLAIN, 35));
            getWorld().getBackground().setColor(Color.WHITE);
            getWorld().getBackground().drawString(highscore + "", 255, 291);
        }
    }

    //set the current highscore
    public static void setHighscore(int hs)
    {
        highscore = hs;
    }

    //return the current highscore
    public static int getHighscore()
    {
        return highscore;
    }
}

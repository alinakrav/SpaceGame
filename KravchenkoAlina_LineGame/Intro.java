import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;

/**
 * The Intro world is the menu that shows up before a user plays the actual game. 
 * It contains buttons like "play", "instructions", and "highscore"
 */
public class Intro extends World
{
    private String key;
    //show the score from the previous game?
    private static boolean scoreShown;
    //score from the previous game
    private static int score;
    //highscore from all the games
    private static int highscore;
   
    public Intro()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(400, 600, 1); 

        //set background image back to normal (if instructions changed it)
        setBackground(new GreenfootImage("introScreen.jpg"));

        //remove Space world from previous round
        // removeObjects(getObjects(Space.class));
        //remove Intro screen from previous button pressed
        // removeObjects(getObjects(Intro.class));

        //add the buttons
        addObject(new Start(), getWidth()/2, getHeight()/2);
        addObject(new Instructions(), getWidth()/2, getHeight()/3);
        addObject(new Highscore(), getWidth()/2, getHeight()*2/3);

        //if world is made after losing a round (score is supposed to be shown)
        if(scoreShown)
        {
            //remove buttons
            removeObjects(getObjects(null));
            //add 'back' button
            addObject(new Back(), getWidth()/2, 520);

            //set background for score
            setBackground("scoreScreen.jpg");

            //set the font and write the score
            getBackground().setFont(new java.awt.Font("Abadi MT Condensed Light", java.awt.Font.PLAIN, 45));
            getBackground().setColor(Color.WHITE);

            //drawt the score for this round
            getBackground().drawString(score + "", 230, 298);
            score = 0;

            //don't show score until another round ends
            scoreShown = false;
        }
    }

    public void act()
    {
    }

    //show the score from the previous round after the Space world ends
    public static void scoreShown(int sc)
    {
        scoreShown = true;
        score = sc;
    }
}

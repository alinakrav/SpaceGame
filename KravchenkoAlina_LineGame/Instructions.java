import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The button that leads to the instructions screen when pressed.
 */
public class Instructions extends Actor
{
    /**
     * Act - do whatever the Button2 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(Greenfoot.mouseClicked(this))
        {
            getWorld().setBackground(new GreenfootImage("instructionsScreen.jpg"));
            getImage().clear();
            getWorld().removeObjects(getWorld().getObjects(Highscore.class));
            getWorld().removeObjects(getWorld().getObjects(Start.class));

            getWorld().addObject(new Back(), getWorld().getWidth()/2, 520);
        }
    }    
}

import java.util.*;
/**
 * A model of a grass.
 * grasses cannot move, and they are eaten by deers and rabbits
 * In this simple model, grasses can reproduce.
 * grasses dont have a maximum age.
 * grasses can also spawn when it rains.
 * @author A K M NAHARUL HAYAT and Noyan Raquib 
 * @version 2018.02.18
 */
public class Grass extends Plant
{
    //breeding probability of grass
    private static final double BREEDING_PROBABILITY = 0.09;
    //maximum aount of new grass that can be produced when a grass breeds
    private static final int MAX_LITTER_SIZE = 6;

    /**
     * Constructor for objects of class Grass
     * creates a grass object and assigns it a location
     */
    public Grass(Field x, Location y)
    {
       super(x,y);
      
    }
    /**
     * New births of grasses will be made into free adjacent locations.
     * @param newGrass A list to return newly born grasses.
     */
    private void giveBirth(List<Species> newGrass)
    {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass young = new Grass(field, loc);
            newGrass.add(young);
        }
    }
    /**
     * The grass breeds most of the time. It doesnt move.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly reproduced grasses.
     */
    public void act(List<Species> newGrass, Boolean day)
    {
        if(isAlive() == true)
        {
            giveBirth(newGrass);
        }
    }
    /**
     * it is assumed that grasses can always breed
     * @return true
     */
    protected boolean canBreed()
    {
       return true;
    }
    /**
     * @return the breeding probability of the grass
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    /**
     * @return the maximum reproduction size a grass can produce upon breeding
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}

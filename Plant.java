import java.util.*;
/**
 * A class representing the characteristics of plant.
 * Plants reproduce Aesexually.
 * Implements the Species Interface
 * 
 * @author A K M NAHARUL HAYAT and Noyan Raquib
 * @version 2018.02.18
 */
public abstract class Plant implements Species
{
    private boolean alive;//whether the plant is alive or not
    private Field field;// The plant's field.
    private Location location;// The plant's position in the field.
    //random number generator
    protected static final Random rand = Randomizer.getRandom();
    /**
     * Constructor for objects of class Plant
     */
    public Plant(Field field, Location location)
    {
        // initialise instance variables
       alive = true;
       this.field = field;
       setLocation(location);
    }
    /**
     * @return whether the plant is alive or not
     */
    public boolean isAlive()
    {
        return alive;
    }
    /**
     * ABSTRACT METHOD - when this method is called on an plant type the method implemented in the subclass will be executed.
     * Make this plant act - that is: make it do
     * whatever it wants/needs to do.
     * @param newSpecies A list to receive newly born plants.
     */
    abstract public void act(List<Species> newSpecies, Boolean day);
    /**
     * Indicate that the plant is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null)
        {
            field.clear(location);
            location = null;
            field = null;
            
        }
    }
    /**
     * Return the plant's location.
     * @return The plant's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    /**
     * @param the value of the age to set for a particular plant
     */
    public void setLocation(Location newLocation)
    {
        if(location != null)
        {
            field.clear(location);
        }
        location = newLocation;
        field.place(this,newLocation);
    }
    /**
     * Return the plant's field.
     * @return The plant's field.
     */
    protected Field getField()
    {
        return field;
    }
    /**
     * @return  number of births of a particular plant after it breeds
     */
    protected int breed()
    {
        int births = 0;
        if(this.getRandom().nextDouble() <= this.getBreedingProbability())
        {
            births = getRandom().nextInt(getMaxLitterSize() + 1);
        }
        return births;
    }
    /**
     * ABSTRACT METHOD - when this method is called on a plant type-
     * the method implemented in the subclass will be executed.
     * @return the maximum possible birth size when a plant reproduces
     */
    protected abstract int getMaxLitterSize();
    /**
     * @return the random number generator object
     */
    protected Random getRandom()
    {
        return rand;
    }
     /**
     * ABSTRACT METHOD - when this method is called on a plant type-
     * the method implemented in the subclass will be executed.
     * @return the reproduction probability of a particular plant
     */
    protected abstract double getBreedingProbability();
}

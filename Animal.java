import java.util.*;

/**
 * A class representing shared characteristics of animals.
 * Animals reproduce sexually (if a male and female of the same
 * species are in adjacet sqares, they breed).
 * Implements the Species Interface
 * 
 * @author A K M NAHARUL HAYAT and Noyan Raquib 
 * @version 2018.02.18
 */
public abstract class Animal implements Species
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;
    private int age;// The animal's age.
    private int foodLevel;// The animal's foodlevel.
    private static final Random rand = Randomizer.getRandom();//random number generator
    private static final double INFECTION_PROBABILITY = 0.001;//Infection start probability
    
    private static final double INFECTION_RECOVER_PROBABILITY = 0.01;//probability that an animal recovers from infection
    private boolean isFemale;//whether the animal is female or not
    private static final double MALE_PROBABILITY = 0.5;//probablity of a new animal's gender
    protected boolean isInfected;//whether the animal is infected or not

    /**
     * Create a new animal at location in field.
     * instantiates it's gender according to the random number generated 
     * and compared against gender probablity
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        if(rand.nextDouble() <= MALE_PROBABILITY) 
        {
            isFemale = true;
        }
        alive = true;
        isInfected = false;
        this.field = field;
        this.setLocation(location);
    }

    /**
     * Check whether the animal is female or not.
     * @return true if the animal is female
     */
    public boolean isFemale()
    {
        if(isFemale==true) 
        {
            return true;
        }
        else 
        {
            return false;
        }
    }

    /**
     * ABSTRACT METHOD - when this method is called on an animal type the method implemented in the subclass will be executed.
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Species> newSpecies, Boolean day);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    /**
     * ABSTRACT METHOD - when this method is called on an animal type-
     * the method implemented in the subclass will be executed.
     */
    abstract void incrementInfection();
    /**
     * uses the random number generator to check against the infection recover probability
     * if recovery succesful based on the probability, then sets the isInfected boolean flag to false
     */
    protected void surviveInfection()
    {
        if(rand.nextDouble()<=INFECTION_RECOVER_PROBABILITY) 
            isInfected = false;
    }

    /**
     * @return the value of the infection start probability 
     */
    protected double infectionProbability(){
        return INFECTION_PROBABILITY;
    }

    /**
     * @return true if the animal is infected, otherwise returns false
     */
    public boolean isInfected(){
        return isInfected;
    }
    /**
     * Sets the infection boolean flag of an animal to true
     */
    public void infect()
    {
        isInfected=true;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * Increment the age of the animal
     */
    protected void incrementAge() 
    {
        setAge(getAge() + 1);
        if (getAge() > getMaxAge()) 
        {
            setDead();
        }

    }

    /**
     * ABSTRACT METHOD - when this method is called on an animal type-
     * the method implemented in the subclass will be executed.
     * to get the maximum age a particuar animal type, can live for
     * @return the maximum age of the tye of the animal
     */
    protected abstract int getMaxAge();

    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField()
    {
        return field;
    }

    /**
     * Return number of births of a particular animal after it breeds
     * @return the number of animal a particular animal has given birth to
     */
    protected int breed() 
    {
        int births = 0;
        if (canBreed() && getRandom().nextDouble() <= getBreedingProbability()) {
            births = getRandom().nextInt(getMaxLitterSize()) + 1;
        }

        return births;
    }

    /**
     * ABSTRACT METHOD - when this method is called on an animal type-
     * the method implemented in the subclass will be executed.
     * @return - true if an animal can breed at the moment (ie: when male and female are together)
     */
    protected abstract boolean canBreed();

    /**
     * @return the current age of an animal
     */
    protected int getAge() 
    {
        return age;
    }

    /**
     * @param the value of the age to set for a particular animal
     */
    protected void setAge(int x) 
    {
        age = x;
    }

    /**
     * @return the current food level of an animal
     */
    protected int getFoodLevel() 
    {
        return foodLevel;
    }

    /**
     * @param the value of the foodlevel to set for an animal
     */
    protected void setFoodLevel(int foodLevel) 
    {
        this.foodLevel = foodLevel;
    }

    /**
     * @return the random number generator object
     */
    protected Random getRandom() 
    {
        return rand;
    }

    /**
     * @return the random number generated
     */
    protected double getRandomNumber() 
    {
        return rand.nextDouble();
    }

    /**
     * ABSTRACT METHOD - when this method is called on an animal type-
     * the method implemented in the subclass will be executed.
     * @return the maximum possible litter size of a particular animal when it breeds
     */
    protected abstract int getMaxLitterSize();

    /**
     * ABSTRACT METHOD - when this method is called on an animal type-
     * the method implemented in the subclass will be executed.
     * @return the breeding probability of a particular animal
     */
    protected abstract double getBreedingProbability();
}

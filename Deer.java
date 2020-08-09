
import java.util.*;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author A K M NAHARUL HAYAT and Noyan Raquib
 * @version 2018.02.18
 */
public class Deer extends Animal implements Infectable
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a deer can live.
    private static final int MAX_AGE = 70;
    // The likelihood of a deer breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single grass. this is the number of steps
    // a deer can go before it has to eat again, after eating a grass.
    private static final int GRASS_FOOD_VALUE = 10;
    //probability of infection spreading
    private static final double INFECTION_SPREAD_PROBABILITY = 0.05;
    //number of maximum steps a rabbit can live after being infected
    // with the disease, provided that it doesn't recover
    protected int infectionTimer=50;
    /**
     * Create a new deer. A deer may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the deer will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Deer(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge == true) 
        {
            setAge(getRandom().nextInt(MAX_AGE));
            setFoodLevel(getRandom().nextInt(GRASS_FOOD_VALUE));
        }
        else {
            setAge(0);
            setFoodLevel(GRASS_FOOD_VALUE); //initial hunger level set
        }
    }

    /**
     * This is what the deer does most of the time - it runs 
     * around and eats grass. Sometimes it will breed or die of old age.
     * @param newDeer A list to return newly born deers.
     * @param day - the boolean of whether its day or not
     */
    public void act(List<Species> newDeer, Boolean day)
    {
        incrementAge();//increment the age of the deer
        incrementHunger();//increments it's hunger
        //if the deer has infection, then count down it's infection timer
        //if the infection timer reaches 0, the deer dies
        incrementInfection();

        
        if(isAlive() == true)
        {
            //check aginst infection start probability to determine whether an infection has started
            if(getRandom().nextDouble() <= infectionProbability()) 
            {
                isInfected = true;
            }
            if(isInfected==true)
            { spreadInfection();//spread infection to deer, if there are any in it's adjacent location
              surviveInfection();//determine whether the rabbit has survived the infection or not
            }
            if(day==true)
            {
                giveBirth(newDeer);            
                // Move towards a source of food if found.
                Location newLocation = searchFood();
                if(newLocation == null) {
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
    }

    
    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDeer A list to return newly born deer.
     */
    private void giveBirth(List<Species> newDeer)
    {
        // New deers are born into adjacent locations.
        // Get a list of adjacent free locations.
        if(canBreed() == true)
        {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Deer young = new Deer(false, field, loc);
            newDeer.add(young);
        }
    }
    }

    /**
     * Make this deer more hungry. This could result in the deer's death.
     */
    private void incrementHunger()
    {
        setFoodLevel(getFoodLevel()-1);
        if(getFoodLevel() <= 0) 
        {
            setDead();
        }
    }

    /**
     * This method checks if a deer can breed
     * A deer can breed when it moves and encounters another deer which happens to be of the opposite sex
     * @return true if the deer can breed, otherwise false
     */
    protected boolean canBreed()
    {
        if(getAge() >= getBreedingAge())
        {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object animal = field.getObjectAt(where);
                if(animal instanceof Deer) 
                {
                    Deer deer = (Deer) animal;

                    if(deer.isFemale() == true && this.isFemale()==false) 
                    { 
                        return true;
                    }
                    if(deer.isFemale() == false && this.isFemale()==true) 
                    { 
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Decrease the amount of time the animal has left to live after
     * being infected, if this becomes 0 the animal dies
     */
    protected void incrementInfection()
    {
        if(isInfected==true)
        {
            infectionTimer--;
            if(infectionTimer<=0) setDead();
        }
    }

    /**
     * Look for grass adjacent to the current location.
     * Only the first live grass is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location searchFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);
            if(species instanceof Grass) 
            {
                Grass grass = (Grass)species;

                if(grass.isAlive() == true) 
                { 
                    grass.setDead();
                    setFoodLevel(GRASS_FOOD_VALUE);
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * If there is another Deer in the adjacent square, then there is a chance the
     * adjacent animal could become infected according to infection spread probability
     */
    public void spreadInfection()
    {
        if(getRandomNumber()<=INFECTION_SPREAD_PROBABILITY){
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Object animal = field.getObjectAt(where);
                if(animal instanceof Deer) 
                {
                    Deer deer = (Deer) animal;
                    deer.infect();
                }
            }
        }
    }

    /**
     * @return the maximum age value that a deer can live up to
     */
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * @return the breeding age of the deer - 
     * which is the age at which the deer, upon reaching, can breed
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return the breeding probability of the deer
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return the maximum litter size the deer can produce upon breeding
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}


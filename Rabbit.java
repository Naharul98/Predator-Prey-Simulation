import java.util.*;
/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * implements the infectable interface.
 * 
 * @author A K M NAHARUL HAYAT and Noyan Raquib
 * @version 2018.02.18
 */
public class Rabbit extends Animal implements Infectable
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 3;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 35;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.8;
    // The maximum number of births when a rabbit breeds.
    private static final int MAX_LITTER_SIZE = 8;
    // The food value of a single grass for rabbits. In effect, this is the
    // number of steps a rabbit can go before it has to eat again.
    private static final int GRASS_FOOD_VALUE = 20;
    //probability of infection spreading
    private static final double INFECTION_SPREAD_PROBABILITY = 0.05;
    //number of maximum steps a rabbit can live after being infected
    // with the disease, provided that it doesn't recover
    protected int infectionTimer = 30;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        //gender assignment of the rabbit is taken care of in the animal superclass
        super(field, location);
        if(randomAge == true) 
        {
            setAge(getRandom().nextInt(MAX_AGE));
            setFoodLevel(getRandom().nextInt(GRASS_FOOD_VALUE));
        }
        else {
            setAge(0);
            setFoodLevel(GRASS_FOOD_VALUE);//the initial food level of the rabbit is set according to the food level of it's primary food source
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     * @param day whether it is day time right now, or not
     * @param day - the boolean of whether its day or not
     */
    public void act(List<Species> newRabbits, Boolean day)
    {
        incrementAge();//increment the age of the rabbit
        incrementHunger();//increment it's hunger
        //if the rabbit has infection, then count down it's infection timer
        //if the infection timer reaches 0, the rabbit dies
        incrementInfection();
        
        if(isAlive() == true)
        {
            //check aginst infection start probability to determine whether an infection has started
            if(getRandom().nextDouble() <= infectionProbability()) 
            {
                isInfected = true;
            }
            if(isInfected==true)
            { 
                spreadInfection();//spread infection to rabbits, if there are any in it's adjacent location
                surviveInfection();//determine whether the rabbit has survived the infection or not
            }
            if(day==true){
                giveBirth(newRabbits);            
                // Try to move into a free location.
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
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Species> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        if(canBreed() == true)
        {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc);
            newRabbits.add(young);
        }
    }
    }

    /**
     * Decrease the amount of time the animal has left to live after
     * being infected, if this becomes 0 the animal dies
     */
    protected void incrementInfection(){
        if(isInfected==true){
            infectionTimer--;
            if(infectionTimer<=0) setDead();
        }
    }

    /**
     * reduce the food level of the rabbit 
     * if food level reaches 0, then the rabbit dies out of hunger
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
     * This method checks if a rabbit can breed
     * A rabbit can breed when it moves and encounters another rabbit which happens to be of the opposite sex
     * @return true if the rabbit can breed, otherwise false
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
                if(animal instanceof Rabbit) 
                {
                    Rabbit rabbit = (Rabbit) animal;

                    if(rabbit.isFemale() == true && this.isFemale()==false) 
                    { 
                        return true;
                    }
                    if(rabbit.isFemale() == false && this.isFemale()==true) 
                    { 
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * If there is another rabbit in the adjacent square, then there is a chance the
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
                if(animal instanceof Rabbit) 
                {
                    Rabbit rabbit = (Rabbit) animal;
                    rabbit.infect();
                }
            }
        }
    }

    /**
     * /**
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
     * @return the maximum age that the rabbit animal type
     */
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * @return the breeding age of the rabbit - 
     * which is the age at which the rabbit, upon reaching, can breed
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return the breeding probability of the rabbit
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return the maximum litter size the rabbit can produce upon breeding
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}


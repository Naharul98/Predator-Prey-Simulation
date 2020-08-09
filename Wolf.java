import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a wolf.
 * wolfs age, move, eat rabbits and deer, and die.
 * implements the infectable interface
 * @author A K M NAHARUL HAYAT and Noyan Raquib 
 * @version 2018.02.18
 */
public class Wolf extends Animal implements Infectable
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a wolf can start to breed.
    private static final int BREEDING_AGE = 6;
    // The age to which a wolf can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a wolf breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a wolf can go before it has to eat again after eating a rabbit.
    private static final int RABBIT_FOOD_VALUE = 20;
    // The food value of a single deer. In effect, this is the
    // number of steps a wolf can go before it has to eat again after eating a deer.
    private static final int DEER_FOOD_VALUE = 20;
    //probability of infection spreading
    private static final double INFECTION_SPREAD_PROBABILITY = 0.04;
    //number of maximum steps a wolf can live after being infected
    // with the disease, provided that it doesn't recover
    protected int infectionTimer=45;
    /**
     * Create a wolf. A wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge == true) 
        {
            setAge(getRandom().nextInt(MAX_AGE));
            setFoodLevel(getRandom().nextInt(DEER_FOOD_VALUE));
        }
        else {
            setAge(0);
            setFoodLevel(DEER_FOOD_VALUE);//initial hunger level set
        }
    }
    /**
     * This is what the wolf does most of the time: it hunts for
     * rabbits and deer. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born Wolfs.
     * @param day - the boolean of whether its day or not
     */
    public void act(List<Species> newWolves, Boolean day)
    {
        incrementAge();//increment the age of the wolf
        incrementHunger();//increment it's hunger

        //if the wolf has infection, then count down it's infection timer
        //if the infection timer reaches 0, the wolf dies
        incrementInfection();

        if(isAlive() == true) 
        {
            //check aginst infection start probability to determine whether an infection has started
            if(getRandom().nextDouble() <= infectionProbability()) 
            {
                isInfected = true;
            }
            if(isInfected==true)
            { spreadInfection();//spread infection to wolfs, if there are any in it's adjacent location
              surviveInfection();//determine whether the wolf has survived the infection or not
            }
            giveBirth(newWolves);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) 
            { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) 
            {
                setLocation(newLocation);
            }
            else 
            {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age. This could result in the wolf's death.
     */
    
    public int getMaxAge()
    {
        return MAX_AGE;
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
     * Make this wolf more hungry. This could result in the wolf's death.
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
     * Look for rabbits and deers adjacent to the current location.
     * Only the first live rabbit/deer is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
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
                if(rabbit.isAlive() == true) 
                { 
                    rabbit.setDead();
                    setFoodLevel(RABBIT_FOOD_VALUE);
                    return where;
                }
             else if(animal instanceof Deer) 
            {
                Deer deer = (Deer) animal;
                
                if(deer.isAlive() == true) 
                { 
                    deer.setDead();
                    setFoodLevel(DEER_FOOD_VALUE);
                    return where;
                }
            }
            
        }
    }
    return null;
}
    
    /**
     * Check whether or not this wolf is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newWolf A list to return newly born wolfs.
     */
    private void giveBirth(List<Species> newWolf)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        if(canBreed() == true)
        {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Wolf young = new Wolf(false, field, loc);
            newWolf.add(young);
        }
    }
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
            if(animal instanceof Wolf) 
            {
                Wolf wolf = (Wolf) animal;
                wolf.infect();
            }
        }
    }
    }
    /**
     * This method checks if a wolf can breed
     * A wolf can breed when it moves and encounters another wolf which happens to be of the opposite sex
     * @return true if the wolf can breed, otherwise false
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
                if(animal instanceof Wolf) 
                {
                    Wolf wolf = (Wolf) animal;

                    if(wolf.isFemale() == true && this.isFemale()==false) 
                    { 
                        return true;
                    }
                    if(wolf.isFemale() == false && this.isFemale()==true) 
                    { 
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * @return the breeding age of the wolf - 
     * which is the age at which the wolf, upon reaching, can breed
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    /**
     * @return the breeding probability of the wolf
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    /**
     * @return the maximum litter size the wolf can produce upon breeding
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
}

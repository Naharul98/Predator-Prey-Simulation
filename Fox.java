import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * implements the infectable interface
 * @author A K M NAHARUL HAYAT and Noyan Raquib
 * @version 2018.02.18
 */
public class Fox extends Animal implements Infectable
{
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 7;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.4;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 17;
    //probability of infection spreading
    private static final double INFECTION_SPREAD_PROBABILITY = 0.03;
    //number of maximum steps a fox can live after being infected
    // with the disease, provided that it doesn't recover
    protected int infectionTimer=40;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge == true) 
        {
            setAge(getRandom().nextInt(MAX_AGE));
            setFoodLevel(getRandom().nextInt(RABBIT_FOOD_VALUE));
        }
        else {
            setAge(0);
            setFoodLevel(RABBIT_FOOD_VALUE);
        }
    }

    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     * @param day - the boolean of whether its day or not
     */
    public void act(List<Species> newFoxes, Boolean day)
    {
        incrementAge();//increment the age of the fox
        incrementHunger();//increment it's hunger
        //if the fox has infection, then count down it's infection timer
        //if the infection timer reaches 0, the fox dies
        incrementInfection();

        
        if(isAlive() == true) 
        {
            //check aginst infection start probability to determine whether an infection has started
            if(getRandom().nextDouble() <= infectionProbability()) 
            {
                isInfected = true;
            }
            if(isInfected==true)
            { spreadInfection();//spread infection to fox, if there are any in it's adjacent location
                surviveInfection();//determine whether the fox has survived the infection or not
            }
            if(day==true){
                giveBirth(newFoxes);            
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
    }

    /**
     * Increase the age. This could result in the fox's death.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
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
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
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
            }
        }
        return null;
    }

    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Species> newFoxes)
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
            Fox young = new Fox(false, field, loc);
            newFoxes.add(young);
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
            if(animal instanceof Fox) 
            {
                Fox fox = (Fox) animal;
                fox.infect();
            }
        }
    }
    }

    /**
     * This method checks if a fox can breed
     * A fox can breed when it moves and encounters another fox which happens to be of the opposite sex
     * @return true if the fox can breed, otherwise false
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
                if(animal instanceof Fox) 
                {
                    Fox fox = (Fox) animal;

                    if(fox.isFemale() == true && this.isFemale()==false) 
                    { 
                        return true;
                    }
                    if(fox.isFemale() == false && this.isFemale()==true) 
                    { 
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * @return the breeding age of the fox - 
     * which is the age at which the fox, upon reaching, can breed
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    /**
     * @return the breeding probability of the fox
     */
    protected double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    /**
     * @return the maximum litter size the fox can produce upon breeding
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

}

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits,foxes,deer,wolf and plants. The foxes and wolves act as
 * the predetors and the rabbits and deer act as prey who eat grass.
 * All animals (and no plants) can become infected and will appear magenta
 * is the simulation. There is a 100ms delay between each step to observe 
 * the behavior in detail
 * In order to start the simulation Create an object of this class.
 * Then call one of:
    + simulate - and supply a number (say 10) for that many steps.
    + runLongSimulation - for a simulation of 3000 steps.
 * @author A K M NAHARUL HAYAT and Noyan Raquib
 * @version 2018.02.19
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 180;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 120;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.05;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.2; 
    // The probability that a Grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.8;
    // The probability that a wolf will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.2;
    // The probability that a deer will be created in any given grid position.
    private static final double DEER_CREATION_PROBABILITY = 0.5;
    // The probability of rain
    private static final double RAIN_PROBABILITY = 0.9;
    //number of steps after which rain probability is compared to enable possibility of rain
    private static final double RAIN_STEP_INTERVAL = 12;
    // List of animals in the field.
    private List<Species> species;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    //population generator object deals with populating the simulator initially
    //it also concerns about the color code of different animals in the simulator
    private PopulationGenerator popGen;
    // Day-Night object which enables simulation of day and night
    private DayNight dayNight;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
        
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        //checks for valid dimension, If valid dimension not input, then chooses default
        if(width <= 0 || depth <= 0) 
        {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        //instantiate the variables
        dayNight = new DayNight();
        species = new ArrayList<>();
        field = new Field(depth, width);
        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        //create a population generator to facilate populating the simulator initially
        //and also to enable color code of different species in the simulator
        popGen = new PopulationGenerator(view);

        //creates the view of the day/night on the simulation form, initially set to day
        view.setInfoText("Time of Day: "+dayNight.toString());

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (2000 steps).
     */
    public void runLongSimulation()
    {
        simulate(3000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        Random rand = Randomizer.getRandom();//generate random number for comparing with probability
        
        int rainStepCount = 0;//count of the number of steps since rain has fallen
        for(int step = 1; step <= numSteps && view.isViable(field); step++) 
        {
            //check if rain step count is the same as the rain step interval
            if(rainStepCount == RAIN_STEP_INTERVAL)
            {
                //generates random number and compares with rain probability
                if(rand.nextDouble() <= RAIN_PROBABILITY) 
                {
                    //popuates plant according to plant creation probability
                    popGen.populatePlant(species, field);
                }
                rainStepCount = 0;
            }
            simulateOneStep();
            delay(100);
            rainStepCount++;//increment rain step count
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * species.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn animals.
        List<Species> newSpecies = new ArrayList<>();        
        // Let all species act.
        for(Iterator<Species> it = species.iterator(); it.hasNext(); ) 
        {
            Species x = (Species)it.next();
            //call the act method on species,also pass in the parameter
            //of whether it's day/night, this parameter determines behavior of
            //the animals depending on the time.
            x.act(newSpecies,dayNight.isDay());
            if(x instanceof Animal) 
            {
                Animal animal = (Animal)x;//cast to animal type
                if(animal.isAlive() == false)//checks for death of animal
                {
                    it.remove();
                }
            }
            else if(x instanceof Plant)//checks for death of plants
            {
                Plant plant = (Plant)x;//cast to plant type
                if(plant.isAlive() == false)
                {
                    it.remove();
                }
            }
        }

        // Add the newly born species to the main lists.
        species.addAll(newSpecies);

        view.showStatus(step, field);

        dayNight.cycle();//cycles day/night

        view.setInfoText("Time of Day: "+dayNight.toString());//displays if its day or night
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        species.clear();
        popGen.populate(species, field);
        // Show the starting state in the view.
        view.showStatus(step, field);
    }


    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) 
        {
            // wake up
        }
    }
}

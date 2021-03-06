import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;
/**
 * This class concerns about the creation probability of 
 * different species in the simulator and the initial creation of 
 * animals when the simulator is first started. 
 * In addition, it also concerns about the color of different
 * species in the simulator display grid. 
 *
 * @author A K M NAHARUL HAYAT and Noyan Raquib 
 * @version 2018.02.18
 */
public class PopulationGenerator
{
    // The probability that a wolf will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.2;
    // The probability that a deer will be created in any given grid position.
    private static final double DEER_CREATION_PROBABILITY = 0.5;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.05;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.3; 
    // The probability that a Grass will be created in any given grid position.
    private static final double GRASS_CREATION_PROBABILITY = 0.8;
    /**
     * Constructor for objects of class PopulationGenerator
     */
    public PopulationGenerator(SimulatorView view)
    {
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Rabbit.class, Color.YELLOW);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(Wolf.class, Color.RED);
        view.setColor(Deer.class, Color.BLACK);
        view.setColor(Infectable.class, Color.MAGENTA);
    }
    public void populate(List<Species> species, Field field)
    {
        Random rand = Randomizer.getRandom();//random number generator to compare against probability
        field.clear();
        //iterate through every block
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                //create new fox if random number fits fox creation probability
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY)
                {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    species.add(fox);
                }//create new rabbit if random number fits rabbit creation probability
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) 
                {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    species.add(rabbit);
                }//create new grass if random number fits grass creation probability
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY)
                {
                    Location location = new Location(row,col);
                    Grass grass = new Grass(field, location);
                    species.add(grass);

                }//create new wolf if random number fits wolf creation probability
                else if(rand.nextDouble() <= WOLF_CREATION_PROBABILITY)
                {
                    Location location = new Location(row,col);
                    Wolf wolf = new Wolf(true, field, location);
                    species.add(wolf);

                }//create new deer if random number fits deer creation probability
                else if(rand.nextDouble() <= DEER_CREATION_PROBABILITY)
                {
                    Location location = new Location(row,col);
                    Deer deer = new Deer(true, field, location);
                    species.add(deer);

                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * After rain step count reaches rain interval, the rain probability is checked against random number
     * If the rain fall probabiity is satisfied
     * then this method is called to populate plants across the grid, using grass creation probability
     */
    public void populatePlant(List<Species> species, Field field)
    {
        Random rand = Randomizer.getRandom();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY && field.getObjectAt(row,col) == null)
                {
                    Location location = new Location(row,col);
                    Grass grass = new Grass(field, location);
                    species.add(grass);
                }
            }
        }

    }

}

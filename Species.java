import java.util.*;
/**
 * Interface for the species in the simulator
 *
 * @author A K M NAHARUL HAYAT and Noyan Raquib 
 * @version 2018.02.18
 */
public interface Species
{
    
    void act(List<Species> newSpecies, Boolean day);//to simulate acting of species in the simulator
    void setLocation(Location variable);//setting location of the species
}


/**
 * This class facilates the cycling of Day/Night in the simulator.
 *
 * @author A K M NAHARUL HAYAT and Noyan Raquib 
 * @version 2018.02.18
 */
public class DayNight
{
    private boolean isDay;

    /**
     * Constructor for objects of class DayNight
     */
    public DayNight()
    {
        
        isDay = true;//initially, the simulator is in day mode
    }
    /**
     * Represent a row and column.
     * @param boolean to set the state of the time of the simulator.
     */
    public void setDayNight(Boolean set)
    {
        isDay = set;
    }
    /**
     * Cycles day/night. If simulator is in day state then changes to night state
     * If simulator is in night state then changes to day state
     */
    public void cycle(){
        if(isDay==true) isDay = false;
        else isDay = true;
    }
    /**
     * @return boolean of the current state of the day
     */
    public boolean isDay(){
        if(isDay==true) return true;
        else return false;
    }
    /**
     * @return String value of the current state of the day
     */
    public String toString(){
        if(isDay==true) return "Day";
        else return "Night";
    }


}

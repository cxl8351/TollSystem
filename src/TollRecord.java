/**
 * A set of information that is to define the history of a vehicle passing through a highway
 *
 * @author Christopher Lee
 */

public class TollRecord implements Comparable<TollRecord> {

    /**
     * For printing toll records in reports
     * using {@link String#format(String, Object...)}
     */
    private static final String TOLL_RECORD_FORMAT = "[%11s] on #%2d, time %5d";
    private static final String OFF_FORMAT = "; off #%2d, time %5d";

    /**
     * Value of uninitialized integer fields in this record
     */
    public static final int UNINITIALIZED = -1;

    private String Tag;
    private int OnTime;
    private int OffTime = UNINITIALIZED;
    private int OnExit;
    private int OffExit = UNINITIALIZED;
    private int hashcode = UNINITIALIZED;

    /**
     * Constructor for TollRecord
     *
     * @param onTime Starting time
     * @param tag Tag
     * @param incoming Starting toll
     */
    public TollRecord(int onTime, String tag, int incoming){
        this.Tag = tag;
        this.OnExit = incoming;
        this.OnTime = onTime;
    }

    /**
     * Sets OffTime and OffExit with given parameters
     *
     * @param outgoing integer to set OffExit
     * @param outTime integer to set OffTime
     */
    public void setOffExit(int outgoing, int outTime) {
        OffExit = outgoing;
        OffTime = outTime;

    }

    /**
     * Gets the tag
     *
     * @return the tag
     */
    public String getTag() {
        return Tag;
    }

    /**
     * Gets onTime
     *
     * @return onTime
     */
    public int getOnTime() {
        return OnTime;
    }

    /**
     * Gets OffTime
     *
     * @return OffTime
     */
    public int getOffTime() {
        if(this.OffTime == UNINITIALIZED){
            return -1;
        }
        return OffTime;
    }

    /**
     * Gets OnExit
     *
     * @return  OnExit
     */
    public int getOnExit() {
        return OnExit;
    }

    /**
     * Gets OffExit
     *
     * @return OffExit
     */
    public int getOffExit() {
        if(this.OffExit == UNINITIALIZED){
            return -1;
        }
        return OffExit;
    }

    /**
     * Gets Fare
     *
     * @return Fare
     */
    public double getFare() {
        return TollSchedule.getFare(getOnExit(),getOffExit());
    }

    /**
     * Determines if two TollRecord objects are equal to one another
     *
     * @param other the object to be checked
     * @return True if they are the same, false if they are not.
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof TollRecord){
            return this.Tag.equals(((TollRecord) other).Tag)
                    && this.OnTime == ((TollRecord) other).OnTime
                    && this.OffTime == ((TollRecord) other).OffTime
                    && this.OffExit == ((TollRecord) other).OffExit
                    && this.OnExit == ((TollRecord) other).OnExit;
                    //&& this.hashcode == ((TollRecord) other).hashcode;

        }
        return false;
    }

    /**
     * A string representation of a TollRecord Object
     *
     * @return a string
     */
    @Override
    public String toString() {
        return "Vehicle " + getTag() + ", starting at time " + getOnTime();
    }

    /**
     * A formatted expression of when a vehicle enters a highway
     *
     * @return a string
     */
    public String report(){
        return String.format(TOLL_RECORD_FORMAT,getTag(),getOnExit(),getOnTime());
    }

    /**
     * A formatted expression of when a vehicle exits a highway
     *
     * @return a string
     */
    public String offReport(){
        return String.format(OFF_FORMAT,getOffExit(),getOffTime());
    }


    /**
     * Calculates a hashcode value
     *
     * @param input a string to be calculated
     * @return a hashcode value of the string
     */
    //@Override
    public int hashCode(String input) {
        int[] anArray;
        anArray = new int[input.length()];

        for( int i = 0; i < input.length(); i++){
            anArray[i] = input.charAt(i)*(int)Math.pow(31,input.length()-1-i);
        }

        int sum = 0;
        int j = 0;
        while(j < input.length()){
            sum += anArray [j];
            j++;
        }
        this.hashcode = sum;
        return sum;
    }


    /**
     * Compares two TollRecord objects first by OnTime, then by Tag
     *
     * @param other the other object
     * @return the result of the compareTo
     */
    @Override
    public int compareTo(TollRecord other){

        int result = this.getOnTime() - other.getOnTime();
        if(result == 0){
            result = this.getTag().compareTo(other.getTag());
        }
        return result;

    }


}


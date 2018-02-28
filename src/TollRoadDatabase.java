/* A few useful items are provided to you. You must write the rest. */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Collection of data from a file to be used to filter out specific kinds of data
 *
 * @author Christopher Lee
 */
public class TollRoadDatabase{
    /**
     * For printing floating point values in dollar/cents format. Example:
     * System.out.println( String.format( DOLLAR_FORMAT, 10.5 );  // $10.50
     */
    private static final String DOLLAR_FORMAT = "$%5.2f";
    private static final String SPEED_FORMAT = "%5.1f MpH";

    /**
     * Universal new line
     */
    private static final String NL = System.lineSeparator();

    /**
     * Conversion constant from minutes to hours
     */
    public static final double MINUTES_PER_HOUR = 60.0;

    /**
     * This toll road's speed limit, in miles per hour
     */
    public static final double SPEED_LIMIT = 65.0;

    private String filename;

    private ArrayList<TollRecord> completeTrips;
    private TreeMap<String,TollRecord> incompleteTrips;

    /**
     * Constructor for parsing data from files
     *
     * @param eventFileName The file name
     * @throws FileNotFoundException
     */
    TollRoadDatabase(String eventFileName) throws FileNotFoundException{
        Scanner input = new Scanner(new File(eventFileName));

        this.filename = eventFileName;
        completeTrips = new ArrayList<TollRecord>();
        incompleteTrips = new TreeMap<>();

        while(input.hasNextLine()){
            String line = input.nextLine();
            String fields[] = line.split(",");

            //fields[0] = Time
            //fields[1] = tag
            //fields[2] = exit

            int onTime = Integer.parseInt(fields[0]);
            String tag = fields[1];
            int onExit = Integer.parseInt(fields[2]);

            enterEvent(onTime,tag,onExit);

        }

        Collections.sort(completeTrips,new RecordComparator());

        input.close();
    }

    /**
     * Determines where licenses should go.
     *
     * @param time the licenses' onTime
     * @param tag the licenses' tag
     * @param exit the licenses' onExit
     */
    private void enterEvent(int time, String tag, int exit){
        if(incompleteTrips.containsKey(tag)){
            TollRecord record = incompleteTrips.remove(tag);
            record.setOffExit(exit,time);
            completeTrips.add(record);
        }
        else{
            TollRecord record = new TollRecord(time,tag,exit);
            incompleteTrips.put(tag,record);
        }

    }

    /**
     * Prints all of the licenses still on the road
     *
     * @return how many licenses are on the road.
     */
    public int summaryReport(){
        int count = 0;
        for(TollRecord record : completeTrips){
            count +=1;
        }
        return count;
    }

    /**
     * Print out a report listing the vehicles that are still on the toll road.
     * The vehicles will be printed in order based on license tag.
     */
    public void onRoadReport(){
        for(TollRecord value : incompleteTrips.values()){
            System.out.println(value.report());
        }

    }

    /**
     * Print out a billing report for the vehicles that completed trips on the toll road.
     * The report lists the trips first by vehicle tag and then by the entry time for the vehicle's trip.
     * The toll that was charged for each trip is handled by TollRecord.getFare
     *
     */
    public void printBills(){
        double total = 0.0;
        for(TollRecord record : completeTrips){
            total += record.getFare();
            System.out.println(record.report() + record.offReport() + ": " + String.format(DOLLAR_FORMAT, record.getFare()));
        }
        total = (double)Math.round(total * 100d) / 100d;
        System.out.println("Total: $" + total);
    }

    /**
     * Calculates the bill associated with a certain tag
     *
     * @param tag The tag to be associated with the bill
     * @return The bill
     */
    private double bill(String tag){
        double sum = 0.0;

        for(TollRecord record : completeTrips){
            if(record.getTag().equals(tag)){
                sum = record.getFare();
                sum = (double)Math.round(sum * 100d) / 100d;
            }
        }

        return sum;
    }


    /**
     * sort by tag
     * List cars going above the speed limit. (A constant should be used for this, and set to 65 (MpH).)
     *
     */
    public void speederReport(){

        double time;
        double firstPos;
        double lastPos;
        double velocity;

        for(TollRecord record : completeTrips){
            firstPos = TollSchedule.getLocation(record.getOnExit());
            lastPos = TollSchedule.getLocation(record.getOffExit());
            time = record.getOffTime()-record.getOnTime();

            velocity = ((lastPos - firstPos)/time)*MINUTES_PER_HOUR;
            velocity = Math.abs(velocity);

            if(velocity > SPEED_LIMIT){
                System.out.println(record.toString());
                System.out.println("    from " + TollSchedule.getInterchange(record.getOnExit()));
                System.out.println("    to " + TollSchedule.getInterchange(record.getOffExit()));
                System.out.println("    at " + String.format(SPEED_FORMAT,velocity));
            }
        }

    }

    /**
     *
     * Print the summary information for a single customer, specified by license tag.
     * The toll records are printed in order of time entered.
     * The complete trips will be listed in the same format as the complete billing listing
     * with the fare of each trip listed at the end of the line.
     * A total due is printed at the end.
     * The incomplete entry, if any, will not be shown.
     *
     * @param tag the license tag
     */
    public void printCustSummary(String tag){
        double total = 0.0;

        for(TollRecord record : completeTrips){
            if(record.getTag().equals(tag)){
                System.out.println(record.report() + record.offReport() + ": " + String.format(DOLLAR_FORMAT, bill(record.getTag())));

                total += bill(record.getTag());
            }
        }
        System.out.println("Total: $" + total);

    }

    /**
     *
     * Print all toll records that include a specific exit as their on or off point.
     * Records are listed completed first, in order by vehicle tag and then by entry time;
     * afterwards incomplete trips are listed in the same ordering.
     *
     * @param exit the exit number
     */
    public void printExitActivity(int exit){

        for(TollRecord record : completeTrips){
            if(record.getOnExit() == exit || record.getOffExit() == exit){
                System.out.println(record.report() + record.offReport() + ": " + String.format(DOLLAR_FORMAT, bill(record.getTag())));
            }
        }
    }

    /**
     * Compares by tags
     *
     */
    class RecordComparator implements Comparator<TollRecord>{
        public int compare(TollRecord record1, TollRecord record2){
            return record1.getTag().compareTo(record2.getTag());
        }
    }



}

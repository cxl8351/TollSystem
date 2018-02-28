import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Printing information in a legible format
 *
 * @author Christopher Lee
 *
 */
public class TollReport {

    /**
     * Main function
     *
     * @param args program arguments
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException{

        Scanner IO = new Scanner(System.in);
        String input;
        String inputFile = args[0];

        TollRoadDatabase data = new TollRoadDatabase(inputFile);

        System.out.println("");
        System.out.println(data.summaryReport() + " completed trips.");

        System.out.println("");
        System.out.println("On-Road Report");
        System.out.println("==============");
        data.onRoadReport();

        System.out.println("");
        System.out.println("BILLING INFORMATION");
        System.out.println("===================");
        data.printBills();

        System.out.println("");
        System.out.println("Speeder Report");
        System.out.println("==============");
        data.speederReport();


        while(true){

            System.out.println("");
            System.out.println("'b <string>' to see bills for a license tag.");
            System.out.println("'e <number> to see actvity at exit.'");
            System.out.println("'q' to quit.");
            input = IO.nextLine();

            String parts[] = input.split(" ");

            if(parts[0].equals("b")){
                System.out.println("");
                System.out.println("Bills for " + parts[1]);
                System.out.println("=====================");
                data.printCustSummary(parts[1]);
            }
            else if(parts[0].equals("e")){
                System.out.println("");
                System.out.println("Exit Activity at " + parts[1]);
                data.printExitActivity(Integer.parseInt(parts[1]));
            }
            else if(parts[0].equals("q")){
                System.out.println("");
                System.out.println("Closing program...");
                break;
            }
            else{
                System.out.println("Unknown command.");
            }

        }
    }
}

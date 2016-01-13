package document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/** A class for timing the EfficientDocument and BasicDocument classes
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 */

public class DocumentBenchmarking {

	
	public static void main(String [] args) {

	    // Run each test more than once to get bigger numbers and less noise.
	    // You can try playing around with this number.
	    int trials = 50;

	    // The text to test on
	    String textfile = "C:\\DEV\\Workspace\\UCSD2\\MOOCTextEditor\\data\\warAndPeace.txt";
		
	    // The amount of characters to increment each step
	    // You can play around with this
		int increment = 20000;

		// The number of steps to run.  
		// You can play around with this.
		int numSteps = 20;
		
		// THe number of characters to start with. 
		// You can play around with this.
		int start = 50000;
		
		// TODO: Fill in the rest of this method so that it runs two loops
		// and prints out timing results as described in the assignment 
		// instructions.
		System.out.println("num\tBasic\tEfficient");
		for (int numToCheck = start; numToCheck < numSteps*increment + start; 
				numToCheck += increment)
		{
			// numToCheck holds the number of characters that you should read from the
			// file to create both a BasicDocument and an EfficientDocument.  
			
			// Each time through this loop you should:

			// 1. Print out numToCheck followed by a tab (\t) (NOT a newline)
			System.out.print(numToCheck + "\t");

			// 2. Read numToCheck characters from the file into a String
			String temp = getStringFromFile(textfile, numToCheck);

			// 3. Time a loop that runs trials times (trials is the variable above) that:
			double begin = System.nanoTime() / 1_000_000_000.0;
			for(int it = 0; it != trials; ++it)
			{
				// Creates a BasicDocument
				BasicDocument basic = new BasicDocument(temp);
				// Calls fleshScore on this document
				basic.getFleschScore();
			}
			double end = System.nanoTime() / 1_000_000_000.0;
			double diff = end - begin;
			// 4. Print out the time it took to complete the loop in step 3
			System.out.print(diff + "\t");
			// 5. Time a loop that runs trials times (trials is the variable above) that:
			begin = System.nanoTime() / 1_000_000_000.0;
			for(int it = 0; it != trials; ++it)
			{
				// Creates an EfficientDocument
				EfficientDocument efficient = new EfficientDocument(temp);
				// Calls fleshScore on this document
				efficient.getFleschScore();
			}
			end = System.nanoTime() / 1_000_000_000.0;
			diff = end - begin;
			// 6. Print out the time it took to complete the loop in step 5
			System.out.print(diff + "\n");
		}
	
	}
	
	/** Get a specified number of characters from a text file
	 * 
	 * @param filename The file to read from
	 * @param numChars The number of characters to read
	 * @return The text string from the file with the appropriate number of characters
	 */
	public static String getStringFromFile(String filename, int numChars) {
		
		StringBuffer s = new StringBuffer();
		try {
			FileInputStream inputFile= new FileInputStream(filename);
			InputStreamReader inputStream = new InputStreamReader(inputFile);
			BufferedReader bis = new BufferedReader(inputStream);
			int val;
			int count = 0;
			while ((val = bis.read()) != -1 && count < numChars) {
				s.append((char)val);
				count++;
			}
			if (count < numChars) {
				System.out.println("Warning: End of file reached at " + count + " characters.");
			}
			bis.close();
		}
		catch(Exception e)
		{
		  System.out.println(e);
		  System.exit(0);
		}
		
		
		return s.toString();
	}
	
}

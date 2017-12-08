import java.io.File;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class GPAHelper {
	
	private static final String INTERACTIVE_TEXT_FILE = "gpaList.txt";

	
	public static void main(String args[]) {
		String inputPrompt = String.format("Enter a class name.");
        Scanner lineScanner = new Scanner(System.in);

        while (true) {
            String courseName = null;
            System.out.println(inputPrompt);

            /*
             * We could just use lineScanner.hasNextInt() and not initialize a separate scanner. But
             * the default Scanner class ignores blank lines and continues to search for input until
             * a non-empty line is entered. This approach allows us to detect empty lines and remind
             * the user to provide a valid input.
             */
            String nextLine = lineScanner.nextLine();
            Scanner inputScanner = new Scanner(nextLine);
            if (!(inputScanner.hasNext())) {
                /*
                 * These should be printed as errors using System.err.println. Unfortunately,
                 * Eclipse can't keep System.out and System.err ordered properly.
                 */
                System.out.println("Invalid input: please enter a listed class.");
                continue;
            }
            courseName = inputScanner.next();
            /*
             * If the line started with a string but contains other tokens, reinitialize userInput
             * and prompt the user again.
             */
            if (inputScanner.hasNext()) {
                System.out.println("Invalid input: please enter only a single name.");
                continue;
            }
            inputScanner.close();

            String gpaList;
            try {
                String gpaListPath = GPAHelper.class.getClassLoader()
                        .getResource(INTERACTIVE_TEXT_FILE).getFile();
                gpaListPath = new URI(gpaListPath).getPath();
                File gpaListFile = new File(gpaListPath);
                Scanner gpaListScanner = new Scanner(gpaListFile, "UTF-8");
                gpaList = gpaListScanner.useDelimiter("\\A").next();
                gpaListScanner.close();
            } catch (Exception e) {
                throw new InvalidParameterException("Bad file path" + e);
            }

            printAverageGPA(courseName, gpaList);
            break;
        }
        lineScanner.close();
	}


	public static void printAverageGPA(final String searchWord, final String corpus) {

        String[] linesOfText = corpus.split("\n");
        int numberOfLines = linesOfText.length;
        int i = 0; //incrementing variable

        for (i = 0; i < numberOfLines; i++) {
            String currentLine = linesOfText[i].replaceAll("[ .!?,;()-]", " ");
            currentLine = currentLine.toUpperCase();
            currentLine = " " + currentLine + " ";
            String universalSearchWord = searchWord.toUpperCase();

            if (currentLine.contains(" " + universalSearchWord + " ")) {
                System.out.println((i + 1) + ": " + linesOfText[i]);
            }
        }	    
	}
}

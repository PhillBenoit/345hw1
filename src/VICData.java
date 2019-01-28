import java.io.File;
import java.util.Scanner;

public class VICData {
    public String agentID = new String();
    public String date = new String();
    public String phrase = new String();
    public String phraseOriginal = new String();
    public String anagram = new String();
    public String message = new String();
    public String messageOriginal = new String();

    public static int     ID_LEN          = 5;   // # of chars in agent ID
    public static int     DATE_LEN        = 6;   // # of chars in date
    public static int     PHRASE_LEN      = 10;  // # letters of phrase to use
    public static int     ANAGRAM_LEN     = 10;  // # of chars in anagram
    public static int     ANAGRAM_LETTERS = 8;   // # of letters in anagram

    public static char    SPACE           = (char)32; // space is ASCII 32
    public static boolean DEBUG           = false;    // toggle debug prints


    /*---------------------------------------------------------------------
        |  Method readVICData (pathName)
        |
        |  Purpose:  VIC requires five pieces of information for an
        |            encoding to be performed:  (1) agent ID (form: #####),
        |            (2) date (form:YYMMDD), (3) a phrase containing at least
        |            10 letters, (4) an anagram of 8 commonly-used letters
        |            and 2 spaces, and (5) a message of at least one letter
        |            to be encoded.  This method reads these pieces from the
        |            given filename (or path + filename), one per line, and
        |            all pieces are sanity-checked.  When reasonable,
        |            illegal characters are dropped.  The program is halted
        |            if an unrecoverable problem with the data is discovered.
        |
        |  Pre-condition:  None.  (We even check to see if the file exists.)
        |
        |  Post-condition:  The returned VICData object's fields are populated
        |            with legal data.
        |
        |  Parameters:  pathName -- The filename or path/filename of the text
        |            file containing the file pieces of data.  If only a
        |            file name is provided, it is assumed that the file
        |            is located in the same directory as the executable.
        |
        |  Returns:  A reference to an object of class VICData that contains
        |            the five pieces of data.
     *-------------------------------------------------------------------*/

    public static VICData readVICData (String pathName) {
        VICData vic = new VICData(); // Object to hold the VIC input data
        Scanner inFile = null;       // Scanner file object reference

        try {
            inFile = new Scanner(new File(pathName));
        } catch (Exception e) {
            System.out.println("File does not exist: " + pathName + "!\n");
            System.exit(1);
        }

        // Read and sanity-check agent ID.  Needs to be ID_LEN long
        // and numeric.

        if (inFile.hasNext()) {
            vic.agentID = inFile.nextLine();
        } else {
            System.out.println("ERROR:  Agent ID not found!\n");
            System.exit(1);
        }

        if (vic.agentID.length() != ID_LEN) {
            System.out.printf("ERROR:  Agent ID length is %d, must be %d!\n",
                    vic.agentID.length(),ID_LEN);
            System.exit(1);
        }

        try {
            Long.parseLong(vic.agentID);
        } catch (NumberFormatException e) {
            System.out.println("Agent ID `" + vic.agentID 
                    + "contains non-numeric characters!\n");
            System.exit(1);
        }

        // Read and sanity-check date.  Needs to be DATE_LEN long
        // and numeric.

        if (inFile.hasNext()) {
            vic.date = inFile.nextLine();
        } else {
            System.out.println("ERROR:  Date not found!\n");
            System.exit(1);
        }

        if (vic.date.length() != DATE_LEN) {
            System.out.printf("ERROR:  Date length is %d, must be %d!\n",
                    vic.date.length(),DATE_LEN);
            System.exit(1);
        }

        try {
            Long.parseLong(vic.date);
        } catch (NumberFormatException e) {
            System.out.println("Date `" + vic.date 
                    + "contains non-numeric characters!\n");
            System.exit(1);
        }

        // Read and sanity-check phrase.  After removing non-letters,
        // at least PHRASE_LEN letters must remain.

        if (inFile.hasNext()) {
            vic.phraseOriginal = inFile.nextLine();
            StringBuffer sb = new StringBuffer(vic.phraseOriginal);
            for (int i = 0; i < sb.length(); i++) {
                if (!Character.isLetter(sb.charAt(i))) {
                    sb.deleteCharAt(i);
                    i--;  // Don't advance to next index o.w. we miss a char
                }
            }
            vic.phrase = sb.toString().toUpperCase();
            if (vic.phrase.length() >= PHRASE_LEN) {
                vic.phrase = vic.phrase.substring(0,PHRASE_LEN);
            }
        } else {
            System.out.println("ERROR:  Phrase not found!\n");
            System.exit(1);
        }

        if (vic.phrase.length() != PHRASE_LEN) {
            System.out.printf("ERROR:  Phrase contains %d letter(s), "
                    + "must have at least %d!\n",
                    vic.phrase.length(),PHRASE_LEN);
            System.exit(1);
        }

        // Read and sanity-check anagram.  Must be ANAGRAM_LEN long,
        // and contain ANAGRAM_LETTERS letters and the rest spaces.

        if (inFile.hasNext()) {
            vic.anagram = inFile.nextLine().toUpperCase();
        } else {
            System.out.println("ERROR:  Anagram not found!\n");
            System.exit(1);
        }

        if (vic.anagram.length() != ANAGRAM_LEN) {
            System.out.printf("ERROR:  Anagram length is %d, must be %d!\n",
                    vic.anagram.length(),ANAGRAM_LEN);
            System.exit(1);
        }

        for (int i = 0; i < vic.anagram.length(); i++) {
            if (    !Character.isLetter(vic.anagram.charAt(i))
                    && vic.anagram.charAt(i) != SPACE             ) {
                System.out.printf("ERROR:  Anagram contains character `%c'.\n",
                        vic.anagram.charAt(i) );
                System.exit(1);
            }
        }

        int numLetters = 0;
        for (int i = 0; i < vic.anagram.length(); i++) {
            char testChar = vic.anagram.charAt(i);
            //Added anagram restriction
            if (testChar == 'Z' || testChar == 'Q' || testChar == 'X') {
                System.out.printf("ERROR:  Anagram cannot contain Q, Z or X.\n");
                System.exit(1);
            }
            if (Character.isLetter(testChar)) {
                numLetters++;
            }
        }
        if (numLetters != ANAGRAM_LETTERS) {
            System.out.printf("ERROR:  Anagram contains %d letters, "
                    + "should have %d plus %d spaces.\n",
                    numLetters, ANAGRAM_LETTERS,
                    ANAGRAM_LEN - ANAGRAM_LETTERS);
            System.exit(1);
        }

        // Read and sanity-check message.  After removing non-letters
        // and capitalizing, at least one letter must remain.

        if (inFile.hasNext()) {
            vic.messageOriginal = inFile.nextLine();
            StringBuffer sb = new StringBuffer(vic.messageOriginal);
            for (int i = 0; i < sb.length(); i++) {
                if (!Character.isLetter(sb.charAt(i))) {
                    sb.deleteCharAt(i);
                    i--;  // Don't advance to next index o.w. we miss a char
                }
            }
            vic.message = sb.toString().toUpperCase();
        } else {
            System.out.println("ERROR:  Message not found!\n");
            System.exit(1);
        }

        if (vic.message.length() == 0) {
            System.out.printf("ERROR:  Message contains no letters!\n");
            System.exit(1);
        }


        if (DEBUG) {
            System.out.printf("vic.agentID = %s\n",vic.agentID);
            System.out.printf("vic.date = %s\n",vic.date);
            System.out.printf("vic.phrase = %s\n",vic.phrase);
            System.out.printf("vic.anagram = %s\n",vic.anagram);
            System.out.printf("vic.message = %s\n",vic.message);
        }

        return vic;
    }

    /**
     * Read VICData for decryption from a file
     * (only slightly modified from the method above)
     * 
     * @param pathName location of the file to decrypt
     * @return VICData read from the file
     */
    public static VICData readVICDrcryptData (String pathName) {
        VICData vic = new VICData(); // Object to hold the VIC input data
        Scanner inFile = null;       // Scanner file object reference

        try {
            inFile = new Scanner(new File(pathName));
        } catch (Exception e) {
            System.out.println("File does not exist: " + pathName + "!\n");
            System.exit(1);
        }

        //removed agentID from read lines

        // Read and sanity-check date.  Needs to be DATE_LEN long
        // and numeric.

        if (inFile.hasNext()) {
            vic.date = inFile.nextLine();
        } else {
            System.out.println("ERROR:  Date not found!\n");
            System.exit(1);
        }

        if (vic.date.length() != DATE_LEN) {
            System.out.printf("ERROR:  Date length is %d, must be %d!\n",
                    vic.date.length(),DATE_LEN);
            System.exit(1);
        }

        try {
            Long.parseLong(vic.date);
        } catch (NumberFormatException e) {
            System.out.println("Date `" + vic.date 
                    + "contains non-numeric characters!\n");
            System.exit(1);
        }

        // Read and sanity-check phrase.  After removing non-letters,
        // at least PHRASE_LEN letters must remain.

        if (inFile.hasNext()) {
            vic.phraseOriginal = inFile.nextLine();
            StringBuffer sb = new StringBuffer(vic.phraseOriginal);
            for (int i = 0; i < sb.length(); i++) {
                if (!Character.isLetter(sb.charAt(i))) {
                    sb.deleteCharAt(i);
                    i--;  // Don't advance to next index o.w. we miss a char
                }
            }
            vic.phrase = sb.toString().toUpperCase();
            if (vic.phrase.length() >= PHRASE_LEN) {
                vic.phrase = vic.phrase.substring(0,PHRASE_LEN);
            }
        } else {
            System.out.println("ERROR:  Phrase not found!\n");
            System.exit(1);
        }

        if (vic.phrase.length() != PHRASE_LEN) {
            System.out.printf("ERROR:  Phrase contains %d letter(s), "
                    + "must have at least %d!\n",
                    vic.phrase.length(),PHRASE_LEN);
            System.exit(1);
        }

        // Read and sanity-check anagram.  Must be ANAGRAM_LEN long,
        // and contain ANAGRAM_LETTERS letters and the rest spaces.

        if (inFile.hasNext()) {
            vic.anagram = inFile.nextLine().toUpperCase();
        } else {
            System.out.println("ERROR:  Anagram not found!\n");
            System.exit(1);
        }

        if (vic.anagram.length() != ANAGRAM_LEN) {
            System.out.printf("ERROR:  Anagram length is %d, must be %d!\n",
                    vic.anagram.length(),ANAGRAM_LEN);
            System.exit(1);
        }

        for (int i = 0; i < vic.anagram.length(); i++) {
            if (    !Character.isLetter(vic.anagram.charAt(i))
                    && vic.anagram.charAt(i) != SPACE             ) {
                System.out.printf("ERROR:  Anagram contains character `%c'.\n",
                        vic.anagram.charAt(i) );
                System.exit(1);
            }
        }

        int numLetters = 0;
        for (int i = 0; i < vic.anagram.length(); i++) {
            char testChar = vic.anagram.charAt(i);
            
            //Added anagram restriction
            if (testChar == 'Z' || testChar == 'Q' || testChar == 'X') {
                System.out.printf("ERROR:  Anagram cannot contain Q, Z or X.\n");
                System.exit(1);
            }
            if (Character.isLetter(testChar)) {
                numLetters++;
            }
        }
        if (numLetters != ANAGRAM_LETTERS) {
            System.out.printf("ERROR:  Anagram contains %d letters, "
                    + "should have %d plus %d spaces.\n",
                    numLetters, ANAGRAM_LETTERS,
                    ANAGRAM_LEN - ANAGRAM_LETTERS);
            System.exit(1);
        }

        // Read and sanity-check message.  After removing non-letters
        // and capitalizing, at least one letter must remain.

        if (inFile.hasNext()) {
            vic.messageOriginal = inFile.nextLine();
            StringBuffer sb = new StringBuffer(vic.messageOriginal);
            for (int i = 0; i < sb.length(); i++) {
                //now checks for digits instead of characters
                if (!Character.isDigit(sb.charAt(i))) {
                    sb.deleteCharAt(i);
                    i--;  // Don't advance to next index o.w. we miss a char
                }
            }
            vic.message = sb.toString();
        } else {
            System.out.println("ERROR:  Message not found!\n");
            System.exit(1);
        }

        if (vic.message.length() == 0) {
            System.out.printf("ERROR:  Message contains no numbers!\n");
            System.exit(1);
        }

        //extra parse int check
        try {

            //break down string in to 8 digit segments
            int eight_digit_segments = (vic.message.length() / 8) * 8;

            //try to parse all segments
            for (int step = 0; step < eight_digit_segments; step+=8) {
                Integer.parseInt(vic.message.substring(step, step+8));
            }
            Integer.parseInt(vic.message.substring(eight_digit_segments));
        }
        catch (NumberFormatException e) {
            System.out.printf("ERROR:  Message should contain only numbers!\n");
            System.exit(1);
        }


        if (DEBUG) {
            System.out.printf("vic.date = %s\n",vic.date);
            System.out.printf("vic.phrase = %s\n",vic.phrase);
            System.out.printf("vic.anagram = %s\n",vic.anagram);
            System.out.printf("vic.message = %s\n",vic.message);
        }

        return vic;

    }
}

import java.util.ArrayList;

/**
 * VIC operations
 * 
 * @author Phillip Benoit
 *
 */
public class VICOperations {

    /**
     * Adds without carrying 1s
     * 
     * @param x string value of an integer
     * @param y string value of an integer
     * @return string value of the total
     */
    public static String noCarryAddition (String x, String y) {

        //make strings the same length by padding zeroes
        if (x.length() > y.length()) y = padZeroes(y, x.length());
        else if (y.length() > x.length()) x = padZeroes(x, y.length());

        //step from right to left to convert and add
        int step = x.length();
        char[] returnString = new char[step--]; 
        for (; step > -1; step--) returnString[step] = intToChar(((
                charToInt(x.charAt(step)) + charToInt(y.charAt(step)))%10));

        return String.copyValueOf(returnString);
    }

    /**
     * Increases the length of an integer by performing chain addition
     * 
     * @param s string value of the integer to change
     * @param newLength new size for the string
     * @return string value of changed integer
     */
    public static String chainAddition (String s, int newLength) {

        //get original size
        int original = s.length();

        //pad passed string
        s=padZeroes(s, 0 - newLength);

        //load passed string into the return array
        char[] returnString = s.toCharArray();

        //edge case for trimming
        if (newLength == 1 || original > newLength)
            return s.substring(0, newLength);

        //edge case for increasing single digits
        else if (original == 1) {
            returnString[1] = returnString[0];
            original++;
        }

        //sequentially perform the no carry addition
        for (int step = original; step < newLength; step++) returnString[step] =
                noCarryAddition(String.valueOf(returnString[step-original]),
                        String.valueOf(returnString[step-original+1])).charAt(0);

        return String.copyValueOf(returnString);
    }

    /**
     * create a numeric key based on the passed string
     * 
     * @param s base string for the key
     * @return string value of the numeric key
     */
    public static String digitPermutation(String s) {

        //reject strings that are too small
        if (s.length() < VICData.PHRASE_LEN) return null;

        s = s.toUpperCase();
        int counter = 0;
        char[] returnString = new char[VICData.PHRASE_LEN];

        //convert numbers to letters
        if (s.charAt(0) < ':')
            for(char number = '0'; number < ':'; number++)
                s=s.replace(number, (char)(number+17));

        //step through each letter in the alphabet and the string to create the key
        for (char letterStep = 'A'; letterStep <= 'Z' &&
                counter < VICData.PHRASE_LEN; letterStep++)
            for (int stringStep = 0; stringStep < VICData.PHRASE_LEN; stringStep++)
                if (letterStep == s.charAt(stringStep))
                    returnString[stringStep] = intToChar(counter++);

        return String.copyValueOf(returnString);
    }

    /**
     * Create a cypher using a base message and a numeric key
     * 
     * @param anagram base message for the table
     * @param key numeric order for the columns
     * @return list of values for every letter or null on bad input
     */
    public static ArrayList<String> straddlingCheckerboard (String anagram, String key){

        //reject anagrams and keys of improper lengths
        if (anagram.length() != VICData.ANAGRAM_LEN || key.length() !=
                VICData.ANAGRAM_LEN) return null;

        anagram = anagram.toUpperCase();
        String[] return_array = new String[26];
        Integer[] column_order = new Integer[VICData.ANAGRAM_LEN];
        Integer[] row_order = new Integer[VICData.ANAGRAM_LEN];

        //counting variables to check for validity
        int space_counter = 0;
        boolean[] digit_counter = new boolean[10];

        for (int step = 0; step < VICData.ANAGRAM_LEN; step++) {

            //assign and record key values
            column_order[step] = charToInt(key.charAt(step));
            digit_counter[column_order[step]] = true;

            //set anagram into the cypher while counting and recording spaces
            char letter = anagram.charAt(step);
            if (letter == ' ') row_order[space_counter++] = column_order[step];
            else if (return_array[letter - 'A'] == null) return_array[letter - 'A']
                    = column_order[step].toString();

            //reject anagrams that repeat letters
            else return null;
        }

        //reject messages with wrong number of spaces
        if (space_counter != VICData.ANAGRAM_LEN-VICData.ANAGRAM_LETTERS) return null;

        //reject keys that do not use each digit 0-9 only once
        for (boolean value:digit_counter) if (!value) return null;

        //fill cypher rows with unused letters
        int row_counter = 0, column_counter = 0;
        for (int step = 0; step < 26; step++) {
            if (return_array[step] == null) {
                return_array[step] = row_order[row_counter].toString() +
                        column_order[column_counter].toString();

                //increment counters
                if (++column_counter > 9) {
                    column_counter = 0;
                    row_counter++;
                }
            }
        }

        //convert array to arraylist
        ArrayList<String> list = new ArrayList<>();
        for (String element:return_array) list.add(element);

        //add column indexes as last elements
        list.add(row_order[0].toString());
        list.add(row_order[1].toString());

        return list;
    }

    /**
     * Converts a string to upper case and removes the spaces
     * 
     * @param s string to modify
     * @return modified string
     */
    public static String formatString(String s) {
        s = s.toUpperCase();
        StringBuilder returnString = new StringBuilder();
        for (char letter:s.toCharArray()) if (letter >= 'A' && letter <= 'Z')
            returnString.append(letter);
        return returnString.toString();
    }

    /**
     * Encodes a string using a cypher
     * 
     * @param s string to encode
     * @param cypher cypher used to encode the string (letters indexed 0-25)
     * @return encoded message
     */
    public static String encodeString(String s, ArrayList<String> cypher) {
        StringBuilder returnString = new StringBuilder();
        for (char letter:s.toCharArray()) returnString.append(
                cypher.get(letter-'A'));
        return returnString.toString();
    }

    /**
     * Decodes a message using a cypher
     * 
     * @param s message to decode
     * @param cypher cypher used to decode the message
     * @return decoded message
     */
    public static String decodeString(String s, ArrayList<String> cypher) {
        StringBuilder returnString = new StringBuilder();

        //read VIC row indexes
        char first_row = cypher.get(cypher.size()-2).charAt(0),
                second_row = cypher.get(cypher.size()-1).charAt(0);

        for (int step = 0; step < s.length(); step++) {
            String letter = "";

            //test for and read row numbers
            if (s.charAt(step) == first_row || s.charAt(step) == second_row)
                letter = letter + s.charAt(step++);

            //always read next character
            letter = letter + s.charAt(step);

            //decypher
            returnString.append((char)(cypher.indexOf(letter)+'A'));
        }

        return returnString.toString();
    }

    /**
     * Inserts a string in to the middle of another
     * 
     * @param message outer message
     * @param inner inner message
     * @param location where to split outer message
     * @return concatenated message
     */
    public static String insertString(String message, String inner, char location) {
        int index = charToInt(location);
        return message.substring(0,index) + inner + message.substring(index);
    }

    /**
     * Extract agent ID from a message
     * 
     * @param data VICData to modify
     */
    public static void removeString(VICData data) {
        int index = charToInt(data.date.charAt(VICData.DATE_LEN-1));

        //extract ID
        data.agentID = data.message.substring(index, index+VICData.ID_LEN);

        //resize message
        data.message = data.message.substring(0, index) +
                data.message.substring(index+VICData.ID_LEN);
    }

    /**
     * Use VICData to generate a cypher
     * (steps 1-6 from the spec)
     * 
     * @param data VICData to read
     * @return cypher
     */
    public static ArrayList<String> buildCypher(VICData data) {
        String output = VICOperations.noCarryAddition(data.agentID, data.date.substring(0, VICData.DATE_LEN-1));
        output = VICOperations.chainAddition(output, VICData.ANAGRAM_LEN);
        data.phrase = VICOperations.digitPermutation(data.phrase);
        output = VICOperations.noCarryAddition(output, data.phrase);
        output = VICOperations.digitPermutation(output);
        return VICOperations.straddlingCheckerboard(data.anagram, output);
    }

    /**
     * pad strings with zeroes
     * 
     * @param s string to pad
     * @param newLength new string length (use negative numbers for right
     * padding, positive for left)
     * @return padded string
     */
    private static String padZeroes(String s, int newLength) {
        s = String.format("%1$" + newLength + "s", s);
        return s.replace(' ', '0');
    }

    /**
     * convert an integer to its char value
     * 
     * @param i integer to convert
     * @return char value of the integer
     */
    private static char intToChar(int i) {
        return (char)(i+'0');
    }

    /**
     * convert a char to the integer value it represents
     * 
     * @param c char to convert (0-9)
     * @return integer value the char represents
     */
    private static int charToInt(char c) {
        return c - '0';
    }

}

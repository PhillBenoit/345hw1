import java.util.ArrayList;

/**
 * Encrypt VICData
 * 
 * @author Phillip Benoit
 *
 */
public class EncryptVIC {

    /**
     * entry point
     * 
     * @param args command line arguments (used to pass file name)
     */
    public static void main(String[] args) {
        //make sure an argument was passed
        if (args.length == 0) {
            System.err.println("Please pass in a file to read for encryption.");
            System.exit(-1);
        }
        System.out.println(encrypt(VICData.readVICData(args[0])));
    }

    /**
     * Runs all steps for encryption
     * 
     * @param data VICData to encrypt
     * @return encrypted message
     */
    public static String encrypt(VICData data) {
        String output = new String();
        data.phrase = VICOperations.formatString(data.phraseOriginal).substring(0, VICData.PHRASE_LEN);
        data.message = VICOperations.formatString(data.messageOriginal);

        //steps 1-6
        ArrayList<String> cypher = VICOperations.buildCypher(data);

        //steps 7-8
        output = VICOperations.encodeString(data.message, cypher);
        output = VICOperations.insertString(output, data.agentID, data.date.charAt(5));

        return output;        
    }

}

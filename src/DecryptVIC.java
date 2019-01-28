import java.util.ArrayList;

/**
 * Decrypt VICData
 * 
 * @author Phillip Benoit
 *
 */
public class DecryptVIC {

    /**
     * entry point
     * 
     * @param args command line arguments (used to pass file name)
     */
    public static void main(String[] args) {
        //make sure an argument was passed
        if (args.length == 0) {
            System.err.println("Please pass in a file to read for decryption.");
            System.exit(-1);
        }
        System.out.println(decrypt(VICData.readVICDrcryptData(args[0])));
    }

    /**
     * Runs all steps for decryption
     * 
     * @param data VICData to decrypt
     * @return decrypted message
     */
    public static String decrypt(VICData data) {
        String output = new String();
        data.phrase = VICOperations.formatString(data.phraseOriginal).substring(0, VICData.PHRASE_LEN);
        VICOperations.removeString(data);

        ArrayList<String> cypher = VICOperations.buildCypher(data);

        output = VICOperations.decodeString(data.message, cypher);

        return output;        
    }

}

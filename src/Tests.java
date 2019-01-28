import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class Tests {

    public static void main(String[] args) {
        assertEquals("15", VICOperations.noCarryAddition("7", "18"));
        assertEquals("412", VICOperations.noCarryAddition("359","163"));

        assertEquals("640", VICOperations.chainAddition("64", 3));
        assertEquals("76238513", VICOperations.chainAddition("762", 8));
        assertEquals("77415", VICOperations.chainAddition("7", 5));
        assertEquals("54", VICOperations.chainAddition("54321", 2));
        assertEquals("5", VICOperations.chainAddition("54321", 1));

        assertNull(VICOperations.digitPermutation("hi"));
        assertEquals("4071826395", VICOperations.digitPermutation("BANANALAND"));
        assertEquals("6704821539", VICOperations.digitPermutation("STARTEARLY"));

        ArrayList<String> test_list;

        //improper length
        test_list = VICOperations.straddlingCheckerboard("a", "4071826395");
        assertNull(test_list);

        test_list = VICOperations.straddlingCheckerboard("a tin shoe", "4");
        assertNull(test_list);

        test_list = VICOperations.straddlingCheckerboard("a tin shoes", "4071826395");
        assertNull(test_list);

        test_list = VICOperations.straddlingCheckerboard("a tin shoe", "444071826395");
        assertNull(test_list);

        //repeat numbers
        test_list = VICOperations.straddlingCheckerboard("a tin shoe", "1111111111");
        assertNull(test_list);

        //repeat letters
        test_list = VICOperations.straddlingCheckerboard("aaa aa aaa", "4071826395");
        assertNull(test_list);

        //improper number of spaces
        test_list = VICOperations.straddlingCheckerboard("a t n shoe", "4071826395");
        assertNull(test_list);

        test_list = VICOperations.straddlingCheckerboard("a tinshoes", "4071826395");
        assertNull(test_list);

        //full list test
        test_list = VICOperations.straddlingCheckerboard("a tin shoe", "4071826395");
        assertEquals("4", test_list.get(0));
        assertEquals("04", test_list.get(1));
        assertEquals("00", test_list.get(2));
        assertEquals("07", test_list.get(3));
        assertEquals("5", test_list.get(4));
        assertEquals("01", test_list.get(5));
        assertEquals("08", test_list.get(6));
        assertEquals("3", test_list.get(7));
        assertEquals("1", test_list.get(8));
        assertEquals("02", test_list.get(9));
        assertEquals("06", test_list.get(10));
        assertEquals("03", test_list.get(11));
        assertEquals("09", test_list.get(12));
        assertEquals("8", test_list.get(13));
        assertEquals("9", test_list.get(14));
        assertEquals("05", test_list.get(15));
        assertEquals("24", test_list.get(16));
        assertEquals("20", test_list.get(17));
        assertEquals("6", test_list.get(18));
        assertEquals("7", test_list.get(19));
        assertEquals("27", test_list.get(20));
        assertEquals("21", test_list.get(21));
        assertEquals("28", test_list.get(22));
        assertEquals("22", test_list.get(23));
        assertEquals("26", test_list.get(24));
        assertEquals("23", test_list.get(25));

        VICData data = new VICData();

        data.agentID = "85721";
        data.date = "470918";
        data.phraseOriginal = "Choose the representation that best supports the operations";
        data.anagram = "heat is on";
        data.messageOriginal = "Run away!";
        assertEquals("3532423085721239", EncryptVIC.encrypt(data));

        data.agentID = "73003";
        data.date = "450508";
        data.phraseOriginal = "Don't do drugs";
        data.anagram = "A ROSE TIN";
        data.messageOriginal = "Send money.";
        assertEquals("67835384730038700", EncryptVIC.encrypt(data));

        data.date = "470918";
        data.phraseOriginal = "Choose the representation that best supports the operations";
        data.anagram = "heat is on";
        data.message = "815052098572156920365";
        assertEquals("THECAKEISALIE", DecryptVIC.decrypt(data));

        data.message = "62072357857210178";
        assertEquals("IAMAROBOT", DecryptVIC.decrypt(data));

        System.out.println("Tests Passed");
    }

}

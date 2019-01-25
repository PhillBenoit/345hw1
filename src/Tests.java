import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public static void main(String[] args) {
        assertEquals("15", VICOperations.noCarryAddition("7", "18"));
        assertEquals("412", VICOperations.noCarryAddition("359","163"));
        
        assertEquals("640", VICOperations.chainAddition("64", 3));
        assertEquals("76238513", VICOperations.chainAddition("762", 8));
        assertEquals("77415", VICOperations.chainAddition("7", 5));
        
        System.out.println("Tests Passed");
    }

}

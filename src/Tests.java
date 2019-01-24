import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public static void main(String[] args) {
        assertEquals("15", VICOperations.noCarryAddition("7", "18"));
        assertEquals("412", VICOperations.noCarryAddition("359","163"));
    }

}

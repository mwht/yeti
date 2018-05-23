package ovh.spajste;

import org.junit.jupiter.api.Test;
import ovh.spajste.yeti.ELMInterface;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ELMInterfaceTest {
    @Test
    public void testArrayConversion() {
        byte[] exp = {0x41,0x0C,0x0D,0x39};
        assertArrayEquals(exp,ELMInterface.convertELMdataToByteArray("41 0C 0D 39"));
    }
}

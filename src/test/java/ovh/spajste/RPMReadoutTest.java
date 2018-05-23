package ovh.spajste;

import org.junit.jupiter.api.Test;
import ovh.spajste.yeti.RPMReadout;

//import static org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RPMReadoutTest {
    @Test
    void readoutInputTest() {
        RPMReadout rpm = new RPMReadout();
        byte[] testInput = {
                (byte) 0x0B,
                (byte) 0xCC
        };
        rpm.setReadoutBuffer(testInput);
        try {
            assertEquals(755, rpm.getValue());
        } catch(Exception e) {
            fail(e);
        }
    }
}

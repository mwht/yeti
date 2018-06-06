package ovh.spajste;

import org.junit.jupiter.api.Test;
import ovh.spajste.yeti.Initialization;
import ovh.spajste.yeti.InitializationException;
import ovh.spajste.yeti.PortNotOpenException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtractPIDTest {
    Initialization init;

    @Test
    public void extractPIDtest() {
        init = new Initialization(null,null) {
            @Override
            public void reciveProtocolSelectAnswer() throws InitializationException, PortNotOpenException {

            }

            @Override
            public void reciveInitailizationAnswer() throws InitializationException, PortNotOpenException {

            }
        };
        byte[] samplePIDs = {
                (byte) 0x80,
                (byte) 0x10,
                (byte) 0x00,
                (byte) 0x00
        };
        init.extractAvailablePIDS(samplePIDs);
        assertEquals(2,init.availablePIDS.size());
        assertEquals(Byte.valueOf((byte) 1),(Byte) init.availablePIDS.get(0));
        assertEquals(Byte.valueOf((byte) 12),(Byte) init.availablePIDS.get(1));

    }
}

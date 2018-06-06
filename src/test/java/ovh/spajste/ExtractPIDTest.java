package ovh.spajste;

import org.junit.jupiter.api.Test;
import ovh.spajste.yeti.Initialization;
import ovh.spajste.yeti.InitializationException;
import ovh.spajste.yeti.PortNotOpenException;
import ovh.spajste.yeti.Readouts;

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
        // BE 3E B8 11 - fiesta wiki
        byte[] samplePIDs = {
                (byte) 0xBE,
                (byte) 0x3E,
                (byte) 0xB8,
                (byte) 0x11
        };
        init.extractAvailablePIDS(samplePIDs);
        /*assertEquals(2,init.availablePIDS.size());
        assertEquals(Byte.valueOf((byte) 1),(Byte) init.availablePIDS.get(0));
        assertEquals(Byte.valueOf((byte) 12),(Byte) init.availablePIDS.get(1));*/
        Readouts.init();
        init.availablePIDS.forEach((pid) -> {
            if(Readouts.readoutMap.containsKey(pid.byteValue())) {
                System.out.println(pid.intValue() + " - " + Readouts.readoutMap.get(pid).getSimpleName());
            }
        });

    }
}

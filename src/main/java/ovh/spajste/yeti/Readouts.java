package ovh.spajste.yeti;

import java.util.HashMap;
import java.util.Map;

public class Readouts {
    public static Map<Byte,Class<?>> readoutMap;

    public Readouts() {

    }

    public static void init() {
        readoutMap = new HashMap<>();
        readoutMap.put((byte) 0x0C,RPMReadout.class);
    }
}

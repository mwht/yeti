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
        readoutMap.put((byte) 0x04,CalculatedEngineLoadReadout.class);
        readoutMap.put((byte) 0x05,EngineCoolantTemperatureReadout.class);
        readoutMap.put((byte) 0x0F,IntakeAirTemperatureReadout.class);
        readoutMap.put((byte) 0x0B,IntakeMainfoldAbsolutePressureReadout.class);
        readoutMap.put((byte) 0x07,LongTermFuelTrimBank1Readout.class);
        readoutMap.put((byte) 0x06,ShortTermFuelTrimBank1Readout.class);
        readoutMap.put((byte) 0x11,ThrottlePositionReadout.class);
        readoutMap.put((byte) 0x0E,TimingAdvanceReadout.class);
        readoutMap.put((byte) 0x0D,VehicleSpeedReadout.class);
    }
}

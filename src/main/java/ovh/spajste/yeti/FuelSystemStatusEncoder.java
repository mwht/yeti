package ovh.spajste.yeti;

/**
 * @author Michał Łukiański and Jakub Politowski
 */

public class FuelSystemStatusEncoder{
    private static byte[] bytesToEncode;

        FuelSystemStatusEncoder(byte[] bytesToEncode)
        {
            bytesToEncode=bytesToEncode;
        }

        public static String encode(){
            try{
                if(bytesToEncode[0]==bytesToEncode[1]){
                    switch (bytesToEncode[0]){
                        case 0x01: return "Open loop due to insufficient engine temperature";
                        case 0x02: return "Closed loop, using oxygen sensor feedback to determine fuel mix";
                        case 0x04: return "Open loop due to engine load OR fuel cut due to deceleration";
                        case 0x08: return "Open loop due to system failure";
                        case 0x16: return "Closed loop, using at least one oxygen sensor but there is a fault in the feedback system";
                        default : return "Unknown Fuel System Status";
                    }
                 }
            }
            catch (IndexOutOfBoundsException e){
                    switch (bytesToEncode[0]){
                        case 0x01: return "Open loop due to insufficient engine temperature";
                        case 0x02: return "Closed loop, using oxygen sensor feedback to determine fuel mix";
                        case 0x04: return "Open loop due to engine load OR fuel cut due to deceleration";
                        case 0x08: return "Open loop due to system failure";
                        case 0x16: return "Closed loop, using at least one oxygen sensor but there is a fault in the feedback system";
                        default : return "Unknown Fuel System Status";
                    }
            }
            catch (NullPointerException e){
                throw new NullPointerException("Incorected receive data");
            }
            return "Unknown Fuel System Status";
        }
}
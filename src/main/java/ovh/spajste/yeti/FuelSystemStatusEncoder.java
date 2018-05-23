package ovh.spajste.yeti;

/**
 * @author Michał Łukiański and Jakub Politowski
 */

public static class FuelSystemStatusEncoder{
    private byte[] bytesToEncode;

        FuelSystemStatusEncoder(byte[] bytesToEncode)
        {
            this.bytesToEncode=bytesToEncode;
        }

        public static String encode(){
            try{
                if(bytesToEncode[0]==bytesToEncode[1]){
                    switch (bytesToEncode[0]){
                        case 0x01: return "Open loop due to insufficient engine temperature"; break;
                        case 0x02: return "Closed loop, using oxygen sensor feedback to determine fuel mix"; break;
                        case 0x04: return "Open loop due to engine load OR fuel cut due to deceleration"; break;
                        case 0x08: return "Open loop due to system failure"; break;
                        case 0x16: return "Closed loop, using at least one oxygen sensor but there is a fault in the feedback system"; break;
                        default : return "Unknown Fuel System Status"; break;
                    }
                 }
            }
            catch (IndexOutOfBoundsException e){
                    switch (bytesToEncode[0]){
                        case 0x01: return "Open loop due to insufficient engine temperature"; break;
                        case 0x02: return "Closed loop, using oxygen sensor feedback to determine fuel mix"; break;
                        case 0x04: return "Open loop due to engine load OR fuel cut due to deceleration"; break;
                        case 0x08: return "Open loop due to system failure"; break;
                        case 0x16: return "Closed loop, using at least one oxygen sensor but there is a fault in the feedback system"; break;
                        default : return "Unknown Fuel System Status"; break;
                    }
            }
            catch (NullPointerException e){
                throw new NullPointerException("Incorected receive data", e);
            }
        }
}
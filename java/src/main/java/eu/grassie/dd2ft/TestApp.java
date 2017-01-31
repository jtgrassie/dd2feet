package eu.grassie.dd2ft;

import org.apache.log4j.Logger;
import org.apache.commons.codec.binary.Hex;

public class TestApp
{
    private static final Logger log = Logger.getLogger(TestApp.class);

    public static void main(String[] args)
    {
        LocationDegrees in_dd = new LocationDegrees(40.730610, -73.935242);
        log.info("NYC " + in_dd);

        LocationFeet out_ft = LocationConverter.toFeet(in_dd);
        String packed = Hex.encodeHexString(out_ft.getPacked()).toUpperCase();
        log.info(out_ft);
        log.info("Packed bytes: " + packed);

        LocationDegrees out_dd = LocationConverter.toDegrees(out_ft);
        log.info(out_dd);

        LocationFeet newft = new LocationFeet(out_ft.getPacked());
        log.info("New from packed " + newft);
    }
}

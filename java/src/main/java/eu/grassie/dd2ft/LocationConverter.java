package eu.grassie.dd2ft;

import org.apache.log4j.Logger;

public class LocationConverter
{
    private static final Logger log = Logger.getLogger(LocationConverter.class);

    // Store to nearest x feet
    private static final int RESOLUTION = 6;

    // Circumference of earth in feet
    private static final double CIRC_EARTH_FT = 131479777.0;

    // Feet to one degree at equator
    private static final double DEG_FT = 365222.0;

    // PI / 180
    private static final double PI180 = 0.01745329251994;

    private static double toRadians(double degrees)
    {
        return degrees * PI180;
    }
    
    public LocationConverter()
    {
    }

    public static LocationFeet toFeet(LocationDegrees degrees)
    {
        double rad = toRadians(degrees.getLatitude());
        double ft2deg = Math.cos( rad ) * DEG_FT;
        int longitude = (int)(ft2deg * degrees.getLongitude());
        if ( degrees.getLongitude() < 0 )
        {
            longitude = (int)(ft2deg * (360.0 + degrees.getLongitude()));
        }

        int latitude = (int)(degrees.getLatitude() * DEG_FT);
        if ( degrees.getLatitude() < 0 )
        {
            latitude = (int)((360.0 - degrees.getLatitude()) * DEG_FT);
        }

        longitude = Math.round( longitude / RESOLUTION );
        latitude = Math.round( latitude / RESOLUTION );

        return new LocationFeet(latitude, longitude);
    }

    public static LocationDegrees toDegrees(LocationFeet feet)
    {
        double latitude = (feet.getLatitude() * RESOLUTION) / DEG_FT;
        if ( latitude > 180 )
        {
            latitude = 360 - latitude;
        }

        double rad = toRadians(latitude);
        double longitude = (feet.getLongitude() * RESOLUTION) / (Math.cos( rad ) * DEG_FT);
        if ( longitude > 180 )
        {
            longitude = longitude - 360;
        }

        return new LocationDegrees(latitude, longitude);
    }
}

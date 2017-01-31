package eu.grassie.dd2ft;

import org.apache.log4j.Logger;

public class LocationDegrees
{
    private static final Logger log = Logger.getLogger(LocationDegrees.class);
    
    private double latitude;
    private double longitude;

    public LocationDegrees()
    {
    }

    public LocationDegrees(LocationFeet feet)
    {
        LocationDegrees temp = LocationConverter.toDegrees(feet);
        this.latitude = temp.getLatitude();
        this.longitude = temp.getLongitude();
    }

    public LocationDegrees(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public String toString()
    {
        return String.format("LocationDegrees: %f, %f", latitude, longitude);
    }
}

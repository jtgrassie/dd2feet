package eu.grassie.dd2ft;

import org.apache.log4j.Logger;

public class LocationFeet
{
    private static final Logger log = Logger.getLogger(LocationFeet.class);

    private int latitude;
    private int longitude;
    private byte[] packed = new byte[6];

    public LocationFeet()
    {
    }

    public LocationFeet(LocationDegrees degrees)
    {
        LocationFeet temp = LocationConverter.toFeet(degrees);
        this.latitude = temp.getLatitude();
        this.longitude = temp.getLongitude();
        this.packed = temp.getPacked();
    }

    public LocationFeet(int latitude, int longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        pack();
    }

    public LocationFeet(byte[] packed)
    {
        this.packed = packed;
        unpack();
    }

    public void setLatitude(int latitude)
    {
        this.latitude = latitude;
        pack();
    }

    public int getLatitude()
    {
        return this.latitude;
    }

    public void setLongitude(int longitude)
    {
        this.longitude = longitude;
        pack();
    }

    public int getLongitude()
    {
        return this.longitude;
    }

    public void setPacked(byte[] packed)
    {
        this.packed = packed;
        unpack();
    }

    public byte[] getPacked()
    {
        return this.packed;
    }

    private void unpack()
    {
        latitude   = (0xFF & packed[0]) << 16;
        latitude  += (0xFF & packed[1]) << 8;
        latitude  += (0xFF & packed[2]);
        longitude  = (0xFF & packed[3]) << 16;
        longitude += (0xFF & packed[4]) << 8;
        longitude += (0xFF & packed[5]);
    }

    private void pack()
    {
        packed[0] = (byte)((latitude  & 0x00FF0000) >> 16);
        packed[1] = (byte)((latitude  & 0x0000FF00) >> 8);
        packed[2] = (byte)(latitude   & 0x000000FF);
        packed[3] = (byte)((longitude & 0x00FF0000) >> 16);
        packed[4] = (byte)((longitude & 0x0000FF00) >> 8);
        packed[5] = (byte)(longitude  & 0x000000FF);
    }

    public String toString()
    {
        return String.format("LocationFeet: %d, %d", latitude, longitude);
    }
}

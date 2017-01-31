#include "dd2ft.h"
#include <stdio.h>
#include <math.h>

void convert_to_feet(location_dd *dd, location_ft *ft)
{
    float rad = RADIANS(dd->latitude);
    float ft2deg = cosf( rad ) * DEG_FT;
    ft->longitude = ft2deg * dd->longitude;
    if ( dd->longitude < 0 )
    {
        ft->longitude = ft2deg * (360.0f + dd->longitude);
    }

    ft->latitude = dd->latitude * DEG_FT;
    if ( dd->latitude < 0 )
    {
        ft->latitude = (360.0f - dd->latitude) * DEG_FT;
    }

    ft->longitude = round( ft->longitude / RESOLUTION );
    ft->latitude = round( ft->latitude / RESOLUTION );

    // pack into 6 bytes
    ft->packed[0] = (ft->latitude  & 0x00FF0000) >> 16;
    ft->packed[1] = (ft->latitude  & 0x0000FF00) >> 8;
    ft->packed[2] = ft->latitude   & 0x000000FF;
    ft->packed[3] = (ft->longitude & 0x00FF0000) >> 16;
    ft->packed[4] = (ft->longitude & 0x0000FF00) >> 8;
    ft->packed[5] = ft->longitude  & 0x000000FF;
}

void convert_to_decimal(location_ft *ft, location_dd *dd)
{
    dd->latitude = (ft->latitude * RESOLUTION) / DEG_FT;
    if ( dd->latitude > 180 )
    {
        dd->latitude = 360 - dd->latitude;
    }

    float rad = RADIANS(dd->latitude);
    dd->longitude = (ft->longitude * RESOLUTION) / (cosf( rad ) * DEG_FT);
    if ( dd->longitude > 180 )
    {
        dd->longitude = dd->longitude - 360;
    }
}

void print_packed(location_ft *ft)
{
    uint32_t lat;
    uint32_t lon;

    lat = ft->packed[0] << 16;
    lat |= ft->packed[1] << 8;
    lat |= ft->packed[2];

    lon = ft->packed[3] << 16;
    lon |= ft->packed[4] << 8;
    lon |= ft->packed[5];

    printf("Un-packed feet: %9d, %d\n", lat, lon);
}

void print(location_ft *ft)
{
    printf("Feet: %19d, %d\n", ft->latitude, ft->longitude);
}

void print(location_dd *dd)
{
    printf("Decimal degrees: %10f, %f\n", dd->latitude, dd->longitude);
}


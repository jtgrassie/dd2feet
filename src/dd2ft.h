#ifndef __DD2FT_H
#define __DD2FT_H

#include <stdint.h>

// Store to nearest x feet
#define RESOLUTION 6

// Circumference of earth in feet
#define CIRC_EARTH_FT 131479777.0

// Feet to one degree at equator
#define DEG_FT 365222.0

// PI / 180
#define PI180 0.01745329251994

// Degrees to radians
#define RADIANS(x)  (x * PI180)

typedef struct location_dd
{
    float longitude;
    float latitude;
} location_dd;

typedef struct location_ft
{
    uint32_t longitude;
    uint32_t latitude;
    uint8_t packed[6];
} location_ft;

void convert_to_feet(location_dd *dd, location_ft *ft);
void convert_to_decimal(location_ft *ft, location_dd *dd);
void print_packed(location_ft *ft);
void print(location_ft *ft);
void print(location_dd *dd);

#endif

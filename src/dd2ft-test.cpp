#include "dd2ft.h"
#include <iostream>
#include <string>
#include <regex>
#include <cmath>

int main(int argc, char *argv[])
{
    location_dd in_dd;
    location_ft out_ft;

    if(argc == 2)
    {
        std::string latlon = argv[1];
        std::cout << "Input: " << latlon << std::endl;

        if(!std::regex_match (latlon, std::regex("-?[[:digit:]]+,-?[[:digit:]]+") ))
        {
            std::cout << "Invalid decimal degrees latitude,logitude. Exiting." << std::endl;
            return 1;
        }
        
        int split_at = latlon.find(",");
        in_dd.latitude = stof(latlon.substr(0, split_at));
        in_dd.longitude = stof(latlon.substr(split_at+1));

        if(fabs(in_dd.latitude) >= 90.0f || fabs(in_dd.longitude) >= 180.0f)
        {
            std::cout << "Invalid decimal degrees latitude,logitude. Exiting." << std::endl;
            return 2;
        }
    }
    else
    {
        // NYC = Lat 40.730610 Lon -73.935242
        std::cout << "No coordinates supplied so using NYC..." << std::endl;
        in_dd.latitude = 40.730610;
        in_dd.longitude = -73.935242;
    }

    print(&in_dd);

    convert_to_feet(&in_dd, &out_ft);
    print(&out_ft);
    print_packed(&out_ft);

    convert_to_decimal(&out_ft, &in_dd);
    print(&in_dd);

    return 0;
}


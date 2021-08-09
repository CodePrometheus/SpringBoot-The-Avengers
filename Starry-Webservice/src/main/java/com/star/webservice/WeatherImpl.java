package com.star.webservice;

/**
 * @Author: zzStar
 * @Date: 08-09-2021 21:22
 */

import javax.jws.WebService;

@WebService
public class WeatherImpl implements Weather {

    @Override
    public String queryWeather(String cityName) {
        System.out.println("cityName = " + cityName);
        String weather = "rain";
        return weather;
    }
}

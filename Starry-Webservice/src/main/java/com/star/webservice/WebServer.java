package com.star.webservice;

import javax.xml.ws.Endpoint;

/**
 * @Author: zzStar
 * @Date: 08-09-2021 21:22
 */
public class WebServer {

    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:8989/weather",
                new WeatherImpl());
    }
}

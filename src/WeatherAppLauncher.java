// Program: WeatherAppLauncher.java
// Author: T-Kalv
// Module:
// Email:
// Student Number:
// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// About:
// Basic Weather App that shows the user the current weather in a certain city with other useful information/metrics using a Weather Forecast API and the Swing framework

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Requirements
// - Implement a basic CLI App that fetches weather data from a Weather Forecast API
// - Implement a simple GUI App that consists of a user search field that when a city is entered display the current temperature
// - Show a weather condition icon based on current weather condition in a city and this changes depending on current weather by fetching this data from Weather Forecast API
// - Show humidity level on selected city fetching the data from the Weather Forecast API
// - Show wind speed level mph on selected city fetching the data from the Weather Forecast API
// - Show different UI background based on time of day such as daytime, sunrise, sunset, night...
// - Implement a modern UI design for ease of use for the user

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Code
import javax.swing.*;

public class WeatherAppLauncher
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new WeatherAppInterface().setVisible(true);
//                System.out.println(WeatherApp.getLocationData("London"));
//                System.out.println(WeatherApp.getCurrentTime());

            }
        });
    }
}

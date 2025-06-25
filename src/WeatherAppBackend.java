// Program: WeatherAppBackend.java
// Author: T-Kalv
// Module:
// Email:
// Student Number:
// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// About:

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Requirements

// -------------------------------------------------------------------------------------------------------------------------------------------------------------
// Code
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;


public class WeatherAppBackend
{
    public static JSONObject getWeatherData(String locationName)
    {
        JSONArray locationData = fetchLocationData(locationName);
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        String APIUrlString = "https://api.open-meteo.com/v1/forecast?" + "latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relativehumidity_2m,weather_code,windspeed_10m" + "&daily=temperature_2m_max,temperature_2m_min,weathercode" + "&timezone=auto" + "&forecast_days=7";
        try
        {
            HttpURLConnection connection = fetchApiResponse(APIUrlString);
            //HTTP Status Codes => 200, 400, 500 good -> bad
            if(connection.getResponseCode() != 200)
            {
                System.out.println("Error Fetching Weather Data via API");
                return null;
            }
            else
            {
                StringBuilder APIDataResult = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext())
                {
                    APIDataResult.append(scanner.nextLine());
                }
                scanner.close();
                connection.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJSONObject = (JSONObject) parser.parse(String.valueOf(APIDataResult));
                JSONObject hourData = (JSONObject) resultJSONObject.get("hourly");
                JSONArray time = (JSONArray) hourData.get("time");
                int timeIndex = currentTimeIndex(time);
                JSONArray temperatureData = (JSONArray) hourData.get("temperature_2m");
                double currentTemperature = (double) temperatureData.get(timeIndex);
                JSONArray weather_code = (JSONArray) hourData.get("weather_code");
                String weatherCondition = convertWeatherCode((long) weather_code.get(timeIndex));
                JSONArray currentRelativeHumidity = (JSONArray) hourData.get("relativehumidity_2m");
                long humidity = (long) currentRelativeHumidity.get(timeIndex);
                JSONArray windspeedData = (JSONArray) hourData.get("windspeed_10m");
                double currentWindspeed = (double) windspeedData.get(timeIndex);
                double convertedCurrentWindspeed = currentWindspeed * 0.621371;
                JSONObject weatherData = new JSONObject();
                weatherData.put("currentTemperature", currentTemperature);
                weatherData.put("weather_condition", weatherCondition);
                weatherData.put("humidity", humidity);
                weatherData.put("currentWindspeed", convertedCurrentWindspeed);
                return weatherData;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray fetchLocationData(String locationName)
    {
        locationName = locationName.replaceAll(" ", "+");
        String APIurlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";
        try
        {
            HttpURLConnection conn = fetchApiResponse(APIurlString);
            //HTTP Status Codes => 200, 400, 500 good -> bad
            if(conn.getResponseCode() != 200)
            {
                System.out.println("Error Fetching Weather Data via API!");
                return null;
            }
            else
            {
                StringBuilder APIDataResult = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while(scanner.hasNext())
                {
                    APIDataResult.append(scanner.nextLine());
                }
                scanner.close();
                conn.disconnect();
                JSONParser dataParser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) dataParser.parse(String.valueOf(APIDataResult));
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString)
    {
        try
        {
            URL APIurl = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) APIurl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static int currentTimeIndex(JSONArray timeList)
    {
        String currentSysTime = getCurrentTime();
        int i = 0;
        while(i < timeList.size())
        {
            String time = (String) timeList.get(i);
            if(time.equals(currentSysTime))
            {
                return i;
            }
            i = i + 1;
        }
        return 0;
    }


    private static String getCurrentTime()
    {
        LocalDateTime currentTimeDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String newDateTime = currentTimeDate.format(formatter);
        return newDateTime;
    }

    private static String convertWeatherCode(long weather_code)
    {
        String weatherInfo = "";
        if(weather_code == 0L)
        {
            weatherInfo = "Clear";
        }
        else if(weather_code > 0L && weather_code <= 3L)
        {
            weatherInfo = "Cloudy";
        }
        else if(weather_code >= 51L && weather_code <= 67L)
        {
            weatherInfo = "Rain";
        }
        else if(weather_code >= 80L && weather_code <= 99L)
        {
            weatherInfo = "Rain";
        }
        else if(weather_code >= 71L && weather_code <= 77L){
            weatherInfo = "Snow";
        }
        else
        {
            weatherInfo = "No Weather Data Fount!";
        }

        return weatherInfo;
    }
}








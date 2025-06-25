// Program: WeatherAppInterface.java
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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import org.json.simple.JSONObject;

public class WeatherAppInterface extends JFrame
{
    private JTextField userSearchField;
    private JLabel temperatureTextField;
    private JLabel weatherConditionIcon;
    private JLabel weatherInformation;
    private JLabel humidityInformation;
    private JLabel windspeedInformation;

    public WeatherAppInterface()
    {
        super("What's The Weather?");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        backgroundColour();
        addGUIComponents();
        setVisible(true);
    }

    private void backgroundColour()
    {
        LocalTime currentTime = LocalTime.now();
        Color backgroundColour;
        LocalTime sunriseTime = LocalTime.of(6, 0);
        LocalTime sunsetTime = LocalTime.of(18, 0);
        if (currentTime.isAfter(sunriseTime.minusSeconds(1)) && currentTime.isBefore(sunsetTime.plusSeconds(1)))
        {
            backgroundColour = Color.decode("#4071b4");//daytime blue
        }
        else if (currentTime.isAfter(sunsetTime.minusHours(1)) && currentTime.isBefore(sunsetTime.plusHours(1)))
        {
            backgroundColour = Color.decode("#ac858a");//sunset/sunrise
        }
        else
        {
            backgroundColour = Color.decode("#39414c");//night
        }
        getContentPane().setBackground(backgroundColour);
    }

    private void addGUIComponents()
    {
        userSearchField = new JTextField("Default City");
        userSearchField.setBounds(15, 15, 351, 45);
        userSearchField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(userSearchField);

        JButton userSearchButton = new JButton(loadImage("src/assets/search.png"));
        userSearchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userSearchButton.setBounds(375, 14, 47, 45);
        userSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentCity = userSearchField.getText().trim();
                if (currentCity.isEmpty()) return;
                JSONObject w = WeatherAppBackend.getWeatherData(currentCity);
                String weatherCond = (String) w.get("weather_condition");
                switch (weatherCond)
                {
                    case "Sunny":
                        weatherConditionIcon.setIcon(loadImage("src/assets/sunny.png"));
                        break;
                    case "Cloudy":
                        weatherConditionIcon.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionIcon.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionIcon.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }
                weatherConditionIcon.setBounds(0, 125, 450, 217);

                double temp = (double) w.get("currentTemperature");
                temperatureTextField.setText(String.format("%.1f °C", temp));
                temperatureTextField.setForeground(Color.WHITE);
                temperatureTextField.setHorizontalAlignment(SwingConstants.CENTER);
                temperatureTextField.setBounds(0, 360, 450, 50);

                weatherInformation.setText(weatherCond);
                weatherInformation.setFont(new Font("Segoe UI", Font.BOLD, 28));
                weatherInformation.setForeground(Color.WHITE);
                weatherInformation.setHorizontalAlignment(SwingConstants.CENTER);
                weatherInformation.setBounds(0, 420, 450, 36);

                long hum = (long) w.get("humidity");
                humidityInformation.setText(String.format("<html><b>Humidity: </b> %d%%</html>", hum));
                humidityInformation.setForeground(Color.WHITE);
                humidityInformation.setBounds(50, 480, 150, 55);
                humidityInformation.setHorizontalAlignment(SwingConstants.LEFT);

                double windspeed = (double) w.get("currentWindspeed");
                double mph = windspeed * 0.621371;
                windspeedInformation.setText(String.format("<html><b>Windspeed: </b> %.1f mph</html>", mph));
                windspeedInformation.setForeground(Color.WHITE);
                windspeedInformation.setBounds(250, 480, 150, 55);
                windspeedInformation.setHorizontalAlignment(SwingConstants.LEFT);
            }
        });
        add(userSearchButton);

        weatherConditionIcon = new JLabel(loadImage("src/assets/sunny.png"));
        weatherConditionIcon.setBounds(0, 125, 450, 217);
        add(weatherConditionIcon);

        SquircleSection iconSquircleSection = new SquircleSection();
        iconSquircleSection.setBounds(75, 125, 300, 217);
        add(iconSquircleSection);

        temperatureTextField = new JLabel("12.0 °C");
        temperatureTextField.setFont(new Font("Segoe UI", Font.BOLD, 35));
        temperatureTextField.setForeground(Color.WHITE);
        temperatureTextField.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureTextField.setBounds(0, 360, 450, 50);
        add(temperatureTextField);

        weatherInformation = new JLabel("Sunny");
        weatherInformation.setFont(new Font("Segoe UI", Font.BOLD, 28));
        weatherInformation.setForeground(Color.WHITE);
        weatherInformation.setHorizontalAlignment(SwingConstants.CENTER);
        weatherInformation.setBounds(0, 420, 450, 36);
        add(weatherInformation);

        SquircleSection infoSquircleSection = new SquircleSection();
        infoSquircleSection.setBounds(125, 360, 200, 100);
        add(infoSquircleSection);

        humidityInformation = new JLabel("<html><b>Humidity: </b> 100%</html>");
        humidityInformation.setForeground(Color.WHITE);
        humidityInformation.setBounds(50, 480, 150, 55);
        add(humidityInformation);

        windspeedInformation = new JLabel("<html><b>Wind Speed: </b> 5.0 mph</html>");
        windspeedInformation.setForeground(Color.WHITE);
        windspeedInformation.setBounds(250, 480, 150, 55);
        add(windspeedInformation);
    }

    private class SquircleSection extends JPanel
    {
        private final Color squircleBackgroundColour = Color.decode("#111d29");
        public SquircleSection()
        {
            setBackground(squircleBackgroundColour);
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(squircleBackgroundColour);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int squircleWidth = getWidth();
            int squircleHeight = getHeight();
            int squircleArc = 50;
            g2d.fillRoundRect(0, 0, squircleWidth, squircleHeight, squircleArc, squircleArc);
        }
    }

    private ImageIcon loadImage(String path)
    {
        ImageIcon icon = null;
        try
        {
            icon = new ImageIcon(path);
        }
        catch (Exception e)
        {
            System.err.println("Cannot Load Image From: " + path);
        }
        return icon;
    }
}
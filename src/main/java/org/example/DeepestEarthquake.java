package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeepestEarthquake {

    public static void main(String[] args) {
        try {
            String url = "jdbc:sqlite:test.db";
            Connection connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("Connected to the database");

                String query = "SELECT Штат FROM data WHERE strftime('%Y', \"Время\") = '2013' " +
                        "ORDER BY CAST(REPLACE(\"Глубина в метрах\", ',', '.') AS DECIMAL) DESC LIMIT 1";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String state = resultSet.getString("Штат");
                    System.out.println("Штат с самым глубоким землетрясением в 2013: " + state);
                } else {
                    System.out.println("Не найдены данные за 2013");
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();
            } else {
                System.out.println("Failed to connect to the database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

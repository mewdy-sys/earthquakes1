package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AverageMagnitude {

    public static void main(String[] args) {
        try {
            String url = "jdbc:sqlite:test.db";
            Connection connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("Connected to the database");

                String query = "SELECT AVG(Magnitude) AS average_magnitude " +
                        "FROM Earthquakes e " +
                        "INNER JOIN States s ON e.StateID = s.StateID " +
                        "WHERE s.StateName = 'West Virginia'";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double averageMagnitude = resultSet.getDouble("average_magnitude");
                    System.out.println("Средняя магнитуда в Западной Вирджинии: " + averageMagnitude);
                } else {
                    System.out.println("Не найдены данные о Западной Вирджинии");
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
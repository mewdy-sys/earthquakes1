package org.example;

import com.opencsv.CSVReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class CSVToSQLite {

    private static String extractStateName(String stateName) {
        if (stateName.contains(",")) {
            String[] parts = stateName.split(", ");
            return parts[parts.length - 1]; // Берем последний элемент после запятой
        } else {
            return stateName;
        }
    }

    public static void main(String[] args) {
        InputStream inputStream = CSVToSQLite.class.getClassLoader().getResourceAsStream("earth.csv");

        try {
            if (inputStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                CSVReader reader = new CSVReader(streamReader);

                String url = "jdbc:sqlite:test.db";
                Connection connection = DriverManager.getConnection(url);
                if (connection != null) {
                    System.out.println("Connected to the database");

                    String createStatesTableSQL = "CREATE TABLE IF NOT EXISTS States (StateID INTEGER PRIMARY KEY AUTOINCREMENT, StateName TEXT)";
                    connection.createStatement().executeUpdate(createStatesTableSQL);

                    String createEarthquakesTableSQL = "CREATE TABLE IF NOT EXISTS Earthquakes (EarthquakeID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "StateID INTEGER, DepthInMeters INTEGER, MagnitudeType TEXT, Magnitude REAL, Time TEXT, " +
                            "FOREIGN KEY(StateID) REFERENCES States(StateID))";
                    connection.createStatement().executeUpdate(createEarthquakesTableSQL);

                    PreparedStatement stateInsertStatement = connection.prepareStatement("INSERT OR IGNORE INTO States (StateName) VALUES (?)");
                    PreparedStatement selectStateIDStatement = connection.prepareStatement("SELECT StateID FROM States WHERE StateName = ?");
                    PreparedStatement earthquakeInsertStatement = connection.prepareStatement("INSERT INTO Earthquakes (StateID, DepthInMeters, MagnitudeType, Magnitude, Time) VALUES (?, ?, ?, ?, ?)");

                    reader.skip(1);
                    String[] nextLine;
                    Set<String> existingStates = new HashSet<>();

                    while ((nextLine = reader.readNext()) != null) {
                        String stateName = extractStateName(nextLine[4]);

                        if (!existingStates.contains(stateName)) {
                            existingStates.add(stateName);
                            stateInsertStatement.setString(1, stateName); // Название штата
                            stateInsertStatement.executeUpdate();
                        }

                        selectStateIDStatement.setString(1, stateName);
                        ResultSet resultSet = selectStateIDStatement.executeQuery();

                        try {
                            if (resultSet.next()) {
                                int stateID = resultSet.getInt("StateID");

                                earthquakeInsertStatement.setInt(1, stateID);
                                earthquakeInsertStatement.setInt(2, Integer.parseInt(nextLine[1]));
                                earthquakeInsertStatement.setString(3, nextLine[2]);
                                earthquakeInsertStatement.setFloat(4, Float.parseFloat(nextLine[3]));
                                earthquakeInsertStatement.setString(5, nextLine[5]);
                                earthquakeInsertStatement.executeUpdate();
                            }
                        } finally {
                            resultSet.close();
                        }
                    }

                    stateInsertStatement.close();
                    selectStateIDStatement.close();
                    earthquakeInsertStatement.close();
                    reader.close();
                    connection.close();
                } else {
                    System.out.println("Failed to connect to the database");
                }
            } else {
                System.out.println("File not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

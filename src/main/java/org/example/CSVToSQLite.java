package org.example;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CSVToSQLite {

    public static void main(String[] args) {
        // Загрузка ресурса через ClassLoader
        InputStream inputStream = CSVToSQLite.class.getClassLoader().getResourceAsStream("earth.csv");

        try {
            if (inputStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                CSVReader reader = new CSVReader(streamReader);

                // Установка соединения с базой данных SQLite
                String url = "jdbc:sqlite:test.db";
                Connection connection = DriverManager.getConnection(url);
                if (connection != null) {
                    System.out.println("Connected to the database");

                    // Создание таблицы с новыми названиями столбцов
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS data (ID TEXT, \"Глубина в метрах\" INTEGER, " +
                            "\"Тип магнитуды\" TEXT, \"Магнитуда\" REAL, \"Штат\" TEXT, \"Время\" TEXT)";
                    connection.createStatement().executeUpdate(createTableSQL);

                    // Подготовленный запрос для вставки данных
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO data VALUES (?, ?, ?, ?, ?, ?)");

                    // Чтение CSV файла (пропускаем первую строку) и вставка данных в базу данных
                    reader.skip(1); // Пропускаем первую строку с заголовками
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        preparedStatement.setString(1, nextLine[0]);
                        preparedStatement.setInt(2, Integer.parseInt(nextLine[1]));
                        preparedStatement.setString(3, nextLine[2]);
                        preparedStatement.setFloat(4, Float.parseFloat(nextLine[3]));
                        preparedStatement.setString(5, nextLine[4]);
                        preparedStatement.setString(6, nextLine[5]);
                        preparedStatement.executeUpdate();
                    }

                    // Закрытие ресурсов
                    preparedStatement.close();
                    reader.close();
                    connection.close();
                } else {
                    System.out.println("Failed to connect to the database");
                }
            } else {
                System.out.println("Файл не найден");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

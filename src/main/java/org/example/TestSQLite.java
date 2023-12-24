package org.example;// Проверка содержимого базы данных
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class TestSQLite {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:test.db";

        try {
            Connection connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("Connected to the database");

                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});

                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    System.out.println("Table: " + tableName);

                    Statement statement = connection.createStatement();
                    ResultSet tableResultSet = statement.executeQuery("SELECT * FROM " + tableName);
                    ResultSetMetaData tableMetaData = tableResultSet.getMetaData();
                    int columnCount = tableMetaData.getColumnCount();

                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(tableMetaData.getColumnName(i) + "\t");
                    }
                    System.out.println();

                    int rowCount = 0;
                    while (tableResultSet.next()) {
                        rowCount++;
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(tableResultSet.getString(i) + "\t");
                        }
                        System.out.println();
                    }

                    System.out.println("Number of rows: " + rowCount + "\n");

                    tableResultSet.close();
                }

                resultSet.close();
                connection.close();
            } else {
                System.out.println("Failed to connect to the database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

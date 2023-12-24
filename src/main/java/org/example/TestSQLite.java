import java.sql.*;

public class TestSQLite {

    public static void main(String[] args) {
        try {
            // Установка соединения с базой данных SQLite
            String url = "jdbc:sqlite:test.db";
            Connection connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("Connected to the database");

                // Выполнение запроса для выборки данных из таблицы
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM data");

                // Вывод результатов запроса
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getString("ID") +
                            ", Глубина в метрах: " + resultSet.getInt("Глубина в метрах") +
                            ", Тип магнитуды: " + resultSet.getString("Тип магнитуды") +
                            ", Магнитуда: " + resultSet.getFloat("Магнитуда") +
                            ", Штат: " + resultSet.getString("Штат") +
                            ", Время: " + resultSet.getString("Время"));
                }

                // Закрытие ресурсов
                resultSet.close();
                statement.close();
                connection.close();
            } else {
                System.out.println("Failed to connect to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.*;

public class EarthquakeGraph {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            String url = "jdbc:sqlite:test.db";
            try (Connection connection = DriverManager.getConnection(url)) {
                if (connection != null) {
                    System.out.println("Connected to the database");

                    String query = "SELECT strftime('%Y', e.\"Time\") AS year, COUNT(*) AS count " +
                            "FROM Earthquakes e " +
                            "INNER JOIN States s ON e.StateID = s.StateID " +
                            "GROUP BY year";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    for (int year = 1970; year <= 2015; year++) { // Заполнить 0 года, в которые не наблюдались землетрясения
                        String yearString = String.valueOf(year);
                        if (!dataset.getColumnKeys().contains(yearString)) {
                            dataset.addValue(0, "Earthquakes", yearString);
                        }
                    }

                    while (resultSet.next()) {
                        String year = resultSet.getString("year");
                        int count = resultSet.getInt("count");

                        if (year != null && !year.isEmpty()) {
                            try {
                                int yearValue = Integer.parseInt(year);
                                dataset.addValue(count, "Earthquakes", String.valueOf(yearValue));
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid year format: " + year);
                            }
                        }
                    }

                    JFreeChart lineChart = ChartFactory.createLineChart(
                            "Average Earthquakes per Year",
                            "Year",
                            "Average Earthquakes",
                            dataset
                    );

                    ChartPanel chartPanel = new ChartPanel(lineChart);
                    CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
                    plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);
                    JFrame frame = new JFrame("Earthquake Graph");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(chartPanel);
                    frame.pack();
                    frame.setVisible(true);

                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                } else {
                    System.out.println("Failed to connect to the database");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

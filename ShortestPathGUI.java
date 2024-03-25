import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShortestPathGUI {
    private static ShortestPath shortestPath;

    public static void main(String[] args) {
        // Create an instance of ShortestPath and read city data from the file
        shortestPath = new ShortestPath();
        shortestPath.readCityData("turkishcities.csv");

        // Create the main JFrame for the application
        JFrame frame = new JFrame("City Path Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // Create a JPanel to hold UI components
        JPanel panel = new JPanel();
        frame.add(panel);

        // Place UI components on the panel
        placeComponents(panel);

        // Make the frame visible
        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        // Set the layout manager to null for manual component placement
        panel.setLayout(null);

        // Create JLabel and JTextField for source city input
        JLabel sourceLabel = new JLabel("Source City:");
        sourceLabel.setBounds(10, 20, 80, 25);
        panel.add(sourceLabel);
        JTextField sourceText = new JTextField(20);
        sourceText.setBounds(130, 20, 165, 25);
        panel.add(sourceText);

        // Create JLabel and JTextField for destination city input
        JLabel destinationLabel = new JLabel("Destination City:");
        destinationLabel.setBounds(10, 50, 120, 25);
        panel.add(destinationLabel);
        JTextField destinationText = new JTextField(20);
        destinationText.setBounds(130, 50, 165, 25);
        panel.add(destinationText);

        // Create JButton for triggering path calculation
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(10, 80, 120, 25);
        panel.add(calculateButton);

        // Create JTextArea for displaying the result
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(10, 110, 470, 140);
        panel.add(scrollPane);

        // ActionListener for the Calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get source and destination cities from user input
                String sourceCity = sourceText.getText();
                String destinationCity = destinationText.getText();

                // Measure BFS execution time and calculate path and distance
                long startTimeBFS = System.nanoTime();
                List<String> pathBFS = shortestPath.bfs(sourceCity, destinationCity);
                long endTimeBFS = System.nanoTime();
                int distanceBFS = shortestPath.calculatePathDistance(pathBFS);
                long durationBFS = endTimeBFS - startTimeBFS;

                // Measure DFS execution time and calculate path and distance
                long startTimeDFS = System.nanoTime();
                List<String> pathDFS = shortestPath.dfs(sourceCity, destinationCity);
                long endTimeDFS = System.nanoTime();
                int distanceDFS = shortestPath.calculatePathDistance(pathDFS);
                long durationDFS = endTimeDFS - startTimeDFS;

                // Display results in the text area
                String resultText = "BFS Shortest Path: " + pathBFS + "\nBFS Path Distance: " + distanceBFS + " km\nBFS Execution Time: " + durationBFS + " nanoseconds\n\n";
                resultText += "DFS Shortest Path: " + pathDFS + "\nDFS Path Distance: " + distanceDFS + " km\nDFS Execution Time: " + durationDFS + " nanoseconds";

                resultArea.setText(resultText);
            }
        });
    }
}

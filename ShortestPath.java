import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class CityGraph {
    private Map<String, Map<String, Integer>> adjacencyList;

    public CityGraph() {
        adjacencyList = new HashMap<>();
    }

    // Add a city to the graph
    public void addCity(String city) {
        adjacencyList.put(city, new HashMap<>());
    }

    // Add a connection (edge) between two cities with a given distance
    public void addConnection(String city1, String city2, int distance) {
        adjacencyList.get(city1).put(city2, distance);
        adjacencyList.get(city2).put(city1, distance);
    }

    // Get neighbours (adjacent cities) of a given city with their distances
    public Map<String, Integer> getNeighbors(String city) {
        return adjacencyList.get(city);
    }
}

public class ShortestPath {
    private static CityGraph cityGraph;

    static {
        cityGraph = new CityGraph();
    }

    public static void main(String[] args) {
        // Initialize CityGraph and read city data from file
        cityGraph = new CityGraph();
        readCityData("turkishcities.csv");
    
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter source city: ");
        String sourceCity = scanner.nextLine();
        System.out.print("Enter destination city: ");
        String destinationCity = scanner.nextLine();
    
        // Measure execution time for BFS
        long bfsStartTime = System.nanoTime();
        List<String> bfsPath = bfs(sourceCity, destinationCity);
        long bfsEndTime = System.nanoTime();
        long bfsDuration = bfsEndTime - bfsStartTime;
    
        // Measure execution time for DFS
        long dfsStartTime = System.nanoTime();
        List<String> dfsPath = dfs(sourceCity, destinationCity);
        long dfsEndTime = System.nanoTime();
        long dfsDuration = dfsEndTime - dfsStartTime;
    
        // Calculate distances for BFS and DFS paths
        int bfsPathDistance = calculatePathDistance(bfsPath);
        int dfsPathDistance = calculatePathDistance(dfsPath);
    
        // Print results
        System.out.println("BFS Shortest Path: " + bfsPath);
        System.out.println("BFS Path Distance: " + bfsPathDistance + " km");
        System.out.println("BFS Execution Time: " + bfsDuration + " nanoseconds");
        System.out.println("DFS Shortest Path: " + dfsPath);
        System.out.println("DFS Path Distance: " + dfsPathDistance + " km");
        System.out.println("DFS Execution Time: " + dfsDuration + " nanoseconds"); 
    }
    

    // Read city data from a CSV file and populate the CityGraph
    public static void readCityData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // Read the first line to get city names
            String[] cities = br.readLine().split(",");

            // Add cities to the CityGraph, starting from index 1 (skipping an empty field at index 0)
            for (int i = 1; i < cities.length; i++) {
                cityGraph.addCity(cities[i]);
            }

            int row = 0;

            // Read subsequent lines to get distances data
            while ((line = br.readLine()) != null) {
                String[] distances = line.split(",");
                String city = cities[row + 1];

                //Iterate through distances to establish connections between cities
                for (int col = 1; col < distances.length; col++) {
                    // Check if the distance is not the placeholder "99999"
                    if (!distances[col].equals("99999")) {
                        String neighborCity = cities[col];
                        int distance = Integer.parseInt(distances[col]);

                        // Add connection between the current city and its neighbor with the corresponding distance
                        cityGraph.addConnection(city, neighborCity, distance);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Breadth-First search algorithm to find the shortest path
    static List<String> bfs(String sourceCity, String destinationCity) {
        // Queue to keep track of the cities to be visited in BFS order
        Queue<String> queue = new LinkedList<>();

        // Map to store the parent-child relationship for reconstructing the path
        Map<String, String> parentMap = new HashMap<>();

        // Set to keep track of visited cities
        Set<String> visited = new HashSet<>();
        
        // Initialize with the source city
        queue.offer(sourceCity);
        visited.add(sourceCity);

        // Perform BFS
        while (!queue.isEmpty()) {
            // Get the current city from the front of the queue
            String currentCity = queue.poll();

            // Explore neighbors of the current city
            for (Map.Entry<String, Integer> neighbor : cityGraph.getNeighbors(currentCity).entrySet()) {
                String neighborCity = neighbor.getKey();

                // Check if the neighbor city has not been visited
                if (!visited.contains(neighborCity)) {
                    // Enqueue the neighbor city for further exploration
                    queue.offer(neighborCity);

                    // Mark the neighbor city as visited
                    visited.add(neighborCity);

                    // Record the parent of the neighbor city (for reconstructing the path)
                    parentMap.put(neighborCity, currentCity);
                }
            }
        }

        // Reconstruct and return the shortest path from source to destination
        return buildPath(parentMap, sourceCity, destinationCity);
    }
    // Depth-First search algorithm to find the shortest path
    public static List<String> dfs(String sourceCity, String destinationCity) {
        // Stack to keep track of the cities to be visited in DFS order
        Stack<String> stack = new Stack<>();
        
        // Map to store the parent-child relationship for reconstructing the path
        Map<String, String> parentMap = new HashMap<>();

        // Set to keep track of visited cities
        Set<String> visited = new HashSet<>();

        // Initialize with the source city
        stack.push(sourceCity);
        visited.add(sourceCity);

        // Perform DFS
        while (!stack.isEmpty()) {
            // Get the current city from the top of the stack
            String currentCity = stack.pop();

            //C heck if the destination city is reached
            if (currentCity.equals(destinationCity)) {
                break; 
            }

            // Explore neighbors of the current city 
            for (Map.Entry<String, Integer> neighbor : cityGraph.getNeighbors(currentCity).entrySet()) {
                String neighborCity = neighbor.getKey();

                // Check if the neighbor city has not been visited
                if (!visited.contains(neighborCity)) {
                    // Push the neighbor city for further exploration
                    stack.push(neighborCity);

                    // Mark the neighbor city as visited
                    visited.add(neighborCity);

                    // Record the parent of the neighbor city (for reconstructing the path)
                    parentMap.put(neighborCity, currentCity);
                }
            }
        }

        // Reconstruct and return the  path from source to destination
        return buildPath(parentMap, sourceCity, destinationCity);
    }

    // Calculate the total distamce of a path
    public static int calculatePathDistance(List<String> path) {
        if (path.size() < 2) return 0; // If the path has one or zero cities, the distance is 0. 
    
        int totalDistance = 0;
        String prevCity = path.get(0);
    
        // Iterate over each city pair in the path and accumulate the distances
        for (int i = 1; i < path.size(); i++) {
            String currentCity = path.get(i);
            totalDistance += cityGraph.getNeighbors(prevCity).get(currentCity);
            prevCity = currentCity;
        }
    
        return totalDistance;
    }
    


    // Build the actual path from the parent map
    private static List<String> buildPath(Map<String, String> parentMap, String source, String destination) {
        List<String> path = new ArrayList<>();
        String currentCity = destination;

        while (currentCity != null) {
            path.add(currentCity);
            currentCity = parentMap.get(currentCity);
        }

        Collections.reverse(path);
        return path;
    }
}
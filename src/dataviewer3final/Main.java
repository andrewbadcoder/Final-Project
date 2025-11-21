package dataviewer3final;

import java.io.FileNotFoundException;

/**
 * Entry point for the DataViewer application.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        
        // Use forward slashes - they work on Windows!
        String data = "C:\\Users\\packe\\Downloads\\GlobalLandTemperaturesByState (1).csv";
        
        // Create and launch the application
        System.out.println("Loading data from: " + data);
        new DataViewerApp(data);
    }
}

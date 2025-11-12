package dataviewer2;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A data object (POJO) to hold the processed data ready for plotting.
 * This class has the TreeMaps that DataModel and DataRenderer expect.
 */
public class PlotData {

    // These fields match the original data structures in DataViewerApp
    private TreeMap<Integer, SortedMap<Integer,Double>> m_plotData;
    private TreeMap<Integer,Double> m_plotMonthlyMaxValue;
    private TreeMap<Integer,Double> m_plotMonthlyMinValue;

    /**
     * Constructor initializes the TreeMaps.
     */
    public PlotData() {
        // Initialize the maps
        m_plotData = new TreeMap<>();
        m_plotMonthlyMaxValue = new TreeMap<>();
        m_plotMonthlyMinValue = new TreeMap<>();
        
        // Pre-populate them just like the original code did
        for (int month = 1; month <= 12; month++) {
            m_plotData.put(month, new TreeMap<>());
            m_plotMonthlyMaxValue.put(month, Double.MIN_VALUE);
            m_plotMonthlyMinValue.put(month, Double.MAX_VALUE);
        }
    }

    // Getters for DataModel and DataRenderer to use
    
    public TreeMap<Integer, SortedMap<Integer, Double>> getPlotData() {
        return m_plotData;
    }

    public TreeMap<Integer, Double> getPlotMonthlyMaxValue() {
        return m_plotMonthlyMaxValue;
    }

    public TreeMap<Integer, Double> getPlotMonthlyMinValue() {
        return m_plotMonthlyMinValue;
    }
}
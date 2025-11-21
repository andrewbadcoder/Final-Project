package dataviewer3final;

import java.awt.Color;

/**
 * Strategy interface for different visualization algorithms.
 * Each concrete strategy implements how to compute cell colors for the plot.
 */
public interface VisualizationStrategy {
    /**
     * Calculates the color for a data cell based on the visualization strategy.
     * 
     * @param value The temperature value to visualize
     * @param monthlyMin The minimum temperature for this month
     * @param monthlyMax The maximum temperature for this month
     * @return The color to display for this cell
     */
    Color getColor(double value, double monthlyMin, double monthlyMax);
    
    /**
     * Returns the name of this visualization strategy.
     * @return Strategy name
     */
    String getName();
}

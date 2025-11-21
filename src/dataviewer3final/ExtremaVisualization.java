package dataviewer3final;

import java.awt.Color;

/**
 * Extrema visualization strategy - highlights values within 10% of 
 * monthly min/max in blue/red, with middle values shown in grayscale.
 * Implements the Strategy pattern for visualization modes.
 */
public class ExtremaVisualization implements VisualizationStrategy {
    
    private static final double EXTREMA_PCT = 0.1;
    private static final double TEMPERATURE_MIN_C = -10.0;
    private static final double TEMPERATURE_MAX_C = 30.0;
    private static final double TEMPERATURE_RANGE = TEMPERATURE_MAX_C - TEMPERATURE_MIN_C;
    
    @Override
    public Color getColor(double value, double monthlyMin, double monthlyMax) {
        double fullRange = monthlyMax - monthlyMin;
        double extremaMinBound = monthlyMin + EXTREMA_PCT * fullRange;
        double extremaMaxBound = monthlyMax - EXTREMA_PCT * fullRange;
        
        // Check if value is in extrema ranges
        if (value <= extremaMinBound) {
            return Color.BLUE;  // Cold extreme
        } else if (value >= extremaMaxBound) {
            return Color.RED;   // Hot extreme
        } else {
            // Middle values shown in grayscale
            double pct = (value - TEMPERATURE_MIN_C) / TEMPERATURE_RANGE;
            if (pct > 1.0) pct = 1.0;
            else if (pct < 0.0) pct = 0.0;
            
            int grayValue = (int)(255.0 * pct);
            return new Color(grayValue, grayValue, grayValue);
        }
    }
    
    @Override
    public String getName() {
        return "Extrema (within 10% of min/max)";
    }
}

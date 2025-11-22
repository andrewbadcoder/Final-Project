package dataviewer3final;

import java.awt.Color;

/**
 * Raw visualization strategy - displays temperature data as a gradient
 * from blue (cold) to red (hot) based on absolute temperature values.
 * Implements the Strategy pattern for visualization modes.
 */
public class RawVisualization implements VisualizationStrategy {
    
    private static final double TEMPERATURE_MIN_C = -10.0;
    private static final double TEMPERATURE_MAX_C = 30.0;
    private static final double TEMPERATURE_RANGE = TEMPERATURE_MAX_C - TEMPERATURE_MIN_C;
    
    @Override
    public Color getColor(double value, double monthlyMin, double monthlyMax) {
        // Calculate percentage based on absolute temperature range
        double pct = (value - TEMPERATURE_MIN_C) / TEMPERATURE_RANGE;
        
        // Clamp to [0, 1]
        if (pct > 1.0) pct = 1.0;
        else if (pct < 0.0) pct = 0.0;
        
        // Blue (cold) to Red (hot) gradient
        int r = (int)(255.0 * pct);
        int g = 0;
        int b = (int)(255.0 * (1.0 - pct));
        
        return new Color(r, g, b);
    }
    
    @Override
    public String getName() {
        return "Raw";
    }
}

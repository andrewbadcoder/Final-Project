package dataviewer3final;

import edu.du.dudraw.Draw;
import java.awt.Color;
import java.util.SortedMap;

/**
 * Handles all rendering operations for the DataViewer application.
 * Uses the Strategy pattern for visualization modes.
 */
public class DataRenderer {
    
    // --- Drawing Constants ---
    private static final double DATA_WINDOW_BORDER = 50.0;
    private static final double MENU_STARTING_X = 40.0;
    private static final double MENU_STARTING_Y = 90.0;
    private static final double MENU_ITEM_SPACING = 5.0;
    private static final String[] MONTH_NAMES = { "", // 1-based
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    private static final int WINDOW_HEIGHT = 720;
    private static final int WINDOW_WIDTH = 1320;
    
    // --- Strategy Pattern Field ---
    private VisualizationStrategy visualizationStrategy;
    
    /**
     * Sets the visualization strategy to use for rendering data.
     * @param strategy The strategy to use
     */
    public void setVisualizationStrategy(VisualizationStrategy strategy) {
        this.visualizationStrategy = strategy;
    }
    
    /**
     * Draws the main menu interface.
     * @param window The drawing window
     * @param state The current application state
     */
    public void drawMainMenu(Draw window, DataViewState state) {
        // Set coordinate system
        window.setXscale(0, 100);
        window.setYscale(0, 100);
        
        window.clear(Color.WHITE);
        
        String[] menuItems = {
            "Commands:",
            "  [P]lot data",
            "  [C]hange country: " + state.getSelectedCountry(),
            "  [T] Change state: " + state.getSelectedState(),
            "  [S]tart year: " + state.getSelectedStartYear(),
            "  [E]nd year: " + state.getSelectedEndYear(),
            "  [V]isualization: " + state.getSelectedVisualization(),
            "  [Q]uit"
        };
        
        drawMenuItems(window, menuItems);
    }
    
    /**
     * Draws menu items on the screen.
     * @param window The drawing window
     * @param items Array of menu item strings
     */
    private void drawMenuItems(Draw window, String[] items) {
        window.setPenColor(Color.BLACK);
        double y = MENU_STARTING_Y;
        
        for (String item : items) {
            window.textLeft(MENU_STARTING_X, y, item);
            y -= MENU_ITEM_SPACING;
        }
    }
    
    /**
     * Draws the plot data visualization.
     * Uses the Strategy pattern to determine cell colors.
     * @param window The drawing window
     * @param state The current application state
     * @param plotData The data to plot
     */
    public void drawPlotData(Draw window, DataViewState state, PlotData plotData) {
        // Set coordinate system to pixel coordinates
        window.setXscale(0, WINDOW_WIDTH);
        window.setYscale(0, WINDOW_HEIGHT);
        
        window.clear(Color.WHITE);
        
        // Get year range
        Integer startYear = state.getSelectedStartYear();
        Integer endYear = state.getSelectedEndYear();
        
        if (startYear == null || endYear == null) {
            window.setPenColor(Color.BLACK);
            window.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0, "No data to display");
            return;
        }
        
        // Calculate cell dimensions
        int numYears = endYear - startYear + 1;
        double dataWindowWidth = WINDOW_WIDTH - 2.0 * DATA_WINDOW_BORDER;
        double dataWindowHeight = WINDOW_HEIGHT - 2.0 * DATA_WINDOW_BORDER;
        double cellWidth = dataWindowWidth / numYears;
        double cellHeight = dataWindowHeight / 12.0; // 12 months
        
        // Draw data cells
        for (int month = 1; month <= 12; month++) {
            SortedMap<Integer, Double> monthData = plotData.getPlotData().get(month);
            double monthlyMin = plotData.getPlotMonthlyMinValue().get(month);
            double monthlyMax = plotData.getPlotMonthlyMaxValue().get(month);
            
            for (int year = startYear; year <= endYear; year++) {
                if (monthData.containsKey(year)) {
                    Double value = monthData.get(year);
                    
                    // Calculate cell position
                    double x = DATA_WINDOW_BORDER + (year - startYear) * cellWidth + cellWidth / 2.0;
                    double y = DATA_WINDOW_BORDER + (12 - month) * cellHeight + cellHeight / 2.0;
                    
                    // Use strategy to determine color
                    Color cellColor = visualizationStrategy.getColor(value, monthlyMin, monthlyMax);
                    
                    // Draw the cell
                    window.setPenColor(cellColor);
                    window.filledRectangle(x, y, cellWidth / 2.0, cellHeight / 2.0);
                }
            }
        }
        
        // Draw month labels on the LEFT
        window.setPenColor(Color.BLACK);
        for (int month = 1; month <= 12; month++) {
            double y = DATA_WINDOW_BORDER + (12 - month) * cellHeight + cellHeight / 2.0;
            window.textLeft(10.0, y, MONTH_NAMES[month]);
        }
        
        // Draw year labels at the BOTTOM
        // Show every 5th or 10th year depending on range to avoid clutter
        int yearStep = (numYears > 100) ? 10 : (numYears > 50) ? 5 : 1;
        
        for (int year = startYear; year <= endYear; year++) {
            if ((year - startYear) % yearStep == 0 || year == endYear) {
                double x = DATA_WINDOW_BORDER + (year - startYear) * cellWidth + cellWidth / 2.0;
                double y = DATA_WINDOW_BORDER - 20.0; // Position BELOW the data area
                
                // Rotate text 90 degrees if needed for better fit
                if (numYears > 50) {
                    // For long ranges, just show key years
                    window.text(x, y, String.valueOf(year));
                } else {
                    window.text(x, y, String.valueOf(year));
                }
            }
        }
        
        // Draw title at the TOP
        String title = state.getSelectedState() + ", " + state.getSelectedCountry() + 
                      " (" + startYear + "-" + endYear + ") - " + 
                      state.getSelectedVisualization();
        window.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT - 25.0, title);
        
        // Draw return instruction at the VERY BOTTOM
        window.text(WINDOW_WIDTH / 2.0, 15.0, "Press [M] to return to menu");
    }
}

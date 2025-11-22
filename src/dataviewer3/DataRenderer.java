package dataviewer3;

import edu.du.dudraw.Draw;
import java.awt.Color;
import java.util.SortedMap;

public class DataRenderer {
    
    // --- Drawing Constants ---
    private static final double DATA_WINDOW_BORDER = 50.0;
    private static final double EXTREMA_PCT = 0.1;
    private static final double MENU_STARTING_X = 40.0;
    private static final double MENU_STARTING_Y = 90.0;
    private static final double MENU_ITEM_SPACING = 5.0;
    private static final String[] MONTH_NAMES = { "", // 1-based
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    private static final double TEMPERATURE_MAX_C = 30.0;
    private static final double TEMPERATURE_MIN_C = -10.0;
    private static final double TEMPERATURE_RANGE = TEMPERATURE_MAX_C - TEMPERATURE_MIN_C;
    private static final int WINDOW_HEIGHT = 720;
    private static final int WINDOW_WIDTH = 1320; // should be a multiple of 12
    
    public void drawMainMenu(Draw window, DataViewState state) {
        window.clear(Color.WHITE);

        String[] menuItems = {
                "Type the menu number to select that option:",
                "",
                String.format("C     Set country: [%s]", state.getSelectedCountry()),
                String.format("T     Set state: [%s]", state.getSelectedState()),
                String.format("S     Set start year [%d]", state.getSelectedStartYear()),
                String.format("E     Set end year [%d]", state.getSelectedEndYear()),
                String.format("V     Set visualization [%s]", state.getSelectedVisualization()),
                String.format("P     Plot data"),
                String.format("Q     Quit"),
        };

        // enable drawing by "percentage" for the menu
        window.setXscale(0, 100);
        window.setYscale(0, 100);

        // draw the menu
        window.setPenColor(Color.BLACK);
        drawMenuItems(window, menuItems);
    }
    
    private void drawMenuItems(Draw window, String[] menuItems) {
        double yCoord = MENU_STARTING_Y;
        for (String item : menuItems) {
            window.textLeft(MENU_STARTING_X, yCoord, item);
            yCoord -= MENU_ITEM_SPACING;
        }
    }
    
    public void drawPlotData(Draw window, DataViewState state, PlotData plotData) {
        // Give a buffer around the plot window
        window.setXscale(-DATA_WINDOW_BORDER, WINDOW_WIDTH + DATA_WINDOW_BORDER);
        window.setYscale(-DATA_WINDOW_BORDER, WINDOW_HEIGHT + DATA_WINDOW_BORDER);

        // gray background
        window.clear(Color.LIGHT_GRAY);

        // white plot area
        window.setPenColor(Color.WHITE);
        window.filledRectangle(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0, WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0);

        window.setPenColor(Color.BLACK);
        
        int startYear = state.getSelectedStartYear();
        int endYear = state.getSelectedEndYear();
        
        double nCols = 12; // one for each month
        double nRows = endYear - startYear + 1; // for the years

        double cellWidth = WINDOW_WIDTH / nCols;
        double cellHeight = WINDOW_HEIGHT / nRows;
        
        boolean extremaVisualization = state.getSelectedVisualization().equals(DataViewState.VISUALIZATION_MODES[DataViewState.VISUALIZATION_EXTREMA_IDX]);

        for (int month = 1; month <= 12; month++) {
            double fullRange = plotData.getPlotMonthlyMaxValue().get(month) - plotData.getPlotMonthlyMinValue().get(month);
            double extremaMinBound = plotData.getPlotMonthlyMinValue().get(month) + EXTREMA_PCT * fullRange;
            double extremaMaxBound = plotData.getPlotMonthlyMaxValue().get(month) - EXTREMA_PCT * fullRange;

            // draw the line separating the months and the month label
            window.setPenColor(Color.BLACK);
            double lineX = (month - 1.0) * cellWidth;
            window.line(lineX, 0.0, lineX, WINDOW_HEIGHT);
            window.text(lineX + cellWidth / 2.0, -DATA_WINDOW_BORDER / 2.0, MONTH_NAMES[month]);

            // there should always be a map for the month
            SortedMap<Integer, Double> monthData = plotData.getPlotData().get(month);

            for (int year = startYear; year <= endYear; year++) {
                // month data structure might not have every year
                if (monthData.containsKey(year)) {
                    Double value = monthData.get(year);

                    double x = (month - 1.0) * cellWidth + 0.5 * cellWidth;
                    double y = (year - startYear) * cellHeight + 0.5 * cellHeight;

                    Color cellColor;

                    // get either color or grayscale depending on visualization mode
                    if (extremaVisualization && value > extremaMinBound && value < extremaMaxBound) {
                        cellColor = getDataColor(value, true); // Grayscale middle
                    } else if (extremaVisualization) {
                        // doing extrema visualization, show "high" values in red "low" values in blue.
                        cellColor = (value >= extremaMaxBound) ? Color.RED : Color.BLUE;
                    } else {
                        cellColor = getDataColor(value, false); // Normal color
                    }

                    // draw the rectangle for this data point
                    window.setPenColor(cellColor);
                    window.filledRectangle(x, y, cellWidth / 2.0, cellHeight / 2.0);
                }
            }
        }
        
        // draw the labels for the y-axis
        window.setPenColor(Color.BLACK);

        double labelYearSpacing = (endYear - startYear) / 5.0;
        double labelYSpacing = WINDOW_HEIGHT / 5.0;
        // spaced out by 5, but need both the first and last label, so iterate 6
        for (int i = 0; i < 6; i++) {
            int year = (int) Math.round(i * labelYearSpacing + startYear);
            String text = String.format("%4d", year);
            
            double yPos = i * labelYSpacing;
            // Ensure first and last labels are at the very bottom and top
            if (i == 0) yPos = 0;
            if (i == 5) yPos = WINDOW_HEIGHT;

            window.textRight(-DATA_WINDOW_BORDER / 10.0, yPos, text);
            window.textLeft(WINDOW_WIDTH + DATA_WINDOW_BORDER / 10.0, yPos, text);
        }

        // draw rectangle around the whole data plot window
        window.rectangle(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0, WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0);

        // put in the title
        String title = String.format("%s, %s from %d to %d. Press 'M' for Main Menu.  Press 'Q' to Quit.",
                state.getSelectedState(), state.getSelectedCountry(), startYear, endYear);
        window.text(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT + DATA_WINDOW_BORDER / 2.0, title);
    }
    
    private Color getDataColor(Double value, boolean doGrayscale) {
        if (null == value) {
            return Color.WHITE; // Should not happen, but good to check
        }
        double pct = (value - TEMPERATURE_MIN_C) / TEMPERATURE_RANGE;

        if (pct > 1.0) pct = 1.0;
        else if (pct < 0.0) pct = 0.0;
        
        int r, g, b;
        
        if (!doGrayscale) {
            // Blue (cold) to Red (hot)
            r = (int) (255.0 * pct);
            g = 0;
            b = (int) (255.0 * (1.0 - pct));
        } else {
            // Grayscale for the middle extema
            r = g = b = (int) (255.0 * pct);
        }
        
        return new Color(r, g, b);
    }
}

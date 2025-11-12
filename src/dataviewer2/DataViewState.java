package dataviewer2;

/**
 * A data object (POJO) that holds the current state of the application's UI
 * and user selections.
 * This class isolates state management from the main application logic,
 * fulfilling Issue #7 of the refactoring plan.
 */
public class DataViewState {

    // --- REFACTORED: Moved constants from DataViewerApp ---
    // These constants define the possible states, so they belong with the state class.
    public final static int GUI_MODE_MAIN_MENU = 0;
    public final static int GUI_MODE_DATA = 1;
    public final static String DEFAULT_COUNTRY = "United States";
    public final static String[] VISUALIZATION_MODES = { "Raw", "Extrema (within 10% of min/max)" };
    public final static int VISUALIZATION_RAW_IDX = 0;
    public final static int VISUALIZATION_EXTREMA_IDX = 1;
    
    // --- REFACTORED: Moved fields from DataViewerApp ---
    // All fields are private, accessible only via getters and setters.
    private int guiMode;
    private String selectedCountry;
    private Integer selectedEndYear;
    private String selectedState;
    private Integer selectedStartYear;
    private String selectedVisualization;
    
    /**
     * Initializes the state to its default values, replicating the
     * initial values from DataViewerApp.
     */
    public DataViewState() {
        // Set defaults based on DataViewerApp.java lines 73, 76, 80
        this.guiMode = GUI_MODE_MAIN_MENU;
        this.selectedCountry = DEFAULT_COUNTRY;
        this.selectedVisualization = VISUALIZATION_MODES[VISUALIZATION_RAW_IDX];
        
        // These are intended to be null until data is loaded,
        // just as they were in DataViewerApp.
        this.selectedEndYear = null;
        this.selectedState = null;
        this.selectedStartYear = null;
    }

    // --- REFACTORED: Added Getters and Setters for all state fields ---
    
    public int getGuiMode() {
        return guiMode;
    }

    public void setGuiMode(int guiMode) {
        this.guiMode = guiMode;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public Integer getSelectedEndYear() {
        return selectedEndYear;
    }

    public void setSelectedEndYear(Integer selectedEndYear) {
        this.selectedEndYear = selectedEndYear;
    }

    public String getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(String selectedState) {
        this.selectedState = selectedState;
    }

    public Integer getSelectedStartYear() {
        return selectedStartYear;
    }

    public void setSelectedStartYear(Integer selectedStartYear) {
        this.selectedStartYear = selectedStartYear;
    }

    public String getSelectedVisualization() {
        return selectedVisualization;
    }

    public void setSelectedVisualization(String selectedVisualization) {
        this.selectedVisualization = selectedVisualization;
    }
}
package dataviewer3;

public class DataViewState {

    // --- Constants ---
    public final static String DEFAULT_COUNTRY = "United States";
    public final static String[] VISUALIZATION_MODES = { "Raw", "Extrema (within 10% of min/max)" };
    public final static int VISUALIZATION_RAW_IDX = 0;
    public final static int VISUALIZATION_EXTREMA_IDX = 1;
    
    // --- Fields (Removed guiMode) ---
    private String selectedCountry;
    private Integer selectedEndYear;
    private String selectedState;
    private Integer selectedStartYear;
    private String selectedVisualization;
    
    public DataViewState() {
        this.selectedCountry = DEFAULT_COUNTRY;
        this.selectedVisualization = VISUALIZATION_MODES[VISUALIZATION_RAW_IDX];
        this.selectedEndYear = null;
        this.selectedState = null;
        this.selectedStartYear = null;
    }

    // --- Getters and Setters (Removed guiMode getters/setters) ---
    
    public String getSelectedCountry() { return selectedCountry; }
    public void setSelectedCountry(String selectedCountry) { this.selectedCountry = selectedCountry; }

    public Integer getSelectedEndYear() { return selectedEndYear; }
    public void setSelectedEndYear(Integer selectedEndYear) { this.selectedEndYear = selectedEndYear; }

    public String getSelectedState() { return selectedState; }
    public void setSelectedState(String selectedState) { this.selectedState = selectedState; }

    public Integer getSelectedStartYear() { return selectedStartYear; }
    public void setSelectedStartYear(Integer selectedStartYear) { this.selectedStartYear = selectedStartYear; }

    public String getSelectedVisualization() { return selectedVisualization; }
    public void setSelectedVisualization(String selectedVisualization) { this.selectedVisualization = selectedVisualization; }
}
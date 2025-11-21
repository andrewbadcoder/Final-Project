package dataviewer3final;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable data object that holds the current state of the application.
 * Implements the Observer pattern as the Subject.
 * Notifies registered observers when state changes occur.
 */
public class DataViewState {
    
    // Constants
    public final static int GUI_MODE_MAIN_MENU = 0;
    public final static int GUI_MODE_DATA = 1;
    public final static String DEFAULT_COUNTRY = "United States";
    public final static String[] VISUALIZATION_MODES = { "Raw", "Extrema (within 10% of min/max)" };
    public final static int VISUALIZATION_RAW_IDX = 0;
    public final static int VISUALIZATION_EXTREMA_IDX = 1;
    
    // State fields
    private int guiMode;
    private String selectedCountry;
    private Integer selectedEndYear;
    private String selectedState;
    private Integer selectedStartYear;
    private String selectedVisualization;
    
    // Observer pattern implementation
    private List<StateObserver> observers;
    
    /**
     * Initializes the state to its default values and creates observer list.
     */
    public DataViewState() {
        this.guiMode = GUI_MODE_MAIN_MENU;
        this.selectedCountry = DEFAULT_COUNTRY;
        this.selectedVisualization = VISUALIZATION_MODES[VISUALIZATION_RAW_IDX];
        this.selectedEndYear = null;
        this.selectedState = null;
        this.selectedStartYear = null;
        
        // Initialize observer list
        this.observers = new ArrayList<>();
    }
    
    // --- OBSERVER PATTERN METHODS ---
    
    /**
     * Register an observer to be notified of state changes.
     * @param observer The observer to register
     */
    public void addObserver(StateObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Remove an observer from the notification list.
     * @param observer The observer to remove
     */
    public void removeObserver(StateObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notify all registered observers that the state has changed.
     */
    private void notifyObservers() {
        for (StateObserver observer : observers) {
            observer.onStateChanged(this);
        }
    }
    
    // --- GETTERS ---
    
    public int getGuiMode() {
        return guiMode;
    }
    
    public String getSelectedCountry() {
        return selectedCountry;
    }
    
    public Integer getSelectedEndYear() {
        return selectedEndYear;
    }
    
    public String getSelectedState() {
        return selectedState;
    }
    
    public Integer getSelectedStartYear() {
        return selectedStartYear;
    }
    
    public String getSelectedVisualization() {
        return selectedVisualization;
    }
    
    // --- SETTERS (with automatic notification) ---
    
    public void setGuiMode(int guiMode) {
        if (this.guiMode != guiMode) {
            this.guiMode = guiMode;
            notifyObservers();
        }
    }
    
    public void setSelectedCountry(String selectedCountry) {
        if (!this.selectedCountry.equals(selectedCountry)) {
            this.selectedCountry = selectedCountry;
            notifyObservers();
        }
    }
    
    public void setSelectedEndYear(Integer selectedEndYear) {
        if (this.selectedEndYear == null || !this.selectedEndYear.equals(selectedEndYear)) {
            this.selectedEndYear = selectedEndYear;
            notifyObservers();
        }
    }
    
    public void setSelectedState(String selectedState) {
        if (this.selectedState == null || !this.selectedState.equals(selectedState)) {
            this.selectedState = selectedState;
            notifyObservers();
        }
    }
    
    public void setSelectedStartYear(Integer selectedStartYear) {
        if (this.selectedStartYear == null || !this.selectedStartYear.equals(selectedStartYear)) {
            this.selectedStartYear = selectedStartYear;
            notifyObservers();
        }
    }
    
    public void setSelectedVisualization(String selectedVisualization) {
        if (!this.selectedVisualization.equals(selectedVisualization)) {
            this.selectedVisualization = selectedVisualization;
            notifyObservers();
        }
    }
}

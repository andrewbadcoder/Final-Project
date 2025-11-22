package dataviewer3final;

import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import edu.du.dudraw.Draw;
import edu.du.dudraw.DrawListener;

/**
 * Main controller class for the DataViewer application.
 * Implements StateObserver to automatically respond to state changes.
 * Coordinates between the Model (DataModel), View (DataRenderer),
 * and State (DataViewState) following the MVC pattern with Observer pattern.
 */
public class DataViewerApp implements DrawListener, StateObserver {
    
    // Window configuration
    private final static String WINDOW_TITLE = "DataViewer Application";
    private final static int WINDOW_WIDTH = 1320;
    private final static int WINDOW_HEIGHT = 720;
    
    // MVC components
    private DataModel m_model;
    private DataViewState m_viewState;
    private DataRenderer m_renderer;
    private Draw m_window;
    private PlotData m_plotData = null;
    
    /**
     * Constructs the DataViewer application.
     * @param dataFile Path to the CSV data file
     * @throws FileNotFoundException if the data file cannot be found
     */
    public DataViewerApp(String dataFile) throws FileNotFoundException {
        // Initialize components
        m_model = new DataModel(dataFile);
        m_viewState = new DataViewState();
        m_renderer = new DataRenderer();
        
        // Load data from file
        m_model.loadData();
        
        // Initialize state with default selections
        // NOTE: Observer not registered yet, so these won't trigger updates
        if (!m_model.getStates().isEmpty()) {
            m_viewState.setSelectedState(m_model.getStates().first());
        }
        
        if (!m_model.getYears().isEmpty()) {
            m_viewState.setSelectedStartYear(m_model.getYears().first());
            m_viewState.setSelectedEndYear(m_model.getYears().last());
        }
        
        // Set initial visualization strategy
        updateVisualizationStrategy();
        
        // Setup window BEFORE registering observer
        m_window = new Draw(WINDOW_TITLE);
        m_window.setCanvasSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        m_window.enableDoubleBuffering();
        m_window.addListener(this);
        
        // NOW register as observer (after window is ready)
        m_viewState.addObserver(this);
        
        // Initial render
        update();
    }
    
    /**
     * Observer pattern callback - automatically invoked when state changes.
     * This eliminates the need for manual update flag tracking.
     * @param state The updated state object
     */
    @Override
    public void onStateChanged(DataViewState state) {
        System.out.println("INFO: State changed, updating display...");
        
        // Update visualization strategy when visualization mode changes
        updateVisualizationStrategy();
        
        // Regenerate plot data if we're in data view mode or have existing data
        if (state.getGuiMode() == DataViewState.GUI_MODE_DATA || m_plotData != null) {
            m_plotData = m_model.generatePlotData(state);
        }
        
        // Update the display
        update();
    }
    
    /**
     * Updates the visualization strategy based on current state.
     * This is part of the Strategy pattern implementation.
     */
    private void updateVisualizationStrategy() {
        String visMode = m_viewState.getSelectedVisualization();
        VisualizationStrategy strategy;
        
        if (visMode.equals(DataViewState.VISUALIZATION_MODES[DataViewState.VISUALIZATION_RAW_IDX])) {
            strategy = new RawVisualization();
        } else {
            strategy = new ExtremaVisualization();
        }
        
        m_renderer.setVisualizationStrategy(strategy);
    }
    
    /**
     * Updates the display based on current GUI mode.
     */
    public void update() {
        if (m_viewState.getGuiMode() == DataViewState.GUI_MODE_MAIN_MENU) {
            m_renderer.drawMainMenu(m_window, m_viewState);
        } else if (m_viewState.getGuiMode() == DataViewState.GUI_MODE_DATA) {
            if (m_plotData != null) {
                m_renderer.drawPlotData(m_window, m_viewState, m_plotData);
            }
        }
        m_window.show();
    }
    
    /**
     * Handles keyboard input from the user.
     * All state changes automatically trigger the Observer pattern.
     */
    @Override
    public void keyPressed(int key) {
        // Handle quit
        if (key == 'Q') {
            System.out.println("INFO: User quit the application");
            System.exit(0);
        }
        
        // Handle mode-specific input
        if (m_viewState.getGuiMode() == DataViewState.GUI_MODE_MAIN_MENU) {
            handleMainMenuInput(key);
        } else if (m_viewState.getGuiMode() == DataViewState.GUI_MODE_DATA) {
            if (key == 'M') {
                m_viewState.setGuiMode(DataViewState.GUI_MODE_MAIN_MENU);
            }
        }
        // Note: No manual update calls needed - Observer pattern handles it!
    }
    
    /**
     * Handles input when in main menu mode.
     * @param key The key that was pressed
     */
    private void handleMainMenuInput(int key) {
        if (key == 'P') {
            // Plot data
            m_viewState.setGuiMode(DataViewState.GUI_MODE_DATA);
            
        } else if (key == 'C') {
            // Change country
            Object selected = JOptionPane.showInputDialog(
                null, 
                "Choose the country", 
                "Input", 
                JOptionPane.INFORMATION_MESSAGE, 
                null,
                m_model.getCountries().toArray(), 
                m_viewState.getSelectedCountry()
            );
            if (selected != null) {
                m_viewState.setSelectedCountry((String)selected);
            }
            
        } else if (key == 'T') {
            // Change state
            Object selected = JOptionPane.showInputDialog(
                null, 
                "Choose the state", 
                "Input", 
                JOptionPane.INFORMATION_MESSAGE, 
                null,
                m_model.getStates().toArray(), 
                m_viewState.getSelectedState()
            );
            if (selected != null) {
                m_viewState.setSelectedState((String)selected);
            }
            
        } else if (key == 'S') {
            // Change start year
            Object selected = JOptionPane.showInputDialog(
                null, 
                "Choose the start year", 
                "Input", 
                JOptionPane.INFORMATION_MESSAGE, 
                null,
                m_model.getYears().toArray(), 
                m_viewState.getSelectedStartYear()
            );
            if (selected != null) {
                m_viewState.setSelectedStartYear((Integer)selected);
            }
            
        } else if (key == 'E') {
            // Change end year
            Object selected = JOptionPane.showInputDialog(
                null, 
                "Choose the end year", 
                "Input", 
                JOptionPane.INFORMATION_MESSAGE, 
                null,
                m_model.getYears().toArray(), 
                m_viewState.getSelectedEndYear()
            );
            if (selected != null) {
                m_viewState.setSelectedEndYear((Integer)selected);
            }
            
        } else if (key == 'V') {
            // Change visualization mode
            Object selected = JOptionPane.showInputDialog(
                null, 
                "Choose the visualization", 
                "Input", 
                JOptionPane.INFORMATION_MESSAGE, 
                null,
                DataViewState.VISUALIZATION_MODES, 
                m_viewState.getSelectedVisualization()
            );
            if (selected != null) {
                m_viewState.setSelectedVisualization((String)selected);
            }
        }
    }
    
    // DrawListener methods (required but unused)
    @Override
    public void keyReleased(int key) {}
    
    @Override
    public void keyTyped(char key) {}
    
    @Override
    public void mouseClicked(double x, double y) {}
    
    @Override
    public void mouseDragged(double x, double y) {}
    
    @Override
    public void mousePressed(double x, double y) {}
    
    @Override
    public void mouseReleased(double x, double y) {}
}


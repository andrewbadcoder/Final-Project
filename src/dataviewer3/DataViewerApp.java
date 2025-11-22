package dataviewer3;

import java.io.FileNotFoundException;
import edu.du.dudraw.Draw;
import edu.du.dudraw.DrawListener;

public class DataViewerApp implements DrawListener {

    private final static String WINDOW_TITLE = "DataViewer Application";
    private final static int WINDOW_WIDTH = 1320;
    private final static int WINDOW_HEIGHT = 720;

    // Core MVC components
    private DataModel m_model;
    private DataViewState m_viewState;
    private DataRenderer m_renderer;
    private Draw m_window;
    
    // NEW: The Current State
    private AppState m_currentState;

    public DataViewerApp(String dataFile) throws FileNotFoundException {
        m_model = new DataModel(dataFile);
        m_viewState = new DataViewState();
        m_renderer = new DataRenderer();
        
        m_model.loadData();

        // Init defaults
        if (!m_model.getStates().isEmpty()) {
            m_viewState.setSelectedState(m_model.getStates().first());
        }
        if (!m_model.getYears().isEmpty()) {
            m_viewState.setSelectedStartYear(m_model.getYears().first());
            m_viewState.setSelectedEndYear(m_model.getYears().last());
        }

        m_window = new Draw(WINDOW_TITLE);
        m_window.setCanvasSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        m_window.enableDoubleBuffering();
        m_window.addListener(this);
        
        // NEW: Initialize into the Main Menu State
        m_currentState = new MainMenuState();
        
        update();
    }
    
    public void update() {
        // Delegate drawing to the current state
        m_currentState.updateView(this);
        m_window.show();
    }
    
    // NEW: Allow states to change the application state
    public void setState(AppState newState) {
        this.m_currentState = newState;
    }

    // --- Accessors for the State classes to use ---
    public DataModel getModel() { return m_model; }
    public DataViewState getViewState() { return m_viewState; }
    public DataRenderer getRenderer() { return m_renderer; }
    public Draw getWindow() { return m_window; }

    // --- DrawListener Implementation ---

    @Override
    public void keyPressed(int key) {
        // Global key handling (Quit)
        if (key == 'Q') {
            System.out.println("INFO: User quit the application");
            System.exit(0);
        }

        // Delegate specific input to the current state
        m_currentState.handleInput(this, key);
    }

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
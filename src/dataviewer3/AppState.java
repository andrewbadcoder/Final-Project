package dataviewer3;

public interface AppState {
    // Handle keyboard input specific to this state
    void handleInput(DataViewerApp app, int key);
    
    // Draw the view specific to this state
    void updateView(DataViewerApp app);
}

package dataviewer3;

public class PlotDataState implements AppState {

    @Override
    public void handleInput(DataViewerApp app, int key) {
        if (key == 'M') {
            // STATE CHANGE: Return to Main Menu
            app.setState(new MainMenuState());
            app.update();
        }
    }

    @Override
    public void updateView(DataViewerApp app) {
        // Calculate data on the fly (or you could cache it)
        PlotData plotData = app.getModel().generatePlotData(app.getViewState());
        
        // Delegate to renderer to draw the plot
        app.getRenderer().drawPlotData(app.getWindow(), app.getViewState(), plotData);
    }
}
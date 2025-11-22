package dataviewer3;

import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

public class MainMenuState implements AppState {

    @Override
    public void handleInput(DataViewerApp app, int key) {
        
        DataModel model = app.getModel();
        DataViewState viewState = app.getViewState();

        if (key == 'C') {
            Object selectedValue = JOptionPane.showInputDialog(
                null, "Choose the country", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                model.getCountries().toArray(), viewState.getSelectedCountry()
            );
            
            if (selectedValue != null) {
                String country = (String) selectedValue;
                if (!viewState.getSelectedCountry().equals(country)) {
                    viewState.setSelectedCountry(country);
                    try {
                        model.loadData();
                        // Reset bounds
                        if (!model.getStates().isEmpty()) viewState.setSelectedState(model.getStates().first());
                        if (!model.getYears().isEmpty()) {
                            viewState.setSelectedStartYear(model.getYears().first());
                            viewState.setSelectedEndYear(model.getYears().last());
                        }
                        app.update(); // Redraw
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } 
        else if (key == 'T') {
            Object selectedValue = JOptionPane.showInputDialog(
                null, "Choose the state", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                model.getStates().toArray(), viewState.getSelectedState()
            );
            if (selectedValue != null) {
                viewState.setSelectedState((String) selectedValue);
                app.update();
            }
        } 
        else if (key == 'S') {
            Object selectedValue = JOptionPane.showInputDialog(
                null, "Choose the start year", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                model.getYears().toArray(), viewState.getSelectedStartYear()
            );
            if (selectedValue != null) {
                Integer year = (Integer) selectedValue;
                if (year.compareTo(viewState.getSelectedEndYear()) <= 0) {
                    viewState.setSelectedStartYear(year);
                    app.update();
                }
            }
        } 
        else if (key == 'E') {
            Object selectedValue = JOptionPane.showInputDialog(
                null, "Choose the end year", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                model.getYears().toArray(), viewState.getSelectedEndYear()
            );
            if (selectedValue != null) {
                Integer year = (Integer) selectedValue;
                if (year.compareTo(viewState.getSelectedStartYear()) >= 0) {
                    viewState.setSelectedEndYear(year);
                    app.update();
                }
            }
        } 
        else if (key == 'V') {
             Object selectedValue = JOptionPane.showInputDialog(
                null, "Choose the visualization mode", "Input",
                JOptionPane.INFORMATION_MESSAGE, null,
                DataViewState.VISUALIZATION_MODES, viewState.getSelectedVisualization()
            );
            if (selectedValue != null) {
                viewState.setSelectedVisualization((String) selectedValue);
                app.update();
            }
        } 
        else if (key == 'P') {
            // STATE CHANGE: Switch to Plot Mode
            app.setState(new PlotDataState());
            app.update();
        }
    }

    @Override
    public void updateView(DataViewerApp app) {
        // Delegate to the renderer to draw the main menu
        app.getRenderer().drawMainMenu(app.getWindow(), app.getViewState());
    }
}

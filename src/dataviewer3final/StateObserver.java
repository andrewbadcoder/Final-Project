package dataviewer3final;

/**
 * Observer interface for components that need to be notified
 * when the application state changes.
 * Part of the Observer design pattern implementation.
 */
public interface StateObserver {
    /**
     * Called when the observed state has changed.
     * Observers should update themselves in response to state changes.
     * 
     * @param state The updated state object
     */
    void onStateChanged(DataViewState state);
}

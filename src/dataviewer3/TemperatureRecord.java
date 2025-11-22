package dataviewer3;

/**
 * A data object (POJO) that represents a single parsed row of data 
 * from the CSV file.
 */
public class TemperatureRecord {

    // Private fields to hold the data from one row
    private final Integer year;
    private final Integer month;
    private final Double temperature;
    private final String state;
    private final String country;
    
    /**
     * Constructs a new TemperatureRecord.
     * @param year The year of the reading.
     * @param month The month of the reading (1-12).
     * @param temperature The temperature reading.
     * @param state The state of the reading.
     * @param country The country of the reading.
     */
    public TemperatureRecord(Integer year, Integer month, Double temperature, String state, String country) {
        this.year = year;
        this.month = month;
        this.temperature = temperature;
        this.state = state;
        this.country = country;
    }

    // Public getters to access the data
    
    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Double getTemperature() {
        return temperature;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
}

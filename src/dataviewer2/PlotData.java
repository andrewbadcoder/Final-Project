package dataviewer2;

import java.util.ArrayList;
import java.util.List;

/**
 * PlotData class encapsulates temperature data for visualization.
 * This class stores and manages temperature records for a specific state
 * across a range of years and months, supporting both Raw and Extrema views.
 */
public class PlotData {
    
    // Instance variables
    private String countryName;
    private String stateName;
    private int startYear;
    private int endYear;
    private List<TemperatureRecord> temperatureData;
    private double[][] monthlyData;  // [year_index][month_index]
    private double[] monthlyMinTemps;  // minimum temp for each month across all years
    private double[] monthlyMaxTemps;  // maximum temp for each month across all years
    
    /**
     * Constructor for PlotData
     * @param countryName The country for the data
     * @param stateName The state/territory for the data
     * @param startYear Starting year for the plot range
     * @param endYear Ending year for the plot range
     */
    public PlotData(String countryName, String stateName, int startYear, int endYear) {
        this.countryName = countryName;
        this.stateName = stateName;
        this.startYear = startYear;
        this.endYear = endYear;
        this.temperatureData = new ArrayList<>();
        
        int yearRange = endYear - startYear + 1;
        this.monthlyData = new double[yearRange][12];
        this.monthlyMinTemps = new double[12];
        this.monthlyMaxTemps = new double[12];
        
        // Initialize min/max arrays
        for (int i = 0; i < 12; i++) {
            monthlyMinTemps[i] = Double.MAX_VALUE;
            monthlyMaxTemps[i] = Double.MIN_VALUE;
        }
    }
    
    /**
     * Adds a temperature record to the plot data
     * @param year The year of the temperature reading
     * @param month The month (1-12) of the temperature reading
     * @param temperature The temperature value
     */
    public void addTemperatureRecord(int year, int month, double temperature) {
        if (year < startYear || year > endYear) {
            throw new IllegalArgumentException("Year out of range: " + year);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month out of range: " + month);
        }
        
        // Add to temperature data list
        temperatureData.add(new TemperatureRecord(year, month, temperature));
        
        // Update monthly data array
        int yearIndex = year - startYear;
        int monthIndex = month - 1;
        monthlyData[yearIndex][monthIndex] = temperature;
        
        // Update min/max tracking
        if (temperature < monthlyMinTemps[monthIndex]) {
            monthlyMinTemps[monthIndex] = temperature;
        }
        if (temperature > monthlyMaxTemps[monthIndex]) {
            monthlyMaxTemps[monthIndex] = temperature;
        }
    }
    
    /**
     * Gets the temperature for a specific year and month
     * @param year The year
     * @param month The month (1-12)
     * @return The temperature value
     */
    public double getTemperature(int year, int month) {
        if (year < startYear || year > endYear) {
            throw new IllegalArgumentException("Year out of range: " + year);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month out of range: " + month);
        }
        
        int yearIndex = year - startYear;
        int monthIndex = month - 1;
        return monthlyData[yearIndex][monthIndex];
    }
    
    /**
     * Checks if a temperature value is an extrema (within 10% of monthly min or max)
     * @param year The year
     * @param month The month (1-12)
     * @return true if the temperature is an extrema, false otherwise
     */
    public boolean isExtrema(int year, int month) {
        double temp = getTemperature(year, month);
        int monthIndex = month - 1;
        
        double minTemp = monthlyMinTemps[monthIndex];
        double maxTemp = monthlyMaxTemps[monthIndex];
        double range = maxTemp - minTemp;
        
        // Check if within 10% of the extremes
        double lowerBound = minTemp + (range * 0.1);
        double upperBound = maxTemp - (range * 0.1);
        
        return (temp <= lowerBound || temp >= upperBound);
    }
    
    /**
     * Determines if a temperature is a hot extrema
     * @param year The year
     * @param month The month (1-12)
     * @return true if temperature is in the hot extreme range
     */
    public boolean isHotExtrema(int year, int month) {
        if (!isExtrema(year, month)) {
            return false;
        }
        
        double temp = getTemperature(year, month);
        int monthIndex = month - 1;
        double maxTemp = monthlyMaxTemps[monthIndex];
        double range = monthlyMaxTemps[monthIndex] - monthlyMinTemps[monthIndex];
        double upperBound = maxTemp - (range * 0.1);
        
        return temp >= upperBound;
    }
    
    /**
     * Determines if a temperature is a cold extrema
     * @param year The year
     * @param month The month (1-12)
     * @return true if temperature is in the cold extreme range
     */
    public boolean isColdExtrema(int year, int month) {
        if (!isExtrema(year, month)) {
            return false;
        }
        
        double temp = getTemperature(year, month);
        int monthIndex = month - 1;
        double minTemp = monthlyMinTemps[monthIndex];
        double range = monthlyMaxTemps[monthIndex] - monthlyMinTemps[monthIndex];
        double lowerBound = minTemp + (range * 0.1);
        
        return temp <= lowerBound;
    }
    
    /**
     * Gets the minimum temperature for a specific month across all years
     * @param month The month (1-12)
     * @return The minimum temperature
     */
    public double getMonthlyMinTemp(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month out of range: " + month);
        }
        return monthlyMinTemps[month - 1];
    }
    
    /**
     * Gets the maximum temperature for a specific month across all years
     * @param month The month (1-12)
     * @return The maximum temperature
     */
    public double getMonthlyMaxTemp(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month out of range: " + month);
        }
        return monthlyMaxTemps[month - 1];
    }
    
    /**
     * Calculates the overall minimum temperature across all data
     * @return The overall minimum temperature
     */
    public double getOverallMinTemp() {
        double min = Double.MAX_VALUE;
        for (double monthMin : monthlyMinTemps) {
            if (monthMin < min) {
                min = monthMin;
            }
        }
        return min;
    }
    
    /**
     * Calculates the overall maximum temperature across all data
     * @return The overall maximum temperature
     */
    public double getOverallMaxTemp() {
        double max = Double.MIN_VALUE;
        for (double monthMax : monthlyMaxTemps) {
            if (monthMax > max) {
                max = monthMax;
            }
        }
        return max;
    }
    
    // Getters
    public String getCountryName() {
        return countryName;
    }
    
    public String getStateName() {
        return stateName;
    }
    
    public int getStartYear() {
        return startYear;
    }
    
    public int getEndYear() {
        return endYear;
    }
    
    public int getYearRange() {
        return endYear - startYear + 1;
    }
    
    public List<TemperatureRecord> getTemperatureData() {
        return new ArrayList<>(temperatureData);  // Return a copy for encapsulation
    }
    
    /**
     * Gets all temperature data for a specific year
     * @param year The year
     * @return Array of 12 temperature values (one per month)
     */
    public double[] getYearData(int year) {
        if (year < startYear || year > endYear) {
            throw new IllegalArgumentException("Year out of range: " + year);
        }
        
        int yearIndex = year - startYear;
        return monthlyData[yearIndex].clone();
    }
    
    /**
     * Gets all temperature data for a specific month across all years
     * @param month The month (1-12)
     * @return Array of temperature values for the specified month
     */
    public double[] getMonthData(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month out of range: " + month);
        }
        
        int monthIndex = month - 1;
        int yearRange = getYearRange();
        double[] monthData = new double[yearRange];
        
        for (int i = 0; i < yearRange; i++) {
            monthData[i] = monthlyData[i][monthIndex];
        }
        
        return monthData;
    }
    
    /**
     * Clears all temperature data
     */
    public void clearData() {
        temperatureData.clear();
        monthlyData = new double[getYearRange()][12];
        
        for (int i = 0; i < 12; i++) {
            monthlyMinTemps[i] = Double.MAX_VALUE;
            monthlyMaxTemps[i] = Double.MIN_VALUE;
        }
    }
    
    @Override
    public String toString() {
        return String.format("PlotData[country=%s, state=%s, years=%d-%d, records=%d]",
                countryName, stateName, startYear, endYear, temperatureData.size());
    }
    
    /**
     * Inner class to represent a single temperature record
     */
    public static class TemperatureRecord {
        private final int year;
        private final int month;
        private final double temperature;
        
        public TemperatureRecord(int year, int month, double temperature) {
            this.year = year;
            this.month = month;
            this.temperature = temperature;
        }
        
        public int getYear() {
            return year;
        }
        
        public int getMonth() {
            return month;
        }
        
        public double getTemperature() {
            return temperature;
        }
        
        @Override
        public String toString() {
            return String.format("TempRecord[%d/%d: %.2f°C]", year, month, temperature);
        }
    }
}

package dataviewer2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class DataModel {

    private static final int FILE_COUNTRY_IDX = 4;
    private static final int FILE_DATE_IDX = 0;
    private static final int FILE_NUM_COLUMNS = 5;
    private static final int FILE_STATE_IDX = 3;
    private static final int FILE_TEMPERATURE_IDX = 1;
   
    private final String m_dataFile;
    private List<TemperatureRecord> m_dataRaw;
    private SortedSet<String> m_dataStates;
    private SortedSet<String> m_dataCountries;
    private SortedSet<Integer> m_dataYears;

    public DataModel(String dataFile) {
        m_dataFile = dataFile;
        // Initialize collections
        m_dataRaw = new ArrayList<>();
        m_dataStates = new TreeSet<>();
        m_dataCountries = new TreeSet<>();
        m_dataYears = new TreeSet<>();
    }

    public void loadData() throws FileNotFoundException {
        // Reset collections
        m_dataRaw.clear();
        m_dataStates.clear();
        m_dataCountries.clear();
        m_dataYears.clear();

        System.out.println("INFO: Loading data from " + m_dataFile + "...");
        
        try (Scanner scanner = new Scanner(new File(m_dataFile))) {
            boolean skipFirst = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (!skipFirst) {
                    TemperatureRecord record = getRecordFromLine(line);
                    if (record != null) {
                        m_dataRaw.add(record);
                        // Add to our sets for the UI menus
                        m_dataStates.add(record.getState());
                        m_dataCountries.add(record.getCountry());
                        m_dataYears.add(record.getYear());
                    }
                } else {
                    skipFirst = false;
                }
            }
            
            System.out.println(String.format("INFO: Loaded %d total data records", m_dataRaw.size()));
            System.out.println(String.format("INFO: Found data for %d countries", m_dataCountries.size()));
            System.out.println(String.format("INFO: Found data for %d states", m_dataStates.size()));
            System.out.println(String.format("INFO: Found data for %d years [%d, %d]", m_dataYears.size(), m_dataYears.first(), m_dataYears.last()));
        }
    }

    private TemperatureRecord getRecordFromLine(String line) {
        List<String> rawValues = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                rawValues.add(rowScanner.next());
            }
        }

        if (rawValues.size() != FILE_NUM_COLUMNS) {
            // System.out.println(String.format("TRACE: malformed line '%s'...skipping", line));
            return null;
        }

        try {
            // Parse values
            Integer year = parseYear(rawValues.get(FILE_DATE_IDX));
            Integer month = parseMonth(rawValues.get(FILE_DATE_IDX));
            
            if (year == null || month == null) {
                return null; // Skip if date is bad
            }
            
            double temp = Double.parseDouble(rawValues.get(FILE_TEMPERATURE_IDX));
            String state = rawValues.get(FILE_STATE_IDX);
            String country = rawValues.get(FILE_COUNTRY_IDX);

            return new TemperatureRecord(year, month, temp, state, country);

        } catch (NumberFormatException e) {
            // System.out.println(String.format("TRACE: unable to parse data line, skipping...'%s'", line));
            return null;
        }
    }

    public PlotData generatePlotData(DataViewState state) {
        System.out.println("INFO: Generating plot data...");
        PlotData plotData = new PlotData();
        
        String selectedState = state.getSelectedState();
        String selectedCountry = state.getSelectedCountry();
        int startYear = state.getSelectedStartYear();
        int endYear = state.getSelectedEndYear();

        for (TemperatureRecord rec : m_dataRaw) {
            // Check if this record matches ALL filter criteria
            if (rec.getCountry().equals(selectedCountry) &&
                rec.getState().equals(selectedState) &&
                rec.getYear() >= startYear && 
                rec.getYear() <= endYear) {

                int month = rec.getMonth();
                double value = rec.getTemperature();

                // Update min/max values for this month
                if (value < plotData.m_plotMonthlyMinValue.get(month)) {
                    plotData.m_plotMonthlyMinValue.put(month, value);
                }
                if (value > plotData.m_plotMonthlyMaxValue.get(month)) {
                    plotData.m_plotMonthlyMaxValue.put(month, value);
                }

                // Add the data point to the plot
                plotData.m_plotData.get(month).put(rec.getYear(), value);
            }
        }
        System.out.println("INFO: Plot data generation complete.");
        // System.out.println("DEBUG: plot data: " + plotData.m_plotData.toString());
        return plotData;
    }

    private Integer parseYear(String dateString) {
        Integer ret = null;
        if (dateString.indexOf("/") != -1) { // M/D/Y
            String[] parts = dateString.split("/");
            if (parts.length == 3) ret = Integer.parseInt(parts[2]);
        } else if (dateString.indexOf("-") != -1) { // Y-M-D
            String[] parts = dateString.split("-");
            if (parts.length == 3) ret = Integer.parseInt(parts[0]);
        }
        if (ret == null) {
            // System.out.println(String.format("TRACE: Unable to parse year from date: '%s'", dateString));
        }
        return ret;
    }

    private Integer parseMonth(String dateString) {
        Integer ret = null;
        if (dateString.indexOf("/") != -1) { // M/D/Y
            String[] parts = dateString.split("/");
            if (parts.length == 3) ret = Integer.parseInt(parts[0]);
        } else if (dateString.indexOf("-") != -1) { // Y-M-D
            String[] parts = dateString.split("-");
            if (parts.length == 3) ret = Integer.parseInt(parts[1]);
        }
        if (ret == null || ret < 1 || ret > 12) {
            // System.out.println(String.format("TRACE: Unable to parse month from date: '%s'", dateString));
            return null;
        }
        return ret;
    }

    
    public SortedSet<String> getStates() {
        return m_dataStates;
    }
    
    public SortedSet<String> getCountries() {
        return m_dataCountries;
    }
    
    public SortedSet<Integer> getYears() {
        return m_dataYears;
    }
}

package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a collection of geographical areas and provides methods for managing and interacting with them.
 */
public class AreaList {
    /** The list of geographical areas. */
    private final List<Area> areas;

    /**
     * Initializes an AreaList by populating it with data from the file CoordinateMonitoraggio.dati.
     * The constructor retrieves the list of areas from the designated file.
     */
    public AreaList() {this.areas = getAreaList();}

    /**
     * Constructs an instance of the AreaList class by converting a provided list of areas into
     * the ClimateMonitor.AreaList format.
     *
     * @param al The list of areas to be included in the AreaList.
     */
    public AreaList(List<Area> al){
        this.areas = al;
    }

    /**
     * Reads and deserializes data from the file specified in the filePath.
     *
     * @return A list of areas containing the deserialized data.
     */
    public List<Area> getAreaList() {
        List<Area> areas = new ArrayList<>();

        String filePath = "../data/CoordinateMonitoraggio.dati";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] values = line.split("\t");
                String[] coordinates = values[2].trim().split(",");

                Area area = new Area(values[0], values[1], Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
                areas.add(area);
            }
            reader.close();
        } catch (Exception e) {
            new Dialog(Dialog.type.ERR, "Failed to read areas from " + filePath);
            e.printStackTrace();
        }
        return areas;
    }

    /**
     * Searches for areas based on the provided search string. The search is case-insensitive.
     * If the search string is a pair of coordinates (latitude, longitude), the function
     * returns the area located at those coordinates. If no such area is found, it returns
     * a list of areas close to the provided coordinates.
     *
     * @param str The search string containing either text or coordinates (latitude, longitude) to search for.
     * @return A list of areas matching the provided search string.
     */
    public List<Area> search(String str) {
        List<Area> result = new ArrayList<>(this.areas);

        if (str.contains(",")){
            String[] stringCrds = str.split(",");
            double[] coordinates;
            try {
                coordinates = Arrays.stream(stringCrds).mapToDouble(Double::parseDouble).toArray();
            }
            catch (Exception e){
                new Dialog(Dialog.type.ERR, "Incorrect coordinates");
                return result;
            }

            Area areaAtCrds = areaAtCoordinates(coordinates);
            if (areaAtCrds != null) {
                result.clear();
                result.add(areaAtCrds);
                return result;
            }

            result.removeIf(area -> !(area.isClose(coordinates)));
            return result;
        }

        result.removeIf(area -> !(area.contains(str)));
        return result;
    }

    /**
     * Searches the ClimateMonitor.AreaList for an area located at the specified coordinates.
     *
     * @param cords Coordinates in the format [latitude, longitude].
     * @return The area located at the provided coordinates, or {@code null} if no such area exists.
     */
    public Area areaAtCoordinates(double[] cords) {
        for (Area value : this.areas) {
            if (value.isAtCoordinates(cords)) {
                return value;
            }
        }

        return null;
    }

    /**
     * Converts the provided list of areas into a two-dimensional array of strings.
     *
     * @return A two-dimensional string array representing the area list.
     */
    public String[][] toStringArray() {
        if (this.areas == null)
            return new String[0][];
        String[][] result = new String[areas.size()][];

        for (int i = 0; i < areas.size(); i++) {
            result[i] = areas.get(i).toStringArray();
        }

        return result;
    }

}

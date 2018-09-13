import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MoCapMarkers {

    private ArrayList<ArrayList<Double>> coordinates;
    private ArrayList<String> markers;
    private String[] markersArray;

    MoCapMarkers() {
        super();
    }

    MoCapMarkers(String[] markersXYZ,String[] markersArray) {
        super();
        this.coordinates = new ArrayList<ArrayList<Double>>();
        this.markersArray = markersArray;
        for(String S: markersXYZ) {
            coordinates.add(new ArrayList<Double>());
        }
        this.markers = new ArrayList<>(Arrays.asList(markersXYZ));

    }

    public boolean comCacl() {
        if(markers.contains("COM-X")) {
            return false;
        }
        else {
            for(int i = 0; i<3; i++) {
                coordinates.add(new ArrayList<Double>());
            }
            for (int i = 0; i < getCoordinate("LIAS-X").size(); i++) {
                coordinates.get(coordinates.size() - 3).add((getCoordinate("LIAS-X").get(i) + getCoordinate("RIAS-X").get(i) + getCoordinate("LIPS-X").get(i) + getCoordinate("RIPS-X").get(i)) / 4);
                coordinates.get(coordinates.size() - 2).add((getCoordinate("LIAS-Y").get(i) + getCoordinate("RIAS-Y").get(i) + getCoordinate("LIPS-Y").get(i) + getCoordinate("RIPS-Y").get(i)) / 4);
                coordinates.get(coordinates.size() - 1).add((getCoordinate("LIAS-Z").get(i) + getCoordinate("RIAS-Z").get(i) + getCoordinate("LIPS-Z").get(i) + getCoordinate("RIPS-Z").get(i)) / 4);
            }
            markers.add("COM-X");
            markers.add("COM-Y");
            markers.add("COM-Z");
            String[] tempArray = new String[markersArray.length+1];

            return true;
        }
    }


    public void addCoordinates(ArrayList<Double> coordinatesRow) {
        for(int i=0; i<coordinatesRow.size(); i++) {
            coordinates.get(i).add(coordinatesRow.get(i));
        }
    }

    public ArrayList<Double> getCoordinate(String marker) {
        ArrayList<Double> toReturn = new ArrayList<Double>();

        for (int i = 0; i < markers.size(); i++) {
            if (marker.equals(markers.get(i))) {
                toReturn = coordinates.get(i);
                break;
            }
            else
                continue;
        }
        return toReturn;
    }

    public void setCoordinates(ArrayList<ArrayList<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    public ArrayList<String> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<String> markers) {
        this.markers = markers;
    }

    public String[] getMarkersArray() {
        return markersArray;
    }

    public void setMarkersArray(String[] markersArray) {
        this.markersArray = markersArray;
    }
}

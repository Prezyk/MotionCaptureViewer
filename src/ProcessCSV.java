import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessCSV {

    public static String[] checkMarkers(String filePath) {
        String[] line;
        ArrayList<String> markers = new ArrayList<>();
        int size = 0;
        String[] tempMark;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            for (int i = 0; i < 4; i++) {
                if (i < 3) {
                    br.readLine();
                    continue;
                }
                else if(i==3){
                    line = br.readLine().split(",");
                    for (int j = 0; j < line.length; j++) {
                        if (line[j].split(":").length<2) {
                            continue;
                        } else if (markers.contains(line[j].split(":")[1])) {
                            continue;
                        } else {
                            markers.add(line[j].split(":")[1]);
                        }
                    }
                }
            }
        } catch(FileNotFoundException e){
                e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] markersArray = new String[markers.size()];
        for(int i=0; i<markers.size(); i++) {
            markersArray[i] = markers.get(i);
        }
        return markersArray;
    }



    public static MoCapMarkers readCSV(String filePath, String[] markersNames) {
        MoCapMarkers moCapMarkers = new MoCapMarkers();
        moCapMarkers.setMarkersArray(markersNames);
        String line;
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        ArrayList<String> tempList = new ArrayList<String>();

        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            int i = 0;
            while ((line = br.readLine()) != null) {
                if(i < 7) {
                    if(i==3) {
                        ArrayList<String> namesLine = new ArrayList<String>(Arrays.asList(line.split(",")));
                        int k=0;
                        for(int j=0; j<namesLine.size(); j++) {
                            if(j==0) {
                                continue;
                            }
                            else if(j==1) {
                                tempList.add("Time");
                                indexes.add(1);
                            }
                            else {
                                for(String S: markersNames) {
                                    if(j==namesLine.size()) {
                                        break;
                                    }
                                    else if (S.equals(namesLine.get(j).split(":")[1])) {
                                        tempList.addAll(Arrays.asList(S + "-X", S + "-Y", S + "-Z"));
                                        indexes.addAll(Arrays.asList(j, j + 1, j + 2));
                                        j += 2;

                                    } else
                                        continue;
                                    j += 1;
                                }
                                String[] tempArray = new String[tempList.size()];
                                tempArray = tempList.toArray(tempArray);
                                moCapMarkers = new MoCapMarkers(tempArray, markersNames);

                            }
                        }
                    }
                }
                else {
                    ArrayList<String> tempDataString = new ArrayList<String>(Arrays.asList(line.split(",")));
                    ArrayList<Double> tempDataDouble = new ArrayList<Double>();
                    for (int I : indexes) {
                        if(tempDataString.get(I).isEmpty()) {
                            tempDataDouble.add(Double.NaN);
                        }
                        else {
                            tempDataDouble.add(Double.parseDouble(tempDataString.get(I)));
                        }
                    }
                    moCapMarkers.addCoordinates(tempDataDouble);
                }
                ++i;

            }

        } catch (IOException e) {
            System.out.println("File doesn't exist or wrong file name");

        }
        return moCapMarkers;

    }

    public static void saveCSV(MoCapMarkers moCapMarker, String filePath, String[] markers) {
        try {
            PrintWriter pw = new PrintWriter(new File(filePath));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < moCapMarker.getCoordinate("Time").size()+7; i++) {
                if (i < 3) {
                    sb.append(" \n");
                }
                else if (i == 3) {
                        int substringIndex=0;
                        for (int j = 0; j < markers.length+2; j++) {
                            if(j==0){
                                sb.append(",");
                            }
                            else if(j==1) {
                                sb.append(",");
                            }
                            else {
                                if (j == markers.length+1) {
                                    sb.append("Bartek:" + markers[j-2] + "," + "Bartek:" + markers[j-2] + "," + "Bartek:" + markers[j-2] + "\n");
                                    continue;
                                } else {
                                    sb.append("Bartek:" + markers[j-2] + "," + "Bartek:" + markers[j-2] + "," + "Bartek:" + markers[j-2] + ",");
                                }
                            }
                        }
                    }
                    else if(i>3 && i<6) {
                        sb.append("\n");
                     }
                    else if(i==6) {
                        int j_end = markers.length+1;
                        for(int j=0; j<j_end; j++) {
                            if(j==0)
                                sb.append("Frame,");
                            else if(j==1)
                                sb.append("Time,");
                            else
                                sb.append("X,Y,Z,");
                        }
                        sb.append("X,Y,Z\n");
                    }
                    else {
                    String tempX;
                    String tempY;
                    String tempZ;

                        for (int j = 0; j < markers.length + 2; j++) {
                            if(j==0) {
                                sb.append(i-7 + ",");
                            }
                            else if(j==1) {
                                sb.append(moCapMarker.getCoordinate("Time").get(i-7)+",");
                            }
                            else if(j==markers.length+1) {
                                tempX = moCapMarker.getCoordinate(markers[j-2]+"-X").get(i-7).toString();
                                tempY = moCapMarker.getCoordinate(markers[j-2]+"-Y").get(i-7).toString();
                                tempZ = moCapMarker.getCoordinate(markers[j-2]+"-Z").get(i-7).toString();

                                if(moCapMarker.getCoordinate(markers[j-2]+"-X").get(i-7).isNaN())
                                    tempX = "";
                                if(moCapMarker.getCoordinate(markers[j-2]+"-Y").get(i-7).isNaN())
                                    tempY = "";
                                if(moCapMarker.getCoordinate(markers[j-2]+"-Z").get(i-7).isNaN())
                                    tempZ = "";

                                sb.append(tempX + "," +
                                        tempY + "," +
                                       tempZ + "\n");
                            }
                            else {
                                tempX = moCapMarker.getCoordinate(markers[j-2]+"-X").get(i-7).toString();
                                tempY = moCapMarker.getCoordinate(markers[j-2]+"-Y").get(i-7).toString();
                                tempZ = moCapMarker.getCoordinate(markers[j-2]+"-Z").get(i-7).toString();

                                if(moCapMarker.getCoordinate(markers[j-2]+"-X").get(i-7).isNaN())
                                    tempX = "";
                                if(moCapMarker.getCoordinate(markers[j-2]+"-Y").get(i-7).isNaN())
                                    tempY = "";
                                if(moCapMarker.getCoordinate(markers[j-2]+"-Z").get(i-7).isNaN())
                                    tempZ = "";

                                sb.append(tempX + "," +
                                        tempY + "," +
                                        tempZ + ",");
                            }
                        }
                    }
                }
                pw.write(sb.toString());
                pw.close();


            }
         catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    }

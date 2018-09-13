//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class ProcessCSV_backup {
//
//    static String filePath = "E:\\Pulpit\\Projekt ZPO\\OptitrackData\\";
//
//
//    public static MoCapMarkers readCSV(String fileName, String[] markersNames) {
//        MoCapMarkers moCapMarkers = new MoCapMarkers();
//        String line;
//        ArrayList<Integer> indexes = new ArrayList<Integer>();
//        ArrayList<String> tempList = new ArrayList<String>();
//
//        try {
//
//            BufferedReader br = new BufferedReader(new FileReader(filePath + fileName));
//            int i = 0;
//            while ((line = br.readLine()) != null) {
//                if(i < 7) {
//                    if(i==3) {
//                        ArrayList<String> namesLine = new ArrayList<String>(Arrays.asList(line.split(",")));
//                        int k=0;
//                        for(int j=0; j<namesLine.size(); j++) {
//                            if(j==0) {
//                                continue;
//                            }
//                            else if(j==1) {
//                                tempList.add("Time");
//                                indexes.add(1);
//                            }
//                            else {
//                                for(String S: markersNames) {
//                                    if(j==namesLine.size()) {
//                                        break;
//                                    }
//                                    else if (S.equals(namesLine.get(j).split(":")[1])) {
//                                        tempList.addAll(Arrays.asList(S + "-X", S + "-Y", S + "-Z"));
//                                        indexes.addAll(Arrays.asList(j, j + 1, j + 2));
//                                        j += 2;
//
//                                    } else
//                                        continue;
//                                    j += 1;
//                                }
//                                String[] tempArray = new String[tempList.size()];
//                                tempArray = tempList.toArray(tempArray);
//                                moCapMarkers = new MoCapMarkers(tempArray);
//
//                            }
//                        }
//                    }
//                }
//                else {
//                    ArrayList<String> tempDataString = new ArrayList<String>(Arrays.asList(line.split(",")));
//                    ArrayList<Double> tempDataDouble = new ArrayList<Double>();
//                    for (int I : indexes) {
//                        tempDataDouble.add(Double.parseDouble(tempDataString.get(I)));
//                    }
//                    moCapMarkers.addCoordinates(tempDataDouble);
//                }
//                ++i;
//
//            }
//
//        } catch (IOException e) {
//            System.out.println("File doesn't exist or wrong file name");
//
//        }
//        return moCapMarkers;
//
//    }
//
//    public static void saveCSV(MoCapMarkers moCapMarker, String filename) {
//        try {
//            PrintWriter pw = new PrintWriter(new File(filePath + filename));
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < moCapMarker.getCoordinate("Time").size()+7; i++) {
//                if (i < 3) {
//                    sb.append(" \n");
//                }
//                else if (i == 3) {
//                        int substringIndex=0;
//                        for (int j = 0; j < moCapMarker.getMarkers().size()+1; j++) {
//                            if(j==0){
//                                sb.append("Frame,");
//                            }
//                            else if(j==1) {
//                                sb.append("Time,");
//                            }
//                            else {
//                                for (int k = 0; k < moCapMarker.getMarkers().get(j - 1).length(); k++) {
//                                    if (moCapMarker.getMarkers().get(j - 1).charAt(k) == '-') {
//                                        substringIndex = k;
//                                        break;
//                                    } else if (k == (moCapMarker.getMarkers().get(j - 1).length() - 1)) {
//                                        substringIndex = moCapMarker.getMarkers().get(j - 1).length();
//                                        continue;
//                                    } else {
//                                        continue;
//                                    }
//                                }
//
//                                if (j == moCapMarker.getMarkers().size()) {
//                                    sb.append("Bartek:"+moCapMarker.getMarkers().get(j-1).substring(0,substringIndex) + "\n");
//                                    continue;
//                                } else {
//                                    sb.append("Bartek:"+moCapMarker.getMarkers().get(j-1).substring(0, substringIndex) + ",");
//                                }
//                            }
//                        }
//                    }
//                    else if (i>3 && i<7) {
//                            sb.append("\n");
//                    }
//
//                    else {
//                        for (int j = 0; j < moCapMarker.getMarkers().size() + 1; j++) {
//                            if (j == moCapMarker.getMarkers().size()) {
//                                sb.append(Double.toString(moCapMarker.getCoordinate(moCapMarker.getMarkers().get(j - 1)).get(i-7)) + "\n");
//                            } else if (j == 0) {
//                                sb.append(i-7 + ",");
//                            } else {
//                                sb.append(Double.toString(moCapMarker.getCoordinate(moCapMarker.getMarkers().get(j - 1)).get(i-7)) + ",");
//                            }
//                        }
//                    }
//                }
//                pw.write(sb.toString());
//                pw.close();
//
//
//            }
//         catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    }

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoCapFX2 extends Application{


    private int wH = 600;
    private int wW = 700;
    final String path = "E:\\Studia\\Projekt ZPO\\Projekt ZPO\\OptitrackData";
    private String fileNameLoad;
    private String fileNameSave;
    private double T0;
    private double Tend;
    private double Tstep;
    private double T;

    private double samplPer;


    GridPane layout = new GridPane();
    GridPane gridData = new GridPane();
    GridPane gridChartAnim = new GridPane();
    GridPane gridChartAnimControls = new GridPane();
    GridPane gridMultiAnim = new GridPane();

    Label labelLoadData;
    Label labelSaveData;
    Label labelChartAnimControls;
    Label labelMultiAnim;
    Label labelT0;
    Label labelTend;
    Label labelSelMarkChartAnim;
    Label labelSelSamplPeriodChartAnim;
    Label labelTxtT0Error;
    Label labelTxtTendError;

    Button btnLoadData;
    Button btnSaveData;
    Button btnAnimPlay;
    Button btnAnimPause;
    Button btnAnimStop;

    ToggleGroup tGChartAnim1;
    ToggleGroup tGChartAnim2;

    ToggleButton tbtnChartAnimView;
    ToggleButton tbtnChartAnimTime;
    ToggleButton tbtnChartAnimOp1;
    ToggleButton tbtnChartAnimOp2;
    ToggleButton tbtnChartAnimOp3;
    ToggleButton tbtnMultiAnimOp1;
    ToggleButton tbtnMultiAnimOp2;
    ToggleButton tbtnMultiAnimOp3;

//    RangeSlider rSTime;

    Slider sliderT0;
    Slider sliderTend;


    CheckComboBox cCBLoadData;
    CheckComboBox cCBSaveData;
    CheckComboBox cCMultiAnim;


    ComboBox cBLoadFile;
    ComboBox cBSaveFile;
    ComboBox cBChartAnimMarker;
    ComboBox cBSamplPeriod;

    TextField txtLoadData;
    TextField txtSaveData;
    TextField txtT0;
    TextField txtTend;

    FileChooser fChLoad;
    FileChooser fChSave;

    ScatterChart<Number, Number> figure;

    NumberAxis x;
    NumberAxis y;

    XYChart.Series valuesChart;


    MoCapMarkers moCapMarkers;

    PathTransition ptr;

    Pane root;


    private String coordinateOX = null;
    private String coordinateOY = null;
    private boolean isAnimationLoaded = false;

    public void loadFigure() {
        try {
            coordinateOX = null;
            coordinateOY = null;
            valuesChart = new XYChart.Series();
            String selectedMarker = cBChartAnimMarker.getSelectionModel().getSelectedItem().toString();

            if (tbtnChartAnimView.isSelected()) {
                if (tbtnChartAnimOp1.isSelected()) {
                    coordinateOX = selectedMarker + "-X";
                    coordinateOY = selectedMarker + "-Y";
                    x.setLabel("X");
                    y.setLabel("Y");
                } else if (tbtnChartAnimOp2.isSelected()) {
                    coordinateOX = selectedMarker + "-Z";
                    coordinateOY = selectedMarker + "-Y";
                    x.setLabel("Z");
                    y.setLabel("Y");
                } else if (tbtnChartAnimOp3.isSelected()) {
                    coordinateOX = selectedMarker + "-X";
                    coordinateOY = selectedMarker + "-Z"; ///do sprawdzenia
                    x.setLabel("X");
                    y.setLabel("Z");
                }
            } else if (tbtnChartAnimTime.isSelected()) {
                coordinateOX = "Time";
                x.setLabel("Time");
                if (tbtnChartAnimOp1.isSelected()) {
                    coordinateOY = selectedMarker + "-X";
                    y.setLabel("X");
                } else if (tbtnChartAnimOp2.isSelected()) {
                    coordinateOY = selectedMarker + "-Y";
                    y.setLabel("Y");
                } else if (tbtnChartAnimOp3.isSelected()) {
                    coordinateOY = selectedMarker + "-Z";
                    y.setLabel("Z");
                }
            }
            for (int i = (int) sliderT0.getValue(); i <= (int) sliderTend.getValue(); i += (int) samplPer) {
                if (Double.isNaN(moCapMarkers.getCoordinate(coordinateOX).get(i)) || Double.isNaN(moCapMarkers.getCoordinate(coordinateOY).get(i)))
                    continue;
                else
                    valuesChart.getData().add(new XYChart.Data(moCapMarkers.getCoordinate(coordinateOX).get(i), moCapMarkers.getCoordinate(coordinateOY).get(i)));
            }

            figure.getData().clear();
            figure.getData().add(valuesChart);
        }catch(NullPointerException npe) {}
    }


//        ptr.play();
//        ptr.pause();
//        ptr.stop();

    public void loadAnim() {

        try {

            double animW = 250;
            double animH = 250;

            double animWStart = 325;
            double animHStart = 125;

            Circle circle = new Circle();
            circle.setRadius(5);
            circle.setFill(Color.GREEN);


            Path path = new Path();


            double signShift = Math.abs(Math.min(Collections.min(moCapMarkers.getCoordinate(coordinateOX)), Collections.min(moCapMarkers.getCoordinate(coordinateOY))));

//        double scale = Math.max(Collections.max(moCapMarkers.getCoordinate(coordinateOX)), Collections.max(moCapMarkers.getCoordinate(coordinateOY)));

            double scale = Math.max((Collections.max(moCapMarkers.getCoordinate(coordinateOX)) - Collections.min(moCapMarkers.getCoordinate(coordinateOX))),
                    (Collections.max(moCapMarkers.getCoordinate(coordinateOY)) - Collections.min(moCapMarkers.getCoordinate(coordinateOY))));
//
//        System.out.println(scale);


            for (int i = (int) sliderT0.getValue(); i < sliderTend.getValue(); i += (int) samplPer) {

                double xTrans = (moCapMarkers.getCoordinate(coordinateOX).get(i) - x.getLowerBound()) / (x.getUpperBound() - x.getLowerBound()) * animW;

                double yTrans = animH - (moCapMarkers.getCoordinate(coordinateOY).get(i) - y.getLowerBound()) / (y.getUpperBound() - y.getLowerBound()) * animH;

                if (i == sliderT0.getValue()) {
                    path.getElements().add(new MoveTo(xTrans, yTrans));
                } else {
                    path.getElements().add(new LineTo(xTrans, yTrans));
                }

            }
            System.out.println(Collections.max(moCapMarkers.getCoordinate(coordinateOX)));
            System.out.println(Collections.min(moCapMarkers.getCoordinate(coordinateOX)));

            System.out.println(Collections.max(moCapMarkers.getCoordinate(coordinateOY)));
            System.out.println(Collections.min(moCapMarkers.getCoordinate(coordinateOY)));

            System.out.println(x.getUpperBound());


            root.getChildren().clear();
            root.getChildren().add(circle);
            ptr = new PathTransition();
            ptr.setDuration(Duration.seconds((sliderTend.getValue() - sliderT0.getValue()) * Tstep));
            ptr.setPath(path);
            ptr.setNode(circle);
        }catch(NullPointerException npe) {}
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Section 1: Load/Save file

        labelLoadData = new Label("Load data");
        labelSaveData = new Label("Save data");

        labelLoadData.setFont(Font.font("Times", FontWeight.BOLD, 13));
        labelSaveData.setFont(Font.font("Times", FontWeight.BOLD, 13));


        btnLoadData = new Button("Load data");
        btnSaveData = new Button("Save data");

        cCBLoadData  = new CheckComboBox();
        cCBLoadData.setMaxWidth(80);

        cCBSaveData = new CheckComboBox();
        cCBSaveData.setMaxWidth(80);

        cBLoadFile = new ComboBox();
        cBLoadFile.setPromptText("Choose file");
        cBLoadFile.getItems().addAll("Test", "Test zapisu", "4 km/h", "6 km/h", "Choose file");

        cBSaveFile = new ComboBox();
        cBSaveFile.getItems().addAll("Test zapisu", "Choose file");
        cBSaveFile.setPromptText("Choose file");

        labelLoadData.setOpacity(80);


        cBLoadFile.setOnAction(c-> {
            switch (cBLoadFile.getSelectionModel().getSelectedIndex()) {
                case 0:
                    fileNameLoad = path + "\\Test.csv";
                    break;
                case 1:
                    fileNameLoad = path + "\\Test_zapisu.csv";
                    break;
                case 2:
                    fileNameLoad = path + "\\Bartek_threadmill_4.csv";
                    break;
                case 3:
                    fileNameLoad = path + "\\Bartek_threadmill_6.csv";
                    break;
                case 4:
                    if (fChLoad == null) {
                        fChLoad = new FileChooser();
                        fChLoad.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data file", "*.csv"));
                    }
                    Stage choiceWindow = new Stage();
                    File file = fChLoad.showOpenDialog(choiceWindow);
                    if(file!=null) {
                        fileNameLoad = file.getPath();
                        fChLoad.setInitialDirectory(file.getParentFile());
                    }
                    break;
                default:
                    cBLoadFile.getSelectionModel().clearSelection();


            }

            String[] markerNames = ProcessCSV.checkMarkers(fileNameLoad);
            cCBLoadData.getCheckModel().clearChecks();
            cCBLoadData.getItems().clear();
            cCBLoadData.getItems().add("ALL");
            List<String> markersList = Arrays.asList(ProcessCSV.checkMarkers(fileNameLoad));
            if(markersList.contains("COM")==false && markersList.contains("LIAS") && markersList.contains("RIAS") && markersList.contains("LIPS") && markersList.contains("RIPS"))
                cCBLoadData.getItems().add("COM");

            for(String S: markerNames) {
                cCBLoadData.getItems().add(S);
            }
        });

        cBSaveFile.setOnAction(c-> {
            switch (cBSaveFile.getSelectionModel().getSelectedIndex()) {
                case 0: fileNameSave = path + "\\Test_zapisu.csv";
                    break;
                case 1:
                    if(fChSave==null) {
                        fChSave = new FileChooser();
                        fChSave.getExtensionFilters().add( new FileChooser.ExtensionFilter("*.csv", "*.csv"));
                    }
                    fChSave.setInitialDirectory(fChLoad.getInitialDirectory());
                    Stage choiceWindow = new Stage();
                    File file = fChSave.showSaveDialog(choiceWindow);
                    if(file!=null) {
                        fileNameSave = file.getPath();
                    }
                    break;
                default:
                    try {
                        String[] markersToSave = new String[cCBSaveData.getCheckModel().getCheckedItems().size()];
                        for (int i = 0; i < markersToSave.length; i++) {
                            markersToSave[i] = cCBSaveData.getCheckModel().getCheckedItems().get(i).toString();
                        }
                        ProcessCSV.saveCSV(moCapMarkers, fileNameSave, markersToSave);
                    } catch(NullPointerException e) {}
                    cBSaveFile.getSelectionModel().clearSelection();
            }
        });


        btnLoadData.setOnAction(e->{
            IndexedCheckModel checkItemsTemp = cCBLoadData.getCheckModel();
            try {
                if (cCBLoadData.getCheckModel().getCheckedItems().contains("ALL")) {
                    cCBLoadData.getCheckModel().checkAll();
                    cCBLoadData.getCheckModel().clearCheck(0);
                }
                if (cCBLoadData.getCheckModel().getCheckedItems().contains("COM") && (Arrays.asList(ProcessCSV.checkMarkers(fileNameLoad)).contains("COM") == false)) {
                    cCBLoadData.getCheckModel().checkIndices(cCBLoadData.getItems().indexOf("LIPS"),
                            cCBLoadData.getItems().indexOf("RIPS"),
                            cCBLoadData.getItems().indexOf("LIAS"),
                            cCBLoadData.getItems().indexOf("RIAS"));
                }

                String[] markersArray = new String[cCBLoadData.getCheckModel().getCheckedItems().size()];

                for (int i = 0; i < markersArray.length; i++) {
                    markersArray[i] = cCBLoadData.getCheckModel().getCheckedItems().get(i).toString();
                }
                moCapMarkers = ProcessCSV.readCSV(fileNameLoad, markersArray);
                if (cCBLoadData.getCheckModel().getCheckedItems().contains("COM") && (Arrays.asList(ProcessCSV.checkMarkers(fileNameLoad)).contains("COM") == false)) {
                    moCapMarkers.comCacl();
                }
                if(moCapMarkers.getCoordinate("Time").size()>2500) {
                    cBSamplPeriod.getSelectionModel().select(4);
                }
                cCBSaveData.getCheckModel().clearChecks();
                cCBSaveData.getItems().clear();
                cCBSaveData.getItems().addAll("ALL");
                for (Object O : cCBLoadData.getCheckModel().getCheckedItems()) {
                    cCBSaveData.getItems().add(O.toString());
                }
                cCBLoadData.setCheckModel(checkItemsTemp);
                Stage dialog = new Stage();
                VBox vBox = new VBox();
                Label txtLoaded = new Label("Data load successful");
                Button btnCloseDialog = new Button("OK");
                vBox.setAlignment(Pos.CENTER);
                vBox.setSpacing(10);
                btnCloseDialog.setMinWidth(100);
                vBox.getChildren().addAll(txtLoaded, btnCloseDialog);
                Scene dialogScene = new Scene(vBox, 120, 60);
                dialog.setScene(dialogScene);
                btnCloseDialog.setOnAction(bd->{{
                    dialog.close();
                }});
                if(cCBLoadData.getCheckModel().getCheckedItems().size() != 0) {
                    dialog.show();
                }
                else {
                    txtLoaded.setText("Try again");
                    dialog.show();
                }
            }catch(NullPointerException npe) {}
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // To section 3
            try {
                for (String S : moCapMarkers.getMarkersArray()) {
                    System.out.println(S);
                }
                T0 = moCapMarkers.getCoordinate("Time").get(0);
                //Tend = moCapMarkers.getCoordinate("Time").size();
                Tstep = moCapMarkers.getCoordinate("Time").get(1) - moCapMarkers.getCoordinate("Time").get(0);
                T = moCapMarkers.getCoordinate("Time").get(moCapMarkers.getCoordinate("Time").size() - 1) / moCapMarkers.getCoordinate("Time").size();
                cBChartAnimMarker.getItems().clear();
                cBChartAnimMarker.getItems().addAll(Arrays.asList(moCapMarkers.getMarkersArray()));
                cBChartAnimMarker.getSelectionModel().selectFirst();


                sliderT0.setMin(0);
                sliderTend.setMin((int) samplPer);

                sliderT0.setMax(moCapMarkers.getCoordinate("Time").size() - 2 * (int) samplPer);
                sliderTend.setMax(moCapMarkers.getCoordinate("Time").size() - (int) samplPer);

                sliderT0.setBlockIncrement((int) samplPer);
                sliderTend.setBlockIncrement((int) samplPer);

                sliderT0.setValue(sliderT0.getMin());
                sliderTend.setValue(sliderTend.getMax());

                txtT0.setText(String.valueOf(sliderT0.getValue() * Tstep));
                txtTend.setText(String.valueOf(sliderTend.getValue() * Tstep));

                sliderT0.setMajorTickUnit((int) samplPer);
                sliderTend.setMajorTickUnit((int) samplPer);

                sliderT0.setMinorTickCount(0);
                sliderTend.setMinorTickCount(0);

                sliderT0.setSnapToTicks(true);
                sliderTend.setSnapToTicks(true);
            }catch(NullPointerException npe) {}

            /////////////////////////////////////////////////////////////////////////

            try {
                figure.getData().removeAll(figure.getData());
                tbtnChartAnimView.setSelected(false);
                tbtnChartAnimTime.setSelected(false);
                tbtnChartAnimOp1.setSelected(false);
                tbtnChartAnimOp2.setSelected(false);
                tbtnChartAnimOp3.setSelected(false);
                tbtnChartAnimOp1.setText("");
                tbtnChartAnimOp2.setText("");
                tbtnChartAnimOp3.setText("");
                ptr.stop();
                ptr = new PathTransition();
                ptr.setNode(null);
                ptr.setPath(null);
            }catch(NullPointerException npe) {}



        });


        btnSaveData.setOnAction(t->{
            try {
                if (cCBSaveData.getCheckModel().getCheckedItems().contains("ALL")) {
                    cCBSaveData.getCheckModel().checkAll();
                    cCBSaveData.getCheckModel().clearCheck(0);
                }

                String[] saveMarkersArray = new String[cCBSaveData.getCheckModel().getCheckedItems().size()];
                for (int i = 0; i < saveMarkersArray.length; i++) {
                    saveMarkersArray[i] = (String) cCBSaveData.getCheckModel().getCheckedItems().get(i);
                }
                ProcessCSV.saveCSV(moCapMarkers, fileNameSave, saveMarkersArray);
                cCBSaveData.getCheckModel().clearChecks();
            }catch(NullPointerException npe) {}
        });






        gridData.setPadding(new Insets(10, 10, 10, 10));
        gridData.setHgap(15);
        gridData.setVgap(15);

        gridData.add(labelLoadData, 0,0,1,1);
        GridPane.setHalignment(labelLoadData, HPos.LEFT);
        GridPane.setValignment(labelLoadData, VPos.TOP);

        gridData.add(cBLoadFile, 1, 0, 1, 1);
        GridPane.setHalignment(cBLoadFile, HPos.LEFT);
        GridPane.setValignment(cBLoadFile, VPos.CENTER);

        gridData.add(cCBLoadData, 0, 1, 1, 1);
        GridPane.setHalignment(cCBLoadData, HPos.CENTER);
        GridPane.setValignment(cCBLoadData, VPos.CENTER);

        gridData.add(btnLoadData, 1, 1, 1, 1);
        GridPane.setHalignment(btnLoadData, HPos.LEFT);
        GridPane.setValignment(btnLoadData, VPos.CENTER);

        gridData.add(labelSaveData, 2, 0, 1, 1);
        GridPane.setHalignment(labelSaveData, HPos.LEFT);
        GridPane.setValignment(labelSaveData, VPos.TOP);

        gridData.add(cBSaveFile, 3, 0, 1, 1);
        GridPane.setHalignment(cBSaveFile, HPos.LEFT);
        GridPane.setValignment(cBSaveFile, VPos.CENTER);

        gridData.add(cCBSaveData, 2, 1, 1, 1);
        GridPane.setHalignment(cCBSaveData, HPos.CENTER);
        GridPane.setValignment(cCBSaveData, VPos.CENTER);

        gridData.add(btnSaveData, 3, 1, 1, 1);
        GridPane.setHalignment(btnSaveData, HPos.LEFT);
        GridPane.setValignment(btnSaveData, VPos.CENTER);



        RowConstraints rowData = new RowConstraints();
        ColumnConstraints colData = new ColumnConstraints();
        rowData.setPercentHeight(50);
        colData.setPercentWidth(25);
        rowData.setVgrow(Priority.ALWAYS);
        colData.setHgrow(Priority.ALWAYS);

        gridData.getColumnConstraints().addAll(colData, colData, colData, colData);
        gridData.getRowConstraints().addAll(rowData, rowData);
        gridData.setGridLinesVisible(false);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Section 2: Chart and animation
        GridPane gridChartAnim = new GridPane();
        x = new NumberAxis();
        y = new NumberAxis();
        x.setForceZeroInRange(false);
        y.setForceZeroInRange(false);
        figure = new ScatterChart<Number, Number>(x,y);

        figure.setLegendVisible(false);
        figure.setAnimated(false);




        ColumnConstraints colChartAnim = new ColumnConstraints();
        colChartAnim.setPercentWidth(50);

        root = new Pane();
        gridChartAnim.add(figure, 0, 0, 1, 1);
        gridChartAnim.add(root, 1, 0, 1, 1);

        gridChartAnim.getColumnConstraints().addAll(colChartAnim, colChartAnim);

        Circle circle = new Circle();
        circle.setRadius(5);
        circle.setFill(Color.GREEN);




        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Section 3: Chart and animation controls

        labelChartAnimControls = new Label("Chart and animation controls");
        labelChartAnimControls.setFont(Font.font("Times", FontWeight.BOLD, 13));


        labelSelMarkChartAnim = new Label("Select marker");
        cBChartAnimMarker = new ComboBox();
        //cBChartAnimMarker.getItems().addAll(moCapMarkers.getMarkers()); // do sprawdzenia

        tGChartAnim1 = new ToggleGroup();
        tGChartAnim2 = new ToggleGroup();

        tbtnChartAnimView = new ToggleButton("View");
        tbtnChartAnimTime = new ToggleButton("Time");

        tbtnChartAnimOp1 = new ToggleButton();
        tbtnChartAnimOp2 = new ToggleButton();
        tbtnChartAnimOp3 = new ToggleButton();


        btnAnimPlay = new Button("Play");
        btnAnimPause = new Button("Pause");
        btnAnimStop = new Button("Stop");


        labelSelSamplPeriodChartAnim = new Label("Set sampling");
        labelSelSamplPeriodChartAnim.setLabelFor(cBSamplPeriod);
        cBSamplPeriod = new ComboBox();
        cBSamplPeriod.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 35, 40, 45, 50);
        cBSamplPeriod.getSelectionModel().selectFirst();
        samplPer = Double.parseDouble(cBSamplPeriod.getSelectionModel().getSelectedItem().toString());
        cBSamplPeriod.setMaxWidth(55);

        labelT0 =  new Label("T start");
        labelTend = new Label("T end");
        sliderT0 = new Slider();
        sliderTend = new Slider();
        sliderT0.setValue(0);


        txtT0 = new TextField();
        txtTend = new TextField();
        txtT0.setMaxWidth(50);
        txtTend.setMaxWidth(50);
        labelTxtT0Error = new Label("Invalid value");
        labelTxtTendError = new Label("Invalid value");
        labelTxtT0Error.setFont(Font.font("Times", FontWeight.NORMAL, 9));
        labelTxtTendError.setFont(Font.font("Times", FontWeight.NORMAL, 9));
        labelTxtT0Error.setVisible(false);
        labelTxtTendError.setVisible(false);

        tbtnChartAnimView.setOnAction(u->{
            if(tbtnChartAnimView.isSelected()==false) {
                tbtnChartAnimTime.setSelected(true);
                tbtnChartAnimOp1.setSelected(true);

                tbtnChartAnimOp1.setText("X(t)");
                tbtnChartAnimOp2.setText("Y(t)");
                tbtnChartAnimOp3.setText("Z(t)");
            }

            if(tbtnChartAnimView.isSelected()) {
                tbtnChartAnimOp1.setSelected(true);

                tbtnChartAnimOp1.setText("F");
                tbtnChartAnimOp2.setText("R");
                tbtnChartAnimOp3.setText("U");


            }
            loadFigure();
            isAnimationLoaded = false;


        });

        tbtnChartAnimTime.setOnAction(o->{
            if(tbtnChartAnimTime.isSelected()==false) {
                tbtnChartAnimView.setSelected(true);
                tbtnChartAnimOp1.setSelected(true);

                tbtnChartAnimOp1.setText("F");
                tbtnChartAnimOp2.setText("R");
                tbtnChartAnimOp3.setText("U");


            }


            if(tbtnChartAnimTime.isSelected()) {
                tbtnChartAnimOp1.setSelected(true);

                tbtnChartAnimOp1.setText("X(t)");
                tbtnChartAnimOp2.setText("Y(t)");
                tbtnChartAnimOp3.setText("Z(t)");

            }
            isAnimationLoaded = false;
            loadFigure();


        });


        tbtnChartAnimOp1.setOnAction(q->{
            if(tbtnChartAnimOp1.isSelected()==false) {
                tbtnChartAnimOp2.setSelected(true);
            }
            else {
            }
            isAnimationLoaded = false;
            loadFigure();


        });


        tbtnChartAnimOp2.setOnAction(w->{
            if(tbtnChartAnimOp2.isSelected()==false) {
                tbtnChartAnimOp3.setSelected(true);
            }

            loadFigure();
            isAnimationLoaded = false;

        });



        tbtnChartAnimOp3.setOnAction(y->{
           if(tbtnChartAnimOp3.isSelected()==false) {
               tbtnChartAnimOp1.setSelected(true);
           }

           else {
           }
           loadFigure();
            isAnimationLoaded = false;

        });



        tbtnChartAnimOp1.setMinWidth(35);
        tbtnChartAnimOp2.setMinWidth(35);
        tbtnChartAnimOp3.setMinWidth(35);

        tbtnChartAnimView.setToggleGroup(tGChartAnim1);
        tbtnChartAnimTime.setToggleGroup(tGChartAnim1);

        tbtnChartAnimOp1.setToggleGroup(tGChartAnim2);
        tbtnChartAnimOp2.setToggleGroup(tGChartAnim2);
        tbtnChartAnimOp3.setToggleGroup(tGChartAnim2);

        cBSamplPeriod.setOnAction(k->{
            samplPer = Double.parseDouble(cBSamplPeriod.getSelectionModel().getSelectedItem().toString());
            sliderT0.setMajorTickUnit((int)samplPer);
            sliderTend.setMajorTickUnit((int)samplPer);
            sliderT0.setMin(0);
            sliderTend.setMin((int)samplPer);
            try {
            int rest = moCapMarkers.getCoordinate("Time").size()%((int)samplPer);

            if((rest==0)==false) {
                sliderTend.setMax(moCapMarkers.getCoordinate("Time").size() - (moCapMarkers.getCoordinate("Time").size()%samplPer));
                sliderT0.setMax(sliderTend.getMax()-samplPer);
            }

                loadFigure();
            }catch(NullPointerException e) {}
            isAnimationLoaded = false;

        });


        sliderT0.valueProperty().addListener(p->{
            if(sliderT0.getValue()>=sliderTend.getValue()){
                while(sliderT0.getValue()>=sliderTend.getValue()) {
                    sliderTend.setValue(sliderT0.getValue()+(int)samplPer);
                }
            }
            try {
            txtT0.setText(String.valueOf(moCapMarkers.getCoordinate("Time").get((int)sliderT0.getValue())));

                isAnimationLoaded = false;
                loadFigure();
            } catch(NullPointerException N) {}
        });

        sliderTend.valueProperty().addListener(l->{
            if(sliderTend.getValue()<=sliderT0.getValue()) {
                while(sliderTend.getValue()<=sliderT0.getValue()) {
                    sliderT0.setValue(sliderTend.getValue()-(int)samplPer);
                }
            }
            try {

                txtTend.setText(String.valueOf(moCapMarkers.getCoordinate("Time").get((int) sliderTend.getValue())));
                loadFigure();
                isAnimationLoaded = false;
            } catch(NullPointerException N) {}
        });

        txtT0.setOnAction(a->{
            try {
                double txtT0Val = Double.parseDouble(txtT0.getText());


                if((txtT0Val/(samplPer*Tstep)) % (samplPer) < samplPer/2 ) {
                    sliderT0.setValue((txtT0Val/(samplPer*Tstep)) - ((txtT0Val/(samplPer*Tstep)) % (samplPer)));
                }
                else {
                    double sliderT0Temp = (txtT0Val/(samplPer*Tstep)) + (samplPer - ((txtT0Val/(samplPer*Tstep)) % (samplPer)));
                    if(sliderT0Temp>=sliderTend.getValue()) {
                        sliderT0.setValue(sliderT0Temp-samplPer);
                    }
                    else {
                        sliderT0.setValue(sliderT0Temp);
                    }
                }
                txtT0.setText(String.valueOf(moCapMarkers.getCoordinate("Time").get((int)sliderT0.getValue())));
                labelTxtT0Error.setVisible(false);
                isAnimationLoaded = false;
                loadFigure();

            } catch (NumberFormatException N) {
                labelTxtT0Error.setVisible(true);
            }
        });

        txtTend.setOnAction(a->{
            try {
                double txtTendVal = Double.parseDouble(txtTend.getText());

                if((txtTendVal/(samplPer*Tstep)) % (samplPer) < samplPer/2 ) {
                    sliderTend.setValue((txtTendVal/(samplPer*Tstep)) - ((txtTendVal/(samplPer*Tstep)) % (samplPer)));
                }
                else {
                    double sliderTendTemp = (txtTendVal/(samplPer*Tstep)) + (samplPer - ((txtTendVal/(samplPer*Tstep)) % (samplPer)));
                    if(sliderTendTemp<=sliderT0.getValue()) {
                        sliderTend.setValue(sliderTendTemp+samplPer);
                    }
                    else {
                        sliderTend.setValue(sliderTendTemp);
                    }
                }

                txtTend.setText(String.valueOf(moCapMarkers.getCoordinate("Time").get((int)sliderTend.getValue())));
                labelTxtTendError.setVisible(false);
                isAnimationLoaded = false;
                loadFigure();
            } catch (NumberFormatException N) {
                labelTxtTendError.setVisible(true);
            }
        });

        cBChartAnimMarker.setOnAction(e->{
            try {
                loadFigure();
            } catch(NullPointerException N) {}
                });






        btnAnimPlay.setOnAction(p->{
            try {
                if (isAnimationLoaded == false) {
                    loadAnim();
                    isAnimationLoaded = true;
                }
                ptr.play();
            }catch(NullPointerException npe) {}
        });

        btnAnimPause.setOnAction(p->{
            try {
                ptr.pause();
            }catch(NullPointerException npe) {}
        });

        btnAnimStop.setOnAction(p->{
            try {
                ptr.stop();
            }catch(NullPointerException npe) {}
        });



        GridPane gridSliders = new GridPane();
        gridSliders.add(labelT0, 0, 0, 1, 1);
        gridSliders.add(labelTend, 0, 1, 1, 1);
        gridSliders.add(sliderT0, 1, 0, 1, 1);
        gridSliders.add(sliderTend, 1, 1, 1, 1);
        gridSliders.add(txtT0, 2, 0, 1, 1);
        gridSliders.add(txtTend, 2, 1, 1, 1);
        gridSliders.add(labelTxtT0Error, 3, 0, 1, 1);
        gridSliders.add(labelTxtTendError, 3, 1, 1, 1);


        gridSliders.setHgap(15);
        gridSliders.setVgap(5);





        gridChartAnimControls.add(labelChartAnimControls, 0, 0,2,1);
        GridPane.setValignment(labelChartAnimControls, VPos.TOP);
        GridPane.setHalignment(labelChartAnimControls, HPos.LEFT);


//        GridPane gridChooseMarker = new GridPane();
        gridChartAnimControls.add(labelSelMarkChartAnim, 0, 1, 1, 1);
        GridPane.setValignment(labelSelMarkChartAnim, VPos.CENTER);
        GridPane.setHalignment(labelSelMarkChartAnim, HPos.LEFT);

        cBChartAnimMarker.setMaxWidth(80);
        gridChartAnimControls.add(cBChartAnimMarker,0, 1,1,1);
        GridPane.setValignment(cBChartAnimMarker, VPos.CENTER);
        GridPane.setHalignment(cBChartAnimMarker, HPos.RIGHT);

        GridPane gridTGBtn1 = new GridPane();
        gridTGBtn1.add(tbtnChartAnimView, 0,0, 1, 1);
        gridTGBtn1.add(tbtnChartAnimTime, 1,0, 1, 1);
        gridTGBtn1.setPadding(new Insets(10, 5, 5, 10));

        GridPane.setHalignment(tbtnChartAnimView, HPos.RIGHT);
        GridPane.setValignment(tbtnChartAnimView, VPos.BOTTOM);
        GridPane.setHalignment(tbtnChartAnimTime, HPos.LEFT);
        GridPane.setValignment(tbtnChartAnimTime, VPos.BOTTOM);
        gridTGBtn1.setGridLinesVisible(false);

        gridChartAnimControls.add(gridTGBtn1, 1, 0, 1, 1);
        GridPane.setValignment(gridTGBtn1, VPos.CENTER);
        GridPane.setHalignment(gridTGBtn1, HPos.RIGHT);

        gridChartAnimControls.add(labelSelSamplPeriodChartAnim, 1, 1, 1, 1);
        gridChartAnimControls.add(cBSamplPeriod, 1, 1, 1, 1);
        GridPane.setHalignment(labelSelSamplPeriodChartAnim, HPos.LEFT);
        GridPane.setValignment(labelSelSamplPeriodChartAnim, VPos.CENTER);
        GridPane.setHalignment(cBSamplPeriod, HPos.RIGHT);
        GridPane.setValignment(cBSamplPeriod, VPos.CENTER);

        GridPane gridTGBtn2 = new GridPane();
        gridTGBtn2.setPadding(new Insets(10, 10, 10, 10));
        gridTGBtn2.add(tbtnChartAnimOp1, 0, 0, 1, 1);
        gridTGBtn2.add(tbtnChartAnimOp2, 1, 0, 1, 1);
        gridTGBtn2.add(tbtnChartAnimOp3, 2, 0, 1, 1);
        GridPane.setHalignment(tbtnChartAnimOp1, HPos.RIGHT);
        GridPane.setValignment(tbtnChartAnimOp1, VPos.CENTER);
        GridPane.setHalignment(tbtnChartAnimOp2, HPos.CENTER);
        GridPane.setValignment(tbtnChartAnimOp2, VPos.CENTER);
        GridPane.setHalignment(tbtnChartAnimOp3, HPos.LEFT);
        GridPane.setValignment(tbtnChartAnimOp3, VPos.CENTER);
        gridTGBtn2.setGridLinesVisible(false);

        gridChartAnimControls.add(gridTGBtn2, 2, 0, 1, 1);
        GridPane.setHalignment(gridTGBtn2, HPos.LEFT);
        GridPane.setValignment(gridTGBtn2, VPos.CENTER);

        GridPane gridAnimControls = new GridPane();
        gridAnimControls.setPadding(new Insets(10, 10, 10, 10));

        gridAnimControls.add(btnAnimPlay, 0, 0, 1, 1);
        GridPane.setHalignment(btnAnimPlay, HPos.CENTER);
        GridPane.setValignment(btnAnimPlay, VPos.CENTER);

        gridAnimControls.add(btnAnimPause, 1,0,1,1);
        GridPane.setHalignment(btnAnimPause, HPos.CENTER);
        GridPane.setValignment(btnAnimPause, VPos.CENTER);

        gridAnimControls.add(btnAnimStop, 2,0,1,1);
        GridPane.setHalignment(btnAnimStop, HPos.CENTER);
        GridPane.setValignment(btnAnimStop, VPos.CENTER);

        ColumnConstraints cConstrAnimControls = new ColumnConstraints();
        cConstrAnimControls.setPercentWidth(100/3);
        cConstrAnimControls.setHgrow(Priority.ALWAYS);
        gridAnimControls.getColumnConstraints().addAll(cConstrAnimControls, cConstrAnimControls, cConstrAnimControls);

        gridChartAnimControls.add(gridAnimControls, 3, 0, 1, 1);
        GridPane.setHalignment(gridAnimControls, HPos.CENTER);
        GridPane.setValignment(gridAnimControls, VPos.CENTER);


        gridChartAnimControls.add(gridSliders, 2, 1, 2, 1);


        gridChartAnimControls.setPadding(new Insets(10, 10, 10, 10));
        gridChartAnimControls.setHgap(15);
        gridChartAnimControls.setVgap(0);


        RowConstraints r0CChartAnimControls = new RowConstraints();
        r0CChartAnimControls.setPercentHeight(50);
        r0CChartAnimControls.setVgrow(Priority.ALWAYS);



        ColumnConstraints c0CChartAnimControls = new ColumnConstraints();
        c0CChartAnimControls.setPercentWidth(30);
        c0CChartAnimControls.setHgrow(Priority.ALWAYS);

        ColumnConstraints c1CChartAnimControls = new ColumnConstraints();
        c1CChartAnimControls.setPercentWidth(20);
        c1CChartAnimControls.setHgrow(Priority.ALWAYS);

        ColumnConstraints c2CChartAnimControls = new ColumnConstraints();
        c2CChartAnimControls.setPercentWidth(20);
        c2CChartAnimControls.setHgrow(Priority.ALWAYS);

        ColumnConstraints c3CChartAnimControls = new ColumnConstraints();
        c3CChartAnimControls.setPercentWidth(30);
        c3CChartAnimControls.setHgrow(Priority.ALWAYS);



        gridChartAnimControls.getRowConstraints().addAll(r0CChartAnimControls, r0CChartAnimControls);
        gridChartAnimControls.getColumnConstraints().addAll(c0CChartAnimControls, c1CChartAnimControls, c2CChartAnimControls, c3CChartAnimControls);
        gridChartAnimControls.setGridLinesVisible(false);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        layout.setPrefSize(wW, wH);
        VBox outerLayout = new VBox();

        RowConstraints row0 = new RowConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row0.setPercentHeight(100.0/6);
        row0.setVgrow(Priority.ALWAYS);
        row1.setPercentHeight(100.0/2);
        row1.setVgrow(Priority.ALWAYS);
        row2.setPercentHeight(100.0/6);
        row2.setVgrow(Priority.ALWAYS);
        row3.setPercentHeight(100.0/6);
        row3.setVgrow(Priority.ALWAYS);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        col.setHgrow(Priority.ALWAYS);

        layout.setMinSize(wW, wH);
        layout.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        layout.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        layout.add(gridData, 0, 0, 1 , 1);
        layout.add(gridChartAnim, 0, 1, 1, 1);
        layout.add(gridChartAnimControls, 0 ,2, 1, 1);
        layout.add(gridMultiAnim, 0, 3, 1, 1);
        layout.setGridLinesVisible(false);
        layout.getRowConstraints().addAll(row0, row1, row2, row3);
        layout.getColumnConstraints().add(col);
        outerLayout.getChildren().add(layout);
        Stage mainStage = new Stage();
        Scene mainScene = new Scene(outerLayout, wW, wH);
        mainStage.setScene(mainScene);
        mainStage.show();

    }


}

package softcare.game;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import softcare.game.databinding.ActivitySolutionBinding;

public class SolutionActivity extends AppCompatActivity {

    private ActivitySolutionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

/*
        Button newCity = new Button("Add new city using  distance");
        Button newCityXY = new Button("Add new city using   location ");


        // Create some menus
        Menu fileMenu = new Menu("File");

        MenuItem exit= new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());
        MenuItem openXY = new MenuItem("Open TSP  Location");
        MenuItem open = new MenuItem("Open TSP  Distance");
        MenuItem save = new MenuItem("save ");
        fileMenu.getItems().addAll(openXY,open,save,exit);
        save.setOnAction(e -> Platform.exit());
        openXY.setOnAction(e -> Platform.exit());
        open.setOnAction(e -> Platform.exit());
        // Add menus to a menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu);
        BorderPane root = new BorderPane();
        HBox topLayout = new HBox(menuBar,newCity, newCityXY);

        Button clear = new Button("clear input");
        Button start = new Button("start");
        Button stop = new Button("stop");
        startInput = new TextArea();
        startInputXY = new TextArea();
        TextArea startResult = new TextArea();
        TextArea startProgress = new TextArea();
        startResult.setWrapText(true);
        startProgress.setWrapText(true);
        // Create the radio buttons.
        RadioButton rbGen = new RadioButton("Gentic algorithm");
        RadioButton rbDyn = new RadioButton("Dynamic algorithm");
        RadioButton rbKnn = new RadioButton("KNN algorithm");
        // Create a toggle group.
        ToggleGroup tg = new ToggleGroup();
        // Add each button to a toggle group.
        rbDyn.setToggleGroup(tg);
        rbKnn.setToggleGroup(tg);
        rbGen.setToggleGroup(tg);

        rbGen.setSelected(true);
        // Handle action events for the radio buttons.
        rbDyn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                System.out.println("Dyn");
                alg = 1;
                algName = "Dynamic Algorithm";
            }
        });
        rbGen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                alg = 2;
                System.out.println("Gen");
                algName = "Genetic Algorithm";
            }
        });
        rbKnn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ae) {
                System.out.println("knn");
                alg = 3;
                algName = "KNN Algorithm";
            }
        });
        startInput.setEditable(false);
        ;
        startInputXY.setEditable(false);
        ;
        VBox rightLay = new VBox(start);
        VBox algLayout = new VBox(rbGen, rbKnn, rbDyn);
        algLayout.setAlignment(Pos.BASELINE_LEFT);

        super.setTop(topLayout);
        super.setRight(rightLay);
        super.setBottom(clear);
        startInputXY.setPrefHeight(700);
        startInput.setPrefHeight(700);
        startProgress.setPrefHeight(700);
        startResult.setPrefHeight(700);
        SplitPane sp1 = new SplitPane(new VBox(new Label("Progress"), startProgress),
                new VBox(new Label("Result"), startResult));
        SplitPane sp = new SplitPane(new VBox(new Label("Ajacent Matrix"), startInput), sp1);
        sp.setOrientation(Orientation.VERTICAL);
        super.setCenter(new SplitPane(sp, new VBox(new Label("Location"), startInputXY)));
        super.setLeft(algLayout);

        open.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();

                File file =  fc.showOpenDialog(stage);
                if (file == null) {
                    util.S.notice(stage, "Error", "File not seleted", " select a file that exist");
                }else
                if (!file.exists()) {
                    util.S.notice(stage, "Error", "File not seleted", " select a file that exist");
                }else
                if (file.isFile()) {

                    if (mCities == null)
                        mCities = new ArrayList();
                    Runnable task = () -> {
                        try {

                            String tsp = readFile(file);
                            if (ps != null)
                                ps.pro(0.5, "Setting result ");
                            if (readTSP(tsp)) {
                                setLocation();
                                String pv = getPreview();
                                String pv2 = getPreviewXY();
                                Platform.runLater(() ->
                                        startInput.setText(pv));
                                startInputXY.setText(pv2);
                            } else {
                                System.out.print(mCities.size());
                                Platform.runLater(
                                        () -> util.S.notice(stage, "Error", "File type or struct not match",
                                                "use the interface to add cities or read file format"));
                                clear();
                            }
                            ps.prepareStop();
                            ;

                            try {
                                Thread.sleep(waitTime);

                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            ps.stop();
                            ;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Platform.runLater(() -> util.S.notice(stage, "Error", "IOException", e.getMessage()));

                        }
                    };
                    // Run the task in a background thread
                    backgroundThread = new Thread(task);

                    ps = new util.ProgressPopUp(backgroundThread);
                    ps.notice(stage, "Loading TSP");
                    // Terminate the running thread if the application exits
                    backgroundThread.setDaemon(true);
                    backgroundThread.start();

                }

                // start.fire();

            }

        });




        openXY.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser fc = new FileChooser();

                File file =
                        //new File("C:\\Users\\GBENGE AONDOAKULA\\Downloads\\csv\\csv\\ulysses22.csv");
                        fc.showOpenDialog(stage);
                if (file == null) {
                    util.S.notice(stage, "Error", "File not seleted", " select a file that exist");
                }else
                if (!file.exists()) {
                    util.S.notice(stage, "Error", "File not seleted", " select a file that exist");
                }else
                if (file.isFile()) {

                    if (mCities == null) {
                        mCities = new ArrayList();
                    }
                    if (pointXY == null) {
                        pointXY = new ArrayList();
                    }
                    Runnable task = () -> {
                        try {
                            String tsp = readFile(file);
                            if (ps != null)
                                ps.pro(0.5, "Setting result ");

                            if (readTSP_XY(tsp)) {
                                countDistancesAndUpdateMatrix();
                                String pv = getPreview();
                                String pvXY = getPreviewXY();
                                Platform.runLater(() -> {

                                    startInputXY.setText(pvXY);
                                    System.out.println(" size: " + mCities.size());
                                    startInput.setText(pv);
                                });
                            } else {
                                System.out.print(mCities.size());
                                Platform.runLater(
                                        () -> util.S.notice(stage, "Error", "File type or struct not match",
                                                "use the interface to add cities or read file format"));
                                clear();
                            }
                            ps.prepareStop();
                            ;

                            try {
                                Thread.sleep(waitTime);

                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            ps.stop();
                            ;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Platform.runLater(() -> util.S.notice(stage, "Error", "IOException", e.getMessage()));

                        }
                    };
                    // Run the task in a background thread
                    backgroundThread = new Thread(task);

                    ps = new util.ProgressPopUp(backgroundThread);
                    ps.notice(stage, "Loading TSP Location");
                    // Terminate the running thread if the application exits
                    backgroundThread.setDaemon(true);
                    backgroundThread.start();

                }

                // start.fire();

            }

        });

        save.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (mCities != null) {
                    if ((mCities.size() < 1)) {
                        util.S.notice(stage, "Notice", "Number of city  is less than one",
                                "Add at leat one city to save project");
                        return;
                    }

                } else {
                    util.S.notice(stage, "Notice", "Number of city  is less than one",
                            "Add at leat one city to save project");
                    return;
                }
                Save save= new Save();
                save.save(stage, "choose type", startInputXY.getText().toString(),
                        startInput.getText().toString() );

            }});

        start.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (mCities != null)
                    if (mCities.size() > 2) {

                        startProgress.setText(startProgress.getText() + "Started 3 Algorthm at " + new Date() + "\n");
                        //start

                        //
                        Runnable task = () -> {
                            long d = startAgl(getData(), alg);
                            System.out.print(" Time used ");
                            System.out.println(d);

                            Platform.runLater(() -> {
                                startResult.setText(new Date() +
                                        "\n" + geFulltResult(d, alg));
                                startProgress.setText(startProgress.getText() +
                                        algName + new Date() + "\n");
                            });

                            if(ps!=null) {
                                ps.prepareStop();
                                ps.stop();
                            }

                        };
                        // Run the task in a background thread
                        backgroundThread = new Thread(task);

                        ps = new util.ProgressPopUp(backgroundThread);
                        ps.notice(stage, "Algorithm running");
                        ps.pro(0, "Algorithm running");
                        // Terminate the running thread if the application exits
                        backgroundThread.setDaemon(true);
                        backgroundThread.start();



                    } else {

                        util.S.notice(stage, "Notice", "Number of cities are/is less than a problem",
                                "Add more cities or load a new project");
                    }
                else
                    util.S.notice(stage, "Notice", "Number of cities are/is less than a problem",
                            "Add more cities or load a new project");
            }

        });
        clear.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                clear();
                startInput.setText("");
                startInputXY.setText("");
            }

        });
        newCity.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (mCities == null) {
                    pointXY = new ArrayList();
                    mCities = new ArrayList();
                }
                addCity(stage, mCities);
                System.out.println("  fiesr");
                startInput.setText(getPreview());
                startInputXY.setText(getPreviewXY());

            }

        });
        newCityXY.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (mCities == null) {
                    pointXY = new ArrayList();
                    mCities = new ArrayList();
                }
                addCityXY(stage, mCities);
                startInputXY.setText(getPreviewXY());
                startInput.setText(getPreview());

            }

        });


*/

    }
}
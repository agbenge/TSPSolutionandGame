package softcare.game;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Constraints;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;

import softcare.game.databinding.ActivitySolutionBinding;
import softcare.game.model.Alg;
import softcare.game.model.CodeX;
import softcare.game.model.SolutionViewModel;
import softcare.game.model.TaskManager;
import softcare.game.model.Tsp;
import softcare.game.model.TspCode;
import softcare.game.model.TspData;
import softcare.game.model.TspResult;
import softcare.gui.PlotTSP;
import softcare.gui.PointXY;

public class SolutionActivity extends AppCompatActivity {

    private ActivitySolutionBinding binding;


    private SolutionViewModel solutionViewModel;

    private TaskManager taskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        taskManager = TaskManager.getInstance();
        _init();
    }

    Snackbar snackbar;

    private void started(String m) {
        snackbar = Snackbar.make(binding.getRoot(), m, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getText(R.string.cancel), v -> {
            if (taskManager != null) {
                taskManager.shutdownNow();
            }
        });
        snackbar.show();
    }

    private void ended() {

        if (snackbar != null) {
            snackbar.setText(getString(R.string.finished));
            snackbar.setDuration(Snackbar.LENGTH_LONG);
        }

    }

    private void _init() {
        binding = ActivitySolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        solutionViewModel = new ViewModelProvider(this).get(SolutionViewModel.class);

        binding.add.setOnClickListener(view -> {

            if (isAddByDistance) addD(view);
            else if (isAddByLocation) addL(view);
            else addI();
        });
        binding.addL.setOnClickListener(view -> addBy());
        binding.solve.setOnClickListener(view -> {
            Tsp tsp = solutionViewModel.getTsp();
            if (tsp != null) {
                if (tsp.getTspActions() == TspCode.READ || tsp.getTspActions() == TspCode.UPDATE) {
                    if (tsp.getCities().size() > 2) {
                        launchAlg(tsp.getCities().size());
                    } else
                        Snackbar.make(view, getString(R.string.cities_small), Snackbar.LENGTH_LONG).show();

                } else if (tsp.getTspActions() == TspCode.SOLVED)
                    launchAlg(tsp.getCities().size());//prepareResult(tsp);
                else
                    Snackbar.make(view, getString(R.string.cities_not_added), Snackbar.LENGTH_LONG).show();

            } else
                Snackbar.make(view, getString(R.string.un_init), Snackbar.LENGTH_LONG).show();

        });
        binding.refresh.setOnClickListener(view -> solutionViewModel.clear());/// change to button toback if already cleard
        solutionViewModel.getTspLiveData().observe(this, tsp -> {
            if (tsp != null) {
                if (tsp.getTspActions() != TspCode.SOLVED) {
                    binding.outputMatrix.setText(solutionViewModel.getPreview());
                    binding.outputLocations.setText(solutionViewModel.getPreviewXY());
                    ended();
                } else {
                    prepareResult(tsp);
                }
            } else {
                binding.outputMatrix.setText("");
                binding.outputLocations.setText("");
                Snackbar.make(binding.getRoot(), getString(R.string.input_empty_start_problem), Snackbar.LENGTH_LONG).show();

            }
        });
        solutionViewModel.getErrorCodeLiveData().observe(this, errorCode -> Snackbar.make(binding.getRoot(), getString(R.string.error) + errorCode.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show());
        solutionViewModel.getProgressXLiveData().observe(this, progressX -> Log.d("game", progressX.toString()));


    }


    ActivityResultLauncher<Intent> addI = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " here " + (resultCode == RESULT_OK));
                    if (resultCode == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        TspData dat = data.getParcelableExtra("data");
                        imgpath = data.getStringExtra("img");
                        if (dat != null)
                            solutionViewModel.addMapData(dat);

                    }
                }
            });


    ActivityResultLauncher<Intent> addD = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " here " + (resultCode == RESULT_OK));
                    if (resultCode == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String name = data.getStringExtra("cityName");
                        double[] distance = data.getDoubleArrayExtra("data");
                        solutionViewModel.addCity(name, distance);

                    }
                }
            }
    );
    ActivityResultLauncher<Intent> addL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " location --------- " + (resultCode == RESULT_OK));

                    if (resultCode == RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        TspData dat = data.getParcelableExtra("data");
                        imgpath = data.getStringExtra("img");
                        if (dat != null)
                            solutionViewModel.addMapData(dat);

                    }
                 /*   if (resultCode == RESULT_OK) {

                        Intent data = result.getData();

                        assert data != null;
                        String name = data.getStringExtra("cityName");
                        double x = data.getDoubleExtra("x", 0);
                        double y = data.getDoubleExtra("y", 0);
                        solutionViewModel.addCityXY(name, new PointXY(x, y));
                    }*/
                }
            }
    );


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solution, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_save) {

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_TITLE, "");
            saveLauncher.launch(intent);

            return true;
        } else if (menuId == R.id.action_open) {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            openFileLauncher.launch(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> saveLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " gallery --------- " + (resultCode == RESULT_OK));
                    if (resultCode == RESULT_OK) {
                        try {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            OutputStream outputStream=
                                    SolutionActivity.this.getContentResolver().openOutputStream(uri);
                            save(outputStream);
                        } catch (Exception e) {

                        }
                    }
                }
            }
    );


    ActivityResultLauncher<Intent> openFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " gallery --------- " + (resultCode == RESULT_OK));
                    if (resultCode == RESULT_OK) {
                        try {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                           InputStream inputStream=
                                   SolutionActivity.this.getContentResolver().openInputStream(uri);
                           open(inputStream);
                        } catch (Exception e) {

                        }
                    }
                }
            }
    );

    private void save(OutputStream outputStream) {
        final CharSequence[] options = {
                getString(R.string.saveByLocation),
                getString(R.string.saveByDistances),
                getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_input_method));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.saveByLocation))) {
                solutionViewModel.saveFile(outputStream, taskManager, true);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.saveByDistances))) {
                solutionViewModel.saveFile(outputStream, taskManager, false);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void open(InputStream inputStream) {
        final CharSequence[] options = {
                getString(R.string.openByLocation),
                getString(R.string.openByDistances),
                getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_input_method));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.openByLocation))) {
                solutionViewModel.openFile(inputStream, taskManager, true);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.openByDistances))) {
                solutionViewModel.openFile(inputStream, taskManager, false);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void prepareResult(Tsp tsp) {
        Intent intent = new Intent(this, PlotTspActivity.class);
        intent.putExtra("result", new TspResult(tsp.getCities(), tsp.getPointXY()
                , tsp.getDirection(), tsp.getCost(), tsp.getDuration(), tsp.getResult(), imgpath));
        startActivity(intent);
        Log.d(CodeX.tag, " Path " + imgpath);
    }

    private String imgpath = null;

    private void dialogResult4(Tsp tsp) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.plot_tsp_result);
        PlotTSP plotTSP = dialog.findViewById(R.id.plotTSP);
        dialog.getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.MATCH_PARENT);
        dialog.show();
        plotTSP.plot(tsp.getPointXY(), tsp.getCities(),
                tsp.getDirection());
        ((TextView) dialog.findViewById(R.id.result)).setText(tsp.getResult());
        dialog.findViewById(R.id.close).setOnClickListener(v -> dialog.cancel());
        dialog.findViewById(R.id.zoom_in).setOnClickListener(v -> plotTSP.zoomIn());
        dialog.findViewById(R.id.zoom_out).setOnClickListener(v -> plotTSP.zoomOut());


    }

    private void addI() {
        imgpath = null;
        Intent intent = new Intent(SolutionActivity.this, AddCityMapActivity.class);
        addI.launch(intent);
    }

    public void addD(View view) {
        imgpath = null;
        Tsp tsp = solutionViewModel.getTsp();
        Intent intent = new Intent(SolutionActivity.this, AddCityDActivity.class);
        if (tsp != null) {
            Log.d(CodeX.tag, tsp.getCities().toString() + " sending names  " + tsp.getCities().size());
            intent.putExtra("Cities", tsp.getCities().toArray(new String[0]));
        } else {
            Log.d(CodeX.tag, " sending names is 00  ");
        }
        addD.launch(intent);
        //Snackbar.make(view, "Unable to intiallsed", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
    }

    public void addL(View view) {
        imgpath = null;
        Intent intent = new Intent(SolutionActivity.this, AddCityLActivity.class);
        addL.launch(intent);
        //Snackbar.make(view, "Unable to intiallsed", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
    }

    public void launchAlg(int length) {
        final CharSequence[] options = {
                getString(R.string.dyn),
                getString(R.string.knn),
                getString(R.string.gen),
                getString(R.string.auto),
                getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_alg));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.dyn))) {
                started("Started " + getString(R.string.dyn));
                solutionViewModel.startAgl(Alg.DYN, taskManager);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.knn))) {
                started("Started " + getString(R.string.knn));
                solutionViewModel.startAgl(Alg.KNN, taskManager);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.gen))) {
                started("Started " + getString(R.string.gen));
                solutionViewModel.startAgl(Alg.GEN, taskManager);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.auto))) {
                started("Started " + getString(R.string.auto));
                if (length < 20)
                    solutionViewModel.startAgl(Alg.DYN, taskManager);
                else solutionViewModel.startAgl(Alg.KNN, taskManager);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    boolean isAddByDistance;
    boolean isAddByLocation = true;

    public void addBy() {
        final CharSequence[] options = {
                getString(R.string.addByLocation),
                getString(R.string.addByLoadingMap),
                getString(R.string.addByDistances),
                getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_input_method));
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.addByDistances))) {
                isAddByDistance = true;
                isAddByLocation = false;
                binding.addL.setText(R.string.addByDistances);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.addByLoadingMap))) {
                isAddByDistance = false;
                isAddByLocation = false;
                binding.addL.setText(R.string.addByLoadingMap);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.addByLocation))) {
                isAddByDistance = false;
                isAddByLocation = true;
                binding.addL.setText(R.string.addByLocation);
                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

}
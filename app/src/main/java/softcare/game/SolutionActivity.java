package softcare.game;

//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar; ;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Constraints;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import softcare.game.databinding.ActivitySolutionBinding;
import softcare.game.model.Alg;
import softcare.game.model.CodeX;
import softcare.game.model.ErrorCode;
import softcare.game.model.ProgressX;
import softcare.game.model.SolutionViewModel;
import softcare.game.model.Tsp;
import softcare.game.model.TspCode;
import softcare.gui.DialogPopup;
import softcare.gui.PlotTSP;
import softcare.gui.PointXY;
import softcare.gui.StyleDialog;

public class SolutionActivity extends AppCompatActivity {

    private ActivitySolutionBinding binding;


    private SolutionViewModel solutionViewModel;
    private boolean addByDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());
        solutionViewModel = new ViewModelProvider(this).get(SolutionViewModel.class);

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addByDistance) addD(view);
                else addL(view);
            }
        });
        binding.addL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addByDistance) {
                    addByDistance = false;
                    ((TextView) view).setText("Add by Points");
                } else {
                    addByDistance = true;
                    ((TextView) view).setText("Add by Distances");
                }
            }
        });
        binding.solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tsp tsp = solutionViewModel.getTsp();
                if (tsp != null) {
                    if (tsp.getTspActions() == TspCode.READ || tsp.getTspActions() == TspCode.UPDATE) {
                        if (tsp.getCities().size() > 2) {
                            launchAlg(view, tsp.getCities().size());
                        } else
                            Snackbar.make(view, "Cities too small to be a program", Snackbar.LENGTH_LONG).show();

                    } else if (tsp.getTspActions() == TspCode.SOLVED) dialogResult(tsp);
                    else Snackbar.make(view, "Cities are not added", Snackbar.LENGTH_LONG).show();

                } else
                    Snackbar.make(view, "Unable to initialised, add distances or locations", Snackbar.LENGTH_LONG).show();

            }
        });
        binding.refresh.setOnClickListener(view -> solutionViewModel.clear());/// change to button toback if already cleard
        solutionViewModel.getTspLiveData().observe(this, new Observer<Tsp>() {

            @Override
            public void onChanged(Tsp tsp) {
                if (tsp != null) {
                    if (tsp.getTspActions() != TspCode.SOLVED) {
                        binding.outputMatrix.setText(solutionViewModel.getPreview());
                        binding.outputLocations.setText(solutionViewModel.getPreviewXY());
                    } else {
                        dialogResult(tsp);
                    }
                } else {
                    binding.outputMatrix.setText("");
                    binding.outputLocations.setText("");
                    Snackbar.make(binding.getRoot(), "Inputs empty start a new problem", Snackbar.LENGTH_LONG).show();

                }
            }
        });
        solutionViewModel.getErrorCodeLiveData().observe(this, new Observer<ErrorCode>() {

            @Override
            public void onChanged(ErrorCode errorCode) {
                Snackbar.make(binding.getRoot(), "Error " + errorCode.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });
        solutionViewModel.getProgressXLiveData().observe(this, new Observer<ProgressX>() {
            @Override
            public void onChanged(ProgressX progressX) {
                Log.d("game", progressX.toString());
            }
        });


    }

    ActivityResultLauncher<Intent> addD = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " here " + (resultCode == RESULT_OK));
                    if (resultCode == RESULT_OK) {
                        Intent data = result.getData();
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

                        String name = data.getStringExtra("cityName");
                        double x = data.getDoubleExtra("x", 0);
                        double y = data.getDoubleExtra("y", 0);
                        solutionViewModel.addCityXY(name, new PointXY(x, y));
                    }
                }
            }
    );

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solution, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_save) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void dialogResult(Tsp tsp) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.plot_tsp_result);
        PlotTSP plotTSP = dialog.findViewById(R.id.plotTSP);
        dialog.getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.MATCH_PARENT);
        dialog.show();
        plotTSP.plot(tsp.getPointXY(), tsp.getCities(),
                tsp.getDirection(), tsp.getDistance());
        ((TextView) dialog.findViewById(R.id.result)).setText(tsp.getResult());
        dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.zoom_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plotTSP.zoomIn();
            }
        });
        dialog.findViewById(R.id.zoom_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plotTSP.zoomOut();
            }
        });


    }


    public void addD(View view) {
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
        Intent intent = new Intent(SolutionActivity.this, AddCityLActivity.class);
        addL.launch(intent);
        //Snackbar.make(view, "Unable to intiallsed", Snackbar.LENGTH_LONG) .setAction("Action", null).show();
    }

    public void launchAlg(View view, int length) {
        final CharSequence[] options = {
                getString(R.string.dyn),
                getString(R.string.knn),
                getString(R.string.gen),
                getString(R.string.auto),
                getString(R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_alg));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.dyn))) {
                    solutionViewModel.startAgl(Alg.DYN);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.knn))) {
                    solutionViewModel.startAgl(Alg.KNN);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.gen))) {
                    solutionViewModel.startAgl(Alg.GEN);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.auto))) {
                    if (length < 20)
                        solutionViewModel.startAgl(Alg.DYN);
                    else solutionViewModel.startAgl(Alg.KNN);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

}
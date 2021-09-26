package softcare.game;

//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import softcare.game.databinding.ActivitySolutionBinding;
import softcare.game.model.Alg;
import softcare.game.model.CodeX;
import softcare.game.model.ErrorCode;
import softcare.game.model.ProgressX;
import softcare.game.model.SolutionViewModel;
import softcare.game.model.TaskManager;
import softcare.game.model.Tsp;
import softcare.game.model.TspCode;
import softcare.game.model.TspData;
import softcare.game.model.TspResult;
import softcare.gui.PlotTSP;
import softcare.gui.PointXY;
import softcare.util.S;

public class SolutionActivity extends AppCompatActivity {

    private ActivitySolutionBinding binding;


    private SolutionViewModel solutionViewModel;
    private ImageView imageView;

private  TaskManager taskManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        taskManager= TaskManager.getInstance();
       _init();
    }

    Snackbar snackbar;
private  void  started(String m){
      snackbar= Snackbar.make(binding.getRoot(),m,Snackbar.LENGTH_INDEFINITE) ;
      snackbar.setAction("Cancel", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (taskManager!=null){
                  taskManager.shutdownNow();
              }
          }
      });
      snackbar.show();
}
    private  void  ended(){

    if(snackbar!=null) {
        snackbar.setText("Finished");
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

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isAddByDistance) addD(view);
                else if(isAddByLocation) addL(view);
                else addI();
            }
        });
        binding.addL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addBy();
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

                    } else if (tsp.getTspActions() == TspCode.SOLVED) prepareResult(tsp);
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
                        prepareResult(tsp);
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




    ActivityResultLauncher<Intent> addI = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " here " + (resultCode == RESULT_OK));
                    if (resultCode == RESULT_OK) {
                        Intent data = result.getData();
                       TspData dat = data.getParcelableExtra("data");
                       imgpath = data.getParcelableExtra("img");
                       if(dat!=null)
                        solutionViewModel.addIdata(dat);

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

    private void prepareResult(Tsp tsp ) {
        Intent intent= new Intent(this, PlotTspActivity.class);
        //List<String> cities, List<PointXY> locations, List<Integer> path,
        // double cost, long time, String result, String imagePath
        intent.putExtra("result",new TspResult(tsp.getCities(), tsp.getPointXY()
                , tsp.getDirection(), tsp.getCost(),tsp.getDuration(),tsp.getResult(),imgpath));
        startActivity(intent);
    }
    private  String imgpath=null;
    private void dialogResult4(Tsp tsp) {
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

    private void addI() {
        Tsp tsp = solutionViewModel.getTsp();
        Intent intent = new Intent(SolutionActivity.this, AddCityIActivity.class);
        addI.launch(intent);
        //Snackbar.make(view, "Unable to intiallsed", Snackbar.LENGTH_LONG) .setAction("Action", null).show();

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
                    started("Started "+ getString(R.string.dyn));
                    solutionViewModel.startAgl(Alg.DYN,taskManager);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.knn))) {
                    started("Started "+ getString(R.string.knn));
                    solutionViewModel.startAgl(Alg.KNN,taskManager);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.gen))) {
                    started("Started "+ getString(R.string.gen));
                    solutionViewModel.startAgl(Alg.GEN,taskManager);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.auto))) {
                    started("Started "+ getString(R.string.auto));
                    if (length < 20)
                        solutionViewModel.startAgl(Alg.DYN,taskManager);
                    else solutionViewModel.startAgl(Alg.KNN,taskManager);
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }



 boolean isAddByDistance;
    boolean  isAddByLocation=true;
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
                   isAddByDistance=true;
                   isAddByLocation=false;

                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.addByLoadingMap))) {
                isAddByDistance=false;
                isAddByLocation=false;

                dialog.dismiss();
            } else if (options[item].equals(getString(R.string.addByLocation))) {
                isAddByDistance=false;
                isAddByLocation=true;

                dialog.dismiss();
            }   else if (options[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

}
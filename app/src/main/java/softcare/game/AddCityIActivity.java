package softcare.game;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import softcare.game.model.CodeX;
import softcare.game.model.TspData;
import softcare.gui.PlotImage;
import softcare.util.S;

public class AddCityIActivity extends AppCompatActivity {
          PlotImage  plotImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city_iactivity);
        plotImage = findViewById(R.id.plotImage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((Switch)  findViewById(R.id.adding)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                plotImage.setAddingPoint(isChecked);
            }
        });
        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(plotImage);
            }
        });
    }


    public boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public  boolean  reinstallOn(int day, int month, int year){
        Date now= new Date();
        now.setTime(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Date end= S.getDateFromString(day + "/" + (month + 1) + "/" + year + " " + hour + ":" + minute);
        if(end.before(now)){
            System.out.println("Time is over");
            return true;
        }else{
            System.out.println("Time  remains ");
            return false;
        }
    }

    public  boolean reinstallOn(AppCompatActivity activity,int day, int month, int year){
        boolean res=reinstallOn(day,month,year);
        SharedPreferences s =activity.getApplicationContext().getSharedPreferences(prefName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putBoolean("grace", !res);
        editor.apply();
        editor.commit();
        if(res){
            //reinstall popup
            //new DialogPopup(activity).showUpdateApp();
        }

        return res;
    }
    public boolean askPermission(String[] permissions) {
        if (!hasPermissions(this, permissions)) {
            Snackbar.make(plotImage,
                    "Oh you have not accepted permission" +
                            " for the App to acess your storage",Snackbar.LENGTH_INDEFINITE)
                    .setAction("Accept now", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(AddCityIActivity.this, permissions, 1);
                        }
                    }).show();

        } else {
            return true;
        }
        Toast.makeText(this, "Permission not set", Toast.LENGTH_LONG).show();
        return false;
    }


    final private String prefName = "SoftCare";

    public void isNewUser(Context context, boolean update  ) {
        SharedPreferences s = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putBoolean("newUser", update);
        editor.apply();
        editor.commit();
        return ;
    }
    public boolean isNewUser(Context context  ) {
        SharedPreferences s = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return s.getBoolean("newUser", true);
    }


    ActivityResultLauncher<Intent> camara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " camara  " + (resultCode == RESULT_OK));
                    setIcon();
                }
            }
    );
    ActivityResultLauncher<Intent> gallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int resultCode = result.getResultCode();
                    Log.d(CodeX.tag, " gallery --------- " + (resultCode == RESULT_OK));
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = result.getData().getData();
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                        c.moveToFirst();
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);
                        c.close();


                        OutputStream outFile = null;
                        initDirectory(new File(Environment.getExternalStorageDirectory().toString() + S.PATH_IMAGES));
                        File file = new File(Environment.getExternalStorageDirectory().toString() + S.PATH_IMAGES,
                                "temp" + ".jpg");

                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath,
                                bitmapOptions);

                        try {
                            outFile = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                            outFile.flush();
                            outFile.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                        img= picturePath;
                        Log.d(CodeX.tag, "path of image from gallery..." + picturePath + "");
                        plotImage.setImageBitmap(thumbnail);
                    }
                }
            }
    );


    void setIcon() {
        initDirectory(new File(Environment.getExternalStorageDirectory().toString() + S.PATH_IMAGES));
        File file = new File(Environment.getExternalStorageDirectory().toString() + S.PATH_IMAGES, "temp" + ".jpg");

img= file.getAbsolutePath();
        return;
    }
String img;
    boolean initDirectory(File file) {
        if (file != null) {
            if (file.exists() & file.isDirectory()) {
                return true;
            }
            return file.mkdirs();

        }
        return false;
    }




    public void selectImage( View v) {
        if ( askPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE} )) {

            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Removed Image", "Cancel"};
            //{ "Take Photo", "Choose from Gallery", "Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        initDirectory(new File(Environment.getExternalStorageDirectory().toString() + S.PATH_IMAGES));
                        File file = new File(Environment.getExternalStorageDirectory().toString() + S.PATH_IMAGES, "temp" + ".jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(AddCityIActivity.this,
                                        getApplicationContext().getPackageName() + ".provider", file));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                       camara.launch( intent);
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                       gallery.launch( intent);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    } else if (options[item].equals("Removed Image")) {

                        dialog.dismiss();
                        ///  deleteIcon();
                    }
                }
            });
            builder.show();
        }

    }










public void undo(View v){
        plotImage.undo();

}

    public void finish(View v){
                    Intent intent = new Intent(this, SolutionActivity.class);
                    TspData data= new TspData(plotImage.getNames(),plotImage.getLocations());int i=0;


                    intent.putExtra("data",data);
        intent.putExtra("img", img);
                    intent.setFlags(RESULT_OK);
                    Log.d(CodeX.tag,"Sending name and data ");
                    setResult(RESULT_OK,intent);
                    finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_i, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == R.id.action_finish) {
            finish(plotImage);
            return true;
        }else  if (menuId == R.id.action_undo) {
            undo(plotImage);
            return true;
        } else  if (menuId == R.id.action_load) {
            selectImage(plotImage);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
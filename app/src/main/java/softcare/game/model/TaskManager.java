package softcare.game.model;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskManager {
    private  static TaskManager instance=null;
    private  static  final int CORE_POL_SIZE=5;
    private  static  final int KEEP_ALIVE_TIME=50;
    private  static  final int MAX_POL_SIZE=10;
    final BlockingQueue<Runnable> workQueue;
    private ThreadPoolExecutor threadPoolExecutor;
    static {
        instance= new TaskManager();
    }
    private  TaskManager(){
        workQueue= new LinkedBlockingDeque<Runnable>();
        threadPoolExecutor= new ThreadPoolExecutor(CORE_POL_SIZE,MAX_POL_SIZE,KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,workQueue);

    }
      public void   shutdownNow(){
          workQueue.clear();
      }
    public static TaskManager getInstance() {
        return instance;
    }

    public TaskManager runTask(Runnable runnable){
        threadPoolExecutor.execute(runnable);

        return instance;
    }
    public  void toast(AppCompatActivity activity, String msg){
       activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(),msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    public  void snackBar(AppCompatActivity activity, String msg, View view){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}

package softcare.game.model;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public   class TaskX<S> implements Runnable {
    @Override
    public void run() {

    }
/*

     public  static S call(Executor e, Callable<S> c){

        return  "4";
    }




     public TaskX<String> searchFolder(String folderName) {
        return TaskX.call(null, new Callable<String>() {
            @Override
            public String call() throws Exception {
              return "folderName";
            }
        });
    }
 */
}

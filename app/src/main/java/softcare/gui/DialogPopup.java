package softcare.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import softcare.game.R;

public class DialogPopup extends StyleDialog {
    public DialogPopup(@NonNull Context context) {
        super(context);
    }
    public void showConfirm(String msg){
        setContentView(R.layout.pop_confirm);
                show();
        ((TextView)  findViewById(R.id.textView)).setText(msg);
      /* WebView webView= findViewById(R.id.webView);
       webView.loadData(context.getString(R.string.start_html)
                       + msg
              + context.getString(R.string.end_html),"text/html","utf-8");*/
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

    }
    public  void showWarning(String msg){ 
        setContentView(R.layout.pop_warning); //style id
       // getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation; //style id
       show();
        ((TextView) findViewById(R.id.textView)).setText(msg);
    }
    public   void showSuccess(String msg){
       setContentView(R.layout.pop_sucess);
        show();
        ((TextView)  findViewById(R.id.textView)).setText(msg);
    }
    public   void showNote(String msg){
        setContentView(R.layout.pop_note);
        show();
        ((TextView)  findViewById(R.id.textView)).setText(msg);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }


}
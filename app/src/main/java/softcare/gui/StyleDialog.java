package softcare.gui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

import softcare.game.R;

public class StyleDialog extends Dialog {
    public StyleDialog(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void show() {
        super.show();
        getWindow().setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
}




    /*
        private boolean submit(View view) {
            EditText name = view.findViewById(R.id.name_edit);
            EditText unit = view.findViewById(R.id.unit_edit);
            EditText quantity = view.findViewById(R.id.quantity_edit);
            EditText price = view.findViewById(R.id.price_edit);
            EditText totalPrice = view.findViewById(R.id.total_edit);
            Ut.resetPrice((EditText) view.findViewById(R.id.quantity_edit), (EditText) view.findViewById(R.id.price_edit), (EditText) view.findViewById(R.id.total_edit));
            Transaction useTransaction = new Transaction(id, System.currentTimeMillis());

            if (Ut.validateEditText(1, name, unit, quantity, price)) {
                useTransaction.setName(name.getText().toString());
                useTransaction.setUnit(unit.getText().toString());
                try {
                    useTransaction.setQuantity(Integer.parseInt(quantity.getText().toString()));
                    useTransaction.setAmount(Integer.parseInt(quantity.getText().toString()) *
                            Double.parseDouble(price.getText().toString()));

                    backgroundSubmit(useTransaction);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
                }

                return true;
            } else {
                Toast.makeText(context, getString(R.string.empty_field_error), Toast.LENGTH_LONG).show();
            }

            return false;
        }
    */

package bd.org.coronacare.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

import bd.org.coronacare.R;

public class OptionPicker {
    public static void chooseAnOption(final Context context, final TextInputEditText field, final String[] options) {
        new MaterialAlertDialogBuilder(context, R.style.AppTheme_MaterialAlertDialog)
            .setTitle("Choose an option")
            .setSingleChoiceItems(options, Arrays.asList(options).indexOf(field.getText().toString()), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    field.setText(options[position]);
                }
            })
            .setPositiveButton("Ok", null)
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .show();
    }
}

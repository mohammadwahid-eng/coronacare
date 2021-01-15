package bd.org.coronacare.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bd.org.coronacare.R;

public class DataPicker {
    public static void chooseAnOption(final Context context, final TextInputEditText field, final String[] options) {
        final StringBuilder selected = new StringBuilder();
        selected.append(field.getText().toString());
        new MaterialAlertDialogBuilder(context, R.style.AppTheme_MaterialAlertDialog)
            .setTitle("Choose an option")
            .setSingleChoiceItems(options, Arrays.asList(options).indexOf(field.getText().toString()), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    selected.setLength(0);
                    selected.append(options[position]);
                }
            })
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    field.setText(selected.toString());
                }
            })
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .show();
    }

    public static void chooseMultipleOptions(final Context context, final TextInputEditText field, final String[] options) {
        final List<String> selectedOptions = new ArrayList<>();
        boolean[] checkedItems = new boolean[options.length];
        Arrays.fill(checkedItems, false);

        if (!TextUtils.isEmpty(field.getText())) {
            Collections.addAll(selectedOptions, field.getText().toString().split(", ", -1));
            for (int i=0;i<selectedOptions.size();i++) {
                checkedItems[Arrays.asList(options).indexOf(selectedOptions.get(i))] = true;
            }
        }

        new MaterialAlertDialogBuilder(context, R.style.AppTheme_MaterialAlertDialog)
            .setTitle("Choose options")
            .setMultiChoiceItems(options, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if (!selectedOptions.contains(options[position]) && isChecked) {
                        selectedOptions.add(options[position]);
                    } else {
                        selectedOptions.remove(options[position]);
                    }
                }
            })
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (selectedOptions.isEmpty()) {
                        field.setText("");
                    } else {
                        StringBuilder output = new StringBuilder();
                        for (int i=0;i<selectedOptions.size();i++) {
                            output.append(selectedOptions.get(i));
                            if (i<selectedOptions.size()-1) {
                                output.append(", ");
                            }
                        }
                        field.setText(output.toString());
                    }
                }
            })
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .show();
    }

    public static void chooseDate(FragmentManager fragmentManager, final TextInputEditText field) {
        MaterialDatePicker.Builder datePickerBuilder = MaterialDatePicker.Builder.datePicker();
        datePickerBuilder.setSelection(DateTimeFormat.milliseconds(field.getText().toString()));
        MaterialDatePicker datePicker = datePickerBuilder.build();
        datePicker.show(fragmentManager, "BIRTH_DATE");
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                field.setText(DateFormat.getDateInstance(DateFormat.LONG).format(selection));
            }
        });
    }

    public static void chooseTime(FragmentManager fragmentManager, final TextInputEditText field) {
        final MaterialTimePicker timePicker = new MaterialTimePicker.Builder().build();
        timePicker.show(fragmentManager, "TIME_PICKER");
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field.setText(DateTimeFormat.getReadableTime(timePicker.getHour(), timePicker.getMinute()));
            }
        });
    }
}

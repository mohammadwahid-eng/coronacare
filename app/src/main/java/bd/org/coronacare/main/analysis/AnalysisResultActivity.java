package bd.org.coronacare.main.analysis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import bd.org.coronacare.R;

public class AnalysisResultActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private ImageView photo;
    private MaterialTextView content;
    private MaterialButton chkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_result);

        toolbar = findViewById(R.id.toolbar);
        photo = findViewById(R.id.rskar_photo);
        content = findViewById(R.id.rskar_content);
        chkBtn = findViewById(R.id.rskar_btn);

        toolbar.setTitle("Assessment Result");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadResult();
    }

    private void loadResult() {
        String severity = getIntent().getStringExtra("SEVERITY");
        int graphic = 0;
        int message = 0;

        switch (severity) {
            case "Mild":
                graphic = R.drawable.gr_risk_mild;
                message = R.string.risk_mild;
                break;
            case "Moderate":
                graphic = R.drawable.gr_risk_moderate;
                message = R.string.risk_moderate;
                break;
            case "Severe":
                graphic = R.drawable.gr_risk_severe;
                message = R.string.risk_severe;
                break;
            case "None":
                graphic = R.drawable.gr_risk_none;
                message = R.string.risk_none;
                break;
        }

        photo.setImageResource(graphic);
        content.setText(getString(message));
    }
}
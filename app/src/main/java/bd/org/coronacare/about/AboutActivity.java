package bd.org.coronacare.about;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import bd.org.coronacare.R;

public class AboutActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialTextView holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = findViewById(R.id.toolbar);
        holder = findViewById(R.id.ac_holder);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadContent();
    }

    private void loadContent() {

        String type = getIntent().getStringExtra("TYPE");

        switch (type) {
            case "FAQ":
                toolbar.setTitle(R.string.frequently_asked_questions);
                holder.setText(R.string.faq_content);
                break;
            case "TERMS":
                toolbar.setTitle(R.string.terms_and_conditions);
                holder.setText(R.string.terms_content);
                break;
            case "PRIVACY":
                toolbar.setTitle(R.string.privacy_policy);
                holder.setText(R.string.privacy_content);
                break;
            case "ABOUT_US":
                toolbar.setTitle(R.string.about_us);
                holder.setText(R.string.about_content);
                break;
        }
    }
}
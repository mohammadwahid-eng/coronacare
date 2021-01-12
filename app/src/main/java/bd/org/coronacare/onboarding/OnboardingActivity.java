package bd.org.coronacare.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import bd.org.coronacare.R;
import bd.org.coronacare.login.LoginOptionsActivity;
import bd.org.coronacare.models.Onboarding;

public class OnboardingActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private OnboardingAdapter adapter;
    private TabLayout indicator;
    private List<Onboarding> onbrdings = new ArrayList<>();
    private MaterialButton nextBtn;
    private MaterialButton skipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager   = findViewById(R.id.ob_viewpager);
        indicator   = findViewById(R.id.ob_indicator);
        nextBtn     = findViewById(R.id.ob_next_btn);
        skipBtn     = findViewById(R.id.ob_skip_btn);

        nextBtn.setOnClickListener(this);
        skipBtn.setOnClickListener(this);

        initOnboaring();
    }

    private void initOnboaring() {
        SharedPreferences sharedPreferences = getSharedPreferences("ONBOARDING", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("ONBOARDING_COMPLETE", false)) {
            showOnboarding();
        } else {
            skipOnboarding();
        }
    }

    private void showOnboarding() {
        onbrdings.add(new Onboarding(getString(R.string.search_doctors), getString(R.string.search_doctors_desc), R.drawable.gr_doctors));
        onbrdings.add(new Onboarding(getString(R.string.online_consultation), getString(R.string.online_consultation_desc), R.drawable.gr_consultation));
        onbrdings.add(new Onboarding(getString(R.string.book_appointment), getString(R.string.book_appointment_desc), R.drawable.gr_appointment));
        onbrdings.add(new Onboarding(getString(R.string.analysis), getString(R.string.analysis_desc), R.drawable.gr_analysis));
        onbrdings.add(new Onboarding(getString(R.string.plasma_management), getString(R.string.plasma_management_desc), R.drawable.gr_plasma));
        onbrdings.add(new Onboarding(getString(R.string.emergency), getString(R.string.emergency_desc), R.drawable.gr_emergency));

        adapter = new OnboardingAdapter(this, onbrdings);
        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                nextBtn.setText((position == (onbrdings.size() - 1)) ? "Get Started" : "Next");
                skipBtn.setVisibility((position == (onbrdings.size() - 1)) ? View.INVISIBLE : View.VISIBLE);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void skipOnboarding() {
        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("ONBOARDING", MODE_PRIVATE).edit();
        sharedPreferencesEditor.putBoolean("ONBOARDING_COMPLETE", true);
        sharedPreferencesEditor.apply();
        startActivity(new Intent(OnboardingActivity.this, LoginOptionsActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(nextBtn)) {
            int currentItem = viewPager.getCurrentItem();
            if(currentItem == onbrdings.size() - 1) {
                skipOnboarding();
            } else if (currentItem < onbrdings.size()) {
                viewPager.setCurrentItem(++currentItem);
            }
        } else if(v.equals(skipBtn)) {
            skipOnboarding();
        }
    }

    private static class OnboardingAdapter extends PagerAdapter {

        private Context mContext;
        private List<Onboarding> onboardings;

        public OnboardingAdapter(Context context, List<Onboarding> onboardings) {
            this.mContext = context;
            this.onboardings = onboardings;
        }

        @Override
        public int getCount() {
            return onboardings.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View page = inflater.inflate(R.layout.layout_onboarding, null);

            MaterialTextView title = page.findViewById(R.id.ob_title);
            MaterialTextView description = page.findViewById(R.id.ob_desc);
            ImageView photo = page.findViewById(R.id.ob_photo);

            title.setText(onboardings.get(position).getTitle());
            description.setText(onboardings.get(position).getDesc());

            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            photo.setColorFilter(new ColorMatrixColorFilter(matrix));

            photo.setImageResource(onboardings.get(position).getPhoto());
            container.addView(page);
            return page;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
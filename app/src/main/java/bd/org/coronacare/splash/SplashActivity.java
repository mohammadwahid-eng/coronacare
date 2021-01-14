package bd.org.coronacare.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import bd.org.coronacare.R;
import bd.org.coronacare.main.MainActivity;
import bd.org.coronacare.onboarding.OnboardingActivity;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CALL_PHONE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            updateUI(mAuth.getCurrentUser());
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError dexterError) {
                        Toast.makeText(SplashActivity.this, "Permission Error Occurred!", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void updateUI(final FirebaseUser currentUser) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser!=null) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                    finish();
                }
            }
        }, 2800);
    }

    private void showSettingsDialog() {
        new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog)
                .setTitle("Need Permissions")
                .setMessage("This app needs permission to use this feature. You can grant them in app settings.")
                .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        settingsScreen();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show();

    }

    private void settingsScreen() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}
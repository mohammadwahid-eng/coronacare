package bd.org.coronacare.main.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import bd.org.coronacare.R;
import bd.org.coronacare.about.AboutActivity;

public class MenuFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialTextView currentUserName;
    private MaterialTextView currentUserMobile;
    private CircularImageView currentUserPhoto;

    private NavigationView account;
    private NavigationView emergency;
    private NavigationView about;
    private NavigationView logout;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frame = inflater.inflate(R.layout.fragment_menu, container, false);

        currentUserName = frame.findViewById(R.id.menu_name);
        currentUserMobile = frame.findViewById(R.id.menu_mobile);
        currentUserPhoto = frame.findViewById(R.id.menu_photo);
        account = frame.findViewById(R.id.menu_account);
        emergency = frame.findViewById(R.id.menu_emergency);
        about = frame.findViewById(R.id.menu_about);
        logout = frame.findViewById(R.id.menu_logout);

        for (int i=0;i<2;i++) {
            account.getMenu().getItem(i).setActionView(R.layout.layout_menu_end_icon);
        }
        for (int i=0;i<3;i++) {
            emergency.getMenu().getItem(i).setActionView(R.layout.layout_menu_end_icon);
        }
        for (int i=0;i<4;i++) {
            about.getMenu().getItem(i).setActionView(R.layout.layout_menu_end_icon);
        }
        logout.getMenu().getItem(0).setActionView(R.layout.layout_menu_end_icon);

        account.setNavigationItemSelectedListener(this);
        emergency.setNavigationItemSelectedListener(this);
        about.setNavigationItemSelectedListener(this);
        logout.setNavigationItemSelectedListener(this);

        return frame;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnue_profile) {
            //startActivity(new Intent(getActivity(), ProfileEditActivity.class));
        } else if (item.getItemId() == R.id.mnue_mobile) {
            //startActivity(new Intent(getActivity(), ProfileMobileUpdateActivity.class));
        } else if (item.getItemId() == R.id.mnue_dc) {
            //getActivity().startActivity(new Intent(getActivity(), EmergencyActivity.class).putExtra("TYPE", "DIAGNOSTIC_CENTER"));
        } else if (item.getItemId() == R.id.mnue_hotlines) {
            //getActivity().startActivity(new Intent(getActivity(), EmergencyActivity.class).putExtra("TYPE", "HOTLINES"));
        } else if (item.getItemId() == R.id.mnue_ambulance) {
            //getActivity().startActivity(new Intent(getActivity(), EmergencyActivity.class).putExtra("TYPE", "AMBULANCE"));
        } else if (item.getItemId() == R.id.mnua_faq) {
            getActivity().startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "FAQ"));
        } else if (item.getItemId() == R.id.mnua_terms) {
            getActivity().startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "TERMS"));
        } else if (item.getItemId() == R.id.mnua_privacy) {
            getActivity().startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "PRIVACY"));
        } else if (item.getItemId() == R.id.mnua_us) {
            getActivity().startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "ABOUT_US"));
        } else if (item.getItemId() == R.id.mnu_logout) {
//            mDatabase.child("users").child(mAuth.getUid()).child("online").setValue(false);
//            mDatabase.child("users").child(mAuth.getUid()).child("lastOnline").setValue(ServerValue.TIMESTAMP);
//            mAuth.signOut();
//            Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
//            getActivity().startActivity(new Intent(getActivity(), LoginOptionsActivity.class));
//            getActivity().finish();
        }
        return true;
    }
}
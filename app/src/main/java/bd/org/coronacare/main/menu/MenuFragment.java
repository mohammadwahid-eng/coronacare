package bd.org.coronacare.main.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import bd.org.coronacare.R;
import bd.org.coronacare.about.AboutActivity;
import bd.org.coronacare.emergency.EmergencyActivity;
import bd.org.coronacare.login.LoginOptionsActivity;
import bd.org.coronacare.models.User;
import bd.org.coronacare.profile.edit.ChangeMobileActivity;
import bd.org.coronacare.profile.edit.EditProfileActivity;

public class MenuFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

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
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

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

        updateUI(mAuth.getCurrentUser());

        return frame;
    }

    private void updateUI(FirebaseUser currentUser) {
        mDatabase.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user!=null) {
                    currentUserName.setText(user.getName());
                    currentUserMobile.setText(user.getMobile());
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.gr_avatar).into(currentUserPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void doLogout() {
        mDatabase.child("users").child(mAuth.getUid()).child("online").setValue(false);
        mDatabase.child("users").child(mAuth.getUid()).child("lastOnline").setValue(ServerValue.TIMESTAMP);
        mAuth.signOut();
        Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnue_profile) {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        } else if (item.getItemId() == R.id.mnue_mobile) {
            startActivity(new Intent(getActivity(), ChangeMobileActivity.class));
        } else if (item.getItemId() == R.id.mnue_dc) {
            startActivity(new Intent(getActivity(), EmergencyActivity.class).putExtra("TYPE", "DIAGNOSTIC_CENTER"));
        } else if (item.getItemId() == R.id.mnue_hotlines) {
            startActivity(new Intent(getActivity(), EmergencyActivity.class).putExtra("TYPE", "HOTLINES"));
        } else if (item.getItemId() == R.id.mnue_ambulance) {
            startActivity(new Intent(getActivity(), EmergencyActivity.class).putExtra("TYPE", "AMBULANCE"));
        } else if (item.getItemId() == R.id.mnua_faq) {
            startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "FAQ"));
        } else if (item.getItemId() == R.id.mnua_terms) {
            startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "TERMS"));
        } else if (item.getItemId() == R.id.mnua_privacy) {
            startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "PRIVACY"));
        } else if (item.getItemId() == R.id.mnua_us) {
            startActivity(new Intent(getActivity(), AboutActivity.class).putExtra("TYPE", "ABOUT_US"));
        } else if (item.getItemId() == R.id.mnu_logout) {
            doLogout();
            startActivity(new Intent(getActivity(), LoginOptionsActivity.class));
            getActivity().finish();
        }
        return true;
    }
}
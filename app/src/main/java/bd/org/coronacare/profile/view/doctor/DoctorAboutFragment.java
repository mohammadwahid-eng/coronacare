package bd.org.coronacare.profile.view.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bd.org.coronacare.R;
import bd.org.coronacare.models.Doctor;

public class DoctorAboutFragment extends Fragment {

    private DatabaseReference mDatabase;
    private String doctorID;
    private MaterialTextView qualification;
    private MaterialTextView specializations;
    private MaterialTextView experience;
    private MaterialTextView fee;
    private MaterialTextView practiceTime;
    private MaterialTextView hospitalName;
    private MaterialTextView offDays;
    private MaterialTextView hospitalThana;
    private MaterialTextView hospitalDistrict;
    private MaterialTextView hospitalMobile;

    public DoctorAboutFragment() {
        // Required empty public constructor
    }

    public DoctorAboutFragment(String doctorID) {
        this.doctorID = doctorID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frame =  inflater.inflate(R.layout.fragment_doctor_about, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        qualification = frame.findViewById(R.id.dpa_qualification);
        specializations = frame.findViewById(R.id.dpa_specializations);
        experience = frame.findViewById(R.id.dpa_experience);
        fee = frame.findViewById(R.id.dpa_fee);
        practiceTime = frame.findViewById(R.id.dpa_ptime);
        offDays = frame.findViewById(R.id.dpa_offdays);
        hospitalName = frame.findViewById(R.id.dpa_hname);
        hospitalThana = frame.findViewById(R.id.dpa_hthana);
        hospitalDistrict = frame.findViewById(R.id.dpa_hdistrict);
        hospitalMobile = frame.findViewById(R.id.dpa_hmobile);

        loadDetails();

        return frame;
    }

    private void loadDetails() {
        mDatabase.child("users").child(doctorID).child("doctor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Doctor mDoctor = snapshot.getValue(Doctor.class);
                if (mDoctor!=null) {
                    qualification.setText(mDoctor.getQualification());
                    specializations.setText(mDoctor.getSpecializations());
                    experience.setText(mDoctor.getExperience());
                    if (!TextUtils.isEmpty(experience.getText())) {
                        experience.append(" Years");
                    }
                    fee.setText(mDoctor.getFee());
                    if (!TextUtils.isEmpty(fee.getText())) {
                        fee.append(" Tk");
                    }
                    if (!TextUtils.isEmpty(mDoctor.getTfrom())) {
                        practiceTime.setText(mDoctor.getTfrom());
                    }
                    if (!TextUtils.isEmpty(mDoctor.getTimeto())) {
                        practiceTime.append(" - ");
                        practiceTime.setText(mDoctor.getTimeto());
                    }
                    offDays.setText(mDoctor.getOffdays());
                    hospitalName.setText(mDoctor.getHname());
                    hospitalThana.setText(mDoctor.getHthana());
                    hospitalDistrict.setText(mDoctor.getHdistrict());
                    hospitalMobile.setText(mDoctor.getHmobile());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
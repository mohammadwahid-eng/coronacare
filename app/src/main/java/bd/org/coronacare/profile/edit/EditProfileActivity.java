package bd.org.coronacare.profile.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import bd.org.coronacare.R;
import bd.org.coronacare.models.Doctor;
import bd.org.coronacare.models.User;
import bd.org.coronacare.utils.DataPicker;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private MaterialToolbar toolbar;
    private NestedScrollView container;
    private CircularImageView photo;
    private CircularImageView photoBtn;
    private Uri photoURI;
    private SwitchMaterial donor;
    private TextInputEditText name;
    private TextInputEditText dob;
    private TextInputEditText gender;
    private TextInputEditText bgroup;
    private TextInputEditText thana;
    private TextInputEditText district;
    private TextInputEditText occupation;

    private LinearLayout doctorLayout;
    private TextInputEditText qualification;
    private TextInputEditText specializations;
    private TextInputEditText hospitalName;
    private TextInputEditText hospitalThana;
    private TextInputEditText hospitalDistrict;
    private TextInputEditText hospitalMobile;
    private TextInputEditText experience;
    private TextInputEditText fee;
    private TextInputEditText timeFrom;
    private TextInputEditText timeTo;
    private TextInputEditText offDays;
    private TextInputEditText service;
    private ProgressDialog preLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mStorage = FirebaseStorage.getInstance().getReference();

        preLoader = new ProgressDialog(this);
        preLoader.setCanceledOnTouchOutside(false);

        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.ep_scroll);
        photo = findViewById(R.id.ep_photo);
        photoBtn = findViewById(R.id.ep_photo_btn);
        donor = findViewById(R.id.ep_donor);
        name = findViewById(R.id.ep_name);
        dob = findViewById(R.id.ep_dob);
        gender = findViewById(R.id.ep_gender);
        bgroup = findViewById(R.id.ep_bgroup);
        thana = findViewById(R.id.ep_thana);
        district = findViewById(R.id.ep_district);
        occupation = findViewById(R.id.ep_occupation);


        doctorLayout = findViewById(R.id.ep_doctor_layout);
        qualification = findViewById(R.id.ep_qualification);
        specializations = findViewById(R.id.ep_specializations);
        hospitalName = findViewById(R.id.ep_hospital_name);
        hospitalThana = findViewById(R.id.ep_hospital_thana);
        hospitalDistrict = findViewById(R.id.ep_hospital_district);
        hospitalMobile = findViewById(R.id.ep_hospital_mobile);
        experience = findViewById(R.id.ep_experience);
        fee = findViewById(R.id.ep_fee);
        timeFrom = findViewById(R.id.ep_time_from);
        timeTo = findViewById(R.id.ep_time_to);
        offDays = findViewById(R.id.ep_offdays);
        service = findViewById(R.id.ep_service);

        photoBtn.setOnClickListener(this);
        dob.setOnClickListener(this);
        gender.setOnClickListener(this);
        bgroup.setOnClickListener(this);
        thana.setOnClickListener(this);
        district.setOnClickListener(this);
        occupation.setOnClickListener(this);

        specializations.setOnClickListener(this);
        hospitalThana.setOnClickListener(this);
        hospitalDistrict.setOnClickListener(this);
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);
        offDays.setOnClickListener(this);
        service.setOnClickListener(this);

        toolbar.setTitle("Edit Profile");
        toolbar.inflateMenu(R.menu.menu_edit_profile);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnup_save) {
                    updateProfile();
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (savedInstanceState==null) {
            updateUI(mAuth.getCurrentUser());
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        mDatabase.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                if (mUser!=null) {
                    Picasso.get().load(mUser.getPhoto()).placeholder(R.drawable.gr_avatar).into(photo);
                    donor.setChecked(mUser.isDonor());
                    name.setText(mUser.getName());
                    dob.setText(mUser.getDob());
                    gender.setText(mUser.getGender());
                    bgroup.setText(mUser.getBgroup());
                    thana.setText(mUser.getThana());
                    district.setText(mUser.getDistrict());
                    occupation.setText(mUser.getOccupation());

                    Doctor mDoctor = mUser.getDoctor();
                    if (mDoctor!=null) {
                        doctorLayout.setVisibility(View.VISIBLE);
                        qualification.setText(mDoctor.getQualification());
                        specializations.setText(mDoctor.getSpecializations());
                        experience.setText(mDoctor.getExperience());
                        fee.setText(mDoctor.getFee());
                        timeFrom.setText(mDoctor.getTfrom());
                        timeTo.setText(mDoctor.getTimeto());
                        offDays.setText(mDoctor.getOffdays());
                        hospitalName.setText(mDoctor.getHname());
                        hospitalThana.setText(mDoctor.getHthana());
                        hospitalDistrict.setText(mDoctor.getHdistrict());
                        hospitalMobile.setText(mDoctor.getHmobile());
                        service.setText(mDoctor.isService() ? "Yes" : "No");
                    }

                    preLoader.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), 111);
    }

    private void uploadPhoto(Bitmap bitmap) {
        if (bitmap!=null) {
            preLoader.setMessage("Processing...");
            preLoader.show();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();

            mStorage.child("photos").child(mAuth.getUid() + ".jpg").putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    preLoader.dismiss();
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoURI = uri;
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    preLoader.dismiss();
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    preLoader.setMessage("Uploading: " + (int)progress + "%");
                }
            });
        }
    }

    private void chooseAnOption(final TextInputEditText field, final String[] options) {
        final StringBuilder selected = new StringBuilder();
        new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog)
                .setTitle("Choose an option")
                .setSingleChoiceItems(options, Arrays.asList(options).indexOf(field.getText().toString()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        selected.append(options[position]);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        field.setText(selected.toString());
                        if (field.getId() == R.id.ep_occupation) {
                            if (field.getText().toString().equals("Doctor")) {
                                doctorLayout.setVisibility(View.VISIBLE);
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        container.smoothScrollTo(0, doctorLayout.getTop() - 44, 2000);
                                    }
                                }, 100);
                            } else {
                                doctorLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show();
    }


    private void updateProfile() {
        preLoader.setMessage("Updating...");
        preLoader.show();

        Map user = new HashMap<>();
        if (photoURI!=null) {
            user.put("users/" + mAuth.getUid() + "/photo", photoURI.toString());
        }
        user.put("users/" + mAuth.getUid() + "/donor", donor.isChecked());
        user.put("users/" + mAuth.getUid() + "/name", name.getText().toString());
        user.put("users/" + mAuth.getUid() + "/dob", dob.getText().toString());
        user.put("users/" + mAuth.getUid() + "/gender", gender.getText().toString());
        user.put("users/" + mAuth.getUid() + "/bgroup", bgroup.getText().toString());
        user.put("users/" + mAuth.getUid() + "/thana", thana.getText().toString());
        user.put("users/" + mAuth.getUid() + "/district", district.getText().toString());
        user.put("users/" + mAuth.getUid() + "/occupation", occupation.getText().toString());

        if (occupation.getText().toString().equals("Doctor")) {
            Map doctor = new HashMap<>();
            doctor.put("qualification", qualification.getText().toString());
            doctor.put("specializations", specializations.getText().toString());
            doctor.put("hname", hospitalName.getText().toString());
            doctor.put("hthana", hospitalThana.getText().toString());
            doctor.put("hdistrict", hospitalDistrict.getText().toString());
            doctor.put("hmobile", hospitalMobile.getText().toString());
            doctor.put("experience", experience.getText().toString());
            doctor.put("fee", fee.getText().toString());
            doctor.put("tfrom", timeFrom.getText().toString());
            doctor.put("timeto", timeTo.getText().toString());
            doctor.put("offdays", offDays.getText().toString());
            doctor.put("service", service.getText().toString().equals("Yes"));
            user.put("users/" + mAuth.getUid() + "/doctor", doctor);
        } else {
            user.put("users/" + mAuth.getUid() + "/doctor", null);
        }

        mDatabase.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                preLoader.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profile has updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.equals(photoBtn)) {
            choosePhoto();
        } else if(v.equals(dob)) {
            DataPicker.chooseDate(getSupportFragmentManager(), dob);
        } else if(v.equals(gender)) {
            DataPicker.chooseAnOption(this, gender, new String[] {"Male", "Female"});
        } else if(v.equals(bgroup)) {
            DataPicker.chooseAnOption(this, bgroup, new String[] {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        } else if(v.equals(thana)) {
            DataPicker.chooseAnOption(this, thana, new String[] {"Adabor", "Airport", "Akbar Shah", "Aranghata", "Badda", "Bakolia", "Banani", "Bandar", "Bangshal", "Barisal University", "Bayazid Bostami", "Belpukur", "Bhashantek", "Boalia", "Cantonment", "Chackbazar", "Chandgaon", "Chandrima", "Char Monai", "Chawk Bazar", "Dakshin Khan", "Damkura", "Darus-Salam", "Daulatpur", "Demra", "Dhanmondi", "Doublemooring", "EPZ", "Gandaria", "Gulshan", "Hajirhat", "Halishahar", "Haragach", "Harintana", "Hatirjheel", "Hazaribag", "Jalalabad", "Jattrabari", "Kadamtoli", "Kafrul", "Kalabagan", "Kamrangirchar", "Karnahar", "Karnophuli", "Kashipur", "Kasiadanga", "Katakhali", "Kawnia", "Khalishpur", "Khan Jahan Ali", "Khilgaon", "Khilkhet", "Khulna Sadar", "Khulshi", "Kotwali", "Kotwali Model", "Labanchara", "Lalbagh", "Mahiganj", "Mirpur Model", "Moglabazar", "Mohammadpur", "Motihar", "Motijheel", "Mugda", "New Market", "Paba", "Pahartali", "Pallabi", "Paltan Model", "Panchlaish", "Parshuram", "Patenga", "Rajpara", "Ramna Model", "Rampura", "Rupatali", "Rupnagar", "Sabujbag", "Sadarghat", "Shah Ali", "Shah Poran (R.)", "Shahbag", "Shahjahanpur", "Shahmukhdum", "Sher e Bangla Nagar", "Shyampur", "Sonadanga", "South Surma", "Sutrapur", "Tajhat", "Tejgaon", "Tejgaon Industrial", "Turag", "Uttar Khan", "Uttara East", "Uttara West", "Vatara", "Wari"});
        } else if(v.equals(district)) {
            DataPicker.chooseAnOption(this, district, new String[] {"Bagerhat", "Bandarban", "Barguna", "Barisal", "Bhola", "Bogra", "Brahmanbaria", "Chandpur", "Chittagong", "Chuadanga", "Comilla", "Cox's Bazar", "Dhaka", "Dinajpur", "Faridpur", "Feni", "Gaibandha", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jessore", "Jhalokati", "Jhenaidah", "Joypurhat", "Khagrachari", "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Lalmonirhat", "Madaripur", "Magura", "Manikganj", "Maulvibazar", "Meherpur", "Munshiganj", "Mymensingh", "Naogaon", "Narail", "Narayanganj", "Narsingdi", "Natore", "Nawabganj", "Netrokona", "Nilphamari", "Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi", "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajgonj", "Sunamganj", "Sylhet", "Tangail", "Thakurgaon"});
        } else if(v.equals(occupation)) {
            chooseAnOption(occupation, new String[] {"Doctor", "Engineer", "Lawyer", "Businessman", "Teacher", "Other"});
        } else if(v.equals(hospitalThana)) {
            DataPicker.chooseAnOption(this, hospitalThana, new String[] {"Adabor", "Airport", "Akbar Shah", "Aranghata", "Badda", "Bakolia", "Banani", "Bandar", "Bangshal", "Barisal University", "Bayazid Bostami", "Belpukur", "Bhashantek", "Boalia", "Cantonment", "Chackbazar", "Chandgaon", "Chandrima", "Char Monai", "Chawk Bazar", "Dakshin Khan", "Damkura", "Darus-Salam", "Daulatpur", "Demra", "Dhanmondi", "Doublemooring", "EPZ", "Gandaria", "Gulshan", "Hajirhat", "Halishahar", "Haragach", "Harintana", "Hatirjheel", "Hazaribag", "Jalalabad", "Jattrabari", "Kadamtoli", "Kafrul", "Kalabagan", "Kamrangirchar", "Karnahar", "Karnophuli", "Kashipur", "Kasiadanga", "Katakhali", "Kawnia", "Khalishpur", "Khan Jahan Ali", "Khilgaon", "Khilkhet", "Khulna Sadar", "Khulshi", "Kotwali", "Kotwali Model", "Labanchara", "Lalbagh", "Mahiganj", "Mirpur Model", "Moglabazar", "Mohammadpur", "Motihar", "Motijheel", "Mugda", "New Market", "Paba", "Pahartali", "Pallabi", "Paltan Model", "Panchlaish", "Parshuram", "Patenga", "Rajpara", "Ramna Model", "Rampura", "Rupatali", "Rupnagar", "Sabujbag", "Sadarghat", "Shah Ali", "Shah Poran (R.)", "Shahbag", "Shahjahanpur", "Shahmukhdum", "Sher e Bangla Nagar", "Shyampur", "Sonadanga", "South Surma", "Sutrapur", "Tajhat", "Tejgaon", "Tejgaon Industrial", "Turag", "Uttar Khan", "Uttara East", "Uttara West", "Vatara", "Wari"});
        } else if(v.equals(hospitalDistrict)) {
            DataPicker.chooseAnOption(this, hospitalDistrict, new String[] {"Bagerhat", "Bandarban", "Barguna", "Barisal", "Bhola", "Bogra", "Brahmanbaria", "Chandpur", "Chittagong", "Chuadanga", "Comilla", "Cox's Bazar", "Dhaka", "Dinajpur", "Faridpur", "Feni", "Gaibandha", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jessore", "Jhalokati", "Jhenaidah", "Joypurhat", "Khagrachari", "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Lalmonirhat", "Madaripur", "Magura", "Manikganj", "Maulvibazar", "Meherpur", "Munshiganj", "Mymensingh", "Naogaon", "Narail", "Narayanganj", "Narsingdi", "Natore", "Nawabganj", "Netrokona", "Nilphamari", "Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi", "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajgonj", "Sunamganj", "Sylhet", "Tangail", "Thakurgaon"});
        } else if(v.equals(specializations)) {
            DataPicker.chooseMultipleOptions(this, specializations, new String[] {"Allergists", "Cardiologists", "Dermatologists", "Endocrinologists", "Gastroenterologists", "Gynecologists", "Nephrologists", "Neurologists", "Oncologists", "Ophthalmologists", "Otolaryngologists", "Psychiatrists", "Pulmonologists", "Radiologists", "Rheumatologists", "Surgeons", "Urologists"});
        } else if(v.equals(timeFrom)) {
            DataPicker.chooseTime(getSupportFragmentManager(), timeFrom);
        } else if(v.equals(timeTo)) {
            DataPicker.chooseTime(getSupportFragmentManager(), timeTo);
        } else if(v.equals(offDays)) {
            DataPicker.chooseMultipleOptions(this, offDays, new String[] {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        } else if(v.equals(service)) {
            DataPicker.chooseAnOption(this, service, new String[] {"Yes", "No"});
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            Bitmap bitmap = null;
            try {
                if (android.os.Build.VERSION.SDK_INT >= 29){
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), data.getData()));
                } else{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                }

                photo.setImageBitmap(bitmap);
                uploadPhoto(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
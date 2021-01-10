package bd.org.coronacare.profile.edit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import bd.org.coronacare.R;
import bd.org.coronacare.utils.DataPicker;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialToolbar toolbar;
    private NestedScrollView container;
    private CircularImageView photo;
    private CircularImageView photoBtn;
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
        occupation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("Doctor")) {
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
        });

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
    }

    private void updateProfile() {
        preLoader.setMessage("Updating...");
        preLoader.show();
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
        }
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
            DataPicker.chooseAnOption(this, occupation, new String[] {"Doctor", "Engineer", "Lawyer", "Businessman", "Teacher", "Other"});
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
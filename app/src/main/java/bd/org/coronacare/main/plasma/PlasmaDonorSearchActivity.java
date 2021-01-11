package bd.org.coronacare.main.plasma;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import bd.org.coronacare.R;
import bd.org.coronacare.utils.DataPicker;

public class PlasmaDonorSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialToolbar toolbar;
    private TextInputEditText bgroup;
    private TextInputEditText thana;
    private TextInputEditText district;
    private MaterialButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plasma_donor_search);

        toolbar = findViewById(R.id.toolbar);
        bgroup = findViewById(R.id.pds_bgroup);
        thana = findViewById(R.id.pds_thana);
        district = findViewById(R.id.pds_district);
        searchBtn = findViewById(R.id.pds_btn);

        bgroup.setOnClickListener(this);
        thana.setOnClickListener(this);
        district.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

        toolbar.setTitle("Search Donors By");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void processFieldsData() {
        if (TextUtils.isEmpty(bgroup.getText()) || TextUtils.isEmpty(thana.getText()) || TextUtils.isEmpty(district.getText())) {
            Toast.makeText(PlasmaDonorSearchActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("BGROUP", bgroup.getText().toString());
            intent.putExtra("THANA", thana.getText().toString());
            intent.putExtra("DISTRICT", district.getText().toString());
            setResult(100, intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(bgroup)) {
            DataPicker.chooseAnOption(this, bgroup, new String[] {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        } else if (v.equals(thana)) {
            DataPicker.chooseAnOption(this, thana, new String[] {"Adabor", "Airport", "Akbar Shah", "Aranghata", "Badda", "Bakolia", "Banani", "Bandar", "Bangshal", "Barisal University", "Bayazid Bostami", "Belpukur", "Bhashantek", "Boalia", "Cantonment", "Chackbazar", "Chandgaon", "Chandrima", "Char Monai", "Chawk Bazar", "Dakshin Khan", "Damkura", "Darus-Salam", "Daulatpur", "Demra", "Dhanmondi", "Doublemooring", "EPZ", "Gandaria", "Gulshan", "Hajirhat", "Halishahar", "Haragach", "Harintana", "Hatirjheel", "Hazaribag", "Jalalabad", "Jattrabari", "Kadamtoli", "Kafrul", "Kalabagan", "Kamrangirchar", "Karnahar", "Karnophuli", "Kashipur", "Kasiadanga", "Katakhali", "Kawnia", "Khalishpur", "Khan Jahan Ali", "Khilgaon", "Khilkhet", "Khulna Sadar", "Khulshi", "Kotwali", "Kotwali Model", "Labanchara", "Lalbagh", "Mahiganj", "Mirpur Model", "Moglabazar", "Mohammadpur", "Motihar", "Motijheel", "Mugda", "New Market", "Paba", "Pahartali", "Pallabi", "Paltan Model", "Panchlaish", "Parshuram", "Patenga", "Rajpara", "Ramna Model", "Rampura", "Rupatali", "Rupnagar", "Sabujbag", "Sadarghat", "Shah Ali", "Shah Poran (R.)", "Shahbag", "Shahjahanpur", "Shahmukhdum", "Sher e Bangla Nagar", "Shyampur", "Sonadanga", "South Surma", "Sutrapur", "Tajhat", "Tejgaon", "Tejgaon Industrial", "Turag", "Uttar Khan", "Uttara East", "Uttara West", "Vatara", "Wari"});
        } else if(v.equals(district)) {
            DataPicker.chooseAnOption(this, district, new String[] {"Bagerhat", "Bandarban", "Barguna", "Barisal", "Bhola", "Bogra", "Brahmanbaria", "Chandpur", "Chittagong", "Chuadanga", "Comilla", "Cox's Bazar", "Dhaka", "Dinajpur", "Faridpur", "Feni", "Gaibandha", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jessore", "Jhalokati", "Jhenaidah", "Joypurhat", "Khagrachari", "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur", "Lalmonirhat", "Madaripur", "Magura", "Manikganj", "Maulvibazar", "Meherpur", "Munshiganj", "Mymensingh", "Naogaon", "Narail", "Narayanganj", "Narsingdi", "Natore", "Nawabganj", "Netrokona", "Nilphamari", "Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari", "Rajshahi", "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajgonj", "Sunamganj", "Sylhet", "Tangail", "Thakurgaon"});
        } else if (v.equals(searchBtn)) {
            processFieldsData();
        }
    }
}
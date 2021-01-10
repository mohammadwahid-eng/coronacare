package bd.org.coronacare.emergency;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import bd.org.coronacare.R;
import bd.org.coronacare.models.Emergency;
import bd.org.coronacare.utils.DividerItemDecorator;

public class EmergencyActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView emergencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        toolbar = findViewById(R.id.toolbar);
        emergencyList = findViewById(R.id.emrg_list);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadContent();
    }

    private void loadContent() {
        List<Emergency> emergencies = new ArrayList<>();
        String type = getIntent().getStringExtra("TYPE");

        switch (type) {
            case "DIAGNOSTIC_CENTER":
                toolbar.setTitle(R.string.diagnostic_centers);
                emergencies.add(new Emergency("Aalok Health Care & Hospital Ltd.", "House# 1& 2 Road# 2 Block# B Section # 10, Dhaka 1216", "01919224895", "https://www.aalokhealthcare.com/branch_one.php"));
                emergencies.add(new Emergency("Ahsania Mission Cancer & General Hospital", "Plot-03 , Embankment Drive Way Sector-10 , Uttara Model Town, Dhaka 1230", "028931141", "http://ahsaniacancer.org.bd"));
                emergencies.add(new Emergency("Aichi Hospital Limited", "Plot- 35 & 37, Sector-08, Abdullahpur, Dhaka 1230", "0244891001", "http://aichihospital.com"));
                emergencies.add(new Emergency("AMZ Hospital Limited", "Cha- 80/3, Shadhinota Sarani (Progati Sarani), Uttar Badda, Dhaka-1212", "01847331010", "http://amzhospitalbd.com"));
                emergencies.add(new Emergency("Anowar Khan Modern Medical College Hospital", "House 17,Road 8, R/A, Dhaka 1205", "029670295", "http://akmmc.edu.bd"));
                emergencies.add(new Emergency("Authentic Diagnostic & Consultation Limited", "71/4, Hoseni, Hussaini Dalan Rd, Dhaka 1211", "01536106758", "http://www.authenticdiag.com/"));
                emergencies.add(new Emergency("Bangladesh Institute of Health Sciences General Hospital", "Ashraf Setu Shopping Complex, Dhaka - Mymensingh Hwy, Tongi 1230", "01712238035", "http://bihsh.org.bd"));
                emergencies.add(new Emergency("Biomed Diagnostics", "4, 1/A Mirpur Rd, Dhaka 1215", "01730990492", "-"));
                emergencies.add(new Emergency("Brahmanbaria Medical College Hospital", "Comilla-Sylhet Highway, Ghatura, Brahmanbaria.Sadar, Brahmanbaria dist, 3400", "01733382345", "-"));
                emergencies.add(new Emergency("Chevron Clinical Laboratory (Pte) Ltd.", "12, 12 O.R. Nizam Rd, Chattogram 4203", "01756203720", "https://www.chevronlab.com/"));
                emergencies.add(new Emergency("CRL Diagnostics", "House # 2(Level-7), Road, # 7 Green Rd, Dhaka 1205", "01319632590", "-"));
                emergencies.add(new Emergency("CSBF Health Centre", "Bijoy Shoroni, 109 Bir Uttam Ziaur Rahman Rd, Dhaka 1215", "01730717009", "-"));
                emergencies.add(new Emergency("DMFR Molecular Lab & Diagnostics, Dhaka", "Navana Newbury Place, 4/1/A (7th Floor)", "09606213233", "https://dmfr.mdxdmfr.com/"));
                emergencies.add(new Emergency("DNA Solution Ltd", "Union Heights, 55-2, Beside Square Hospital, West Panthapath, Dhaka 1205", "01751594204", "https://www.dnasolutionbd.com/"));
                emergencies.add(new Emergency("Dr Lal Path Labs Bangladesh Pvt Ltd", "44/9 West Panthapath, Haque Tower, 1st Floor, Dhaka 1205", "01678592071", "-"));
                emergencies.add(new Emergency("Enam Medical College & Hospital", "Enam Medical College Jame Mosque, Savar Union", "01716358146", "http://emcbd.com"));
                emergencies.add(new Emergency("EverCare Hospital Dhaka", "Plot: 81 Block: E, Dhaka 1229", "01729276556", "http://evercarebd.com"));
                emergencies.add(new Emergency("Gazi Covid 19 PCR Lab, Rupganj", "Rupganj", "01777774220", "-"));
                emergencies.add(new Emergency("Gulshan Clinic Limited", "Bir Uttam Rafiqul Islam Ave, Dhaka 1212", "01708800888", "http://www.gulshanclinicbd.org/"));
                emergencies.add(new Emergency("IBN Sina Medical College & Hospital", "39,9/A,Dhanmondi,Dhaka-1209", "029010396", "http://ibnsinatrust.comMedical_College_Hospital.php"));
                emergencies.add(new Emergency("ICDDR B Molecular Diagnostics Laboratory", "nil Shaheed Tajuddin Ahmed Ave, Dhaka 1213 Shaheed Tajuddin Ahmed Ave, Dhaka 1213", "029827038", "http://labservices.icddrb.org/"));
                emergencies.add(new Emergency("Imperial Hospital Limited", "Zakir Hossain Rd, Chattogram 4202", "09612247247", "http://ihlbd.org"));
                emergencies.add(new Emergency("Lab Aid Hospital", "House- 06, Road-04, Dhanmondi, Dhaka 1205", "01713333337", "http://labaidgroup.com"));
                emergencies.add(new Emergency("Praava Health Bangladesh Ltd", "Plot 9, Road 17, Block C, Banani, Dhaka 1213", "01886555200", "https://praavahealth.com/contactus"));
                emergencies.add(new Emergency("Sheikh Fazilatunnesa Mujib Memorial KPJ Specialized Hospital And Nursing College", "C/12, Tetuibari, Kasimpur, Gazipur, Bangladesh. (Near DEPZ).", "01810008080", "https://www.sfmmkpjsh.com/"));
                emergencies.add(new Emergency("Square Hospital", "18/F, Bir Uttam Qazi Nuruzzaman Sarak West, Panthapath, Dhaka 1205", " 09610010616", "http://squarehospital.com"));
                emergencies.add(new Emergency("The DNA Lab Limited", "K 69 Panthapath, Dhaka 1205", "01706319181", "https://www.dnasolutionbd.com/"));
                emergencies.add(new Emergency("United Hospital Limited", "Plot # 15 Rd No 71, Dhaka 1212", "01914001313", "http://uhlbd.com"));
                emergencies.add(new Emergency("Universal Medical College & Hospital", "74G/75, Pea-cock Square, New Airport Road, Dhaka 1215", "01841480000", "http://umchltd.com"));
                emergencies.add(new Emergency("Zainul Hoque Shikdars Womens Medical College & Hospital", "Monica Estate., Dhaka 1209", "028115951", "-"));
                break;
            case "HOTLINES":
                toolbar.setTitle(R.string.hotlines);
                emergencies.add(new Emergency("Access to information Program, ICT division", "Sher-e-Bangla Nagar, Dhaka", "333", "http://a2i.gov.bd"));
                emergencies.add(new Emergency("Bangabandhu Sheikh Mujib Medical University", "Shahbag, Dhaka", "09611677777", "http://www.bsmmu.edu.bd"));
                emergencies.add(new Emergency("Bangladesh Institute of Tropical and Infectious Diseases (BITID)", "Fouzderhat, Sitakunda, Chittagong", "0244075042", "http://bitid.gov.bd"));
                emergencies.add(new Emergency("Department of Health Service", "Mohakhali, Dhaka", "16263", "http://dghs.gov.bd"));
                emergencies.add(new Emergency("Institute of Epidemiology, Disease Control and Research", "Mohakhali, Dhaka", "10655", "http://iedcr.gov.bd"));
                emergencies.add(new Emergency("International Center for Diarrhea Research Bangladesh (ICDDRB)", "GPO Box 128, Dhaka", "01401184551", "http://www.icddrb.org/"));
                emergencies.add(new Emergency("Ministry of Women and Children Affairs", "Abdul Gani Road, Dhaka 1000", "109", "http://mowca.gov.bd"));
                emergencies.add(new Emergency("Mymensingh Medical College", "Char Para, Medical Rd, Mymensingh", "01306497095", "http://www.mmc.gov.bd"));
                emergencies.add(new Emergency("Rajshahi Medical College", "Medical College Road, Rajshahi", "01744595842", "http://rmc.gov.bd"));
                emergencies.add(new Emergency("Rangpur Medical College", "Dhap, Rangpur", "01712177244", "http://www.rcmc.com.bd"));
                break;
            case "AMBULANCE":
                toolbar.setTitle(R.string.ambulance);
                emergencies.add(new Emergency("Al-Amin Ambulance", "20/5 Kornel Roshid Plaza, East Panthopath, Bir Uttam Kazi Nuruzzaman Road, Dhaka-1215", "801720448666", "http://alaminambulance.com"));
                emergencies.add(new Emergency("Alif Ambulance", "76/A, (A1) Ahmed Plaza, West Panthapath, Bir Uttam Kazi Nuruzzaman Rd, Dhaka 1215", "01713205555", "http://alifambulance.com"));
                emergencies.add(new Emergency("Desh Ambulance", "82 East Ahmed Nagor, Dhaka 1216", "01790509607", "http://deshambulance.com"));
                emergencies.add(new Emergency("Mahim Ambulance", "Shahid Shahabuddin Shorok, Dhaka 1207", "01719228739", "http://mahimambulance.com"));
                emergencies.add(new Emergency("URAL EMS", "Mohakhali DOHS, Dhaka", "01969906555", "http://uralems.com"));
                emergencies.add(new Emergency("Obhai Solutions Ltd.", "Kazi Nazrul Islam Ave, Dhaka", "16633", "http://www.obhai.com"));
                break;
        }

        EmergencyAdapter adapter = new EmergencyAdapter(this, emergencies);
        emergencyList.setLayoutManager(new LinearLayoutManager(this));
        emergencyList.addItemDecoration(new DividerItemDecorator(getResources().getDrawable(R.drawable.gr_line_horizontal)));
        emergencyList.setHasFixedSize(true);
        emergencyList.setAdapter(adapter);
    }

    private class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder> {

        private Context mContext;
        private List<Emergency> emergencies;

        public EmergencyAdapter(Context context, List<Emergency> emergencies) {
            this.mContext = context;
            this.emergencies = emergencies;
        }

        @NonNull
        @Override
        public EmergencyAdapter.EmergencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new EmergencyAdapter.EmergencyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_emergency, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EmergencyAdapter.EmergencyViewHolder holder, final int position) {
            holder.name.setText(emergencies.get(position).getName());
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmergencyBottomSheet dialog = new EmergencyBottomSheet(emergencies.get(position));
                    dialog.show(getSupportFragmentManager(), "EMERGENCY");
                }
            });
        }

        @Override
        public int getItemCount() {
            return emergencies.size();
        }

        private class EmergencyViewHolder extends RecyclerView.ViewHolder{
            private MaterialTextView name;
            private LinearLayout item;
            public EmergencyViewHolder(@NonNull View itemView) {
                super(itemView);
                item = itemView.findViewById(R.id.emrg);
                name = itemView.findViewById(R.id.emrg_name);
            }
        }
    }

    public static class EmergencyBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
        private Emergency emergency;
        private MaterialTextView name;
        private RelativeLayout addressHolder;
        private MaterialTextView address;
        private RelativeLayout mobileHolder;
        private MaterialTextView mobile;
        private RelativeLayout websiteHolder;
        private MaterialTextView website;

        public EmergencyBottomSheet(Emergency emergency) {
            this.emergency = emergency;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View dialog = inflater.inflate(R.layout.layout_bs_emergency, container, false);
            name = dialog.findViewById(R.id.emrg_place_name);
            addressHolder = dialog.findViewById(R.id.emrgh_address);
            address = dialog.findViewById(R.id.emrg_address);
            mobileHolder = dialog.findViewById(R.id.emrgh_mobile);
            mobile = dialog.findViewById(R.id.emrg_mobile);
            websiteHolder = dialog.findViewById(R.id.emrgh_website);
            website = dialog.findViewById(R.id.emrg_website);

            name.setText(emergency.getName());
            address.setText(emergency.getAddress());
            mobile.setText(emergency.getMobile());
            website.setText(emergency.getWebsite());

            addressHolder.setOnClickListener(this);
            mobileHolder.setOnClickListener(this);
            websiteHolder.setOnClickListener(this);
            return dialog;
        }

        @Override
        public void onClick(View v) {
            if (v.equals(addressHolder)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com.bd/maps?q=" + emergency.getName())));
            } else if (v.equals(mobileHolder)) {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + emergency.getMobile())));
            } else if (v.equals(websiteHolder)) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(emergency.getWebsite())));
            }
        }
    }
}
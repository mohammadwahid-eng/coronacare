package bd.org.coronacare.models;

public class User {
    private String id;
    private String name;
    private String dob;
    private String gender;
    private String bgroup;
    private String mobile;
    private String thana;
    private String district;
    private String photo;
    private String occupation;
    private boolean donor;
    private boolean online;
    private long lastOnline;
    private Doctor doctor;

    public User() {

    }

    public User(String id, String name, String dob, String gender, String bgroup, String mobile, String thana, String district, String photo, String occupation, boolean donor, boolean online, long lastOnline, Doctor doctor) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bgroup = bgroup;
        this.mobile = mobile;
        this.thana = thana;
        this.district = district;
        this.photo = photo;
        this.occupation = occupation;
        this.donor = donor;
        this.online = online;
        this.lastOnline = lastOnline;
        this.doctor = doctor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBgroup() {
        return bgroup;
    }

    public void setBgroup(String bgroup) {
        this.bgroup = bgroup;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public boolean isDonor() {
        return donor;
    }

    public void setDonor(boolean donor) {
        this.donor = donor;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
package bd.org.coronacare.models;

public class Doctor {
    private String qualification;
    private String specializations;
    private String hname;
    private String hthana;
    private String hdistrict;
    private String hmobile;
    private String experience;
    private String fee;
    private String tfrom;
    private String timeto;
    private String offdays;
    private String rating;
    private boolean service;
    private Feedback feedbacks;

    public Doctor() {

    }

    public Doctor(String qualification, String specializations, String hname, String hthana, String hdistrict, String hmobile, String experience, String fee, String tfrom, String timeto, String offdays, String rating, boolean service, Feedback feedbacks) {
        this.qualification = qualification;
        this.specializations = specializations;
        this.hname = hname;
        this.hthana = hthana;
        this.hdistrict = hdistrict;
        this.hmobile = hmobile;
        this.experience = experience;
        this.fee = fee;
        this.tfrom = tfrom;
        this.timeto = timeto;
        this.offdays = offdays;
        this.rating = rating;
        this.service = service;
        this.feedbacks = feedbacks;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getHthana() {
        return hthana;
    }

    public void setHthana(String hthana) {
        this.hthana = hthana;
    }

    public String getHdistrict() {
        return hdistrict;
    }

    public void setHdistrict(String hdistrict) {
        this.hdistrict = hdistrict;
    }

    public String getHmobile() {
        return hmobile;
    }

    public void setHmobile(String hmobile) {
        this.hmobile = hmobile;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTfrom() {
        return tfrom;
    }

    public void setTfrom(String tfrom) {
        this.tfrom = tfrom;
    }

    public String getTimeto() {
        return timeto;
    }

    public void setTimeto(String timeto) {
        this.timeto = timeto;
    }

    public String getOffdays() {
        return offdays;
    }

    public void setOffdays(String offdays) {
        this.offdays = offdays;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public Feedback getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Feedback feedbacks) {
        this.feedbacks = feedbacks;
    }
}
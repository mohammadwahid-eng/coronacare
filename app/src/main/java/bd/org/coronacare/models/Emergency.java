package bd.org.coronacare.models;

public class Emergency {
    private String name;
    private String address;
    private String mobile;
    private String website;

    public Emergency() {

    }

    public Emergency(String name, String address, String mobile, String website) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

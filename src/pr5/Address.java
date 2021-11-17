package pr5;

public class Address {

    private String locality;
    private String province;
    private String zipCode;
    private String address;
    
    //Constructor
    public Address(String locality, String province, String zipCode, String address) {
        this.locality = locality;
        this.province = province;
        this.zipCode = zipCode;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address{" + "locality=" + locality + ", province=" + province + ", zipCode=" + zipCode + ", address=" + address + '}';
    }
    
    //Setters
    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    //Getters
    public String getLocality() {
        return locality;
    }

    public String getProvince() {
        return province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return address;
    }

}

package inc.synapse.notify;

public class Summary {
    String pckg_name;
    String time_stamp;
    String no_notf;
    // constructor
    public Summary(String pckg_name, String time_stamp, String no_notf){
        this.pckg_name = pckg_name;
        this.time_stamp = time_stamp;
        this.no_notf = no_notf;
    }

    // getting name
    public String get_pckg_name(){
        return this.pckg_name;
    }
    // getting phone number
    public String get_timestamp(){
        return this.time_stamp;
    }
    // getting no_notf
    public String get_no_notf(){
        return this.no_notf;
    }
}

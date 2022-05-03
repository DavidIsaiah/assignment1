package android.assignment.assignment1.network;

import com.google.gson.annotations.Expose;

public class CountrySchema {
    @Expose
    private String name;
    @Expose
    private String region;
    @Expose
    private String code;
    @Expose
    private String capital;

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCode() {
        return code;
    }

    public String getCapital() {
        return capital;
    }
}

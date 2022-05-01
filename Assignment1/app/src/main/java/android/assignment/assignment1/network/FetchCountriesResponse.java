package android.assignment.assignment1.network;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class FetchCountriesResponse {

    @Expose
    private ArrayList<CountrySchema> countries;

    public ArrayList<CountrySchema> getCountries() {
        return countries;
    }
}

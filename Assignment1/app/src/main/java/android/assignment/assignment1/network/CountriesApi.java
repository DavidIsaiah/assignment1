package android.assignment.assignment1.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountriesApi {
    @GET("peymanowmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json")
    Call<FetchCountriesResponse> getCountries();
}

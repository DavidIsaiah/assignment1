package android.assignment.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.assignment.assignment1.network.CountriesApi;
import android.assignment.assignment1.network.CountrySchema;
import android.assignment.assignment1.network.FetchCountriesUseCase;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.assignment.assignment1.Constants.COUNTRIES_URL;

public class MainActivity extends AppCompatActivity implements FetchCountriesUseCase.Listener {


    private static final String TAG = "FetchCountriesUseCase";

    /**
     * Needed to create instance of {@link CountriesApi}
     */
    protected Retrofit mCountriesApiRetrofit;

    /**
     * Used as intermediary between {@link CountriesApi} and this
     * Activity to better manage subscription & unsubscription of this class
     * listening for HTTP Request to complete depending on scenarios :
     *  - device is rotated during HTTP request
     *  - app is closed in background during HTTP request
     */
    protected FetchCountriesUseCase fetchCountriesUseCase;

    protected Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchCountriesUseCase = new FetchCountriesUseCase(getFoodBarcodeDbApi());

        button = findViewById(R.id.button);
        button.setOnClickListener((view) -> {
            fetchData();
        });
    }

    /**
     * Make sure we only initialize RequestQueue once & use that same instance
     * throughout the life of this app
     * @return
     */
    private Retrofit getCountriesApiRetrofit() {
        if(mCountriesApiRetrofit == null) {
            mCountriesApiRetrofit = new Retrofit.Builder()
                    .baseUrl(COUNTRIES_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mCountriesApiRetrofit;
    }

    /**
     * Make {@link CountriesApi} API service available to clients
     * @return Countries API Service
     */
    public CountriesApi getFoodBarcodeDbApi() {
        return getCountriesApiRetrofit().create(CountriesApi.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        fetchCountriesUseCase.registerListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        fetchCountriesUseCase.unregisterListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void fetchData() {
        showToast("Let it begin");
        fetchCountriesUseCase.fetchCountriesAndNotify();
    }

    @Override
    public void onCountriesFetchSuccess(ArrayList<CountrySchema> countries) {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchSuccess 1 countries.size()="+countries.size());
        showToast("Found some countries!");
    }

    @Override
    public void onCountriesFetchFailureNotFound() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureNotFound ");
        showToast("Sorry, I couldn't find the source for country info");
    }

    @Override
    public void onCountriesFetchFailureNetwork() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureNetwork ");
        showToast("Sorry, you seem to have an internet problem.");
    }

    @Override
    public void onCountriesFetchFailureAccessDenied() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureAccessDenied ");
        showToast("Sorry, I was denied access to the country service.");
    }

    @Override
    public void onCountriesFetchFailureServiceUnavailable() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureServiceUnavailable ");
        showToast("Sorry, the country service is unavailable.");
    }

    @Override
    public void onCountriesFetchFailureUnknown() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureUnknown ");
        showToast("Sorry, I couldn't fetch the countries at this time.");
    }
}

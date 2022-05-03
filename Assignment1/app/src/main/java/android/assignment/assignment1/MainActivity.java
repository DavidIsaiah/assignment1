package android.assignment.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.assignment.assignment1.network.CountriesApi;
import android.assignment.assignment1.network.CountrySchema;
import android.assignment.assignment1.network.FetchCountriesUseCase;
import android.assignment.assignment1.ui.CountriesAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

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
    protected RecyclerView recyclerView;
    protected ProgressBar loader;
    private CountriesAdapter mCountriesAdapter;

    /**
     * Use this for checking the internet connection
     */
    ConnectivityManager connectivityManager;
    private boolean isNetworkAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize use-case for fetching the countries
        fetchCountriesUseCase = new FetchCountriesUseCase(getFoodBarcodeDbApi());

        //Initialize the button widget
        button = findViewById(R.id.button);
        button.setOnClickListener((view) -> {
            fetchData();
        });

        loader = findViewById(R.id.loader);

        //Initialize the adapter to list all fetched countries
        mCountriesAdapter = new CountriesAdapter(getLayoutInflater());

        //Initialize the recycler-view to bind our adapter to
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mCountriesAdapter);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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

    /**
     * Only listen to our use-case when the screen is showing
     * AND check the network connection in-case something happened
     * since the last time this app was opened
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        fetchCountriesUseCase.registerListener(this);

        checkNetworkConnection();
    }

    /**
     * Stop listening to our use-case when the screen is hidden
     *
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

        fetchCountriesUseCase.unregisterListener(this);
    }

    /**
     * Check if we're connected to the internet
     */
    private void checkNetworkConnection() {
        Log.d(TAG, "checkNetworkConnection 1");
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        Log.d(TAG, "checkNetworkConnection 2 activeNetwork="+activeNetwork);
        isNetworkAvailable = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.d(TAG, "checkNetworkConnection 3 isNetworkAvailable="+isNetworkAvailable);
    }

    /**
     * Show a toast message to the user
     * @param message
     */
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Send an HTTP request to fetch countries
     * ONLY if we're connected to the internet
     */
    private void fetchData() {
        if(isNetworkAvailable) {
//            showToast("Let it begin");
            loader.setVisibility(View.VISIBLE);
            fetchCountriesUseCase.fetchCountriesAndNotify();
        } else {
            onCountriesFetchFailureNetwork();
        }
    }

    @Override
    public void onCountriesFetchSuccess(List<CountrySchema> countries) {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchSuccess 1 countries.size()="+countries.size());
        showToast("Found some countries!");
        mCountriesAdapter.bindCountries(countries);
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onCountriesFetchFailureNotFound() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureNotFound ");
        showToast("Sorry, I couldn't find the source for country info");
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onCountriesFetchFailureNetwork() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureNetwork ");
        showToast("Sorry, you seem to have an internet problem.");
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onCountriesFetchFailureAccessDenied() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureAccessDenied ");
        showToast("Sorry, I was denied access to the country service.");
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onCountriesFetchFailureServerError() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureServerError ");
        showToast("Sorry, the country service had a problem.");
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onCountriesFetchFailureUnknown() {
        Log.d(TAG, "FetchCountriesUseCase.onCountriesFetchFailureUnknown ");
        showToast("Sorry, something went wrong.");
        loader.setVisibility(View.GONE);
    }
}

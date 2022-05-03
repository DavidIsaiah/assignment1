package android.assignment.assignment1.network;

import android.assignment.assignment1.BaseObservable;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchCountriesUseCase extends BaseObservable<FetchCountriesUseCase.Listener> {

    public interface Listener {
        void onCountriesFetchSuccess(List<CountrySchema> countries);
        void onCountriesFetchFailureNotFound();
        void onCountriesFetchFailureNetwork();
        void onCountriesFetchFailureAccessDenied();
        void onCountriesFetchFailureServerError();
        void onCountriesFetchFailureUnknown();
    }

    private final CountriesApi mCountriesApi;

    private static final String TAG = "FetchCountriesUseCase";

    public FetchCountriesUseCase(CountriesApi countriesApi) {
        this.mCountriesApi = countriesApi;
    }

    public void fetchCountriesAndNotify() {
        try {
            mCountriesApi.getCountries().enqueue(new Callback<List<CountrySchema>>() {
                @Override
                public void onResponse(Call<List<CountrySchema>> call, Response<List<CountrySchema>> response) {
                    Log.d(TAG, "fetchCountriesAndNotify 2.1 response.isSuccessful()=" + response.isSuccessful());
                    if (response.isSuccessful()) {
                        //Success!
                        notifySuccess(response.body());
                    } else {
                        Log.d(TAG, "fetchCountriesAndNotify 2.2 response.code()=" + response.code());
                        //Error response
                        switch (response.code()) {
                            case 404:
                                notifyFailureNotFound();
                                break;
                            case 401:
                                notifyFailureAccessDenied();
                                break;
                            case 500:
                            default:
                                notifyServerError();
                            break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<CountrySchema>> call, Throwable t) {
                    Log.d(TAG, "fetchCountriesAndNotify 2.1 t=" + t);
                    notifyFailureNetwork();
                }
            });
        } catch (Exception e) {
            notifyFailureUnknown();
        }
    }

    private void notifyFailureNetwork() {
        for(Listener listener : getListeners()) {
            listener.onCountriesFetchFailureNetwork();
        }
    }

    private void notifyFailureAccessDenied() {
        for(Listener listener : getListeners()) {
            listener.onCountriesFetchFailureAccessDenied();
        }
    }

    private void notifySuccess(List<CountrySchema> countries) {
        Log.d(TAG, "FetchCountriesUseCase.notifySuccess 1 countries.size()="+countries.size());
        for(Listener listener : getListeners()) {
            listener.onCountriesFetchSuccess(countries);
        }
    }

    private void notifyFailureUnknown() {
        Log.d(TAG, "FetchCountriesUseCase.notifyFailureUnknown ");
        for(Listener listener : getListeners()) {
            listener.onCountriesFetchFailureUnknown();
        }
    }

    private void notifyFailureNotFound() {
        Log.d(TAG, "FetchCountriesUseCase.notifyFailureNotFound ");
        for(Listener listener : getListeners()) {
            listener.onCountriesFetchFailureNotFound();
        }
    }

    private void notifyServerError() {
        Log.d(TAG, "FetchCountriesUseCase.notifyServerError ");
        for(Listener listener : getListeners()) {
            listener.onCountriesFetchFailureServerError();
        }
    }
}

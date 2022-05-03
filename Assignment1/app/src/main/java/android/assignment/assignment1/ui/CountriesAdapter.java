package android.assignment.assignment1.ui;

import android.assignment.assignment1.network.CountrySchema;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.MyViewHolder> {

    private final LayoutInflater mInflater;
    private List<CountrySchema> mCountries = new ArrayList<CountrySchema>();

    public CountriesAdapter(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountriesListItemViewMvc viewMvc = new CountriesListItemViewMvc(mInflater, parent);
        return new MyViewHolder(viewMvc);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mViewMvc.bindCountry(mCountries.get(position));
    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    public void bindCountries(List<CountrySchema> newCountries) {
        mCountries = new ArrayList<>(newCountries);
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CountriesListItemViewMvc mViewMvc;
        public MyViewHolder(CountriesListItemViewMvc viewMvc) {
            super(viewMvc.getRootView());
            mViewMvc = viewMvc;
        }
    }
}

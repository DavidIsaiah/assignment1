package android.assignment.assignment1.ui;

import android.assignment.assignment1.R;
import android.assignment.assignment1.network.CountrySchema;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CountriesListItemViewMvc {
    private final View mRootView;
    private CountrySchema mCountry;
    private final TextView mTvNameAndRegion;
    private final TextView mTvCode;
    private final TextView mTvCapital;

    public CountriesListItemViewMvc(LayoutInflater inflater, ViewGroup parent) {

        mRootView = inflater.inflate(R.layout.view_item, parent, false);
        mTvNameAndRegion = findViewById(R.id.tv_name_and_region);
        mTvCode = findViewById(R.id.tv_code);
        mTvCapital = findViewById(R.id.tv_capital);

    }

    private <T extends View> T findViewById(int id) {
        return mRootView.findViewById(id);
    }

    public View getRootView() { return mRootView; }

    public void bindCountry(CountrySchema countrySchema) {
        mCountry = countrySchema;
        mTvNameAndRegion.setText(mCountry.getName()+", "+mCountry.getRegion());
        mTvCode.setText(mCountry.getCode());
        mTvCapital.setText(mCountry.getCapital());
    }
}

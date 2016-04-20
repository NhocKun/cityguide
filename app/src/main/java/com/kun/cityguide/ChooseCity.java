package com.kun.cityguide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kun.cityguide.db.IPreferenceManager;
import com.kun.cityguide.db.PreferenceManager;
import com.kun.cityguide.network.NetworkFetcher;
import com.kun.cityguide.util.Constant;

import org.json.JSONObject;

public class ChooseCity extends AppCompatActivity {

    private LinearLayout layoutResult;
    private EditText txtCity;
    private Button btnFind, btnGo;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        initView();
        handle();
    }

    void initView() {
        layoutResult = (LinearLayout) findViewById(R.id.layoutResult);
        txtCity = (EditText) findViewById(R.id.txtCity);
        btnFind = (Button) findViewById(R.id.btnFind);
        btnGo = (Button) findViewById(R.id.btnGo);
        tvResult = (TextView) findViewById(R.id.txtResult);
    }

    String query(String s) {
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + s + "&key=" + getString(R.string.google_api_key);
    }

    void handle() {
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = txtCity.getText().toString().trim();
                if (!NetworkFetcher.isNetworkConnected(ChooseCity.this)) {
                    Toast.makeText(ChooseCity.this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                    return;
                }
                new AsyncTask<Void, Void, JSONObject>() {

                    @Override
                    protected JSONObject doInBackground(Void... voids) {
                        return NetworkFetcher.executeRequest(query(text), false);
                    }

                    @Override
                    protected void onPostExecute(JSONObject result) {
                        if (result != null) {

                        }
                    }
                }.execute();

            }
        });

    }

    void save(CityInfo info) {
        IPreferenceManager manager = new PreferenceManager(ChooseCity.this);
        manager.SetValue(Constant.LATITUDE, info.getLatitude());
        manager.SetValue(Constant.LONGITUDE, info.getLongLatitude());
        manager.SetValue(Constant.TOWN, info.getTown());
        manager.SetValue(Constant.COUNTRY, info.getContry());
    }

    private static class CityInfo implements Parcelable {
        private String Latitude, LongLatitude, Town;

        public CityInfo() {
        }

        protected CityInfo(Parcel in) {
            Latitude = in.readString();
            LongLatitude = in.readString();
            Town = in.readString();
            Contry = in.readString();
        }

        public static final Creator<CityInfo> CREATOR = new Creator<CityInfo>() {
            @Override
            public CityInfo createFromParcel(Parcel in) {
                return new CityInfo(in);
            }

            @Override
            public CityInfo[] newArray(int size) {
                return new CityInfo[size];
            }
        };

        public String getContry() {
            return Contry;
        }

        public void setContry(String contry) {
            Contry = contry;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String latitude) {
            Latitude = latitude;
        }

        public String getLongLatitude() {
            return LongLatitude;
        }

        public void setLongLatitude(String longLatitude) {
            LongLatitude = longLatitude;
        }

        public String getTown() {
            return Town;
        }

        public void setTown(String town) {
            Town = town;
        }

        private String Contry;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(Latitude);
            parcel.writeString(LongLatitude);
            parcel.writeString(Town);
            parcel.writeString(Contry);
        }
    }
}

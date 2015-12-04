package com.example.hacknotts.hacknotts;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAddressFieldListener();
    }

    private void createAddressFieldListener() {
        EditText addressField = (EditText) findViewById(R.id.editText);
        addressField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String address = textView.getText().toString();
                    searchForAddress(address);
                    closeKeyboard();
                    return true;
                } else {
                    return false;
                }
            }

            private void closeKeyboard() {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private void searchForAddress(String address) {
        if (addressExists(address)) {
            Coordinate coordinate = getLatLongFromAddress(address);
            Log.d("Latitude", "" + coordinate.getLat());
            Log.d("Longitude", "" + coordinate.getLon());
            ImageDatabase db = new ImageDatabase();
        } else {
            Log.d("error", "address does not exist");
        }
    }

    private boolean addressExists(String address) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            return addresses.size() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Coordinate getLatLongFromAddress(String address) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                Address firstAddress = addresses.get(0);
                return new Coordinate(firstAddress.getLatitude(), firstAddress.getLongitude());
            } else {
                throw new Exception("Address does not exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

package com.siroe.plugins.lcprint;

import android.util.Log;

import org.json.JSONObject;
import com.getcapacitor.JSObject;
public class Lcprint {

    public Boolean start() {
        return true;
    }
    public Boolean printText(Integer size, Boolean bold, Boolean underline, String value) {
        return true;
    }
    public Boolean feed(Integer lines) {
        return true;
    }
    public Boolean printBarcode128(String code) {
        return true;
    }
    public Boolean printImageUrl(String url) {
        return true;
    }

    public Boolean voucher(JSObject receipt) {
        return true;
    }

}

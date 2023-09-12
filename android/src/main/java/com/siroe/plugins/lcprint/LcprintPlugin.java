package com.siroe.plugins.lcprint;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.example.lc_print_sdk.PrintUtil;
import com.example.lc_print_sdk.PrintConfig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

@CapacitorPlugin(name = "Lcprint")
public class LcprintPlugin extends Plugin {

    private Lcprint implementation = new Lcprint();
    private PrintUtil printUtil = null;

    @PluginMethod
    public void start(PluginCall call) {
        printUtil=PrintUtil.getInstance (this.getActivity().getApplicationContext());
        //printUtil.setPrintEventListener (this.onPrintCallback);// Set listening
        printUtil.setUnwindPaperLen (3);
        printUtil.printEnableMark (true);
        printUtil.printConcentration (25);
        int getsupportprint=printUtil.getSupportPrint ();

        JSObject ret = new JSObject();
        ret.put("success", implementation.start());
        call.resolve(ret);
    }

    @PluginMethod
    public void printText(PluginCall call) {
        String value = call.getString("value");
        Boolean bold = call.getBoolean("bold");
        Boolean underline = call.getBoolean("underline");
        Integer size = call.getInt("size");

        printUtil.printText(0, size, bold, underline, value);

        JSObject ret = new JSObject();
        ret.put("success", implementation.printText(size, bold, underline, value));
        call.resolve(ret);
    }

    @PluginMethod
    public void feed(PluginCall call) {
        Integer lines = call.getInt("lines");
        printUtil.printLine(lines);

        JSObject ret = new JSObject();
        ret.put("success", implementation.feed(lines));
        call.resolve(ret);
    }

    @PluginMethod
    public void printBarcode128(PluginCall call) {
        String code = call.getString("code");
        printUtil.printBarcode(10, code, PrintConfig.BarCodeType.TOP_TYPE_CODE128);

        JSObject ret = new JSObject();
        ret.put("success", implementation.printBarcode128(code));
        call.resolve(ret);
    }

    @PluginMethod
    public void printImageUrl(PluginCall call) {

        String urlPath = call.getString("url");

        try {
            URL url = new URL(urlPath);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            printUtil.printBarcode(10, urlPath, PrintConfig.BarCodeType.TOP_TYPE_CODE128);
        } catch(Exception e) {
            System.out.println(e);
        }

        JSObject ret = new JSObject();
        ret.put("success", implementation.printImageUrl(urlPath));
        call.resolve(ret);
    }

    @PluginMethod
    public void voucher(PluginCall call) {

        JSObject receipt = call.getObject("receipt");

        printUtil=PrintUtil.getInstance (this.getActivity().getApplicationContext());
        printUtil.setPrintEventListener (new PrintUtil.PrinterBinderListener() {
            @Override
            public void onPrintCallback(int i) {
                Log.d ("TAG", "onPrintCallback: state"+i);
            }
            @Override
            public void onVersion(String s) {

            }
        });

        printUtil.resetPrint();
        printUtil.setUnwindPaperLen (printUtil.getUnwindPaperLen());
        printUtil.printEnableMark (true);
        printUtil.printConcentration (25);
        int getsupportprint=printUtil.getSupportPrint ();

        try {
            printUtil.printLine(4);
            if (receipt.has("header")) {
                JSONObject header = receipt.getJSONObject("header");
                if (header.has("docname")) {
                    printUtil.printText(4, 18, true, true, header.getString("docname"));
                    printUtil.printLine(2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printUtil.start();

        /*
        try {
            URL url = new URL(urlPath);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            printUtil.printBarcode(10, urlPath, PrintConfig.BarCodeType.TOP_TYPE_CODE128);
        } catch(Exception e) {
            System.out.println(e);
        }
        */

        JSObject ret = new JSObject();
        ret.put("success", implementation.voucher(receipt));
        call.resolve(ret);
    }


}

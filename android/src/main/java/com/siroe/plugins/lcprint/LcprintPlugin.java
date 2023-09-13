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
import org.json.JSONArray;

import java.net.URL;

@CapacitorPlugin(name = "Lcprint")
public class LcprintPlugin extends Plugin {

    private Lcprint implementation = new Lcprint();
    private PrintUtil printUtil = null;

    @PluginMethod
    public void start(PluginCall call) {
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

        printUtil.setUnwindPaperLen ( printUtil.getUnwindPaperLen());
        printUtil.printEnableMark (true);
        printUtil.printConcentration (25);
        int getsupportprint=printUtil.getSupportPrint ();

        JSObject ret = new JSObject();
        ret.put("success", implementation.start());
        call.resolve(ret);
    }
    @PluginMethod
    public void print(PluginCall call) {

        printUtil.start();

        JSObject ret = new JSObject();
        ret.put("success", implementation.print());
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
        printUtil.setUnwindPaperLen (60);
        printUtil.printEnableMark (false);
        printUtil.printConcentration (25);
        int getsupportprint=printUtil.getSupportPrint ();

        try {
            printUtil.printLine(1);
            if (receipt.has("header") && receipt.has("totals") && receipt.has("from") && receipt.has("to") && receipt.has("payment")
            && receipt.has("barcode") && receipt.has("signed") && receipt.has("footer") && receipt.has("items")) {

                JSONObject header = receipt.getJSONObject("header");
                JSONObject totals = receipt.getJSONObject("totals");
                JSONObject from = receipt.getJSONObject("from");
                JSONObject to = receipt.getJSONObject("to");
                JSONObject payment = receipt.getJSONObject("payment");
                JSONObject signed = receipt.getJSONObject("signed");
                String footer = receipt.getString("footer");
                String barcode = receipt.getString("barcode");
                JSONArray items = receipt.getJSONArray("items");

                Integer headerDoctype = 0, totalsSubtotal = 0, totalsDiscounts = 0, totalsNeto = 0, totalsTax = 0, totalsTotal = 0,
                        paymentCash = 0, paymentCard = 0, paymentTransfers = 0, paymentCheck = 0;

                String headerDocname = "", fromCode = "", fromName = "", fromAddress = "", fromCounty = "",
                fromPhone = "", fromActivity = "", toCode = "", toName = "", toAddress = "", toCounty = "",
                toPhone = "", toActivity = "", signedImage = "", signedText1 = "", signedText2 = "", headerFolio = "";

                // header
                if (header.has("docname")) { headerDocname = header.getString("docname"); }
                if (header.has("doctype")) { headerDoctype = header.getInt("doctype"); }
                if (header.has("folio")) { headerFolio = header.getString("folio"); }

                // totals
                if (totals.has("subtotal")) { totalsSubtotal = totals.getInt("subtotal"); }
                if (totals.has("discounts")) { totalsDiscounts = totals.getInt("discounts"); }
                if (totals.has("net")) { totalsNeto = totals.getInt("net"); }
                if (totals.has("tax")) { totalsTax = totals.getInt("tax"); }
                if (totals.has("total")) { totalsTotal = totals.getInt("total"); }

                //from
                if (from.has("code")) { fromCode = from.getString("code"); }
                if (from.has("name")) { fromName = from.getString("name"); }
                if (from.has("county")) { fromCounty = from.getString("county"); }
                if (from.has("phone")) { fromPhone = from.getString("phone"); }
                if (from.has("activity")) { fromActivity = from.getString("activity"); }
                if (from.has("address")) { fromAddress = from.getString("address"); }

                //to
                if (to.has("code")) { toCode = to.getString("code"); }
                if (to.has("name")) { toName = to.getString("name"); }
                if (to.has("county")) { toCounty = to.getString("county"); }
                if (to.has("phone")) { toPhone = to.getString("phone"); }
                if (to.has("activity")) { toActivity = to.getString("activity"); }
                if (to.has("address")) { toAddress = to.getString("address"); }

                //payment
                if (payment.has("card")) { paymentCard = payment.getInt("card"); }
                if (payment.has("transfers")) { paymentTransfers = payment.getInt("transfers"); }
                if (payment.has("check")) { paymentCheck = payment.getInt("check"); }
                if (payment.has("cash")) { paymentCash = payment.getInt("cash"); }

                //signed
                if (signed.has("image")) { signedImage = signed.getString("image"); }
                if (signed.has("text1")) { signedText1 = signed.getString("text1"); }
                if (signed.has("text2")) { signedText2 = signed.getString("text2"); }

                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, fromCode);
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, fromName);
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, fromAddress);
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, fromCounty);
                printUtil.printLine(4);

                printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_XLARGE, true, false, headerDocname);
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_XLARGE, true, false, headerFolio);
                printUtil.printLine(2);

                if (headerDoctype == 4) {

                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "********* SEÑORES *********");
                    printUtil.printLine(1);
                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "RUT: " + toCode);
                    printUtil.printLine(1);
                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "R.SOCIAL: " + toName);
                    printUtil.printLine(1);
                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "DIREC.: " + toAddress);
                    printUtil.printLine(1);
                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "COMUNA: " + toCounty);
                    printUtil.printLine(1);
                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "GIRO: " + toActivity);
                    printUtil.printLine(2);
                }

                printUtil.printLine(2);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "******** ARTICULOS *******");
                printUtil.printLine(2);

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String pxq = item.getInt("quantity") + "x" + item.getInt("price");
                    for (int j = pxq.length(); j < 18; j++) {
                        pxq += " ";
                    }

                    String code = item.getString("code");
                    for (int j = code.length(); j < 16; j++) {
                        code += " ";
                    }

                    Integer total = item.getInt("total");

                    String cxt = item.getString("description");

                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XSMALL, false, false, pxq + code + "$ " + miles(total) );
                    printUtil.printLine(1);
                    printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XSMALL, false, false, cxt);
                    printUtil.printLine(1);
                }

                printUtil.printLine(2);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "******** TOTALES *******");
                printUtil.printLine(1);

                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "Subtotal:    $" + this.miles(totalsSubtotal));
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "Descuentos:  $" + this.miles(totalsDiscounts));
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "Neto:        $" + this.miles(totalsNeto));
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "Imptos:      $" + this.miles(totalsTax));
                printUtil.printLine(1);
                printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XMIDDLE, false, false, "Total:       $" + this.miles(totalsTotal));
                printUtil.printLine(3);
                printUtil.printBarcode (PrintConfig.Align.ALIGN_CENTER, 100, barcode, PrintConfig.BarCodeType.TOP_TYPE_CODE128, PrintConfig.HRIPosition.POSITION_BELOW);
                printUtil.printLine(5);
                //timbre
                //signedImage = "https://api.pointsale.cl/timbre/2023-09/9240.png";
                if (!signedImage.equalsIgnoreCase("")) {
                    try {
                        URL url = new URL(signedImage);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        printUtil.printBitmap(PrintConfig.Align.ALIGN_CENTER, image);
                        printUtil.printLine(1);
                        printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_XSMALL, false, false, signedText1);
                        printUtil.printLine(1);
                        printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_XSMALL, false, false, signedText2);
                        printUtil.printLine(1);
                    } catch(Exception e) {
                        System.out.println(e);
                    }
                    printUtil.printLine(4);
                }

                printUtil.printText (PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, footer);
                printUtil.printLine(6);
                printUtil.printLine(6);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printUtil.start();

        /*

        */

        JSObject ret = new JSObject();
        ret.put("success", implementation.voucher(receipt));
        call.resolve(ret);
    }

    public String center (int width, String s) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
    public String miles(int number) {
        if (number < 0) {
            return "-" + miles(-number);
        } else if (number > 999) {
            return miles(number / 1000) +
                    String.format(".%03d", number % 1000); // note the space in the format string.
        } else {
            return String.format("%d", number);
        }
    }


}

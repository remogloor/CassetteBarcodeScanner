package example.ivorycirrus.mlkit.barcode;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;

/** Draw camera overlays of detected information */
public class OverlayView extends View {

    private Rect mRect;
    private HashSet<String> barcodes = new HashSet<String>();
    private HashMap<Integer, String> data = new HashMap<Integer, String>();
    private String mText;
    private String cassetteType = "";
    private String ContainerCode = "";
    private String Lot = "";
    private String ExpirationDate = "";
    private String ManufacutrerDate = "";
    private String SerialNumber = "";

    public OverlayView(Context c) {
        super(c);
    }

    public void setOverlay(Rect rect, String text)
    {
        if (text == null || text.length() != 8)
        {
            return;
        }
        int sum = 0;
        for (int i = 0; i < 7; i++){
            char c = text.charAt(i);
            sum += Decode(c);
        }

        if (sum % 43 != Decode(text.charAt(7))){
            return;
        }

        int index = Integer.parseInt(text.substring(0, 1));
        this.data.put(index, text);

        switch (index){
            case 0:
                DecodeCassetteType(text);
                if (this.data.containsKey(1))
                {
                    DecodeContainerCode(text, this.data.get(1));
                }
                break;
            case 1:
                if (this.data.containsKey(0)){
                    DecodeContainerCode(this.data.get(0), text);
                }
                if (this.data.containsKey(2) && this.data.containsKey(4))
                {
                    DecodeLot(text, this.data.get(2), this.data.get(4));
                }
                break;
            case 2:
                if (this.data.containsKey(1) && this.data.containsKey(4))
                {
                    DecodeLot(this.data.get(1), text, this.data.get(4));
                }
                DecodeExpirationDate(text);
                if (this.data.containsKey(3))
                {
                    DecodeManufacturerDate(text, this.data.get(3));
                }
                break;
            case 3:
                if (this.data.containsKey(2))
                {
                    DecodeManufacturerDate(this.data.get(2), text);
                }

                DecodeSerialNumber(text);
                break;
            case 4:
                if (this.data.containsKey(1) && this.data.containsKey(2))
                {
                    DecodeLot(this.data.get(1), this.data.get(2), text);
                }
                break;
        }

        mRect = rect;
        mText = text;
        this.barcodes.add(text);
    }

    private void DecodeSerialNumber(String text) {
        this.SerialNumber = text.substring(2,7);
    }

    private void DecodeManufacturerDate(String text, String s) {
        this.ManufacutrerDate = text.substring(6,7) + s.substring(1, 2);
    }

    private void DecodeExpirationDate(String text) {
        this.ExpirationDate = text.substring(4,6) + "/" + text.substring(2,4);
    }

    private void DecodeLot(String text, String text2, String text3) {
        this.Lot = text.substring(2,7) + text2.substring(1, 2) + text3.substring(2, 4);
    }

    private void DecodeContainerCode(String text, String text2) {
        this.ContainerCode = "07" + text.substring(3,7) + text2.substring(1, 2);
    }

    private void DecodeCassetteType(String text) {
        switch (text.substring(1,3))
        {
            case "03": this.cassetteType = "URC1"; break;
            case "04": this.cassetteType = "Coag"; break;
            default: this.cassetteType = "Unknown"; break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mRect != null) {
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(4.5f);
            //canvas.drawRect(mRect, p);

            if(mText != null) {
                p.setTextSize(30);
                int i = 0;
                canvas.drawText("Container Code: " + this.ContainerCode, 100, 100 + i++*30, p);
                canvas.drawText("Lot: " + this.Lot, 100, 100 + i++*30, p);
                canvas.drawText("Serial Number: " + this.SerialNumber, 100, 100 + i++*30, p);
                canvas.drawText("Expiration Date: " + this.ExpirationDate, 100, 100 + i++*30, p);
                canvas.drawText("Cassette Type: " + this.cassetteType, 100, 100 + i++*30, p);
                canvas.drawText("Manufacturer Date: " + this.ManufacutrerDate, 100, 100 + i++*30, p);

                /*
                for (String text: this.barcodes)
                {
                    i++;
                    canvas.drawText(text, 100, 100 + i*30, p);
                }*/
            }
        }
    }

    private int Decode(char c){
        switch (c){
            case '0': return 0;
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            case 'A': return 10;
            case 'B': return 11;
            case 'C': return 12;
            case 'D': return 13;
            case 'E': return 14;
            case 'F': return 15;
            case 'G': return 16;
            case 'H': return 17;
            case 'I': return 18;
            case 'J': return 19;
            case 'K': return 20;
            case 'L': return 21;
            case 'M': return 22;
            case 'N': return 23;
            case 'O': return 24;
            case 'P': return 25;
            case 'Q': return 26;
            case 'R': return 27;
            case 'S': return 28;
            case 'T': return 29;
            case 'U': return 30;
            case 'V': return 31;
            case 'W': return 32;
            case 'X': return 33;
            case 'Y': return 34;
            case 'Z': return 35;
            case '-': return 36;
            case '.': return 37;
            case ' ': return 38;
            case '$': return 39;
            case '/': return 40;
            case '+': return 41;
            case '%': return 42;
        }

        return 0;
    }

    public void Clear() {
        this.ManufacutrerDate = "";
        this.ContainerCode = "";
        this.cassetteType = "";
        this.ExpirationDate = "";
        this.SerialNumber = "";
        this.Lot = "";
        this.data.clear();
        this.invalidate();
    }
}

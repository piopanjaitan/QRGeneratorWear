package com.piopanjaitan.qrgeneratorwear.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.piopanjaitan.qrgeneratorwear.R;

public class MainActivity extends AppCompatActivity {

    EditText etInput, et_input_ascii;
    Button btGenerate;
    ImageView ivOutput;
    Spinner spAscii;
    TextView tvAscii, tvVersionApps, tv_powerby, tv_qrcode_ascii_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.support.wearable.R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etInput = findViewById(R.id.et_input);
        et_input_ascii = findViewById(R.id.et_input_ascii);
        btGenerate = findViewById(R.id.bt_generate);
        spAscii = findViewById(R.id.sp_ascii);
        tvAscii = findViewById(R.id.tv_ascii);
        ivOutput = findViewById(R.id.iv_output);
        tvVersionApps = findViewById(R.id.tv_versionapps);
        tv_powerby = findViewById(R.id.tv_powerby);
        tv_qrcode_ascii_view = findViewById(R.id.tv_qrcode_ascii_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_powerby.setText(Html.fromHtml(getString(R.string.link_github), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv_powerby.setText(Html.fromHtml(getString(R.string.link_github)));
        }
        Linkify.addLinks(tv_powerby, Linkify.ALL);
        tv_powerby.setMovementMethod(LinkMovementMethod.getInstance());

        String _version = "";
        try {
            _version = "Versi : " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvVersionApps.setText(_version);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ListAsciiCode, android.R.layout.simple_spinner_item);

        spAscii.setAdapter(adapter);

        spAscii.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object item = parent.getItemAtPosition(position);
                String d = item.toString();
                tvAscii.setText(d);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get input value from edit text
                if (etInput.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "TEXT EMPTY", Toast.LENGTH_SHORT).show();
                } else if (et_input_ascii.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "ASCII EMPTY", Toast.LENGTH_SHORT).show();
                } else if (!etInput.getText().toString().isEmpty() && !et_input_ascii.toString().isEmpty()) {


                    int sTextToAscii = Integer.parseInt(et_input_ascii.getText().toString());
                    String sTextToAsciiChange = Character.toString((char) sTextToAscii);
//                String sText = etInput.getText().toString().trim() + tvAscii.getText().toString();
                    String sText = etInput.getText().toString().trim() + sTextToAsciiChange;
                    tv_qrcode_ascii_view.setText(sText);
//                String sText2 = etInput.getText().toString();

               /* String outputText = String.valueOf(toAscii(sText2));
                etInput.setText(outputText);
                Log.v("cek","ASCII = " + tvAscii);*/
                    //Initialize multi format write
                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        //Initialize bit matrix
                        BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE, 350, 350);
                        //Initialize barcode encoder
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        //Initialize bitmap
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        //Set bitmap on imageview
                        ivOutput.setImageBitmap(bitmap);
                        //Initialize input manager
                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //Hide soft keyboard
                        manager.hideSoftInputFromWindow(etInput.getApplicationWindowToken(), 0);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

//    public static String isAsciiPrintable(char ch) {
//        String c = (char)(i + 48);
//              return ch >=192 && ch < 256;
//           }

    public static long toAscii(String s){
        StringBuilder sb = new StringBuilder();
        String ascString = null;
        long asciiInt;
        for (int i = 0; i < s.length(); i++){
            sb.append((int)s.charAt(i));
            char c = s.charAt(i);
        }
        ascString = sb.toString();
        asciiInt = Long.parseLong(ascString);
        return asciiInt;
    }

    /*public static String versionApps(@Nullable Context ctx) {
        String tv = "";
        try {
            tv = "piopanjaitan - v " + ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            tv = "piopanjaitan - v ";
            //e.printStackTrace();
        }

        return tv;
    }*/
}
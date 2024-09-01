package com.example.recipegenius.Customer;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipegenius.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PurchaseRecipeActivity extends AppCompatActivity {

    private String recipeId;
    private static final String PAYMENT_RETURN_URI = "http://localhost:63342/RecipeGenius/app/src/main/assets/payment.html?_ijt=m0ija3q0m67pkrge68d453755&_ij_reload=RELOAD_ON_SAVE";
    private static final String PAYMENT_URL = "https://sandbox.jazzcash.com.pk/CustomerPortal/transactionmanagement/merchantform/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_recipe);

        // Retrieve the recipe ID from the intent


        // Set up WebView for JazzCash payment gateway
        WebView webView = findViewById(R.id.jazzcash_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true); // Enables DOM storage

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Error: " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Handle page load completion if needed
            }
        });

        String postData = generatePostData();
        Log.d("PurchaseRecipeActivity", "Post Data: " + postData); // Log the post data
        webView.postUrl(PAYMENT_URL, postData.getBytes());


    }
    private String generatePostData() {
        StringBuilder postData = new StringBuilder();
        try {
            // Replace these with actual values
            String price = "1000"; // Amount to be paid (in Paisa, 1 PKR = 100 Paisa)
            String roomId = "12345"; // Example product ID
            String pp_MerchantID = "MC119128";
            String pp_Password = "9vy504041u";
            String pp_ReturnURL = PAYMENT_RETURN_URI;

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddkkmmss");
            String dateString = dateFormat.format(date);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.HOUR, 1); // Add one hour to expiry time
            Date currentDateHourPlusOne = c.getTime();
            String expireDateString = dateFormat.format(currentDateHourPlusOne);

            String transactionIdString = "T" + dateString;

            // Generating pp_SecureHash (this is a simplified example; replace with your logic)
            String pp_SecureHash = ""; // You need to generate this using your secret key and the required fields

            appendEncodedPostData(postData, "pp_Version", "1.1");
            appendEncodedPostData(postData, "pp_TxnType", "");
            appendEncodedPostData(postData, "pp_Language", "EN");
            appendEncodedPostData(postData, "pp_MerchantID", pp_MerchantID);
            appendEncodedPostData(postData, "pp_SubMerchantID", "");
            appendEncodedPostData(postData, "pp_Password", pp_Password);
            appendEncodedPostData(postData, "pp_BankID", "TBANK");
            appendEncodedPostData(postData, "pp_ProductID", roomId);
            appendEncodedPostData(postData, "pp_TxnRefNo", transactionIdString);
            appendEncodedPostData(postData, "pp_Amount", price);
            appendEncodedPostData(postData, "pp_TxnCurrency", "PKR");
            appendEncodedPostData(postData, "pp_TxnDateTime", dateString);
            appendEncodedPostData(postData, "pp_BillReference", "bilRef");
            appendEncodedPostData(postData, "pp_Description", "Description of transaction");
            appendEncodedPostData(postData, "pp_TxnExpiryDateTime", expireDateString);
            appendEncodedPostData(postData, "pp_ReturnURL", pp_ReturnURL);
            appendEncodedPostData(postData, "pp_SecureHash", pp_SecureHash);
            appendEncodedPostData(postData, "pp_mpf_1", "1");
            appendEncodedPostData(postData, "pp_mpf_2", "2");
            appendEncodedPostData(postData, "pp_mpf_3", "3");
            appendEncodedPostData(postData, "pp_mpf_4", "4");
            appendEncodedPostData(postData, "pp_mpf_5", "5");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return postData.toString();
    }

    private void appendEncodedPostData(StringBuilder postData, String key, String value) throws UnsupportedEncodingException {
        if (postData.length() > 0) {
            postData.append("&");
        }
        postData.append(URLEncoder.encode(key, "UTF-8"))
                .append("=")
                .append(URLEncoder.encode(value, "UTF-8"));
    }


}

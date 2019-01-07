package cms.co.in.kat.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cms.kat.cws.R;

import cms.co.in.kat.utils.Constant;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {

    private TextView phone,email,fax;
    private SharedPreferences permissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        initLayout();
        initListner();

        permissionStatus = getSharedPreferences(Constant.MY_PREF_PHONE, MODE_PRIVATE);
    }

    private void initListner() {
        email.setOnClickListener(this);
        phone.setOnClickListener(this);
        fax.setOnClickListener(this);
    }

    private void initLayout() {
        phone=(TextView)findViewById(R.id.phone);
        email=(TextView)findViewById(R.id.email);
        fax=(TextView)findViewById(R.id.fax);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:

                callToNumber(getResources().getString(R.string.phone));

                break;
            case R.id.email:
                sendmail(getResources().getString(R.string.email));
                break;
            case R.id.fax:

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(intent);
                break;
        }
    }

    private void callToNumber(String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(ContactUs.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            checkCallPermision();
            return;
        }
        startActivity(callIntent);
    }

    private void checkCallPermision() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ContactUs.this, Manifest.permission.CALL_PHONE)) {

            ActivityCompat.requestPermissions(ContactUs.this, new String[]{Manifest.permission.CALL_PHONE},
                    Constant.PERMISSIONS_REQUEST_CALL_PHNUMBER);

        } else if (permissionStatus.getBoolean(Constant.MY_PREF_PHONE, false)) {
            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(ContactUs.this);
            builder.setTitle("Need CALL Permission");
            builder.setMessage("This app needs CALL PHONE permission");
            builder.setCancelable(false);
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(ContactUs.this, "Until you grant the permission, CALL not Allowed", Toast.LENGTH_SHORT).show();

                }
            });
            builder.show();
        } else {
            //just request the permission
            ActivityCompat.requestPermissions(ContactUs.this, new String[]{Manifest.permission.CALL_PHONE},
                    Constant.PERMISSIONS_REQUEST_CALL_PHNUMBER);
        }
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Constant.MY_PREF_PHONE, true);
        editor.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == Constant.PERMISSIONS_REQUEST_CALL_PHNUMBER) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You need to grant permission to make a call", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendmail(String mail) {
        if (mail != null && mail.trim().length() > 0) {
            String[] TO = {mail};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            startActivity(Intent.createChooser(emailIntent, "KAT CWS"));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 11) {
            if (resultCode == 1) {
            }
        }
    }
}

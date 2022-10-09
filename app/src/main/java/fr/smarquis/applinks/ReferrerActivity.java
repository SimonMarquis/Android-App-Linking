package fr.smarquis.applinks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import fr.smarquis.applinks.databinding.ActivityReferrerBinding;


public class ReferrerActivity extends AppCompatActivity {

    private ActivityReferrerBinding binding;

    static void start(@NonNull Context context) {
        context.startActivity(new Intent(context, ReferrerActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Referrer.isAvailable(this)) {
            finish();
            return;
        }
        Referrer.setDisplayed(this);
        binding = ActivityReferrerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateReferrer();
    }

    private void updateReferrer() {
        boolean isAvailable = Referrer.isAvailable(this);
        String firstLaunch = Referrer.getFirstLaunch(this);
        String referrerDate = Referrer.getDate(this);
        String referrerDataRaw = Referrer.getRawData(this);
        String referrerDataDecoded = Referrer.getDecodedData(this);
        String installerPackageName = getPackageManager().getInstallerPackageName(getPackageName());
        Printer printer = new Printer(this)
                .appendKeyValue(getString(R.string.referrer_installer_package), installerPackageName)
                .appendKeyValue(getString(R.string.referrer_first_launch), firstLaunch)
                .appendKeyValue(getString(R.string.referrer_detection), referrerDate);
        if (isAvailable) {
            printer.appendKeyValue(getString(R.string.referrer_raw), referrerDataRaw);
            if (referrerDataDecoded != null) {
                printer.appendKeyValue(getString(R.string.referrer_decoded), referrerDataDecoded);
            }
        }
        binding.referrerContent.setText(printer.stripNewLines().build());
        binding.referrerContent.setMovementMethod(new LinkMovementMethod());
    }

}

package fr.smarquis.applinks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferrerActivity extends AppCompatActivity {

    @BindView(R.id.referrer_content)
    TextView content;

    @BindString(R.string.referrer_installer_package)
    String installerPackageNameString;

    @BindString(R.string.referrer_first_launch)
    String firstLaunchString;

    @BindString(R.string.referrer_detection)
    String detectionString;

    @BindString(R.string.referrer_raw)
    String referrerRawString;

    @BindString(R.string.referrer_decoded)
    String referrerDecodedString;

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
        setContentView(R.layout.activity_referrer);
        ButterKnife.bind(this);

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
                .appendKeyValue(installerPackageNameString, installerPackageName)
                .appendKeyValue(firstLaunchString, firstLaunch)
                .appendKeyValue(detectionString, referrerDate);
        if (isAvailable) {
            printer.appendKeyValue(referrerRawString, referrerDataRaw);
            if (referrerDataDecoded != null) {
                printer.appendKeyValue(referrerDecodedString, referrerDataDecoded);
            }
        }
        content.setText(printer.stripNewLines().build());
        content.setMovementMethod(new LinkMovementMethod());
    }

}

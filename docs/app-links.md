# App Links

!!! info "Documentation"
    https://developer.android.com/training/app-links/verify-android-applinks

[`https://smarquis.fr/action?key=value#data`](https://smarquis.fr/action?key=value#data){ .md-button }

A JSON verification file needs to be available at https://smarquis.fr/.well-known/assetlinks.json containing the application's package name and keystore fingerprint

```json title="/.well-known/assetlinks.json"
[
    {
        "relation": [
            "delegate_permission/common.handle_all_urls"
        ],
        "target": {
            "namespace": "android_app",
            "package_name": "fr.smarquis.applinks",
            "sha256_cert_fingerprints": [
                "6B:41:26:A6:1E:D8:BD:91:D3:8B:57:10:5F:07:5C:2D:AB:3E:26:A4:D1:3C:9C:97:15:78:9E:0D:56:0A:CE:DC"
            ]
        }
    }
]
```

To test an existing statement file, you can use the official [Statement List Generator and Tester](https://developers.google.com/digital-asset-links/tools/generator) tool.

During app install/update, an Android `Service` will verify if the App Links configuration complies with the server side `assetlinks.json` file.  
The results will be sent to logcat:

???+ "Logcat"

    === "Android 12+"
    
        ???+ success
            ```
            AppLinksAsyncVerifierV2  I  Verification result: checking for a statement with source https://smarquis.fr, relation delegate_permission/common.handle_all_urls, and target fr.smarquis.applinks --> true. [CONTEXT service_id=244 ]
            AppLinksHostsVerifierV2  I  Verification fr.smarquis.applinks complete. Successful hosts: smarquis.fr. Failed hosts: . Error hosts: . [CONTEXT service_id=244 ]
            ```
    
        ???+ failure
            ```
            AppLinksAsyncVerifierV2  I  Verification result: checking for a statement with source https://smarquis.fr, relation delegate_permission/common.handle_all_urls, and target fr.smarquis.applinks --> false. [CONTEXT service_id=244 ]
            AppLinksHostsVerifierV2  I  Verification fr.smarquis.applinks complete. Successful hosts: . Failed hosts: smarquis.fr. Error hosts: . [CONTEXT service_id=244 ]
            ```
    
    === "Android 11"
    
        ???+ success
            ```
            I/IntentFilterIntentSvc: Verifying IntentFilter. verificationId:0 scheme:"https" hosts:"smarquis.fr" package:"fr.smarquis.applinks".
            I/SingleHostAsyncVerifier: Verification result: checking for a statement with source a <
                                         a: "https://smarquis.fr"
                                       >
                                       , relation delegate_permission/common.handle_all_urls, and target b <
                                         a: "fr.smarquis.applinks"
                                         b <
                                           a: "D2:18:2B:0E:34:38:3B:FD:A7:80:AC:21:88:F1:F7:1F:13:33:AD:CB:E3:94:2A:75:96:FB:A1:7A:0B:6B:CE:68"
                                         >
                                       >
                                        --> true.
            I/IntentFilterIntentSvc: Verification 0 complete. Success:true. Failed hosts:.
            ```
    
        ???+ failure
            ```
            I/IntentFilterIntentSvc: Verifying IntentFilter. verificationId:1 scheme:"https" hosts:"smarquis.fr" package:"fr.smarquis.applinks".
            I/SingleHostAsyncVerifier: Verification result: checking for a statement with source a <
                                         a: "https://smarquis.fr"
                                       >
                                       , relation delegate_permission/common.handle_all_urls, and target b <
                                         a: "fr.smarquis.applinks"
                                         b <
                                           a: "D2:18:2B:0E:34:38:3B:FD:A7:80:AC:21:88:F1:F7:1F:13:33:AD:CB:E3:94:2A:75:96:FB:A1:7A:0B:6B:CE:68"
                                         >
                                       >
                                        --> false.
            I/IntentFilterIntentSvc: Verification 1 complete. Success:false. Failed hosts:smarquis.fr.
            ```

## Android

Same as [Web url](web-url.md) but with `https` only and `android:autoVerify="true"` attribute.  

```xml title="AndroidManifest.xml"
<activity android:name=".MainActivity">
    <intent-filter
        android:autoVerify="true"
        tools:targetApi="m">
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="https"
            android:host="smarquis.fr"
            android:pathPattern="/action" />
    </intent-filter>
</activity>
```

## Data

| Uri | Value |
|---|---|
| scheme | `https` |
| host | `smarquis.fr` |
| path | `/action` |
| query | `?key=value` |
| fragment | `data` |

## Features

| Feature | ✔️/❌ |
|---|:---:|
| App not installed | ❌ |
| Offline | ✔️ |
| Referrer | ❌ |
| Deeplink | ✔️ |
| Deferred deeplink | ❌ |

## Pros and Cons

➕ No more disambiguation dialog  
➕ No potential app hijacking  
➖ Doesn't work on the same domain  
➖ Some (in-app) browsers might directly handle these links and prevent the app to launch  

## Demo

<video class="device" controls muted>
    <source src="../assets/app-links.mp4" type="video/mp4">
</video>

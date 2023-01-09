# Custom scheme

!!! info "Documentation"
    https://developer.android.com/training/app-links/deep-linking.html

[`link://smarquis.fr/action?key=value#data`](link://smarquis.fr/action?key=value#data){ .md-button }

## Android

```xml title="AndroidManifest.xml"
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <data android:scheme="link" />
    </intent-filter>
</activity>
```

## Data

| Uri | Value |
|---|---|
| scheme | `link` |
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

➖ Some browsers don't handle non-http links  

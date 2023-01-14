# Market scheme

!!! info "Documentation"
    https://developer.android.com/distribute/marketing-tools/linking-to-google-play.html#UriSummary

[`market://details?id=fr.smarquis.applinks&url=link%3A%2F%2Fsmarquis.fr%2Faction%3Fkey%3Dvalue%23data&referrer=my%20referrer%20data`](market://details?id=fr.smarquis.applinks&url=link%3A%2F%2Fsmarquis.fr%2Faction%3Fkey%3Dvalue%23data&referrer=my%20referrer%20data){ .md-button }

## 🌍 Web

This url contains additional query parameters that will be handled by the Play Store app:

- `url` is the forwarded url
- `referrer` will trigger a `com.android.vending.INSTALL_REFERRER` Broadcast once the app is installed.

## 🤖 Android

The url will be rewritten by the Play Store to `link://smarquis.fr/action?key=value#data`

## 📦 Data

| Uri | Value |
|---|---|
| scheme | `link` |
| host | `smarquis.fr` |
| path | `/action` |
| query | `?key=value` |
| fragment | `data` |

| Referrer |
|---|
| `my referrer data` |

## ⚙️ Features

| Feature | ✔️/❌ |
|---|:---:|
| App not installed | ✔️ |
| Offline | ✔️ |
| Referrer | ✔️ |
| Deeplink | ✔️ |
| Deferred deeplink | ✔️ |

## 📈 Pros and Cons

➕ Changes the "Open" button in Play Store to "Continue"  
➕ Triggers a notification with "Tap to continue"  

![](assets/store_deferred_deeplink_notification.png)
![](assets/store_deferred_deeplink_button.png)

## 📹 Demo

<video class="device" controls muted>
    <source src="../assets/market-scheme.mp4" type="video/mp4">
</video>

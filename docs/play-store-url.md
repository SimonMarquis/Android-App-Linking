# Play Store url

!!! info "Documentation"
    https://developer.android.com/distribute/marketing-tools/linking-to-google-play.html#UriSummary

[`https://play.google.com/store/apps/details?id=fr.smarquis.applinks&url=link%3A%2F%2Fsmarquis.fr%2Faction%3Fkey%3Dvalue%23data&referrer=my%20referrer%20data`](https://play.google.com/store/apps/details?id=fr.smarquis.applinks&url=link%3A%2F%2Fsmarquis.fr%2Faction%3Fkey%3Dvalue%23data&referrer=my%20referrer%20data){ .md-button .timestamp-referrer }

Very similar to the [Market scheme](market-scheme.md).

## 🌍 Web

This url contains additional query parameters that will be handled by the Play Store app:

- `url` is the forwarded url
- `referrer` will trigger a `com.android.vending.INSTALL_REFERRER` Broadcast once the app is installed.

## 🤖 Android

The url will be rewritten by the Play Store to `link://smarquis.fr/action?key=value#data`

## 📦 Data

*Uri available in deferred deeplink only*

| Uri | Value |
|---|---|
| scheme | `link` |
| host | `smarquis.fr` |
| path | `/action` |
| query | `?key=value` |
| fragment | `data` |

| Referrer |
|---|
| `my referrer data`{ .timestamp-referrer } |

## ⚙️ Features

| Feature | ✔️/❌ |
|---|:---:|
| App not installed | ✔️ |
| Offline | ✔️ |
| Referrer | ✔️ |
| Deeplink | ❌ |
| Deferred deeplink | ✔️ |

## 📈 Pros and Cons

➕ Changes the "Open" button in Play Store to "Continue"  
➕ Triggers a notification with "Tap to continue"  
➖ Some (in-app) browsers might directly handle these links and prevent the Play Store app to launch  
➖ When app is installed, it still opens the Play Store app and completely ignores the deeplink  

## 📹 Demo

<video class="device" controls muted>
    <source src="../assets/play-store-url.mp4" type="video/mp4">
</video>

<script type="text/javascript">
(function(){
    let referrer = "my referrer data at " + Date.now();
    let a = document.querySelector("a.timestamp-referrer");
    let params = new URLSearchParams(a.search);
    params.set("referrer", referrer);
    a.search = params.toString();
    document.querySelector("a.timestamp-referrer > code").innerText = a.href;
    document.querySelector("code.timestamp-referrer").innerText = referrer;
 })();
</script>

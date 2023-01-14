# Intent scheme

!!! info "Documentation"
    https://developer.chrome.com/multidevice/android/intents

[`intent://smarquis.fr/action?key=value#data#Intent;scheme=link;package=fr.smarquis.applinks;S.key=value;S.market_referrer=my%20referrer%20data;end`](intent://smarquis.fr/action?key=value#data#Intent;scheme=link;package=fr.smarquis.applinks;S.key=value;S.market_referrer=my%20referrer%20data;end){ .md-button }

## Web

Extra parameters can be added to the link and will be transfered as extras `Bundle` in the `Intent`:

- String: `S.key=value`
- Boolean: `B.key=value`
- Integer: `i.key=value`
- Long: `l.key=value`
- Float: `f.key=value`

More:

- `S.browser_fallback_url` is the fallback URL if the corresponding link doesn't work of if app isn't available (will be removed from the `Intent`).
- `S.market_referrer` will trigger a `com.android.vending.INSTALL_REFERRER` Broadcast once the app is installed.

## Android

The url will be rewritten by the [parseUri()](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/content/Intent.java#5831) method from the Android source code.  
The new url will be `link://smarquis.fr/action?key=value#data`

And will contain additional parameters in the `Intent`.  

## Data

| Uri | Value |
|---|---|
| scheme | `link` |
| host | `smarquis.fr` |
| path | `/action` |
| query | `?key=value` |
| fragment | `data` |

| Extra | Value |
|---|---|
| `key` | `value` |
| `market_referrer` | `my referrer data` |

| Referrer |
|---|
| `my referrer data` |

## Features

| Feature | ✔️/❌ |
|---|:---:|
| App not installed | ✔️ |
| Offline | ✔️ |
| Referrer | ✔️ |
| Deeplink | ✔️ |
| Deferred deeplink | ❌ |

## Pros and Cons

➖ Some browsers don't handle non-http links  

## Demo

<video class="device" controls muted>
    <source src="../assets/intent-scheme.mp4" type="video/mp4">
</video>

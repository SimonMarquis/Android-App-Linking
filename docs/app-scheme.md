# App scheme

!!! info "Documentation"
    https://developer.android.com/reference/android/content/Intent.html#URI_ANDROID_APP_SCHEME

[`android-app://fr.smarquis.applinks/https/smarquis.fr/action?key=value#data#Intent;S.key=value;S.market_referrer=my%20referrer%20data;end`](android-app://fr.smarquis.applinks/https/smarquis.fr/action?key=value#data#Intent;S.key=value;S.market_referrer=my%20referrer%20data;end){ .md-button }

Very similar to [Intent scheme](intent-scheme.md).

## Web

Extra parameters can be added to the link and will be transfered as extras `Bundle` in the `Intent`:

- String: `S.key=value`
- Boolean: `B.key=value`
- Integer: `i.key=value`
- Long: `l.key=value`
- Float: `f.key=value`

More:

- `S.market_referrer` will trigger a `com.android.vending.INSTALL_REFERRER` Broadcast once the app is installed.

## Android

The url will be rewritten by the [parseUri()](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/content/Intent.java#5831) method from the Android source code.  
The new url will be `https://smarquis.fr/action?key=value#data`

And will contain additional parameters in the `Intent`.  

## Data

| Uri | Value |
|---|---|
| scheme | `https` |
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
    <source src="../assets/app-scheme.mp4" type="video/mp4">
</video>

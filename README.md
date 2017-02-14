# HackFSU Android App
`com.hackfsu.mobile.android`

The HackFSU Android app is an open-source, easily configurable software package
to be shared by the hacker community.

## Package Structure

> `com.hackfsu.mobile.android.app.*`

These sub-packages are front-end, UI related packages including Activities,
Fragments, Adapters, custom UI, etc.

> `com.hackfsu.mobile.android.api.*`

These classes are the server-facing, networking elements which connect to
the HackFSU web API.

## `API.java`

The `API.java` class is the primary class to be used by the UI code. This class
contains calls such as `getUpdates()`, `getSchedule()`, etc., which take
`APICallback` objects that will return various models extending the `BaseModel`
abstract class, such as `UpdateModel`, `ScheduleModel`, etc.

An important note about modifying `API.java` is that it uses a private method,
`performCallback(APICallback, <? extends BaseModel>)` to execute the APICallback
as defined in an Activity/Fragment on the UI thread. 

## Key Signing

To release this app on the Play Store, it need digitally signed. For security,
the keystore is not on this repo. Please contact Andrew at `andrew@andrewsosa.com`
to obtain a copy of the keystore for future releases.

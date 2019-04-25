


# Trust Technologies 
![image](https://avatars2.githubusercontent.com/u/42399326?s=200&v=4)

 
# Description

Trust is a platform that allows building trust and security between people and technology.

**Trust-device-info**  allows you to obtain a unique universal ID for each device from a set of characteristics of this device. It also allows the monitoring of device status changes, to have knowledge of the device status at all times.


# Implementation

```java
dependencies {
	implementation 'lat.trust.trustdemo:trusttrifles:1.0.33'
}

```
> LINK PARA OBTENER EL MAS ACTUALIZADO **Synchronize** sub-menu.


# Initialize
```java
import ...
public class TestApp extends Application {  
  @Override  
  public void onCreate() {  
        super.onCreate();  
		TrustClient.init(this);  
		TrustPreferences.init(this);
		TrustConfig.init();
  }
}

```
# Permissions
In order for the library to work without problems, the following permissions must be added to the application. **Remember: This permissions are granted from user directly, additionally to write at manifest**:

```java
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.CAMERA"/>
```
> Mencionar permiso de SMS **Synchronize** sub-menu.
## Permissions that the library owns

These are the permissions that the library currently uses:

```java
<uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES" />
<uses-permission android:name="android.permission.NFC"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
# Minimum Api Lvl 15
The trust library works from API LEVEL 15 because Google Play services and Firebase for Android will support API level 15 at minimum.
   
See more information [here](https://android-developers.googleblog.com/2016/11/google-play-services-and-firebase-for-android-will-support-api-level-14-at-minimum.html).
# Firebase

# Methods
## SendTrifles

```java

TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {  
  @Override  
  public void onSuccess(int code, Audit data) { }  
  @Override  
  public void onError(int code) { }  
  @Override  
  public void onFailure(Throwable t) { }   
  @Override  
  public void onPermissionRequired(ArrayList<String> permisos) {  }  
});

```
## Audits

StackEdit stores your files in your browser, which means all your files are automatically saved locally and are accessible **offline!**


|     Audit    |Operation                       |Result                      |
|--------------|-------------------------------|-----------------------------|
|SMS           |`Tester`                       |`Tester`                     |
|CALL          |`Tester`                       |`Tester`                     |














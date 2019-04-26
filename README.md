



# Trust Technologies 
![image](https://avatars2.githubusercontent.com/u/42399326?s=200&v=4)

 
# Description

Trust is a platform that allows building trust and security between people and technology.

**Trust-device-info**  allows you to obtain a unique universal ID for each device from a set of characteristics of this device. It also allows the monitoring of device status changes, to have knowledge of the device status at all times.


# Implementation

```java
dependencies {
	implementation 'lat.trust.trustdemo:trusttrifles:1.0.38'
}

```
>  
See the actual version[here](https://bintray.com/fcarotrust/trust/trustidentify).

# Initialize
  

>This initiation establishes by default that automatic audits are not initiated
```java
import ...
public class TestApp extends Application {  
  @Override  
  public void onCreate() {  
        super.onCreate();  
		TrustClient.init(this);  
  }
}

```
You can establish what type of automatic audits are what you need in your application in the following way:
```java
import ...
public class TestApp extends Application {  
  @Override  
  public void onCreate() {  
        super.onCreate();  
		TrustClient.init(this);  
		TrustClient.init(this);  
		String[] audits = {  
	        TrustConfig.AUDIT_BOOT,  
			TrustConfig.AUDIT_NETWORK,  
			TrustConfig.AUDIT_ALARM,  
			TrustConfig.AUDIT_CALL,  
			TrustConfig.AUDIT_SIM,  
			TrustConfig.AUDIT_SMS  
		};  
		TrustClient.getInstance().setAudits(audits);  //Enable only the listed Audits
		TrustClient.getInstance().setAllAudit(); //Enables all audits
  }
}
```
>  In the audits section, the types of audits that exist are reported.


# Permissions
In order for the library to work without problems, the following permissions must be added to the application. **Remember: This permissions are granted from user directly, additionally to write at manifest**:

```java
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.CAMERA"/>
```
> Permiso de SMS Opcional
> If in your application you need automatic SMS audits, it is necessary to add the following permissions
> ```java
><uses-permission android:name="android.permission.RECEIVE_SMS"/>  
> <uses-permission android:name="android.permission.READ_SMS"/>  
> <uses-permission android:name="android.permission.SEND_SMS"/>
> ``` 
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
   
See more information  [here](https://android-developers.googleblog.com/2016/11/google-play-services-and-firebase-for-android-will-support-api-level-14-at-minimum.html).
# Firebase
Sometimes it is necessary to create remote audits using firebase, for this it is necessary to register your application in Firebase, following the following steps:


# Methods
## SendTrifles

With this method you get a trust id

```java
TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {  
  @Override  
  public void onSuccess(int code, Audit data) { 
		String trustId = data.getTrustid();
  }  
  @Override  
  public void onError(int code) { }  
  @Override  
  public void onFailure(Throwable t) { }   
  @Override  
  public void onPermissionRequired(ArrayList<String> permisos) {  }  
});
```
## Notifications

In trust you use your own notification service, to send push notifications, in order to make use of this service you need to use the following method, sending as parameter the firebase token and the context.

```java
Notifications.registerDevice(FirebaseInstanceId.getInstance().getToken(),context);
```
## Audits
  
Audits are events that are to be reported to be stored and subsequently consulted as a record
```java
AutomaticAudit.createAutomaticAudit("operation test","method test","result test",context);
```
Where
**Operation**: It is the context that you want to register, example: Daily audit of login.
**Method**: It is the process that executed the audit, example: DailyAudit.
**Result**: It is the result of that audit, example: start of successful session!

|     Audit    |Operation                       |Result                      |
|--------------|-------------------------------|-----------------------------|
|SMS           |`Tester`                       |`Tester`                     |
|CALL          |`Tester`                       |`Tester`                     |

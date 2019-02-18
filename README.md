


# Trust Technologies 
![image](https://avatars2.githubusercontent.com/u/42399326?s=200&v=4)

  # Description
  
Trust is a platform that allows building trust and security between people and technology.
  
**Trust-device-info** allows you to obtain a unique universal ID for each device from a set of characteristics of this device. It also allows the monitoring of device status changes, to have knowledge of the device status at all times.
# Usage
  
In order to make the correct use of the library, the following steps must be followed.
  ## Implementation
``` java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```java
dependencies {
	implementation 'com.github.trusttechnologies:trust-device-info:1.1.4'
}

```

  

Use this [Jitpack.io link](https://jitpack.io/#trusttechnologies/trust-device-info) to get the most updated version.
  
  ## Class extends from Application
  
For the correct use of the library, it is necessary to create a class that extends the application, then make a call to the class TrustClient.init (this) and send as a parameter the context. in this way the library will be instantiated with the context of the entire application.

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
  
For these changes to take effect, it is necessary to declare the class that extends the application on the "name" tag of the initial application.

```java
<application  
	...
	  android:name=".TestApp" 
	...
</application>
```
## Necessary permissions in the application
  
In order for the library to work without problems, the following permissions must be added to the application. **Remember: This permissions are granted from user directly, additionally to write at manifest**:

```java
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.CAMERA"/>
```
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

See more information about permissions [here](https://developer.android.com/guide/topics/security/permissions?hl=es-419#normal-dangerous).

# Minimum Api Lvl 14
The trust library works from API LEVEL 14 because Google Play services and Firebase for Android will support API level 14 at minimum.
  
See more information [here](https://android-developers.googleblog.com/2016/11/google-play-services-and-firebase-for-android-will-support-api-level-14-at-minimum.html).


# Methods

## getTrifles()

 
Get the minutiae of the Device. If requestTrustId is True, they will be sent to the service to obtain the Trust ID. If the listener is not null, the result of the request will be notified.
   
This method forces the turning on and off of bluetooth and wifi.

**Params:**
- **boolean:** requestTrustId, *si se requiere enviar las minucias al servicio*
- **TrustListener:** listener
### Example

  

```java

TrustClient.getInstance().getTrifles(true, new  TrustListener.OnResult<Audit>() {
	@Override
	public  void  onSuccess(int  i, Audit  audit) {/**/}
	@Override
	public  void  onError(int  i) {/**/}
	@Override
	public  void  onFailure(Throwable  throwable) {/**/}
});

```
## getTrifles()

Get the minutiae of the Device. IF requestTrustId is True, they will be sent to the service to obtain the Trust ID. If the listener is not null, the result of the request.


**Params:**
- **boolean:** requestTrustId

- **boolean:** required_permits  ,*indicates if permission should be requested*
- **boolean:** forceWifi , *Indicates whether to turn on WiFi power to get information*
- **boolean:** forceBluetooth ,  *indicates whether bluetooth power should be forced to obtain information*
- **TrustListener:** listener

  
*forceWifi is true, it will force the wifi on to obtain information.*
  
*forceBluetooth is true, it will force the wifi on to obtain information.*

*required_permits is true, it will force the application to request the permissions, if they are denied it can be managed with the listener.*

### Example



```java

TrustClient.getInstance().getTrifles(true,true,true,true, new  TrustListener.OnResult<Audit>() {
	@Override
	public  void  onSuccess(int  i, Audit  audit) {/**/}
	@Override
	public  void  onError(int  i) {/**/}
	@Override
	public  void  onFailure(Throwable  throwable) {/**/}
});

```
   ## notifyEvent()
  
  
  
This method is used to report a change in the device to the server. Use the trust id stored by the library. Do not expect audit in the response.

*14/11/2018: Currently this method returns error code 404 in its onFailure method* 

**Params**
- **String** packageName, *the package name*
- **String** eventType, *the type of event e.g : Bluetooth*
- **String** eventValue, *the value of the type event, e.g: Status:true*
- **double** lat, *latitude in which the change occurred*
- **double** lng, *length at which the change occurred*
- **TrustListener** listener


### Example

```java

TrustClient.getInstance().notifyEvent(packageName, eventType, eventValue, lat, lng, listener);

```

   ## remoteEvent()
  
  
  
Method used to report a change in the device to the server expect an audit in the response.

*14/11/2018: Currently this method returns error code 500 in its onFailure method* 

**Params**

- **String:** tid, *the unique trust id*
- **String:** object, *the name of the object to report*  
- **String:** key, *the name of the change e.g: Wifi*  
- **String:** value, *the description or value of the change e.g: status :false*  
- **double:** lat,  *latitude in which the change occurred*  
- **double:** lng, *length at which the change occurred* 
- **TrustListener:** listener
### Example

```java

TrustClient.getInstance().remoteEvent(trustId,packageName,eventType,eventValue,lat,lng,listener) {  
	@Override  
	public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) {/**/}  
  
	@Override  
	public void onFailure(Call<TrifleResponse> call, Throwable t) {/**/}
};

```
   ## remoteEvent2()
  
  
RemoteEvent clone method, try to try another Body to send information. Use sending by String for SIM info (use prior to the creation of SIM Class). Expect an audit in the response.

*14/11/2018: Currently this method returns error code 500 in its onFailure method* 

**Params**
- **String:** tid, *the unique trust id*
- **String:** object, *the name of the object to report*  
- **String:** key, *the name of the change e.g: Wifi*  
- **String:** value, *the description or value of the change e.g: status :false*  
- **double:** lat,  *latitude in which the change occurred*  
- **double:** lng, *length at which the change occurred* 
- **TrustListener:** listener

### Example

```java

TrustClient.getInstance().remoteEvent2(trustid,operation,method,timestamp,lat,lng,listener){
	@Override  
	public void onResponse(Call<TrifleResponse> call, Response<TrifleResponse> response) { /**/ }  
  
	@Override  
	public void onFailure(Call<TrifleResponse> call, Throwable t) {/**/}
}

```
## createAudit()
  
  
This method notify any action realized (transaction), i.e. document sign.

**Params**
- **String:** tid, *the unique trust id*
- **String:** operation, *the name of the operation to report*  
- **String:** method, *the name of the method to report*  
- **long:** timestamp, *time the change occurred*
- **double:** lat,  *latitude in which the change occurred*  
- **double:** lng, *length at which the change occurred* 
- **TrustListener:**  listener

### Example

```java

TrustClient.getInstance().createAudit(trustid,operation,method,timestamp,lat,lng,listener){
	@Override  
	public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) { /**/ }  
  
	@Override  
	public void onFailure(Call<Void> call, Throwable t) {/**/}
}

```

## getImei()

  
Get the device **IMEI**

**Return:**   
- Returns the **IMEI** of the device as a **String**
### Example

```java

TrustClient.getInstance().getImei();

```

## getSIMSerialID()

  
Get the serial ID of the SIM card.

**Return**
Returns the serial ID of the SIM card as String, if it does not find it, it returns "UNKNOWN".

```java

TrustClient.getInstance().getSIMSerialID();

```
## getSIMCarrier()

  
Get the Carrier of the SIM card.

**Return**
Returns Carrier SIM as a String, if it does not find it returns "UNKNOWN".

```java

TrustClient.getInstance().getSIMCarrier();

```
## getSIMState()

  
  
Get the status of the SIM card.

**Return**
Returns the status of the SIM as String, possible values: "ABSENT", "LOADED" and "UNKNOWN". if it does not find it, it returns "UNKNOWN".

```java

TrustClient.getInstance().getSIMState();

```

# External Library
These libraries are under the Apache 2.0 license.

* [logging-interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
* [okhttp](https://github.com/square/okhttp) 
* [okhttp-urlconnection](https://github.com/square/okhttp) 
* [retrofit](https://github.com/square/retrofit) 
* [retrofit-converters/gson](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)

 
# Modified external library
These libraries are under the Apache 2.0 license.
  
### Original libraries
* [Dexter](https://github.com/Karumi/Dexter) *currently in use*
* [RootBeer](https://github.com/scottyab/rootbeer) *currently in disuse*
 ###   Modified libraries
* [Dexter Modified  ](https://github.com/trusttechnologies/Dexter) *currently in disuse*
* [RootBeer Modified ](https://github.com/trusttechnologies/rootbeer) *currently in use,   this library was modified to be used in **Api Level** less than 14.*









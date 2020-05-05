

  
  
  
  
# Trust Technologies 
![image](https://avatars2.githubusercontent.com/u/42399326?s=200&v=4)  
  
   
# Description  
  
Trust is a platform that allows building trust and security between people and technology.  
  
**Trust-device-info** allows you to obtain a unique universal ID for each device from a set of characteristics of this device. It also allows the monitoring of device status changes, to have knowledge of the device status at all times.  
  
  
# Implementation  
  
```java  
dependencies {  
 implementation 'lat.trust.trustdemo:trusttrifles:2.0.12'
 }  
  
```  
> See the actual version [here](https://bintray.com/fcarotrust/trust/trustidentify).  
  
# Initialize  - TrustClient
    
This initiation establishes by default that automatic audits are not initiated  
```java  
import ...  
public class TestApp extends Application {    
  @Override    
  public void onCreate() {    
        super.onCreate();    
        TrustClient.init(this);  //this init is for normal Trust Id          
  }  
}   
```    
  # Initialize  - TrustClientLite
    
This initiation establishes by default that automatic audits are not initiated  
```java  
import ...  
public class TestApp extends Application {    
  @Override    
  public void onCreate() {    
        super.onCreate();   
        TrustClientLite.init(this); //this init is for lite Trust Id  (1 permission)   
  }  
}   
```  
  # Initialize  -   TrustClientZero.init(this); 

    
This initiation establishes by default that automatic audits are not initiated  
```java  
import ...  
public class TestApp extends Application {    
  @Override    
  public void onCreate() {    
        super.onCreate();   
        TrustClientZero.init(this);  //this init is for lite Trust Id  (0 permission) 
  }  
}   
``` 
# Permissions  
In order for the library to work without problems, the following permissions must be added to the application. **Remember: This permissions are granted from user directly, additionally to write at manifest**:  
  
Trust identify normal permission
```java  
<uses-permission android:name="android.permission.READ_PHONE_STATE" />  
<uses-permission android:name="android.permission.CAMERA" />  
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />  
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> 

```  
Trust identify lite permission

```java  
<uses-permission android:name="android.permission.READ_PHONE_STATE" />  
```  
 ## Permissions that the library owns  
  
These are the permissions that the library currently uses:  
  
```java  
<uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES" />  
<uses-permission android:name="android.permission.NFC" />  
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />  
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />  
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />  
<uses-permission android:name="android.permission.CAMERA" />  
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />  
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />  
<uses-permission android:name="android.permission.INTERNET" />   
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />  
<uses-permission android:name="android.permission.BLUETOOTH" />  
<uses-permission android:name="android.permission.READ_PHONE_STATE" /> 
```  
# Minimum Api Lvl 15  
The trust library works from API LEVEL 15 because Google Play services and Firebase for Android will support API level 15 at minimum.  
     
See more information  [here](https://android-developers.googleblog.com/2016/11/google-play-services-and-firebase-for-android-will-support-api-level-14-at-minimum.html). 
>  For more information visit the documentation in Firebase console [here](https://firebase.google.com/docs/?hl=es)
>  
# Methods  
  
This section describes the methods that the library has to get the trust id.
## Get Trust Id Zero  
  
With this method you get a trust id lite version
  
```java  
TrustClientZero.getTrustIdZero(this, new TrustListener.OnResult<Audit>() {
            @Override
            public void onSuccess(int code, Audit data) {
            String trustId = data.getTrustid();  //get trust id
            }
            @Override
            public void onError(int code) {}
            @Override
            public void onFailure(Throwable t) {}
            @Override
            public void onPermissionRequired(ArrayList<String> permisos) {}
        });
```

## Get Trust Id Lite  
  
With this method you get a trust id lite version
  
```java  
TrustClientLite.getTrustIDLite(this, new TrustListener.OnResult<Audit>() {  
    @Override  
    public void onSuccess(int code, Audit data) {
    String trustId = data.getTrustid();  //get trust id
    }  
    @Override  
    public void onError(int code) {}  
  
    @Override  
    public void onFailure(Throwable t) {}  
  
    @Override  
    public void onPermissionRequired(ArrayList<String> permissions) {}  
});
```
## Get Trust Id Normal  
  
With this method you get a trust id normal version

```java  
TrustClient.getInstance().getTrifles(true, new TrustListener.OnResult<Audit>() {    
  @Override    
  public void onSuccess(int code, Audit data) {   
      String trustId = data.getTrustid();  //get trust id
  }  
  @Override    
  public void onError(int code) { }
      
  @Override    
  public void onFailure(Throwable t) { }
       
  @Override    
  public void onPermissionRequired(ArrayList<String> permisos) {  } 
});  
```
## Send a Identify
If in your application there is a login with user data, please send that information as follows
```java  
Identity identity = new Identity();
            identity.setDni("11222333-7");
            identity.setEmail("example@mail.com");
            identity.setName("John");
            identity.setLastname("Doe");
            identity.setPhone("+56982554411");
            Hawk.put(Constants.IDENTITY, identity);//  <- this save the identify
            TrustClientLite.getTrustID(context, listener{});    //  <- this send the identify
            TrustClientLite.getTrustIDLite(context, listener{});//  <- this send the identify
            TrustClientLite.getTrustIDZero(context, listener{});//  <- this send the identify
```

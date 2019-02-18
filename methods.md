
# Methods
## getTrifles()

 
Get the minutiae of the Device. If requestTrustId is True, they will be sent to the service to obtain the Trust ID. If the listener is not null, the result of the request will be notified


**Params:**
- **boolean:** requestTrustId
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

  ## getCameraData()
  Add some information from the cameras of the device
  
**Params:**
- **Device:** device, *object that stores the information obtained from the device*


### Example

  

```java

TrustClient.getInstance().getCameraData(device);

```

## getNFCData()

Add information regarding the presence of NFC in the device

**Params:**
- **Device:** device, *object that stores the information obtained from the device*
### Example

  

```java

TrustClient.getInstance().getNFCData(device);

```

  ## getBatteryData()

Add information related to the device's battery

**Params:**
- **Device:** device, *object that stores the information obtained from the device*

### Example

```java

TrustClient.getInstance().getBatteryData(device);

```

  ## getBatteryCapacity()

Obtaining capacity in mAh of the battery of the device.
**Params:**
- **Context:** context, *Necessary context for the use of reflection*

**Return:**
- Returns the capacity of the battery in mAh as a **double**
### Example

  

```java

TrustClient.getInstance().getBatteryCapacity(context);

```
## getSensorsData()

Add some information from the device's sensors

**Params:**
- **Device:** device, *object that stores the information obtained from the device*

### Example

  

```java

TrustClient.getInstance().getSensorsData(device);

```
## getDeviceData()

Add all the relevant information obtained from Build

**Params:**
- **Device:** device, *object that stores the information obtained from the device*

### Example

  

```java

TrustClient.getInstance().getDeviceData(device);

```
## getImei()

This method adds the **IMEI** of the device and the software version

**Return:**   
- Returns the **IMEI** of the device as a **String**
### Example

  

```java

TrustClient.getInstance().getImei();

```
  ## getImei()

This method adds the imei of the device and the software version

**Params:**
- **Device:** device, *object that stores the information obtained from the device*
### Example

  

```java

TrustClient.getInstance().getImei(device);

```
  ## getTelInfo()

This method obtains the info of each available SIM.

- **Return:** returns a list of **SIMS** objects

### Example

  

```java

TrustClient.getInstance().getTelInfo();

```
  ## getSimDataAtSlot()
 
Obtain the state of the sim in the indicated slot

  **Params**
  - **int:** slot, *represents the slot number of the SIM card (e.g: slot 2)*

**Return:** 
- Returns a **SIM** object
### Example

```java

TrustClient.getInstance().getSimDataAtSlot(slot);

```
  ## getSIMStateBySlot()
 
Obtain the state of the sim in the indicated slot
  **Params:**
  - **int:** slotID, *represents the slot number of the SIM card (e.g: slot 2)*

**Return:** 
- Returns the status of the requested SIM, *__true__ if the sim is available in the indicated slotID*
### Example


```java

TrustClient.getInstance().getSIMStateBySlot(slotID);

```
  ## getDeviceIdBySlot()

  
  
Gets the value of the methodo consulted

**Param.**
 - **String:** predictedMethodName, *represents the name of the method from which you want to obtain information*
- **int:** slotID, *represents the slot number of the SIM card (e.g: slot 2)*

**Return:** 
- Returns a **String** with the information associated with the **SIM**
### Example

  

```java

TrustClient.getInstance().getDeviceIdBySlot(predictedMethodName,slotID);

```
  ## getCPUDataCat()
 
  
This method adds to the received object the values ​​sought on the CPU obtained from the **/proc/cpuinfo** file using system commands

**Param:**
- **Device** device, *Object where the data obtained from the device is stored*
### Example

  

```java

TrustClient.getInstance().getCPUDataCat(device);

```
  ## getMemDataCat()
 
  
This method adds to the received object the values ​​sought on the CPU obtained from the **/proc/cpuinfo** file using system commands

**Param:**
- **Device:** device, *object that stores the information obtained from the device*
### Example

```java

TrustClient.getInstance().getMemDataCat(device);

```
 ## getMacAddress()
  
Get the **mac-address** of the **wlan0** device if available

**Return:**
- Returns the **mac-address** of the device as a **String**. If an **error** occurs or is **not available**, return 
02: 00: 00: 00: 00: 00
### Example

```java

TrustClient.getInstance().getMacAddress();

```
 ## getBluetoothMacAddress()
  
  
Gets the **mac-address** associated with the device's Bluetooth if available

**Return:**
- Returns the **mac-address** of the Bluetooth device as a **String**. If an **error** occurs or is **not available**, return 
02: 00: 00: 00: 00: 00
### Example

```java

TrustClient.getInstance().getBluetoothMacAddress();

```

 ## getAndroidDeviceID()
  
  
  
Get the **DEVICE ID** of the device if it is available

**Return:**
- Returns the **DEVICE ID** of the device, if it is not available returns a string **"not found"**
### Example

```java

TrustClient.getInstance().getAndroidDeviceID();

```
 ## getGoogleServiceFramework()
  
  
  
  
Obtain the Google Service Framework (GSF) of the device if available

**Return:**
- Return **GSF** of the device, in case of error returns the string or **"Not found"**
### Example

```java

TrustClient.getInstance().getGoogleServiceFramework();

```
   ## getRooted()
  
    
Verify if the device is rooted, although as it considers different parameters, there is the possibility of obtaining false positives. An example is OnePlus cell phones, since there are traces of Busybox left in the pre-installed rom.


**Return:**
- Returns the "Rooted" String if it is considered rooted or "No Rooted" if it is not in that state
### Example

```java

TrustClient.getInstance().getRooted();

```
   ## sendTrifles()
  
  
Send the collected details to the server to obtain an identifier
**Params**
- **TrifleBody:** mBody  
- **TrustListener.OnResult< Audit>:** listener, *send it if you want to recover the answer from your application*

**Return:**
- Returns the "Rooted" String if it is considered rooted or "No Rooted" if it is not in that state
### Example

```java

TrustClient.getInstance().sendTrifles(mBody,listener){
	@Override  
	public void onResponse(@NonNull Call<TrifleResponse> call, @NonNull Response<TrifleResponse> response) { /**/ }
	@Override  
	public void onFailure(Call<TrifleResponse> call, Throwable t) {/**/}
};

```
   ## notifyEvent()
  
  
This method is used to report a change in the device to the server use the trust id that the user obtained do not expect audit in the response

*14/11/2018: Currently this method returns error code 404 in its onFailure method* 

**Params**
- **String:** trustId  ,id unique trust
- **String:** packageName  ,package name in context
- **String:** eventType  
- **String:** eventValue  
- **double:** lat  
- **double:** lng  
- **TrustListener:** listener

### Example

```java

TrustClient.getInstance().notifyEvent(trustId,packageName,eventType,eventValue,lat,lng,listener) {  
	@Override  
	public void onResponse(Call<Void> call, Response<Void> response) {/**/}  
  
    @Override  
	public void onFailure(Call<Void> call, Throwable t) {/**/}
};

```
   ## notifyEvent()
  
  
  
This method is used to report a change in the device to the server. Use the trust id stored by the library. Do not expect audit in the response

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
  
  
RemoteEvent clone method, try to try another Body to send information. Use sending by String for SIM info (use prior to the creation of SIM Class). Expect an audit in the response

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
  
  
This method notify any action realized (transaction), i.e. document sign

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
   ## getWifiState()
  
  
This method get state of Wifi (true, false)

**Params**
- **Device:** device, *object that stores the information obtained from the device*


### Example

```java

TrustClient.getInstance().getWifiState(device);

```

   ## getBluetoothState()
  
  
This method get state of Bluetooth(true, false)

**Params**
- **Device:** device, *object that stores the information obtained from the device*


### Example

```java

TrustClient.getInstance().getBluetoothState(device);

```
   ## getRedGState()
  
  
This method get state of Bluetooth(true, false)

**Params**
- **Device:** device, *object that stores the information obtained from the device*


### Example

```java

TrustClient.getInstance().getRedGState(device);

```


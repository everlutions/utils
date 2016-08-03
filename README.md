# utils
An Andorid utility Library

### PrefsUtils.class
Can be used to make it easily read and write variables and POJO's to SharedPreferences. 

```java
        //Easily store a pojo in shared preferences
        PrefsUtil.setObject(this, "pref_key_myObject", MyObject.class);

        //Easily store a list of pojo's
        PrefsUtil.setObjectList(this, "pref_key_myObject", myList);

        //Easily read a list of pojo's
        myList = (ArrayList<MyObject>) PrefsUtil
                .getObjectList(this, "pref_key_myObject", new TypeReference<List<MyObject>>() {
                });
```

### Utils.class
Can be used for a many quick functionalities which you are too lazy remember and look up everytime you need them.

```java
        Utils.isConnected(this);

        Utils.getRandomNumber(0,10);
        
        Utils.getObjectFromJsonFile(this, "assets_file_name", MyObject.class);
        
        Utils.getAndroiAPIVersion();

        Utils.getDeviceID(this);

        Utils.lockOrientation(this);

        Utils.parseFormatAndDateStringToCal("13/04/1988", "dd/MM/yyyy");
        
        //etc...
```

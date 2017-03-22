### Android Multipicker Library (Images, Videos, Files, Audios, Contacts)
---

**Makes it easy and simple to integrate "Attach that file" feature into your android apps.**

>Don't worry about various devices/OS variations.

>Don't worry about out-of-memory errors.

>Don't worry about creating thumbnails to show a preview.

>Picking up any file for your app, and it's details.

>Picking up audio files.

##### Code less for capturing  images/videos/files
- Choose images from device or take a photo
- Choose videos from device or record one
- Choose files available on your device
- Choose audio files available on your device
- Choose a contact from the phonebook
- Works with almost all content providers
- Get all metadata about the media that you would probably need
- Similar code base to implement irrespective of Android version of device.


##### Gradle
```groovy
compile 'com.kbeanie:multipicker:1.1.31@aar'
```

##### Maven
```xml
<dependency>
    <groupId>com.kbeanie</groupId>
    <artifactId>multipicker</artifactId>
    <version>1.1.31</version>
</dependency>
```

See [Wiki Pages](https://github.com/coomar2841/android-multipicker-library/wiki) for code snippets.

_Try out the sample app if you want to test it out first._

<a href="https://play.google.com/store/apps/details?id=com.kbeanie.multipicker.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1">
    <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" width="150px"/>
</a>

##### Recent changes

###### Version 1.1.4-beta  (compile 'com.kbeanie:multipicker:1.1.4-beta@aar)
- library conflict issue #93

###### Version 1.1.31
- #60: Bug: resource leak found in strict mode
- Take picture/video with ```CacheLocation.INTERNAL_APP_DIR```
- By default logs are disabled. Use ```PickerManager.setDebuggable(boolean debug)``` to enable logging
- Fix for Xiaomi Devices when picking images/files

###### Version 1.1.2
- Pull Request: #43
- #44: Crash when application label is not specified
- #37: Set Custom folder name
- #46: FileUriExposedException
- #48: Provider Conflict
- #50: Wrong file name for PickerUtils.java
- #56: If use ensureMaxSize with CameraImagePicker, unable to delete original photo
- #55: resultCode always return RESULT_CANCELED when configure CameraImagePicker to use internal app directory

###### Version 1.1.1
- Fixed problem where for some files, mimetypes were not being properly set
- Removed `Intent.createChooser` implementation Issue #33
- Added option for `CacheLocation.INTERNAL_APP_DIR` for saving files into application's internal storage.

##### License
---

Copyright 2016 Kumar Bibek

Licensed under the Apache License, Version 2.0 (the "License");<br />
you may not use this file except in compliance with the License.<br />
You may obtain a copy of the License at
   
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
	
Unless required by applicable law or agreed to in writing, software<br />
distributed under the License is distributed on an "AS IS" BASIS,<br />
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br />
See the License for the specific language governing permissions and<br />
limitations under the License.

---

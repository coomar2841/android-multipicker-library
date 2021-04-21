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
implementation 'com.kbeanie:multipicker:1.6.2@aar'
```

##### Maven
```xml
<dependency>
    <groupId>com.kbeanie</groupId>
    <artifactId>multipicker</artifactId>
    <version>1.6.2</version>
</dependency>
```

See [Wiki Pages](https://github.com/coomar2841/android-multipicker-library/wiki) for code snippets.

_Try out the sample app if you want to test it out first._

<a href="https://play.google.com/store/apps/details?id=com.kbeanie.multipicker.sample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1">
    <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" width="150px"/>
</a>

##### Recent changes

###### Version 1.6.2
- Issue #180: Crash "FileProvider" metadata not found

###### Version 1.6.1
- Issue #180: use of "requestLegacyExternalStorage"

###### Version 1.6
- Issue #120: Image selection issue in Android 8.0
- Other minor bug fixes.

###### Version 1.5
- Issue #126: UPT: Made picker classes non final
- Issue #47: Add quality/compression level property
- Issue #130: android.database.CursorIndexOutOfBoundsException
- Issue #121: CursorIndexOutOfBoundsException

###### Version 1.3
- Updated build tools and min SDK version
- Issue #86: Added equals and hashcode methods


##### License
---

Copyright 2018 Kumar Bibek

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

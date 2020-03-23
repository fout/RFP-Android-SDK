# RFP-Android-SDK

## Setup Instructions

### Gradle Settings

```
repositories {
    maven { url 'https://raw.github.com/fout/RFP-Android-SDK/master/repos/' }
}
```

```
dependencies {
    implementation 'jp.fout.rfp.android.sdk:rfp-sdk:x.x.x'
    implementation 'com.google.android.gms:play-services-base:11.8.0'

    // for rfp-video-sdk

    // if using exoplayer2.6
    //implementation 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer26:x.x.x'
    //implementation 'com.google.android.exoplayer:exoplayer-core:2.6.+'
    //implementation 'com.google.android.exoplayer:exoplayer-hls:2.6.+'
    
    // if using exoplayer2.7
    //implementation 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer27:x.x.x'
    //implementation 'com.google.android.exoplayer:exoplayer-core:2.7.+'
    //implementation 'com.google.android.exoplayer:exoplayer-hls:2.7.+'
    
    // if using exoplayer2.8
    // implementation 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer28:x.x.x'
    // implementation 'com.google.android.exoplayer:exoplayer-core:2.8.+'
    // implementation 'com.google.android.exoplayer:exoplayer-hls:2.8.+'

    // if using exoplayer2.9(set compileOptions.targetCompatibility to JavaVersion.VERSION_1_8 is requried)
    // implementation 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer29:x.x.x'
    // implementation 'com.google.android.exoplayer:exoplayer-core:2.9.+'
    // implementation 'com.google.android.exoplayer:exoplayer-hls:2.9.+'

    // if using exoplayer2.10
    //implementation 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer210:x.x.x'
    //implementation 'com.google.android.exoplayer:exoplayer-core:2.10.+'
    //implementation 'com.google.android.exoplayer:exoplayer-hls:2.10.+'

    // if using exoplayer2.11
    //implementation 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer211:x.x.x'
    //implementation 'com.google.android.exoplayer:exoplayer-core:2.11.+'
    //implementation 'com.google.android.exoplayer:exoplayer-hls:2.11.+'
}
```

## docs
- [RFP-Android-SDK Docs](https://fout.github.io/RFP-Android-SDK/)

## javadoc
- [javadoc/sdk](https://fout.github.io/RFP-Android-SDK/sdk/)
- [javadoc/sdk-video](https://fout.github.io/RFP-Android-SDK/sdk-video/)

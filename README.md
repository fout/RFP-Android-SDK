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
    compile 'jp.fout.rfp.android.sdk:rfp-sdk:x.x.x'
    compile 'com.google.android.gms:play-services-base:11.8.0'

    // for rfp-video-sdk
    // using version 2.5.x
    //compile 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer25:x.x.x'
    //compile 'com.google.android.exoplayer:exoplayer-core:r2.5.4'
    //compile 'com.google.android.exoplayer:exoplayer-hls:r2.5.4'

    // if using exoplayer2.6
    //compile 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer26:x.x.x'
    //compile 'com.google.android.exoplayer:exoplayer-core:r2.6.1'
    //compile 'com.google.android.exoplayer:exoplayer-hls:r2.6.1'

    // if using exoplayer2.4
    //compile 'jp.fout.rfp.android.sdk:rfp-video-sdk-exoplayer24:x.x.x'
    //compile 'com.google.android.exoplayer:exoplayer-core:r2.4.4'
    //compile 'com.google.android.exoplayer:exoplayer-hls:r2.4.4'
}
```

See the wiki pages for more info.

## javadoc
- [javadoc/sdk](https://fout.github.io/RFP-Android-SDK/sdk/)
- [javadoc/sdk-video](https://fout.github.io/RFP-Android-SDK/sdk-video/)

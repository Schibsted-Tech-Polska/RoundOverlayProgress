#RoundOverlayProgress
Android library for progress component with coloured overlay. You don't need rounded image to make it work. Library will crop it for you.

Library is available from Android 4.0 (API 14).

<img src="http://g.recordit.co/l4dqVNcQ2K.gif" height="340" />

#Usage
```
Add RoundOverlayProgress in your layout:
    <pl.schibsted.roundoverlayprogress.RoundOverlayProgressView
        android:id="@+id/progress_view"
        android:layout_width="150dp"
        android:layout_height="150dp"
        android:src="@drawable/sample_image"
        app:maxProgress="100" />
```
        
Change progress like this:
```
progressView.setProgress(80);
```

#Configuration
It's a simple component extending ImageView. It's easily configurable. Below list of available xml attributes:

        app:maxProgress="100"           //max progress - 100 by default
        app:animate="false"             //should animate? -true by default
        app:animationDuration="500"     //animation duration in milis - 300 by default
        app:progressColor="#66FFFFFF"   //progress overlay color - transparent white by default
#Setup
```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile('com.github.Schibsted-Tech-Polska:RoundOverlayProgress:1.0.2') {
        exclude module: 'appcompat-v7'
    }
}
```

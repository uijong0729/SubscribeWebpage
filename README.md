# SubscribeWebpage
## 概要

- 登録画面でURLを登録し、該当URLの更新をモニタリングするアプリケーションです。
- キーワードを登録し、サイトが更新されかつ、キーワードが含まれた場合、通知が届きます。



## 開発環境

- Kotlin 1.5.0 *( JVM target1.8 )*
- Android Studio 4.2.1



## スケージュール登録

- モニタリングのサイクルは最低15分です。未満に設定した場合は15分と設定されます。
- スケージュール登録時、URLの有効性を確認します。不正URLは登録できません。
- 登録したサイトが閉鎖し、閲覧できなくなった場合、モニタリングが中止されます。
- 更新検索キーワード以外に、CssQuery, HTML Attributeなどの高級使用者向けの機能も含みます。



## dependencies

```
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'androidx.core:core-ktx:1.3.1'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.2.1'
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.1'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.2.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.3.0'
    implementation 'androidx.navigation:navigation-ui-ktx:2.3.0'

    testImplementation 'junit:junit:4.+'

    // work manager
    implementation 'androidx.work:work-runtime:2.4.0'

    // OKHTTP
    implementation 'io.github.rybalkinsd:kohttp:0.12.0'

    // jsoup HTML parser library
    implementation 'org.jsoup:jsoup:1.13.1'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'

    // Room Database
    implementation 'androidx.room:room-runtime:2.2.5'
    implementation "androidx.room:room-ktx:2.2.5"
    kapt "androidx.room:room-compiler:2.2.5"
```



***Thanks to Typora. this document is written by Typora known as markdown editor***

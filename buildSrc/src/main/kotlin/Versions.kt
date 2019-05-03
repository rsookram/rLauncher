object Versions {

    private const val kotlin = "1.3.31"
    private const val dagger = "2.22.1"

    val minSdk = 23
    val targetSdk = 28

    val androidGradlePlugin = "com.android.tools.build:gradle:3.4.0"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin"

    val appCompat = "androidx.appcompat:appcompat:1.0.2"
    val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
    val ktxCore = "androidx.core:core-ktx:1.0.1"
    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"
    val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:2.0.0"
    val ktxViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0"

    val daggerRuntime = "com.google.dagger:dagger:$dagger"
    val daggerCompiler = "com.google.dagger:dagger-compiler:$dagger"

    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlin"

    val coreTesting = "android.arch.core:core-testing:1.1.1"
    val truth = "com.google.truth:truth:0.42"
}

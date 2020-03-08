object Versions {

    private const val kotlin = "1.3.70"
    private const val dagger = "2.26"
    private const val lifecycle = "2.2.0"

    const val minSdk = 25
    const val targetSdk = 29

    const val androidGradlePlugin = "com.android.tools.build:gradle:3.5.3"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin"

    const val appCompat = "androidx.appcompat:appcompat:1.1.0"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
    const val ktxCore = "androidx.core:core-ktx:1.2.0"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycle"
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:$lifecycle"
    const val ktxViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle"

    const val daggerRuntime = "com.google.dagger:dagger:$dagger"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$dagger"

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlin"

    const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
    const val truth = "com.google.truth:truth:1.0.1"
}

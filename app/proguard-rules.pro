-allowaccessmodification

-overloadaggressively

-repackageclasses

-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkNotNullParameter(java.lang.Object, java.lang.String);
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

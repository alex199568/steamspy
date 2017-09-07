package md.ins8.steamspy

import android.content.Context

fun retrieveVersionName(context: Context): String =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
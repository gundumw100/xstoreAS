package com.base.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *	//首先需要在Application中初始化Once
 * Once.initialise(this);
 *	//然后即可使用，以下为几种典型场景用法
 * //每一个应用程序的升级前，说明文本只弹出一次
	if (!Once.beenDone(Once.THIS_APP_INSTALL, SHOW_FRESH_INSTALL_DIALOG)) {
        showDialog("This dialog should only appear once per app installation");
        Once.markDone(SHOW_FRESH_INSTALL_DIALOG);
    }
	//第一次进入App的一次性介绍说明
	if (!Once.beenDone(Once.THIS_APP_VERSION, SHOW_NEW_VERSION_DIALOG)) {
        showDialog("This dialog should only appear once per app version");
        Once.markDone(SHOW_NEW_VERSION_DIALOG);
    }
	//内容每小时,每分钟可执行一次
	if (!Once.beenDone(TimeUnit.MINUTES, 1, SHOW_MINUTE_DIALOG)) {
		showDialog("This dialog should only appear once per minute");
        Once.markDone(SHOW_MINUTE_DIALOG);
    }
	//每1秒可执行一次
	if (!Once.beenDone(1000L, SHOW_SECOND_DIALOG)) {
        showDialog("This dialog should only appear once per second");
        Once.markDone(SHOW_SECOND_DIALOG);
    }
    //如果有必要，可以取消Once
    Once.clearAll();
 * @author https://github.com/jonfinerty/Once
 *
 */
public class Once {

    public static final int THIS_APP_INSTALL = 0;
    public static final int THIS_APP_VERSION = 1;

    private static long lastAppUpdatedTime = -1;

    private static PersistedMap tagLastSeenMap;

    private Once() {
    }

    /**
     * This method needs to be called before Once can be used.
     * Typically it will be called from your Application class's onCreate method.
     *
     * @param context Application context
     */
    public static void initialise(Context context) {
        if (tagLastSeenMap == null) {
            tagLastSeenMap = new PersistedMap(context, "TagLastSeenMap");
        }

        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            lastAppUpdatedTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException ignored) {

        }
    }

    /**
     * Checks if a tag has been marked done within a given scope.
     *
     * @param scope The scope in which to check whether the tag has been done, either
     *              {@code THIS_APP_INSTALL} or {@code THIS_APP_VERSION}.
     * @param tag   A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked done within
     * the given {@code scope}.
     */
    public static boolean beenDone(@Scope int scope, String tag) {

        Long tagLastSeenDate = tagLastSeenMap.get(tag);

        if (tagLastSeenDate == null) {
            return false;
        }

        if (scope == THIS_APP_INSTALL) {
            return true;
        }

        return tagLastSeenDate > lastAppUpdatedTime;
    }

    /**
     * Checks if a tag has been marked done within a given time span (e.g. the last 5 minutes)
     *
     * @param timeUnit The units of time to work in.
     * @param amount   The quantity of timeUnit.
     * @param tag      A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked done
     * within the last provide time span.
     */
    public static boolean beenDone(TimeUnit timeUnit, long amount, String tag) {
        long timeInMillis = timeUnit.toMillis(amount);
        return beenDone(timeInMillis, tag);
    }

    /**
     * Checks if a tag has been marked done within a the last X milliseconds
     *
     * @param timeSpanInMillis How many milliseconds ago to check if a tag has been marked done
     *                         since.
     * @param tag              A string identifier unique to the operation.
     * @return {@code true} if the operation associated with {@code tag} has been marked done
     * within the last X milliseconds.
     */
    public static boolean beenDone(long timeSpanInMillis, String tag) {
        Long timeTagSeen = tagLastSeenMap.get(tag);

        if (timeTagSeen == null) {
            return false;
        }

        long sinceSinceCheckTime = new Date().getTime() - timeSpanInMillis;

        return timeTagSeen > sinceSinceCheckTime;
    }

    /**
     * Marks a tag (associated with some operation) as done. The tag is marked done at the time
     * of calling this method
     *
     * @param tag A string identifier unique to the operation.
     */
    public static void markDone(String tag) {
        tagLastSeenMap.put(tag, new Date().getTime());
    }

    /**
     * Clears a tag as done. All checks with {@code beenDone()} with that tag will return true until
     * it is marked done again.
     *
     * @param tag A string identifier unique to the operation.
     */
    public static void clearDone(String tag) {
        tagLastSeenMap.remove(tag);
    }

    /**
     * Clears all tags as done. All checks with {@code beenDone()} with any tag will return true
     * until they are marked done again.
     */
    public static void clearAll() {
        tagLastSeenMap.clear();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({THIS_APP_INSTALL, THIS_APP_VERSION})
    public @interface Scope {
    }

}

package com.swifty.maptrackerlib;


import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Swifty.Wang on 2015/8/4.
 */
public class Log {

    private Log() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final Logger logger = Logger.getLogger(Log.class.getName());
    public static boolean isDebug = true;
    private static final String TAG = "SwiftyDebug";

    public static void setIsDebug(boolean isDebug1) {
        isDebug = isDebug1;
    }

    private static void log(int type, String tag, String m, Throwable t) {
        if (t == null) {
            android.util.Log.println(type, tag, m);
        } else {
            android.util.Log.println(type, tag, m + '\n' + android.util.Log.getStackTraceString(t));
        }
    }

    private static void log(int type, String tag, Object m, Throwable t) {
        if (!isDebug) return;
        if (m == null) m = "null";
        try {
            if (m instanceof Throwable) {
                t = (Throwable) m;
                log(type, tag, m.toString(), t);
                return;
            }
            if (m instanceof Object[]) {
                log(type, tag, "***** Array log start *****", null);
                for (Object o : (Object[]) m) {
                    log(type, tag, o, t);
                }
                log(type, tag, "***** Array log end *****", null);
                return;
            }
            if (m instanceof Collection) {
                log(type, tag, "***** Collection log start *****", null);
                for (Object o : ((Collection) m)) {
                    log(type, tag, o, t);
                }
                log(type, tag, "*****  Collection log end  *****", null);
                return;
            }
            if (m instanceof Map) {
                log(type, tag, "***** Map log start *****", null);
                for (Object o : ((Map) m).entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    log(type, tag, pair.getKey() + " = " + pair.getValue(), t);
                    if (pair.getValue() instanceof Collection || pair.getValue() instanceof Map)
                        log(type, tag, pair.getValue(), t);
                }
                log(type, tag, "*****  Map log end  *****", null);
                return;
            }

            //unknown msg just case to String
            String msg = m.toString();
            if (msg.length() > 4000) {
                if (t == null) {
                    log(type, tag, "log_message.length = " + msg.length(), null);
                } else {
                    log(type, tag, "log_message.length = " + msg.length(), t);
                }
                int chunkCount = msg.length() / 4000;     // integer division
                for (int i = 0; i <= chunkCount; i++) {
                    int max = 4000 * (i + 1);
                    if (max >= msg.length()) {
                        log(type, tag, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i), null);
                    } else {
                        log(type, tag, "chunk " + i + " of " + chunkCount + ":" + msg.substring(4000 * i, max), null);
                    }
                }
            } else {
                log(type, tag, msg, t);
            }
        } catch (Exception e) {
            android.util.Log.w(TAG, e);
        }
    }

    public static void v(Object m) {
        v(TAG, m);
    }

    public static void v(String tag, Object m) {
        v(tag, m, null);
    }

    public static void v(String tag, Object m, Throwable t) {
        log(android.util.Log.VERBOSE, tag, m, t);
    }

    public static void d(Object m) {
        d(TAG, m);
    }

    public static void d(String tag, Object m) {
        d(tag, m, null);
    }

    public static void d(String tag, Object m, Throwable t) {
        log(android.util.Log.DEBUG, tag, m, t);
    }

    public static void i(Object m) {
        i(TAG, m);
    }

    public static void i(String tag, Object m) {
        i(tag, m, null);
    }

    public static void i(String tag, Object m, Throwable t) {
        log(android.util.Log.INFO, tag, m, t);
    }

    /**
     * Use it when in try block
     *
     * @param m
     */
    public static void w(Object m) {
        w(TAG, m);
    }

    public static void w(String tag, Object m) {
        w(tag, m, null);
    }

    public static void w(String tag, Object m, Throwable t) {
        log(android.util.Log.WARN, tag, m, t);
    }

    /**
     * Use it when in catch block or error happens
     *
     * @param m
     */
    public static void e(Object m) {
        e(TAG, m);
    }

    public static void e(String tag, Object m) {
        e(tag, m, null);
    }

    public static void e(String tag, Object m, Throwable t) {
        log(android.util.Log.ERROR, tag, m, t);
    }

    public static void wtf(Object m) {
        wtf(TAG, m);
    }

    public static void wtf(String tag, Object m) {
        wtf(tag, m, null);
    }

    public static void wtf(String tag, Object m, Throwable t) {
        log(android.util.Log.ASSERT, tag, m, t);
    }
}
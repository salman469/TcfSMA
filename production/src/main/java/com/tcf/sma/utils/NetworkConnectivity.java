package com.tcf.sma.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;

public class NetworkConnectivity {

    private static NetworkConnectivity instance = null;

    public static NetworkConnectivity getInstance() {
        if (instance == null)
            instance = new NetworkConnectivity();

        return instance;
    }

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
    public NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */
    public boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */
    public boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    public boolean isConnectedFast(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    public boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public String getNetworkOperatorName(Context context) {
        // Get carrier name (Network Operator Name)
        String carrierName = getTelephonyManager(context).getNetworkOperatorName();
        return carrierName;
    }

    public TelephonyManager getTelephonyManager(Context context) {
        // Get System TELEPHONY service reference
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tManager;
    }

    public String getNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);

        int type = -1;
        if (getNetworkInfo(context) != null)
            type = getNetworkInfo(context).getType();

        if (type == ConnectivityManager.TYPE_WIFI) {
            return "Wifi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            int networkType = 0;
            try {
                networkType = mTelephonyManager.getNetworkType();
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return "4G";
                case TelephonyManager.NETWORK_TYPE_NR:
                    return "5G";
                default:
                    return "Unknown";
            }
        }
        return "";
    }

    public String getDataSimOperator(Context context) {
        if (context == null) {
            return null;
        }

        int type = getNetworkInfo(context).getType();
        if (type == ConnectivityManager.TYPE_MOBILE) {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        int dataSubId = SubscriptionManager.getDefaultDataSubscriptionId();
                        TelephonyManager dataSimManager = tm.createForSubscriptionId(dataSubId);

                        return dataSimManager.getSimOperatorName();
                    } else {
                        String operator = getDataSimOperatorBeforeN(context);
                        if (operator != null) {
                            return operator;
                        } else {
                            return tm.getSimOperatorName();
                        }
                    }
                } else {
                    return tm.getSimOperatorName();
                }
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private String getDataSimOperatorBeforeN(Context context) {
        if (context == null) {
            return null;
        }

        int dataSubId = -1;
        try {
            Method getDefaultDataSubId = SubscriptionManager.class.getDeclaredMethod("getDefaultDataSubId");
            if (getDefaultDataSubId != null) {
                getDefaultDataSubId.setAccessible(true);
                dataSubId = (int) getDefaultDataSubId.invoke(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dataSubId != -1) {
            SubscriptionManager sm = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (sm != null && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                SubscriptionInfo si = sm.getActiveSubscriptionInfo(dataSubId);
                if (si != null) {
                    // format keep the same with android.telephony.TelephonyManager#getSimOperator
                    // MCC + MNC format
                    return String.valueOf(si.getMcc()) + si.getMnc();
                }
            }
        }
        return null;
    }

    public String getIMEINumber(Context context) {
        String IMEI = "";
        String IMEI2 = "";
        TelephonyManager tm = NetworkConnectivity.getInstance().getTelephonyManager(context);

        try {
            IMEI = tm.getDeviceId(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (IMEI.isEmpty()) {
            try {
                IMEI = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            IMEI2 = tm.getDeviceId(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return IMEI + "," + IMEI2;
    }
}

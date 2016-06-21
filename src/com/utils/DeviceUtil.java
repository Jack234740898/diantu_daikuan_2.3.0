package com.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.sxjs.diantu_daikuan.AppConfig;

import org.json.JSONObject;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by salah on 2015/1/12.
 */
public class DeviceUtil {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String DEVICE_RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv997lXP/EtWC5fw2GMK95kxDLr/qhLrFrcPW3TvOJHcqJ89ZBY+l5qaBZxkyr8/bxJMi8LArZQp/1mSpUl8MyizubnWADWwuwsgusWDg2NNEX4AAxx7AWgJAve6Wb33imPXNnPX9pEaVg3i1ras87GgieaVY8QLlHRdpFtAY4IwIDAQAB";
    public static final String DEVICE_RSA_PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAO/33uVc/8S1YLl/DYYwr3mTEMuv+qEusWtw9bdO84kdyonz1kFj6XmpoFnGTKvz9vEkyLwsCtlCn/WZKlSXwzKLO5udYANbC7CyC6xYODY00RfgADHHsBaAkC97pZvfeKY9c2c9f2kRpWDeLWtqzzsaCJ5pVjxAuUdF2kW0BjgjAgMBAAECgYEA2CVlUSWGgBF70Tm+3csGk7RDIaUeVIaxanxx5y4nMK9z2vSWXYn4KA0BETwLnar/GFAwu0XUc8OOP3M57L5SI3s7RFiONYZP7EXlOv1EmLpzdS+Hf6upWfvwIJHA07uJ3OkbEAxSjNv9SEXvCsCdihfy1DaVjGUoERYKwmOROJkCQQD6s4nPSKEXNC0TB6T4nQc+nobTrsgXLCp92fD2qjsz7ENy6PUwDLDMFTYLkpdeoM7hDFlw5AYBK8FGCkRC3xYNAkEA9QpCXkRa6Xr3YbAu9yU72Im6LW849Q+pJn5Zvu1hhvYbxOPjrmKNQglrnusokBFjyABpB0Ugq4serl/M2oyq7wJBAMU4IB104WVDooHp7Zm2zlAwnZhBUx8Hm5XNsYYafOBn6Neo89mM4jhqJ04LUBEdKCJaeLIab67UIQNcaD7DCLECQQDtC9KubMPh0BIWj6Cvd3aQgJP5tfnMoCzLTzm0iOFq9RRiAO9V2IK8Wm6hGu5viFMVvMfdv5LeQEQa/hQnbCNDAkEAyW4RVA7sKEv8Tl2rzIPPE3vWC43uYgIM1BzxXaU7fiNqmwsnt4tb/nF4Y6eHxosn/dqAKg0sueQusq9ZLhHTcA==";

   // public static final String DEVICE_RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDs8jefTV72lQfwCBBPAOVmGdVJGAbfVz14jip63/AH7O9MKqU/9JCXtgtySaoY+2809P4wI2YGujm8nubK9gLPg8Px3X+nwxz/ykGtyT//CE0Yx6v1Y9FpzgSObA86zb4YASH+bAxaL8ZC3ofDuEUr6SqkN+Jmqj+VNtUW38dwBQIDAQAB";
	//public static final String DEVICE_RSA_PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAO/33uVc/8S1YLl/DYYwr3mTEMuv+qEusWtw9bdO84kdyonz1kFj6XmpoFnGTKvz9vEkyLwsCtlCn/WZKlSXwzKLO5udYANbC7CyC6xYODY00RfgADHHsBaAkC97pZvfeKY9c2c9f2kRpWDeLWtqzzsaCJ5pVjxAuUdF2kW0BjgjAgMBAAECgYEA2CVlUSWGgBF70Tm+3csGk7RDIaUeVIaxanxx5y4nMK9z2vSWXYn4KA0BETwLnar/GFAwu0XUc8OOP3M57L5SI3s7RFiONYZP7EXlOv1EmLpzdS+Hf6upWfvwIJHA07uJ3OkbEAxSjNv9SEXvCsCdihfy1DaVjGUoERYKwmOROJkCQQD6s4nPSKEXNC0TB6T4nQc+nobTrsgXLCp92fD2qjsz7ENy6PUwDLDMFTYLkpdeoM7hDFlw5AYBK8FGCkRC3xYNAkEA9QpCXkRa6Xr3YbAu9yU72Im6LW849Q+pJn5Zvu1hhvYbxOPjrmKNQglrnusokBFjyABpB0Ugq4serl/M2oyq7wJBAMU4IB104WVDooHp7Zm2zlAwnZhBUx8Hm5XNsYYafOBn6Neo89mM4jhqJ04LUBEdKCJaeLIab67UIQNcaD7DCLECQQDtC9KubMPh0BIWj6Cvd3aQgJP5tfnMoCzLTzm0iOFq9RRiAO9V2IK8Wm6hGu5viFMVvMfdv5LeQEQa/hQnbCNDAkEAyW4RVA7sKEv8Tl2rzIPPE3vWC43uYgIM1BzxXaU7fiNqmwsnt4tb/nF4Y6eHxosn/dqAKg0sueQusq9ZLhHTcA==";

    public static String getEncryptDeviceInfo(Context context) {
        String result = Constants.DEFAULT_DEVICE_INFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("machineId", getMachineId(context));
            jsonObject.put("machineInfo", getMachineInfo(context));
            jsonObject.put("platform", Constants.PLATFORM);
            jsonObject.put("applePushToken", "");
            jsonObject.put("channel", Constants.CHANNEL);
            jsonObject.put("appId", AppConfig.appId);
            result = encryptData(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encryptData(String data) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(DEVICE_RSA_PUBLIC_KEY, Base64.NO_WRAP));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        return RSAUtil.encryptByPublicKey(data, publicKey);
    }

    public static String getMachineId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getMachineInfo(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Build.BRAND);
        stringBuilder.append("|");
        stringBuilder.append(Build.MODEL);
        stringBuilder.append("|");
        stringBuilder.append(Build.VERSION.RELEASE);
        stringBuilder.append("|");
        DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;  // 屏幕高度（像素）
        int width = displayMetrics.widthPixels;  // 屏幕宽度（像素）
        stringBuilder.append(height + "*" + width);
        return stringBuilder.toString();

    }

    //用于测试
   // public static final String DEVICE_RSA_PRIVATE_KEY = "";

    public static String decryptData(String data) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(DEVICE_RSA_PRIVATE_KEY, Base64.NO_WRAP));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        return RSAUtil.decryptByPrivateKey(data, privateKey);
    }
}

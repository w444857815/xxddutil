package com.dxhy.order.util;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

/**
 * @author ljh
 */
public class WXAppletUserInfo {
    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        JSONObject userInfo = getUserInfo("jMetqy4ScFFxtzx88wIUJIXCP8GIcSh19l7aIMb6wiZ6O/zmIUkMlj9HQd2NqvBGWEDVQsLJY95ttB6B5m3EKBpoVS9NbhTDt9F63usEmw/XyecDtX7FMLr8cqqfyl+DdIMC5LoDcBik4MIbohwfZTSIq8jfM7P4NrJJYm7eHBemgiL0m7A3Ui9x3IHzZqfJ2FMMjI0s2MR9xwIna5iUZPetcsr9vjnMoI9Hd/I79vsZBSyrVO6yGi+s/MhqJX5Tv65BR89YlwEwXyA8GNFfWKkfrJ+AEFeJbJF9sl7balWci3lJxgvUvmXoCgOYk/55z4+UB6hCQ8lRSDyrJ2JhpImAH/axG0sXYcc4rGYuRj/Z1rKRAMSOQOmXU7c+GqCAaDiYtuRZi1gAFs1ogQ9T5bpiQidPNkj6txtTUNXVrhPBzk30pIJi2TDSQGcQsTfiM/ZB2vyu+Vi1TeIVtt4TdzEKyn6umFFE/3oqV6r/b/A="
                , "6d0a66ba5b53664f03ab20918e96e4a4e73ca426", "EbQtJBZ3suFZJ5XXD3Pn8A==");
        System.out.println(userInfo);
    }
}
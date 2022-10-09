package com.lq.springdemo.utils;


import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/***
 *  Cipher不是线程安全的 下面地址
 * https://blog.csdn.net/qq_38536878/article/details/124243680
 *
 * 本测试类HOPoifHeadUtil  参考
 * https://www.cnblogs.com/nanfengxiangbei/p/14200269.html
 */

public class HOPoifHeadUtil {
    private static final Logger logger = LoggerFactory.getLogger(HOPoifHeadUtil.class);

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 2048;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;



    /*
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String encodeAES(String encodeRules, String content) {
        try {
//1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
//2.根据ecnodeRules规则初始化密钥生成器
//生成一个128位的随机源,根据传入的字节数组
//防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
//3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
//4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
//5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
//6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
//7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
//8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
//9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
//10.将加密后的数据转换为字符串
//这里用Base64Encoder中会找不到包
//解决办法：
//在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new BASE64Encoder().encode(byte_AES);
//11.将字符串返回
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            logger.error("encodeAES.NoSuchAlgorithmException异常" , e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error("encodeAES.NoSuchPaddingException异常" , e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("encodeAES.InvalidKeyException异常" , e.getMessage());
        } catch (IllegalBlockSizeException e) {
            logger.error("encodeAES.IllegalBlockSizeException异常" , e.getMessage());
        } catch (BadPaddingException e) {
            logger.error("encodeAES.BadPaddingException异常" , e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error("encodeAES.UnsupportedEncodingException异常" , e.getMessage());
        }

//如果有错就返加nulll
        return null;
    }

    /*
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String deCodeAES(String encodeRules, String content) {
        try {
//1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
//2.根据ecnodeRules规则初始化密钥生成器
//生成一个128位的随机源,根据传入的字节数组
//防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
//3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
//4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
//5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
//6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
//7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
//8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            logger.error("deCodeAES.NoSuchAlgorithmException异常" , e.getMessage());
        } catch (NoSuchPaddingException e) {
            logger.error("deCodeAES.NoSuchPaddingException异常" , e.getMessage());
        } catch (InvalidKeyException e) {
            logger.error("deCodeAES.InvalidKeyException异常" , e.getMessage());
        } catch (IOException e) {
            logger.error("deCodeAES.IOException异常" , e.getMessage());
        } catch (IllegalBlockSizeException e) {
            logger.error("deCodeAES.IllegalBlockSizeException" , e.getMessage());
        } catch (BadPaddingException e) {
            logger.error("deCodeAES.BadPaddingException异常" , e.getMessage());
        }

//如果有错就返加nulll
        return null;
    }


    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
// 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
// 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
// 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
// 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
// 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    public static void main(String[] args) {
// 获取6位随机码
         String randomCode =String.valueOf((int)(Math.random()*9+1)*100000);
         System.out.println("本次产生的随机数："+randomCode);
        try {
// 生成密钥对
            KeyPair keyPair = getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));

            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
// RSA加密
            String data = randomCode;
            String encryptData = encrypt(data, getPublicKey(publicKey));
            System.out.println("加密后内容:" + encryptData);
// RSA解密
            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
            System.out.println("解密后内容:" + decryptData);

            System.out.println("--------------------------加密过程：先RAS(非对称)将随机码加密，然后再用随机码（明文）AES(对称)加密-----------------------------------------");
            System.out.println("--------------------------解密过程（核心，要先拿到秘钥，即随机码）：先RAS(非对称)解密获取随机码，然后再用随机码AES(对称)获取内容-----------------------------------------");

            String jsonStr = "{\"switchStatus\":1,\"aaa\":\"120\",\"bbb\":\"500\"}";
            System.out.println("AES加密规则:" + decryptData + "\t加密后的字符串为:" + encodeAES(decryptData, jsonStr));
            String encodeAES = encodeAES(decryptData, jsonStr);
            System.out.println("AES解密规则:" + decryptData + "\t解密后的字符串为:" + deCodeAES(decryptData, encodeAES));

            String jsonStr2 = "{\"cccc\":\"2\"}";
            System.out.println("AES加密规则:" + decryptData + "\t加密后的字符串为:" + encodeAES(decryptData, jsonStr2));
            String encodeAES2 = encodeAES(decryptData, jsonStr2);
            System.out.println("AES解密规则:" + decryptData + "\t解密后的字符串为:" + deCodeAES(decryptData, encodeAES2));

//String lastData = deCodeAES(decryptData,encodeAES);
//PaymentLimitPO paymentLimitPO = JSON.parseObject(lastData, PaymentLimitPO.class);
//System.out.println("JSON解析内容:" + paymentLimitPO.getSwitchStatus());
        } catch (Exception e) {
            logger.error("加密解密异常",e.getMessage());
        }
    }
}

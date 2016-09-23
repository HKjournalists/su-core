package com.gettec.fsnip.fsn.sign;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * ASE 工具类，提供ASE加密和ASE解密
 * @author tangxin
 *
 */
public class AES {

	/**
     * ASE加密
     * 备注，由于当前的算法不兼容PHP，暂时注释
     * @param content 待加密内容
     * @param key 加密的密钥
     * @return
     */
   /* public static String encrypt(String content, String key) {
        try {
        	if(content == null || key == null) {
        		return null;
        	}
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(key.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteRresult = cipher.doFinal(byteContent);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteRresult.length; i++) {
                String hex = Integer.toHexString(byteRresult[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }*/
 
    /**
     * ASE解密
     * 备注，由于当前的算法不兼容PHP，暂时注释
     * @param content 待解密内容
     * @param key 解密的密钥
     * @return
     */
   /* public static String decrypt(String content, String key) {
    	if(content == null && key == null) {
    		return null;
    	}
        if (content.length() < 1) {
        	return null;
        }
        byte[] byteRresult = new byte[content.length() / 2];
        for (int i = 0; i < content.length() / 2; i++) {
            int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
            byteRresult[i] = (byte) (high * 16 + low);
        }
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(byteRresult);
            return new String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }*/

	 /**
	  * AES 使用 16 位 key 加密
	  * @param content 待加密内容
	  * @param key 16位key
	  * @return 返回加密后的内容
	  * @throws Exception
	  * @author tangxin 2016/7/2
	  */
	 public static String encrypt_16key(String content, String key) throws Exception {
		 if(content == null || "".equals(content)
				 || key == null || "".equals(key)) {
			 throw new Exception("AES 进行加密操作时，参数异常！");
		 }
		 if(key.length() != 16) {
			 throw new Exception("加密失败，密钥key必须是16位。");
		 }
		 try{
			 Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			 int blockSize = cipher.getBlockSize();
			 byte[] dataBytes = content.getBytes();
			 int plaintextLength = dataBytes.length;
			 if (plaintextLength % blockSize != 0) {
				 plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
			 }
			 byte[] plaintext = new byte[plaintextLength];
			 System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
			 SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			 IvParameterSpec ivspec = new IvParameterSpec(key.getBytes());
			 cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			 byte[] encrypted = cipher.doFinal(plaintext);
			 return new String(Base64.encodeBase64(encrypted), "utf-8");
		 }catch(Exception e){
			 throw new Exception("AES使用16key加密时出现异常，detailMassage：" + e.getMessage());
		 }
	 }
	
	/**
	 * AES 16位 key 解密
	 * @param content 待解密的内容
	 * @param key 16 位密钥
	 * @return 返回解密成功后的字符串
	 * @throws Exception
	 * @author tangxin 2016/7/2
	 */
	public static String desEncrypt_16key(String content, String key) throws Exception {
		if(content == null || "".equals(content) 
				|| key == null || "".equals(key)) {
			throw new Exception("AES 进行解密操作时，参数异常！");
		}
		if(key.length() != 16) {
			 throw new Exception("解密失败，密钥key必须是16位。");
		 }
		try{
			 byte[] encrypted1 = Base64.decodeBase64(content.getBytes("utf-8"));
			 Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			 SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			 IvParameterSpec ivspec = new IvParameterSpec(key.getBytes());
			 cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			 byte[] original = cipher.doFinal(encrypted1);
			 String originalString = new String(original);
			 return originalString;
		   }catch (Exception e) {
		        throw new Exception("在AES进行16位解密的时候，出现异常，Message："+e.getMessage());
		   }
	}
	
}

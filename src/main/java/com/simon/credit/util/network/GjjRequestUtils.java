package com.simon.credit.util.network;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.simon.credit.util.codec.Base64Utils;
import com.simon.credit.util.rsa.RSAUtils;

/**
 * 接口加解密流程
 */
@SuppressWarnings("unused")
public class GjjRequestUtils {

	private final static String TOUNA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKe7B1EftzOGIwxvCvV3MozibvrUXg4WdjgSdUokoYgDSu8q+TNOkgdv9guA9rQi4a15zUFOTHyE2ev695mPD7geguzlPlKXgWz3tbyeBjH2J/3CKebLMyFf/lfpwH92u+UzT6D+GaVvvt19C/kSmb1dywas3yRfY7oVay4FOlh1AgMBAAECgYBIjzkgGAdJy8pXs6XbAICmv8Hg0oQJth/YK+v/cg9K5pnBEycQ0fmMLBwVZ+nRjZFxWVK44KVn7JTVaBE6pc/XU/moKDS0Ljd1QtReTDxNU0M/Og2vQmhencrI+NZcty206caK61keAgB+wgRWJMOJ9LS6d09jFjKLcEn+lwba4QJBAN0irXgN4MnPMICb4TQ9QkGwiVrsxz9CkSLkFCV3LvjEheDeWXKl40beTg6XGzkph6PlekfyvJcNcqJzt8ZyhV0CQQDCLNvW79fEZiE/SsBmaVNAfATwOoUVmau26L04GQu+jTpSujJfBFH0vEERQq/zX9d0SjNVlVjJvW2gxS1CHhX5AkEA2j9EZ22DAooq+3NdI7ql7sE1/vgbebLQHVhvDb8AK+OBojqwmzdgBWyYK0w1QQhc54QvWF0YhhhuoCiHIyOZvQJARxlf4gLucbanw+IeMyGr3zfKIyB6bJ9ZNd9gsbLSDxLtYS9WlF4jmEZ6WNSfUrEsViN5SoQl/7DB/JVWbhhQGQJAeXZB2bkD3DKFlopiU9C1vDxzay79xfitEDQ05pI6kW0fSjYeolRvzQ/LoYPiD+0dSDVGASsfKOpR4NOLa/yepw==";
	private final static String TOUNA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnuwdRH7czhiMMbwr1dzKM4m761F4OFnY4EnVKJKGIA0rvKvkzTpIHb/YLgPa0IuGtec1BTkx8hNnr+veZjw+4HoLs5T5Sl4Fs97W8ngYx9if9winmyzMhX/5X6cB/drvlM0+g/hmlb77dfQv5Epm9XcsGrN8kX2O6FWsuBTpYdQIDAQAB";

	private final static String GJJ_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALPs+d8zDTcMekCb9IlI/bHdQChpGWfyl24H146kkjI8MTQwMCsnsxprmm9lxV2Up9maSicFFEToU0TKaLvrX4m01k38KnHTv8OYmE2s80nVmj0cF7iK1YOfAIo+0ttrsuKRlvjA0rOvxqBbRw8HBqX3yWWUaXqNRbWPlR4ks9bBAgMBAAECgYEAqhmRqUfUaIStV0OZjeipn2uyagHiePlJ3EYhrcM8S63IPrSH9WSU2Jd9627lhjEx9nD9RaJxpSuW/WP2XCKLsEX0BIfbiyEmwRS7mQ1tIxyecbFE39k4Ck+YnAY/tIBjfj2ExnVw1mmpGFeB2E7iYze0Zix5pJkYLmEc0a+K+AECQQDxAY2ZFd2cNGhYn5gEBIvWPW2tcMaL8MKxsIZzDs3sESXvlLH28qdybMGTScsR5QwK0PKMMVWdxcmOLU0niLyhAkEAvx6cMVU0esUNaIbaj6lZmFMj+H72CUXvrREpuP3tpmeoQB8iIUykJgQ5wvsTi8Zi9JU6YY77kInqMtvNRM/GIQJBAIFP8AXfsZSKmiJTH2Gofs9pDlwtUprrh8uHh9xcaSjs2on+Iq7569Z9fC/nPEpRt3BmRH3sSp/o7AiZTfiUaYECQQCz/oYnyBFG1rLMEMViq6Hf3d1wuVHKZf1WR2W9/hiFsfY5fZjNN7sYur4Trhc4erzTkH+NRfYSqHDTOUNXXT9BAkA6pSfBOyQ/3MVfrNSG/4JwYhK6hlxJvX8hubzkl5SPozeg2iIOxMNW1IkAzOVgro42QtJdHhX0hdldsgS8vmYn";
	private final static String GJJ_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz7PnfMw03DHpAm/SJSP2x3UAoaRln8pduB9eOpJIyPDE0MDArJ7Maa5pvZcVdlKfZmkonBRRE6FNEymi761+JtNZN/Cpx07/DmJhNrPNJ1Zo9HBe4itWDnwCKPtLba7LikZb4wNKzr8agW0cPBwal98lllGl6jUW1j5UeJLPWwQIDAQAB";

	/**
	 * 组装HTTP请求的入参
	 * @return
	 */
	public static JSONObject encodeParam(JSONObject paramJson) {
		// HTTP请求入参组装
		JSONObject data = new JSONObject();

		// 业务参数JSON字符串组装
		String source = paramJson.toString();
		try {
			// 加密部分demo
			// 从字符串中得到公钥
			PublicKey publicKey = RSAUtils.loadPublicKey(TOUNA_PUBLIC_KEY);// 投哪公钥加密

			byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);

			// 为了方便观察把加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
			String afterEncrypt = Base64Utils.encode(encryptByte);
			data.put("content", afterEncrypt);// 1.请求参数加密

			// 从字符串中得到私钥
			PrivateKey privateKey = RSAUtils.loadPrivateKey(GJJ_PRIVATE_KEY);// 51私钥加签
			// 加签
			String signStr = RSAUtils.rsaSign(encryptByte, privateKey);
			data.put("sign", signStr);// 2.请求参数签名
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * 解密、验签，获取接口业务入参
	 * @param url 请求地址
	 * @param data http请求入参
	 * @return
	 */
	public static JSONObject decodeParam(JSONObject data) {
		String afterencrypt = data.getString("content");
		String signStr = data.getString("sign");

		try {
			// 投哪公钥解签
			PublicKey publicKey = RSAUtils.loadPublicKey(TOUNA_PUBLIC_KEY);

			// 验签
			boolean result = RSAUtils.doCheck(Base64Utils.decode(afterencrypt), Base64Utils.decode(signStr), publicKey);
			if (!result) {
				System.out.println("验签失败");
				return null;
			}

			// 51私钥解密
			PrivateKey privateKey = RSAUtils.loadPrivateKey(GJJ_PRIVATE_KEY);

			// 因为RSA加密后的内容经Base64再加密转换了一下，所以先Base64解密回来再给RSA解密
			byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(afterencrypt), privateKey);
			return JSON.parseObject(new String(decryptByte));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONObject sendRequest(String url, JSONObject param) throws IOException {
		JSONObject requestParam = encodeParam(param);
		// TODO 此处值为示例，具体值由51公积金提供
		requestParam.put("product_cid", "3");

		// String response = HttpRequest.sendPost(url, StringUtils.join(requestParam.entrySet(), "&"));

		Map<String, String> params = 
			JSON.parseObject(requestParam.toJSONString(), new TypeReference<Map<String, String>>() {});
		String response = WJHttpUtils.doPost(url, params, 5000, 10000);

		// 获取响应入参 JSONObject
		JSONObject responseJson = JSON.parseObject(response);
		System.out.println("=== responseJson: " + responseJson);

		if (isLogicOk(responseJson)) {
			return decodeParam(responseJson);
		} else {
			return responseJson;
		}
	}

	private static boolean isLogicOk(JSONObject response) {
		return response.containsKey("content") && response.containsKey("sign");
	}

	public static JSONObject getResponse(JSONObject param) {
		return encodeParam(param);
	}

}

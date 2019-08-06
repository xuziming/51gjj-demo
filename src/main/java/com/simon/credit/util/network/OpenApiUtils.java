package com.simon.credit.util.network;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.codec.Base64;
import com.simon.credit.util.codec.Base64Utils;
import com.simon.credit.util.rsa.RSAUtils;

/**
 * 51公积金参数编解码器
 * @author XUZIMING 2019-03-31
 */
public class OpenApiUtils {

	// === test environment
//	private final static String TN_PRIVATE_KEY  = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKe7B1EftzOGIwxvCvV3MozibvrUXg4WdjgSdUokoYgDSu8q+TNOkgdv9guA9rQi4a15zUFOTHyE2ev695mPD7geguzlPlKXgWz3tbyeBjH2J/3CKebLMyFf/lfpwH92u+UzT6D+GaVvvt19C/kSmb1dywas3yRfY7oVay4FOlh1AgMBAAECgYBIjzkgGAdJy8pXs6XbAICmv8Hg0oQJth/YK+v/cg9K5pnBEycQ0fmMLBwVZ+nRjZFxWVK44KVn7JTVaBE6pc/XU/moKDS0Ljd1QtReTDxNU0M/Og2vQmhencrI+NZcty206caK61keAgB+wgRWJMOJ9LS6d09jFjKLcEn+lwba4QJBAN0irXgN4MnPMICb4TQ9QkGwiVrsxz9CkSLkFCV3LvjEheDeWXKl40beTg6XGzkph6PlekfyvJcNcqJzt8ZyhV0CQQDCLNvW79fEZiE/SsBmaVNAfATwOoUVmau26L04GQu+jTpSujJfBFH0vEERQq/zX9d0SjNVlVjJvW2gxS1CHhX5AkEA2j9EZ22DAooq+3NdI7ql7sE1/vgbebLQHVhvDb8AK+OBojqwmzdgBWyYK0w1QQhc54QvWF0YhhhuoCiHIyOZvQJARxlf4gLucbanw+IeMyGr3zfKIyB6bJ9ZNd9gsbLSDxLtYS9WlF4jmEZ6WNSfUrEsViN5SoQl/7DB/JVWbhhQGQJAeXZB2bkD3DKFlopiU9C1vDxzay79xfitEDQ05pI6kW0fSjYeolRvzQ/LoYPiD+0dSDVGASsfKOpR4NOLa/yepw==";
//	private final static String TN_PUBLIC_KEY   = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnuwdRH7czhiMMbwr1dzKM4m761F4OFnY4EnVKJKGIA0rvKvkzTpIHb/YLgPa0IuGtec1BTkx8hNnr+veZjw+4HoLs5T5Sl4Fs97W8ngYx9if9winmyzMhX/5X6cB/drvlM0+g/hmlb77dfQv5Epm9XcsGrN8kX2O6FWsuBTpYdQIDAQAB";
//	private final static String GJJ_PUBLIC_KEY  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqlfpBtY0eYAIDIa9CL3fgz5aD76PqRyNCCnwXHG9ItTzHcSxJvMjSqCynQCeqX+psqSbXq8vuEB9/rorbv/5mVUw4TlwWOUIU36ALKuM8P9L+8N2a0fsyVstsPXRimrxmu2UQrJ309a63vQCCAVyW/QyHP54prr3h8Hcfarc66ElVxpMRXSjMvpitsxUdw2prAL0eqw/fZvOQ1yoaw4/z0EbKvEG7SgJBn85pvrSmq/cz9tNfa00S9YaZH2JhXzvFC2mY+B35jdR9ExH1jpNiYPyCpmxrC4Djnbm0F7AH1GYl9NDePd7BOIjQsJ/HDO22kOuew021tMGPcmeUnKRhQIDAQAB";
//	private final static String GJJ_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCqV+kG1jR5gAgMhr0Ivd+DPloPvo+pHI0IKfBccb0i1PMdxLEm8yNKoLKdAJ6pf6mypJtery+4QH3+uitu//mZVTDhOXBY5QhTfoAsq4zw/0v7w3ZrR+zJWy2w9dGKavGa7ZRCsnfT1rre9AIIBXJb9DIc/nimuveHwdx9qtzroSVXGkxFdKMy+mK2zFR3DamsAvR6rD99m85DXKhrDj/PQRsq8QbtKAkGfzmm+tKar9zP2019rTRL1hpkfYmFfO8ULaZj4HfmN1H0TEfWOk2Jg/IKmbGsLgOOdubQXsAfUZiX00N493sE4iNCwn8cM7baQ657DTbW0wY9yZ5ScpGFAgMBAAECggEAAjjpYlxzDM69zCQJ3U5U1yp+FEyBJUypDl01tI8LT6QFk/LX+NEdOcEE84VpjH6M4TbCjCau8bFts2AMZwNR5000OQv9yjsas7Qnye1nBlHeVOeE6PtkPHBDFVbvp+WjR0koMQKRC6N2hqgaSq5/gnGaIcS0IEC6L/w4ey86eJx5IV+2+aO1WW8nT0VtSsvYu1AQLIx9Sz05TYbttuep41nWSs2Bgoijsi6GrjEdMiPMq9LIjJkg0i91PXpa7oZF+7DS+QlUORwxAzsRvfNZL612NtyIsnj3XtRutfDzI2l3A7rslmFZUbeMLD+3F/g6MCwa+2kMtQZxczeT1XWSAQKBgQDe7As8A1mii+W37Ftbtq6omplb60sAHgAKSmbekMxdcNSFeoc2Z9XL7tGH7qOiyPKsnOy2UARmMRDzAgbp1DoJrzf5YFVsIbrwENAXCyE7KJD419sv0zuUsfU1a7vdo59tbzONY4iVdDT/pd50/DjMvNbP0c10dA52KmlL3+AkBQKBgQDDnprJMNWKo++dyevc1skjc/tD8RcKKKkTsQf7Aaq91fkeB+ctBzkE2yFPTAOIq2pOjYTJEQMaXI7VEIt1z3DaoE00sNBuvhLzk9Du6oJsy1jOlaBl//jEjh5stxO9i/uyb5qGr1oHFHV7GmEXsWZbeUIB1nOF4azERpFpMS2vgQKBgGbjOywvv6cuOpBxHplt758R+8ZZCvy62/nj7Sa8XhaihPQ2YUHTGvJM/5hlpFD1vku9AeQDeoRQESqXvOkfcsoVfRg0RjjqOIWkvOZl1KihSIgR2LByJgWzPSGy37qkUmkH8htH3TpdsDhuLgaAgxa78clpZBJjnmkbmg3S/nihAoGAUTiqVfzTvSPCGsa7Cs+XZgQDALu6cDpfsVBg2uRfS0DrlX98ieKk1xJQ60bJdNaPZhw2VPGeUzS5DVg4P2spFW1XgRglAsYiwkXOi1SNYLSxo9zWY2N7m8jScxs0tA/KKtVR2Lg37hadjMZwJ9E9iZ94O6EaCC2ieUA/oeiMaoECgYB7vVhJWDCPUE95oeWqOwzyzHk1yg02xPh7KzwGvykMUOF/oZzpwm6vQ8Z6vpV9ulMJBwTLxSoeWY3N/fzT8XZkiL45GG866etpUSj/su5fSkxpcXms/avIZ6o91fRH2iwVCNyKtlwaYZS04QsSbj91yPRhFy8B7Lnli3ZIUEH/Xw==";

	// === product environment
	private final static String TN_PRIVATE_KEY  = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIYq1c1Ktp+5NrhU2ILHX/7FMa97kAI7xgqRLoKXxxNgT+mqm9WkN9AeuXe/LkoEsDjbbcQLKVGYb8ULwcKfw0u9A86zo17indFu3J+Xe6SgZC6wMCZ7cXLstD07Dwd+N9zNW7W8wUykTYXVvsVwYOmT3QNSV3brWgzN1G2Tz2dBAgMBAAECgYBcosLQZh/GFJnlmB+N3J85R2pQ4Z3DDQZmUWutm6JGjJ9glP3X+vTWbABe38/UJ3yNJWSakaJLxlaV1NJJ0QmEBU5FkP+pcn1e/hyKlSbtod1D6T1vXdwOPjr+lhEFWyyqY3bosdHqjUzNfxGHjBi2VcGcB+d7QdeQb9xCT3VUiQJBANWt8AYGuLwcZadC/BGNyMbvcPCkiIYmFdvnei/UGqLP5L9GGHgcFpYqslMjZpI40fkThDLmkBe/Wt+nsNsf9ycCQQCgvXEQOcyklUJNWFkHCPzOKJ2fUBjdY6UPkiJ6rErqtxANn97SmU5GCQ7BfUrDaBwMes1yimBUef5MSc3jQ+9XAkBtnbFIJiKWvaqYR5CwIKAjDapOiNlQmDEWgoT4DEMtyqB6Tyc2VpJvrq+UXtC7qlJtCEoFIqpAxHWFzqy3fIp5AkBnaVl7GeRpokk0n3kvSH30WDcIDqzgicHgoaCjEYNlO/8M9OSS0uSDtEIVA3l8+9bHxJ22agYNcyrCg9z8sHEjAkEAv5+E9VHcmLRuCpricK8tPHm/Um6nF/52mQZ6YhRResTyscblhDioVnzmg4te3o/XO2bib+zoPL9V6XfId9nZRw==";
	private final static String TN_PUBLIC_KEY   = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGKtXNSrafuTa4VNiCx1/+xTGve5ACO8YKkS6Cl8cTYE/pqpvVpDfQHrl3vy5KBLA4223ECylRmG/FC8HCn8NLvQPOs6Ne4p3Rbtyfl3ukoGQusDAme3Fy7LQ9Ow8HfjfczVu1vMFMpE2F1b7FcGDpk90DUld261oMzdRtk89nQQIDAQAB";
	private final static String GJJ_PUBLIC_KEY  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxIQP0kFsdwgGoCOcm6/sDpuIOlHcL0XGb2kUcafdzxqWT0W41KURE8SMqOgXFgcsa4NiuwwKhV4nnSC7h1eJjZuaFGOsSyNLPzV3nloxBu3wHYNeiFYA9MJ1X5uLbmSX2m31MORECu2/x6IDsas7TOkm/bkAvf3iUABvMfnyol6k5HB5Z2CXRew63go0oXaB6Aivgbw9F0hY3FhG8NDUsvwMp3iL/kSMDCkmsidFZMtn4NpNhlhb79uSeAeaXkZmZREKFm8amXbhJea3HkJ5tAjuI4tTvfHCSWd8zpV00YcivxAbez/TYxbXKTUHERFKiGuRjgEKs1qr8zmiG3YTrQIDAQAB";
	private final static String GJJ_PRIVATE_KEY = "";

	/**
	 * 组装HTTP请求的入参
	 * @return
	 */
	public static JSONObject tnEncodeParam(JSONObject paramJson) throws Exception {
		// HTTP请求入参组装
		JSONObject data = new JSONObject();
		// 业务参数JSON字符串组装
		String source = paramJson.toString();

		// 从字符串中得到公钥
		PublicKey publicKey = RSAUtils.loadPublicKey(GJJ_PUBLIC_KEY);// 51公钥加密

		// 加密数据
		byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);

		// 为了方便观察把加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
		String encryptText = new String(new Base64().encode(encryptByte));
		data.put("content", encryptText);// 1.参数加密

		// 从字符串中得到私钥
		PrivateKey privateKey = RSAUtils.loadPrivateKey(TN_PRIVATE_KEY);// 投哪私钥加签

		// 加签
		String sign = RSAUtils.rsaSign(encryptByte, privateKey);
		data.put("sign", sign);// 2.签名信息

		return data;
	}

	/**
	 * 解密、验签，获取接口业务入参
	 * @param url 请求地址
	 * @param data HTTP请求入参
	 * @return
	 */
	public static JSONObject tnDecodeParam(JSONObject data) throws Exception {
		String encryptText = data.getString("content");
		String sign = data.getString("sign");

		// 51公钥验签
		PublicKey publicKey = RSAUtils.loadPublicKey(GJJ_PUBLIC_KEY);
		boolean isOk = RSAUtils.doCheck(new Base64().decode(encryptText.getBytes()), new Base64().decode(sign.getBytes()), publicKey);
		if (!isOk) {
			throw new Exception("签名验证失败");
		}

		// 投哪私钥解密
		PrivateKey privateKey = RSAUtils.loadPrivateKey(TN_PRIVATE_KEY);

		// 因为RSA加密后的内容经Base64再加密转换了一下，所以先Base64解密回来再给RSA解密
		byte[] decryptByte = RSAUtils.decryptData(new Base64().decode(encryptText.getBytes()), privateKey);
		String decryptText = new String(decryptByte);

		return JSON.parseObject(decryptText);
	}

	/**
	 * 发送HTTP请求
	 * @param url 资源地址
	 * @param paramJSON
	 * @return
	 * @throws Exception
	 */
	public static JSONObject tnSendRequest(String url, JSONObject paramJSON) throws Exception {
		JSONObject requestParam = tnEncodeParam(paramJSON);
		// TODO 此处值为示例，具体值由51公积金提供
		requestParam.put("product_cid", "132");
		System.out.println("请求JSON：" + requestParam);
		String response = HttpUtils.jsonPost(url, requestParam.toString());
		// String response = "{\"sign\":\"BSqZJJDKr+7DSobhJYgkJ56uME2YEfGCVb6MjZ+aMSHYufnmYHsxeHfDfFLtJG2y8VsWzd3Ka1p/roRv+XxXORdDxT0i+nt7TTOHRjwMOo7B/wg2R0wykrA5FJPnLj9aNCCj3+rNDzjVlWV2eaLBmYNYGa2Kq1Ht7w+5StikDZI=\",\"content\":\"j5TJstXOsJbIoSEe2V1eIJpre2BN6W8TU1Kk79x5KDcsK1a62FtzKaXnhz8PyH7jAsSt5dYa3TMfGP+hppmxSHWaeHolOiLA7NFfBBJucgzUN1bAt2We4NFCgKH4Auhqlo2iu8+GtUOKxAHBZ1Nwp0maV6SWlPQTtZNVyslw8mj8cSsCzSRm3DHqmpPw+XHya0FPb6GZP/T5Jqcc9blh9JelcAmD2Y44yYx4u0+ETGjT56oCCyouMjoevFmN5Uz53NXqwRFCtLgeiANl+Te7bG00I3gSgXwI2nMmUB9w5LLKx7eBrwft4iD0mhfw3v0h9Y7R/+hV0KeYO9mUpTZWEQ==\"}";
		System.out.println("响应字符串：" + response);

		// 获取响应入参 JSONObject
		JSONObject responseJson = JSON.parseObject(response);
		System.out.println("响应JSON：" + responseJson);

		if (isLogicOk(responseJson)) {
			return tnDecodeParam(responseJson);
		} else {
			return responseJson;
		}
	}

	public static void main(String[] args) throws Exception {
		String response = "{\"sign\":\"BSqZJJDKr+7DSobhJYgkJ56uME2YEfGCVb6MjZ+aMSHYufnmYHsxeHfDfFLtJG2y8VsWzd3Ka1p/roRv+XxXORdDxT0i+nt7TTOHRjwMOo7B/wg2R0wykrA5FJPnLj9aNCCj3+rNDzjVlWV2eaLBmYNYGa2Kq1Ht7w+5StikDZI=\",\"content\":\"j5TJstXOsJbIoSEe2V1eIJpre2BN6W8TU1Kk79x5KDcsK1a62FtzKaXnhz8PyH7jAsSt5dYa3TMfGP+hppmxSHWaeHolOiLA7NFfBBJucgzUN1bAt2We4NFCgKH4Auhqlo2iu8+GtUOKxAHBZ1Nwp0maV6SWlPQTtZNVyslw8mj8cSsCzSRm3DHqmpPw+XHya0FPb6GZP/T5Jqcc9blh9JelcAmD2Y44yYx4u0+ETGjT56oCCyouMjoevFmN5Uz53NXqwRFCtLgeiANl+Te7bG00I3gSgXwI2nMmUB9w5LLKx7eBrwft4iD0mhfw3v0h9Y7R/+hV0KeYO9mUpTZWEQ==\"}";
		System.out.println("响应字符串：" + response);

		// 获取响应入参 JSONObject
		JSONObject responseJson = JSON.parseObject(response);
		System.out.println("响应JSON：" + responseJson);

		if (isLogicOk(responseJson)) {
			System.out.println(tnDecodeParam(responseJson));
		} else {
			System.out.println(responseJson);
		}
	}

	private static boolean isLogicOk(JSONObject response) {
		return response.containsKey("content") && response.containsKey("sign");
	}

	/**
	 * 获取加密、签名后的返回信息
	 * @param param 待返回的明文参数信息
	 * @return
	 */
	public static JSONObject tnEncodeResponse(JSONObject param) {
		try {
			return tnEncodeParam(param);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ==============================================================================

	/**
	 * 组装HTTP请求的入参
	 * @return
	 */
	public static JSONObject gjjEncodeParam(JSONObject paramJson) {
		// HTTP请求入参组装
		JSONObject data = new JSONObject();

		// 业务参数JSON字符串组装
		String source = paramJson.toString();
		try {
			// 加密部分demo
			// 从字符串中得到公钥
			PublicKey publicKey = RSAUtils.loadPublicKey(TN_PUBLIC_KEY);// 投$$哪公钥加密

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
	public static JSONObject gjjDecodeParam(JSONObject data) {
		String afterencrypt = data.getString("content");
		String signStr = data.getString("sign");

		try {
			// 投$$哪公钥解签
			PublicKey publicKey = RSAUtils.loadPublicKey(TN_PUBLIC_KEY);

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

	public static JSONObject gjjSendRequest(String url, JSONObject param) throws IOException {
		long start = System.currentTimeMillis();
		JSONObject requestParam = gjjEncodeParam(param);
		// TODO 此处值为示例，具体值由51公积金提供
		requestParam.put("product_cid", "132");
		requestParam.put("notEncrypt", "true");

		System.out.println("=== requestParam: " + requestParam.toString());
		String response = HttpUtils.jsonPost(url, requestParam.toString());
		// String response = TNHttpUtils.doPost(url, params, 5000, 10000);

		// 获取响应入参 JSONObject
		JSONObject responseJson = JSON.parseObject(response);
		System.out.println("=== responseJson: " + responseJson);

		JSONObject result = isLogicOk(responseJson) ? gjjDecodeParam(responseJson) : responseJson;
		long end = System.currentTimeMillis();
		System.out.println("=== waste time: " + (end - start));
		return result;
	}

	public static JSONObject gjjGetResponse(JSONObject param) {
		return gjjEncodeParam(param);
	}

}

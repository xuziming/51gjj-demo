package com.simon.credit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.codec.Base64Utils;
import com.simon.credit.util.lang.StringUtils;
import com.simon.credit.util.network.HttpUtils;
import com.simon.credit.util.network.OpenApiUtils;

/**
 * TN开放接口调用
 * @author XUZIMING 2019-03-30
 */
public class TNOpenApiInvoker {

	public static void main(String[] args) throws Exception {
		// 2.1 前置检测接口
		// testGjjCreditCheckUser();

		// 2.2 进件
		testGjjApply("17041356");

//		 testGjjBatchApply();

		// 2.6 还款试算(还款计划表)
		// testPreRepayPlan();

		// 2.8 查询还款明细
		// testQueryRepayDetail();

		// 2.9.2 获取合作方还款页面URL接口
		// testGetRepayUrl();

		// 2.14 获取绑卡URL
		// testGetBindUrl();

		// 车轮渠道黑名单检查
		// testBlacklistCheck();
	}

	/**
	 * 2.6 还款试算(还款计划表)
	 * @throws Exception
	 */
	public static void testPreRepayPlan() throws Exception {
		// {"amount":6400,"f":"pre_repay_plan","apply_id":"15268127","periods":"24"}
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"		, "pre_repay_plan");
		paramJson.put("amount"	, "6400"		  );
		paramJson.put("apply_id", "15268127"	  );
		paramJson.put("periods"	, "24"			  );

		// 2.1前置检测接口(开放平台必须配置开放服务method: gjj.credit.checkUser, 否则会报参数异常)
		String url  = "https://openapi.touna.cn:8080/gateway/gjj/gjj.credit.preRepayPlan";

		// 发送请求
		JSONObject response = requestWithoutEncrypt(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	/** 车轮渠道DES加解密密钥 */
	private static final String CHELUN_DES_KEY = "cJh4n6U3";

	/**
	 * 测试车轮渠道黑名单检查
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public static void testBlacklistCheck() throws Exception {
		// 业务参数
		Map<String, String> params = new HashMap<String, String>(16);

		params.put("from"  , "db"	 	  );
		params.put("time"  , String.valueOf(System.currentTimeMillis()));
		params.put("imei"  , "111111"	  );
		params.put("idfa"  , "222222"	  );
		params.put("mobile", "15070033333");

		Map<String, String> extInfo = new HashMap<String, String>(8);
		extInfo.put("sex", "男");

		params.put("ext_info", JSON.toJSONString(extInfo));
		System.out.println(JSON.toJSONString(params));

		String encryptText = encrypt(JSON.toJSONString(params).getBytes("UTF-8"), "cJh4n6U3");
		System.out.println("=== encryptText: " + encryptText);

		String decryptText = decrypt(encryptText, CHELUN_DES_KEY);
		System.out.println("=== decryptText: " + decryptText);

		// 车轮黑名单检测
		// String url  = "https://openapitest.touna.cn/gateway/chelun/touna.bigdata.risk.query";
		String url  = "https://openapi.touna.cn:8080/gateway/chelun/touna.bigdata.risk.query";
		String response = HttpUtils.jsonPost(url, encryptText);
		System.out.println(response);
	}

	private static String encrypt(byte[] data, String key) throws Exception {
		SecretKeyFactory sf = SecretKeyFactory.getInstance("DES");
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, sf.generateSecret(new DESKeySpec(key.getBytes("UTF-8"))));

		String afterEncrypt = Base64Utils.encode(cipher.doFinal(data));
		return afterEncrypt;
	}

	private static String decrypt(String data, String key) throws Exception {
		SecretKeyFactory sf = SecretKeyFactory.getInstance("DES");
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, sf.generateSecret(new DESKeySpec(key.getBytes("UTF-8"))));

		byte[] bytes = Base64Utils.decode(data);
		return new String(cipher.doFinal(bytes), "UTF-8");
	}

	/**
	 * 2.1前置检测接口
	 * <pre>
	 * 调用关系: 51公积金调用投哪开放服务
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGjjCreditCheckUser() throws IOException {
		JSONObject paramJson = new JSONObject();

		String name   = "李超凡";
		String idCard = "35018119960319173X";
		String phone  = "13754345678";

//		String name   = "张三";
//		String idCard = "420624199409284312";
//		String phone  = "13912345004";

//		String name   = "华明诚";
//		String idCard = "341103198209173712";
//		String phone  = "15812347788";

		paramJson.put("f"			, "check_user"						);
		paramJson.put("product_cid"	, "3"								);
		paramJson.put("ID"			, idCard							);
		paramJson.put("name"		, name								);
		paramJson.put("id_md5"		, DigestUtils.md5Hex(idCard)		);
		paramJson.put("phone_md5"	, DigestUtils.md5Hex(phone)			);
		paramJson.put("id_phone_md5", DigestUtils.md5Hex(idCard + phone));
		paramJson.put("phone_id_md5", DigestUtils.md5Hex(phone + idCard));

		// 2.1前置检测接口(开放平台必须配置开放服务method: gjj.credit.checkUser, 否则会报参数异常)
		String url  = "https://openapitest.to$$u$$na.cn/gateway/gjj/gjj.credit.checkUser";
		// String url  = "http://10.0.4.137:8443/gateway/gjj/gjj.credit.checkUser";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.checkUser";
		// String url  = "http://127.0.0.1:8080/gateway/gjj/gjj.credit.checkUser";

		// 发送请求
		JSONObject response = OpenApiUtils.gjjSendRequest(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	public static void testGjjBatchApply() throws IOException, InterruptedException {
		String[] applyIds = { "16911442" };

		for (String applyId : applyIds) {
			testGjjApply(applyId);
			Thread.sleep(2000);
		}
	}

	/**
	 * 2.2 进件
	 * <pre>
	 * 调用关系: 51公积金调用投哪开放服务
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGjjApply(String applyId) throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"		   , "apply"   );
//		paramJson.put("apply_id"   , "15727381");
		paramJson.put("apply_id"   , applyId);
		paramJson.put("product_cid", "132"	   );

		// 2.1前置检测接口(开放平台必须配置开放服务method: gjj.credit.apply, 否则会报参数异常)
		// String url  = "https://openapitest.to$$u$$na.cn/gateway/gjj/gjj.credit.apply";
		// String url  = "https://preopenapi.touna.cn:8080/gateway/gjj/gjj.credit.apply";
		String url  = "https://openapi.touna.cn:8080/gateway/gjj/gjj.credit.apply";
		// String url  = "http://10.0.4.137:8443/gateway/gjj/gjj.credit.apply";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.apply";
		// String url  = "http://127.0.0.1:8080/gateway/gjj/gjj.credit.apply";

		// 发送请求
		// JSONObject response = OpenApiUtils.gjjSendRequest(parseCorrectUrl(url), paramJson);
		System.out.println(parseCorrectUrl(url));
		System.out.println(paramJson);
		JSONObject response = requestWithoutEncrypt(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	public static void testQueryRepayDetail() throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"		, "query_repay_detail");
		paramJson.put("apply_id", "15160349"		  );

		// 2.1前置检测接口(开放平台必须配置开放服务method: gjj.credit.apply, 否则会报参数异常)
		// String url  = "https://openapitest.to$$u$$na.cn/gateway/gjj/gjj.credit.apply";
		String url  = "https://preopenapi.touna.cn:8080/gateway/gjj/gjj.credit.queryRepayDetail";

		// 发送请求
		// JSONObject response = OpenApiUtils.gjjSendRequest(parseCorrectUrl(url), paramJson);
		JSONObject response = requestWithoutEncrypt(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	/**
	 * 2.9.2获取合作方还款页面URL接口
	 * <pre>
	 * 调用关系: 51公积金调用投哪开放服务
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGetRepayUrl() throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "get_repay_url"		);
		paramJson.put("apply_id"	, "361706400"			);
		paramJson.put("loan_no"		, "3617064001555657613"	);
		paramJson.put("return_url"	, "https://test.jianbing.com/pt?apply_id=361706400&loan_no=3617064001555657613");

		// 2.12.9.2获取合作方还款页面URL接口(开放平台必须配置开放服务method: gjj.credit.getRepayUrl, 否则会报参数异常)
		String url  = "https://openapitest.to$$u$$na.cn/gateway/gjj/gjj.credit.get.url";
		// String url  = "http://10.0.4.137:8443/gateway/gjj/gjj.credit.getRepayUrl";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.getRepayUrl";
		// String url  = "http://127.0.0.1:8080/gateway/gjj/gjj.credit.getRepayUrl";

		// 发送请求
		JSONObject response = OpenApiUtils.gjjSendRequest(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	public static void testGetBindUrl() throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"		  , "get_bind_url");
		paramJson.put("apply_id"  , "361711065"	  );
		paramJson.put("from_tab"  , "loan"		  );
		paramJson.put("return_url", "https%3A%2F%2Ftest.jianbing.com%2Fbusiness%2Fstandard%2Fbind_notice%3Fapply_id%3D361711065%26product_cid%3D132%26from_tab%3Ddefault");

		// 2.12.9.2获取合作方还款页面URL接口(开放平台必须配置开放服务method: gjj.credit.getRepayUrl, 否则会报参数异常)
		String url  = "https://openapitest.to$$u$$na.cn/gateway/gjj/gjj.credit.get.url";
		// String url  = "http://10.0.4.137:8443/gateway/gjj/gjj.credit.getRepayUrl";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.getRepayUrl";
		// String url  = "http://127.0.0.1:8080/gateway/gjj/gjj.credit.getRepayUrl";

		// 发送请求
		JSONObject response = OpenApiUtils.gjjSendRequest(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	private static String parseCorrectUrl(String url) {
		return StringUtils.replace(url, "$$", "");
	}

	private static JSONObject requestWithoutEncrypt(String url, JSONObject params) {
		params.put("notEncrypt"	, "true");
		params.put("sign"		, "test123");
		params.put("content"	, "test123");

		// 发送请求
		String response = HttpUtils.jsonPost(url, params.toJSONString());
		// System.out.println("response: " + response);// 解密后的明文
		return JSON.parseObject(response);
	}

}

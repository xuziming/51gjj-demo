package com.simon.credit;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.lang.StringUtils;
import com.simon.credit.util.network.GjjRequestUtils;

/**
 * TN开放接口调用
 * @author XUZIMING 2019-03-30
 */
public class TNOpenApiInvoker {

	public static void main(String[] args) throws Exception {
		// 2.1 前置检测接口
		testGjjCreditCheckUser();

		// 2.9.2 获取合作方还款页面URL接口
		testGetRepayUrl();
	}

	/**
	 * 2.1前置检测接口
	 * <pre>
	 * 调用关系: 51公积金调用投哪开发服务
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGjjCreditCheckUser() throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "check_user"						);
		paramJson.put("product_cid"	, "3"								);
		paramJson.put("ID"			, "420624199409284312"			   	);
		paramJson.put("name"		, "张三"				   				);
		paramJson.put("id_md5"		, "9b5eaa1f623ed663c9e6969e2b1ed04f");
		paramJson.put("phone_md5"	, "75a8e965cfd8396de1d8636484d5835f");
		paramJson.put("id_phone_md5", "311cad615afcf62bfcaf540ae5cce8ad");
		paramJson.put("phone_id_md5", "46415fb12f05f96cf9e8956ceebb07b2");

		// 2.1前置检测接口(开放平台必须配置开放服务method: gjj.credit.checkUser, 否则会报参数异常)
		String url  = "https://openapitest.to$$u$$na.cn/gateway/gjj/gjj.credit.checkUser";
		// String url  = "http://10.0.4.137:8443/gateway/gjj/gjj.credit.checkUser";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.checkUser";
		// String url  = "http://127.0.0.1:8080/gateway/gjj/gjj.credit.checkUser";

		// 发送请求
		JSONObject response = GjjRequestUtils.sendRequest(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	/**
	 * 2.9.2获取合作方还款页面URL接口
	 * <pre>
	 * 调用关系: 51公积金调用投哪开发服务
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGetRepayUrl() throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "get_repay_url");
		paramJson.put("apply_id"	, 1111			 );
		paramJson.put("loan_no"		, "2222"		 );
		paramJson.put("return_url"	, "https://kaifa.jianbing.com/pt?apply_id=1111&loan_no=2222");

		// 2.12.9.2获取合作方还款页面URL接口(开放平台必须配置开放服务method: gjj.credit.getRepayUrl, 否则会报参数异常)
		String url  = "https://openapitest.to$$u$$na.cn/gateway/gjj/gjj.credit.getRepayUrl";
		// String url  = "http://10.0.4.137:8443/gateway/gjj/gjj.credit.getRepayUrl";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.getRepayUrl";
		// String url  = "http://127.0.0.1:8080/gateway/gjj/gjj.credit.getRepayUrl";

		// 发送请求
		JSONObject response = GjjRequestUtils.sendRequest(parseCorrectUrl(url), paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	private static String parseCorrectUrl(String url) {
		return StringUtils.replace(url, "$$", "");
	}

}

package com.simon.credit;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.lang.StringUtils;
import com.simon.credit.util.network.HttpRequest;
import com.simon.credit.util.network.TNRequestUtils;

/**
 * 51公积金开放接口调用
 * <p>包括：TN获取订单以及运营商信息、TN消息通知至51公积金</p>
 * @author XUZIMING 2019-03-30
 */
public class GJJOpenApiInvoker {

	public static void main(String[] args) throws Exception {
		// 3.1 获取订单额外信息接口
		// testGjjApiLoadApplyExtInfoMethod1();
		testGjjApiLoadApplyExtInfoMethod2();
	}

	/**
	 * 3.1 获取订单额外信息接口(投哪相关系统直接调用51公积金开发平台)
	 * <pre>
	 * 调用关系: TN系统调用51公积金接口
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	@Deprecated
	public static void testGjjApiLoadApplyExtInfoMethod1() throws Exception {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "load_apply_ext_info");
		paramJson.put("product_cid"	, "132");
		paramJson.put("apply_id"	, "361706398");
		paramJson.put("type"		, "GJJ");
		paramJson.put("time"		, "2019-04-12 10:00:00");

		// 3.1 获取订单额外信息接口
		String url  = "https://kaifa.jianbing.com/api/apiBusiness.php";

		// 发送请求
		JSONObject response = TNRequestUtils.tnSendRequest(url, paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	/**
	 * 3.1 获取订单额外信息接口(TN相关系统调用)
	 * <pre>
	 * 调用关系: TN系统调用51公积金接口
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGjjApiLoadApplyExtInfoMethod2() throws Exception {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "load_apply_ext_info");
		paramJson.put("product_cid"	, "132");
		paramJson.put("apply_id"	, "361706398");
		paramJson.put("type"		, "GJJ");
		paramJson.put("time"		, "2019-04-12 10:00:00");

		// 3.1 获取订单额外信息接口
		// String url  = "https://kaifa.jianbing.com/api/apiBusiness.php";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.loadApplyExtInfo";
		String url  = "https://openapitest.to$$un$$a.cn/gateway/gjj/gjj.credit.loadApplyExtInfo";

		// 发送请求
		String response = HttpRequest.sendPost(parseCorrectUrl(url), paramJson.toString());
		System.out.println("response: " + response);// 解密后的明文
	}

	private static String parseCorrectUrl(String url) {
		return StringUtils.replace(url, "$$", "");
	}

}

package com.simon.credit;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.network.GjjRequestUtils;

/**
 * 开放接口调用
 * @author XUZIMING 2019-03-30
 */
public class OpenApiInvoker {

	public static void main(String[] args) throws Exception {
		// 2.1前置检测接口
		// testGjjCreditCheckUser();

		// 3.1 获取订单额外信息接口
		testGjjApiLoadApplyExtInfo();
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
		// String url  = "http://10.0.4.137:8443/gateway/gjj/gjj.credit.checkUser";
		String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.checkUser";

		// 发送请求
		JSONObject response = GjjRequestUtils.sendRequest(url, paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

	/**
	 * 3.1 获取订单额外信息接口
	 * <pre>
	 * 调用关系: 投哪系统调用51公积金接口
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGjjApiLoadApplyExtInfo() throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "load_apply_ext_info");
		paramJson.put("product_cid"	, "3");
		paramJson.put("apply_id"	, "5E74977B-3101-5149-92F4-00B53606DE6D");
		paramJson.put("type"		, "GJJ");
		paramJson.put("time"		, "2018-04-04 12:00:00");

		// 3.1 获取订单额外信息接口
		String url  = "https://kaifa.jianbing.com/api/apiBusiness.php";

		// 发送请求
		JSONObject response = GjjRequestUtils.sendRequest(url, paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

}

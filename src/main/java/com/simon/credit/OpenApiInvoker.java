package com.simon.credit;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.network.GjjRequestUtils;

/**
 * 开放接口调用
 * @author XUZIMING 2019-03-30
 */
public class OpenApiInvoker {

	public static void main(String[] args) throws IOException {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "check_user"						);
		paramJson.put("product_cid"	, "22"								);
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

}
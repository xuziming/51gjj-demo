package com.simon.credit;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.simon.credit.util.network.TounaRequestUtils;

/**
 * 51公积金开放接口调用
 * <p>包括：TN获取订单以及运营商信息、TN消息通知至51公积金</p>
 * @author XUZIMING 2019-03-30
 */
public class GJJOpenApiInvoker {

	public static void main(String[] args) throws Exception {
		// 3.1 获取订单额外信息接口
		testGjjApiLoadApplyExtInfo();
	}

	/**
	 * 3.1 获取订单额外信息接口
	 * <pre>
	 * 调用关系: 投哪系统调用51公积金接口
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws IOException
	 */
	public static void testGjjApiLoadApplyExtInfo() throws Exception {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "load_apply_ext_info");
		paramJson.put("product_cid"	, 132);
		paramJson.put("apply_id"	, "5E74977B-3101-5149-92F4-00B53606DE6D");
		paramJson.put("type"		, "GJJ");
		paramJson.put("time"		, "2019-04-09 10:00:00");

		// 3.1 获取订单额外信息接口
		String url  = "https://kaifa.jianbing.com/api/apiBusiness.php";

		// 发送请求
		JSONObject response = TounaRequestUtils.tounaSendRequest(url, paramJson);
		System.out.println("response: " + response);// 解密后的明文
	}

}

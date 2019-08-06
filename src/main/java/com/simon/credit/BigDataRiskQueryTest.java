package com.simon.credit;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.simon.credit.util.network.HttpUtils;

/**
 * 大数据黑名单查询测试
 * @author XUZIMING 2019-05-16
 */
public class BigDataRiskQueryTest {

	public static void main(String[] argss) {
		String url = "http://10.0.4.149:8082/openApiDispatcher";
		// "openApiMethod":"touna.bigdata.risk.query","openApiVersion":"1.0"}

		// 业务参数
		Map<String, String> oparams = new HashMap<String, String>(16);
		oparams.put("application"	, "bigdata"					);
		oparams.put("openApiMethod" , "touna.bigdata.risk.query");
		oparams.put("openApiVersion", "1.0"						);

		// 业务参数
		Map<String, String> params = new HashMap<String, String>(16);

		params.put("from"  , "db"		  );
		params.put("time"  , String.valueOf(System.currentTimeMillis()));
		params.put("imei"  , "111111"	  );
		params.put("idfa"  , "222222"	  );
		params.put("mobile", "13067912192");

		Map<String, String> extInfo = new HashMap<String, String>(8);
		extInfo.put("sex", "男");

		params.put("ext_info", JSON.toJSONString(extInfo));

		oparams.put("openApiParams", JSON.toJSONString(params));

		System.out.println("=== params: " + JSON.toJSONString(params));

		String resp = HttpUtils.jsonPost(url, JSON.toJSONString(oparams));
		System.out.println(resp);
	}

}

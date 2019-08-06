package com.simon.credit;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.simon.credit.util.CommonUtils;
import com.simon.credit.util.JsonUtil;
import com.simon.credit.util.lang.StringUtils;
import com.simon.credit.util.network.HttpUtils;
import com.simon.credit.util.network.OpenApiUtils;

/**
 * 51公积金开放接口调用
 * <p>包括：TN获取订单以及运营商信息、TN消息通知至51公积金</p>
 * @author XUZIMING 2019-03-30
 */
public class GJJOpenApiInvoker {

	private static final ThreadLocal<String> TL = new ThreadLocal<String>() {
		protected String initialValue() {
			try {
				File templateFile = new File("f:/share/notify.txt");
				return FileUtils.readFileToString(templateFile, "UTF-8").trim();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		};
	};

	public static void main(String[] args) throws Exception {
		// 3.1 获取订单额外信息接口
		// testGjjApiLoadApplyExtInfoMethod1();
		// testGjjApiLoadApplyExtInfoMethod2("USER");
		String[] applyIds = { "16355579", "16338756", "16512297", "16782026",
				"16701860", "16742956", "16287702", "16469732", "16332604",
				"16555469", "16430947", "16439973", "16520468", "16541725",
				"16698827", "16607106", "16641349", "16666221", "16730150",
				"16804548", "16287779", "16596232", "16673277", "16445087",
				"16775914", "16345441", "16550105", "16374186", "16427536",
				"16547887", "16556111", "16385275", "16617475", "16870865",
				"16887077", "16841559", "16590328", "16830482", "16391035",
				"16172844", "16758484", "16051242", "16432338", "16420140",
				"16754208", "16565764", "16848024", "16312313", "16366236",
				"16314990", "16329479", "16319243", "16400747", "16461905",
				"16440239", "16538506", "16597051", "16629798", "16658605",
				"16704251", "16803998", "16888122", "16974685", "16381052",
				"16318242", "16525970", "16818255", "16506561", "16305423",
				"16540956", "16314997", "16738833", "16445145", "16801544",
				"16307306", "16889252", "16292323", "16286328", "16711649",
				"16791296", "16739373", "16313834", "16453833", "16333069",
				"16391449", "16434727", "16492656", "16553766", "16648176",
				"16726926", "16821089", "16888618" };
		for (String applyId : applyIds) {
			testNotifyGjj(applyId);
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		paramJson.put("apply_id"	, "15160349");
		paramJson.put("type"		, "USER");
		paramJson.put("time"		, "2019-05-10 17:34:00");

		// 3.1 获取订单额外信息接口
		String url  = "https://api.jianbing.com/api/apiBusiness.php";
//		String url  = "https://test.jianbing.com/api/apiBusiness.php";

		// 发送请求
		JSONObject response = OpenApiUtils.tnSendRequest(url, paramJson);
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
	public static void testGjjApiLoadApplyExtInfoMethod2(String type) throws Exception {
		JSONObject paramJson = new JSONObject();

		paramJson.put("f"			, "load_apply_ext_info");
		paramJson.put("product_cid"	, "132");
		paramJson.put("apply_id"	, "15160349");
//		paramJson.put("type"		, "GJJ");
//		paramJson.put("type"		, "ORDER");
//		paramJson.put("type"		, "USER");
//		paramJson.put("type"		, "IMAGE");
//		paramJson.put("type"		, "FACE_INFO");
//		paramJson.put("type"		, "SHEBAO");
//		paramJson.put("type"		, "HOLD_IMAGE");
//		paramJson.put("type"		, "FACE_INFO");
		paramJson.put("type"		, type);
		paramJson.put("time"		, "2019-04-12 10:00:00");

		// 3.1 获取订单额外信息接口
		// String url  = "https://kaifa.jianbing.com/api/apiBusiness.php";
		// String url  = "http://127.0.0.1:8443/gateway/gjj/gjj.credit.loadApplyExtInfo";
		String url  = "https://preopenapi.touna.cn:8080/gateway/gjj/gjj.credit.loadApplyExtInfo";
		// String url  = "https://openapitest.to$$un$$a.cn/gateway/gjj/gjj.credit.loadApplyExtInfo";

		// 发送请求
		String response = HttpUtils.jsonPost(parseCorrectUrl(url), paramJson.toString());
		System.out.println("response: " + response);// 解密后的明文
	}

	/**
	 * 51公积金消息通知
	 * <pre>
	 * 调用关系: TN系统调用51公积金接口
	 * 51公积金(煎饼网络)业务标准化对接文档地址: https://apizza.net/console/project/6addf1454ea049c2534318c469a3c85b/browse
	 * </pre>
	 * @throws Exception
	 */
	public static void testNotifyGjj(String applyId) throws Exception {
		// String msgTemplate = FileUtils.readFileToString(new File("f:/share/notify.txt"), "UTF-8").trim();
		String msgTemplate = TL.get();
		System.out.println("=== msgTemplate: " + msgTemplate);

		// 消息模板
		Map<String, Object> originMsg = JSON.parseObject(msgTemplate, new TypeReference<Map<String, Object>>() {});
		originMsg.put("notify_id", UUID.randomUUID().toString());
		originMsg.put("time", CommonUtils.formatDate(new Date()));
		originMsg.put("freezing_date", CommonUtils.formatDate(DateUtils.addDays(new Date(), 31)));
		originMsg.put("state_time", CommonUtils.formatDate(new Date()));
		originMsg.put("apply_id", applyId);

		// 字段命名转为下划线方式
        String attributes = JsonUtil.toJSONString(originMsg, PropertyNamingStrategy.SnakeCase);
		Map<String, Object> bizDataMap = JSON.parseObject(attributes, new TypeReference<Map<String, Object>>(){});

		JSONObject params = getBizParams(bizDataMap);
		System.out.println("params:" + params.toJSONString());

		// String url  = "https://test.jianbing.com/api/apiBusiness.php";
		String url  = "https://api.jianbing.com/api/apiBusiness.php";

		// 发送请求
		JSONObject response = OpenApiUtils.tnSendRequest(url, params);
		System.out.println("response: " + response);// 解密后的明文
	}

	private static String parseCorrectUrl(String url) {
		return StringUtils.replace(url, "$$", "");
	}

	/**
     * 获取接口业务参数
     * @param params
     * @return
     */
    private static JSONObject getBizParams(Map<String, Object> params) {
    	JSONObject bizParams = new JSONObject();
        bizParams.putAll(params);

        // 这行代码必须位于bizParams.remove(Constants.NOTIFY_TYPE);之前
        bizParams.put("f", parseNotifyMethod(params));

        bizParams.remove(Constants.APP_ID);
        bizParams.remove(Constants.NOTIFY_TYPE);
        bizParams.remove(Constants.VERSION);
        bizParams.remove(Constants.NOTIFY_ID);

        return bizParams;
    }

	/**
	 * 解析通知接口名称
	 * @param params 通知参数
	 */
	private static String parseNotifyMethod(Map<String, Object> params) {
		String notifyType = (String) params.get(Constants.NOTIFY_TYPE);
		System.out.println(notifyType);
		return humpToUnderline(notifyType.substring(8));
	}

	/***
	 * 驼峰命名转为下划线命名
	 * @param str 驼峰命名的字符串(如: setApplyState)
	 * return 下划线方式命名的字符串(如: set_apply_state)
	 */
	public static String humpToUnderline(String str) {
		return str.replaceAll("[A-Z]", "_$0").toLowerCase();
	}


}

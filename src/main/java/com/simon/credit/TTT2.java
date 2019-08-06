package com.simon.credit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TTT2 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		Map<String, String> map = new HashMap<String, String>(1024);

		List<String> lines = FileUtils.readLines(new File("e:/taobaoAuth.txt"));
		for (String line : lines) {
			JSONObject json = JSON.parseObject(line);
			String tcOrderId = json.getString("tcOrderId");
			String mobile = json.getString("mobile");
			map.put(tcOrderId, mobile);
		}
		// System.out.println(map.size());
		for (String tcOrderId : map.values()) {
			System.out.println(tcOrderId);
		}
	}

}

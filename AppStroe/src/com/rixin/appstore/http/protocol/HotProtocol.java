package com.rixin.appstore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * 排行页面访问数据并解析
 * 
 * @author 飘渺云轩
 * @date 2016-10-04
 */
public class HotProtocol extends BaseProtocol<ArrayList<String>> {

	@Override
	public String getKey() {
		return "hot";
	}

	@Override
	public String getParms() {
		return "";
	}

	@Override
	public ArrayList<String> parseData(String json) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				list.add(array.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}

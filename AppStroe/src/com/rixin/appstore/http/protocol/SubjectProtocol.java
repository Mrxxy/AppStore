package com.rixin.appstore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rixin.appstore.domain.SubjectInfo;

/**
 * 获取专题数据并解析
 * 
 * @author 飘渺云轩
 * @date 2016-10-02
 */
public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParms() {
		return "";
	}

	@Override
	public ArrayList<SubjectInfo> parseData(String json) {
		ArrayList<SubjectInfo> list = new ArrayList<SubjectInfo>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				SubjectInfo info = new SubjectInfo();
				JSONObject object = array.getJSONObject(i);
				info.des = object.getString("des");
				info.url = object.getString("url");

				list.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}

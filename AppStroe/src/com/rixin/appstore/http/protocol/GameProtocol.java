package com.rixin.appstore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rixin.appstore.domain.AppInfo;

/**
 * 获取游戏的数据并解析
 * 
 * @author 飘渺云轩
 * @date 2016-10-01
 */
public class GameProtocol extends BaseProtocol<ArrayList<AppInfo>> {

	@Override
	public String getKey() {
		return "game";
	}

	@Override
	public String getParms() {
		return "";
	}

	@Override
	public ArrayList<AppInfo> parseData(String json) {
		ArrayList<AppInfo> list = new ArrayList<AppInfo>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				AppInfo info = new AppInfo();
				JSONObject object = array.getJSONObject(i);
				info.id = object.getString("id");
				info.name = object.getString("name");
				info.packageName = object.getString("packageName");
				info.iconUrl = object.getString("iconUrl");
				info.stars = (float) object.getDouble("stars");
				info.size = object.getLong("size");
				info.downloadUrl = object.getString("downloadUrl");
				info.des = object.getString("des");
				list.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}

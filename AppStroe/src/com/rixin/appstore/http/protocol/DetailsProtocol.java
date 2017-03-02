package com.rixin.appstore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rixin.appstore.domain.AppInfo;
import com.rixin.appstore.domain.AppInfo.SafeInfo;

/**
 * 详情页访问网络数据并解析
 * 
 * @author 飘渺云轩
 * @date 2016-10-05
 */
public class DetailsProtocol extends BaseProtocol<AppInfo> {

	private String packageName;
	
	public DetailsProtocol(String packageName){
		this.packageName = packageName;
	}
	
	@Override
	public String getKey() {
		return "detail";
	}

	@Override
	public String getParms() {
		return "&packageName=" + packageName;
	}

	@Override
	public AppInfo parseData(String json) {
		AppInfo info = new AppInfo();
		try {
			JSONObject jsonObject = new JSONObject(json);
			
			info.id = jsonObject.getString("id");
			info.name = jsonObject.getString("name");
			info.packageName = jsonObject.getString("packageName");
			info.iconUrl = jsonObject.getString("iconUrl");
			info.stars = (float) jsonObject.getDouble("stars");
			info.size = jsonObject.getLong("size");
			info.downloadUrl = jsonObject.getString("downloadUrl");
			info.des = jsonObject.getString("des");
			info.author = jsonObject.getString("author");
			info.date = jsonObject.getString("date");
			info.downloadNum = jsonObject.getString("downloadNum");
			info.version = jsonObject.getString("version");
			
			JSONArray jsonArray1 = jsonObject.getJSONArray("safe");
			ArrayList<SafeInfo> safeList = new ArrayList<AppInfo.SafeInfo>();
			for (int i = 0; i < jsonArray1.length(); i++) {
				JSONObject object = jsonArray1.getJSONObject(i);
				SafeInfo safeInfo = new SafeInfo();
				safeInfo.safeDes = object.getString("safeDes");
				safeInfo.safeDesUrl = object.getString("safeDesUrl");
				safeInfo.safeUrl = object.getString("safeUrl");
				safeList.add(safeInfo);
			}
			info.safe = safeList;
			
			JSONArray jsonArray2 = jsonObject.getJSONArray("screen");
			ArrayList<String> screenList = new ArrayList<String>();
			for (int i = 0; i < jsonArray2.length(); i++) {
				String screen = jsonArray2.getString(i);
				screenList.add(screen);
			}
			info.screen = screenList;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}

}

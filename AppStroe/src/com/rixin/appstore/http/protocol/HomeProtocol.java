package com.rixin.appstore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rixin.appstore.domain.AppInfo;

/**
 * 获取首页数据并解析
 * 
 * @author 飘渺云轩
 * @date 2016-09-29
 */
public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {

	private ArrayList<AppInfo> list;
	private ArrayList<String> pictures;

	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParms() {
		return "";
	}

	@Override
	public ArrayList<AppInfo> parseData(String json) {
		try {
			JSONObject root = new JSONObject(json);
			
			//初始化应用列表的数据
			JSONArray array1 = root.getJSONArray("list");
			list = new ArrayList<AppInfo>();
			for (int i = 0; i < array1.length(); i++) {
				JSONObject object = array1.getJSONObject(i);
				
				AppInfo info = new AppInfo();
				info.des = object.getString("des");
				info.downloadUrl = object.getString("downloadUrl");
				info.iconUrl = object.getString("iconUrl");
				info.id = object.getString("id");
				info.name = object.getString("name");
				info.packageName = object.getString("packageName");
				info.size = object.getLong("size");
				info.stars = (float) object.getDouble("stars");
				
				list.add(info);
			}
			
			//初始化轮播条的数据
			JSONArray array2 = root.getJSONArray("picture");
			pictures = new ArrayList<String>();
			for (int i = 0; i < array2.length(); i++) {
				String pic = array2.getString(i);
				pictures.add(pic);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<String> getPictures(){
		return pictures;
	}

}

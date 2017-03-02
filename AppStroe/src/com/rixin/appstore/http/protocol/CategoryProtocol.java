package com.rixin.appstore.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rixin.appstore.domain.CategoryInfo;

/**
 * 分类模块请求网络数据并解析
 * 
 * @author 飘渺云轩
 * @date 2016-10-05
 */
public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {

	@Override
	public String getKey() {
		return "category";
	}

	@Override
	public String getParms() {
		return "";
	}

	@Override
	public ArrayList<CategoryInfo> parseData(String json) {
		ArrayList<CategoryInfo> list = new ArrayList<CategoryInfo>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				// 初始化标题对象
				if (jsonObject.has("title")) {
					CategoryInfo titleInfo = new CategoryInfo();
					titleInfo.title = jsonObject.getString("title");
					titleInfo.isTitle = true;
					list.add(titleInfo);
				}
				
				//初始化分类对象
				if(jsonObject.has("infos")){
					JSONArray array = jsonObject.getJSONArray("infos");
					for (int j = 0; j < array.length(); j++) {
						JSONObject object = array.getJSONObject(j);
						CategoryInfo info = new CategoryInfo();
						info.name1 = object.getString("name1");
						info.name2 = object.getString("name2");
						info.name3 = object.getString("name3");
						info.url1 = object.getString("url1");
						info.url2 = object.getString("url2");
						info.url3 = object.getString("url3");
						info.isTitle = false;
						list.add(info);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}

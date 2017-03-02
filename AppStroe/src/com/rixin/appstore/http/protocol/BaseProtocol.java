package com.rixin.appstore.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.rixin.appstore.http.HttpHelper;
import com.rixin.appstore.http.HttpHelper.HttpResult;
import com.rixin.appstore.ui.view.LoadingPage.ResultState;
import com.rixin.appstore.utils.IOUtils;
import com.rixin.appstore.utils.StringUtils;
import com.rixin.appstore.utils.UIUtils;

/**
 * 访问网络的基类
 * 
 * @author 飘渺云轩
 * @date 2016-09-26
 */
public abstract class BaseProtocol<T> {

	/**
	 * 获取数据
	 * 
	 * @param index
	 * @return
	 */
	public T getData(int index) {
		// 先判断是否有缓存，有就先读缓存
		String result = getCache(index);
		// 没有缓存或缓存失效
		if (StringUtils.isEmpty(result)) {
			// 请求服务器
			result = getDataFormServer(index);
		}

		// 开始解析数据
		if (result != null) {
			T data = parseData(result);
			return data;
		}

		return null;
	}

	/**
	 * 从网络获取数据
	 * 
	 * @param index
	 *            ：表示的是从哪个位置开始返回20条数据，用于分页
	 * @return
	 */
	private String getDataFormServer(int index) {
		HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey()
				+ "?index=" + index + getParms());
		if (httpResult != null) {
			String result = httpResult.getString();
			
			//写缓存
			if(!StringUtils.isEmpty(result)){
				setCache(index, result);
			}
			
			return result;
		}
		return null;
	}

	/**
	 * 写缓存，以url为文件名，以json为文件内容，保存在本应用的缓存目录中
	 * 
	 * @param index
	 * @param json
	 */
	public void setCache(int index, String json) {
		File cacheDir = UIUtils.getContext().getCacheDir(); // 获取缓存目录
		// 生成缓存文件
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index
				+ getParms());
		FileWriter writer = null;
		try {
			writer = new FileWriter(cacheFile);
			// 缓存失效的截止日期
			long deadLine = System.currentTimeMillis() + 30 * 60 * 1000;
			writer.write(deadLine + "\n"); // 在第一行写入缓存时间，并换行
			writer.write(json); // 写入json
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(writer);
		}
	}

	/**
	 * 获取缓存
	 * 
	 * @param index
	 * @return
	 */
	public String getCache(int index) {
		File cacheDir = UIUtils.getContext().getCacheDir(); // 获取缓存目录
		// 获得缓存文件
		File cacheFile = new File(cacheDir, getKey() + "?index=" + index
				+ getParms());
		if (cacheFile.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(cacheFile));
				String readLine = reader.readLine();  //读取第一行的deadLine
				long deadLine = Long.parseLong(readLine);

				if (System.currentTimeMillis() < deadLine) { // 缓存有效
					StringBuffer sb = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					return sb.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.close(reader);
			}
		}
		return null;
	}
	
	/**
	 * 对网络返回数据的合法性进行校验
	 * 
	 * @param object
	 * @return
	 */
	public ResultState check(Object object){
		if(object != null){
			if(object instanceof ArrayList){   //判断是否是集合
				
				ArrayList list = (ArrayList) object;
				if(list.isEmpty()){
					return ResultState.STATE_EMPTY;
				}else{
					return ResultState.STATE_SUCCESS;
				}
			}
		}
		return ResultState.STATE_ERROR;
	}

	/**
	 * 获取网络链接关键词，子类必须实现
	 * 
	 * @return
	 */
	public abstract String getKey();

	/**
	 * 获取网络链接参数，子类必须实现
	 * 
	 * @return
	 */
	public abstract String getParms();

	/**
	 * 解析数据，子类必须实现
	 * 
	 * @param json
	 * @return
	 */
	public abstract T parseData(String json);

}

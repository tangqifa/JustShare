package com.logan.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebImageBuilder {


	/**
	 * 通过图片url返回图片Bitmap
	 * @param url
	 * @return
	 */
	public static Bitmap returnBitMap(String path) {
		URL url = null;
		Bitmap bitmap = null;
		try {
			url = new URL(path);
			Log.v("WebImageBuilder:returnBitMap:url", url.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Log.v("WebImageBuilder:returnBitMap:conn", conn.toString());
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();	//得到网络返回的输入流
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}

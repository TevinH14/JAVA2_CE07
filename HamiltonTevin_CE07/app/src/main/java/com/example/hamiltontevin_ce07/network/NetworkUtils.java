package com.example.hamiltontevin_ce07.network;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtils{

	private Bitmap mBitmap;

	public static String getNetworkData(String _url) {

		if(_url == null){
			return null;
		}

		HttpURLConnection connection = null;
		String data = null;
		URL url;

		try {
			url = new URL(_url);

			connection = (HttpURLConnection)url.openConnection();

			connection.connect();

		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try{
			if(connection != null){
				is = connection.getInputStream();
				data = IOUtils.toString(is, "UTF_8");
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			if(connection != null){
				if(is != null){
					try{
						is.close();
					}catch(Exception e){
						e.printStackTrace();
					}
					connection.disconnect();

				}
			}
		}
		return data;
	}

	public static boolean checkUrlForImage(String urlImageString){
		boolean image = false;
		if(urlImageString.contains(".jpg")){
			return true;
		}
		return image;
	}


}
package com.paperton.kissmetrics;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class KISSmetrics {
	private String apiKey, identity = null;

	public KISSmetrics(String apiKey){
		this.apiKey = apiKey;
	}

	public void identify(String identity){
		this.identity = identity;
	}

	public void record(String name, HashMap<String, String> data) throws KISSmetricsException{
		if(identity == null){
			throw new KISSmetricsException("Identity-field was not set.");
		}

		data.put("_p", identity);
		data.put("_n", name);

		try {
			sendRequest(getUrlForAction("e", data));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new KISSmetricsException("Unsupported Encoding; UTF-8 not available?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new KISSmetricsException("General URL error, check internet connectivity and proxy settings.");

		}
	}

	public void set(HashMap<String, String> data) throws KISSmetricsException{
		if(identity == null){
			throw new KISSmetricsException("Identity-field was not set.");
		}

		data.put("_p", identity);

		try {
			sendRequest(getUrlForAction("s", data));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new KISSmetricsException("Unsupported Encoding; UTF-8 not available?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new KISSmetricsException("General URL error, check internet connectivity and proxy settings.");

		}
	}

	public void alias(String identity1 , String identity2) throws KISSmetricsException{
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("_p", identity1);
		data.put("_n", identity2);
		
		try {
			sendRequest(getUrlForAction("a", data));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new KISSmetricsException("Unsupported Encoding; UTF-8 not available?");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new KISSmetricsException("General URL error, check internet connectivity and proxy settings.");

		}
	}

	private void sendRequest(String urlForAction) throws IOException {
		URL url = new URL(urlForAction);
		
		URLConnection conn = url.openConnection();
		conn.setUseCaches(false);

	}

	private String getUrlForAction(String action, HashMap<String, String> data) throws UnsupportedEncodingException {
		//URLEncode!
		String sendData = "";

		String key;
		for (int i = 0; i < data.keySet().size(); i++){
			key = (String) data.keySet().toArray()[i];

			sendData += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(data.get(key), "UTF-8");

			if(i < data.keySet().size() - 1)
				sendData += "&";
		}
		String retVal = "http://trk.kissmetrics.com/" + action +"?_k=" + apiKey + "&" + sendData;
		return retVal;
	}



	//-----------------------
	//Inner classes
	//-----------------------
	public class KISSmetricsException extends Exception {

		public KISSmetricsException(String string) {
			super("KISSmetricsException: " +string);
		}


	}
}

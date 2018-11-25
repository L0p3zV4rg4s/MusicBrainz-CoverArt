package com.company;

import javax.swing.text.html.HTML;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CoverArt {
	private String cantante;
	private String cancion;
	private String HTML_original;
	
	private boolean full = false;
	
	public CoverArt() {
		cantante = "ELTON JOHN";
		cancion = "AQUI NO SE PUEDE VIVIR";
		parse(cantante, cancion);
		HTML_original = getURLSource("https://musicbrainz.org/release/b7931017-b24a-4c45-9c62-1173accf9d60");
		if (HTML_original != null) {
			full = true;
		}
	}
	
	private void parse(String cant, String canc) {
		cantante = cant.toLowerCase();
		cancion = canc.toLowerCase();
		
		String cantante_trim = cantante.replace(" ", "%20");
		
		System.out.println(cantante_trim);
	}
	
	private String getURLSource(String url) {
		URL urlObject = null;
		try {
			urlObject = new URL(url);
			URLConnection urlConnection = urlObject.openConnection();
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			return toString(urlConnection.getInputStream());
		} catch (MalformedURLException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	private String toString(InputStream inputStream) {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
			String inputLine;
			StringBuilder stringBuilder = new StringBuilder();
			while ((inputLine = bufferedReader.readLine()) != null) {
				stringBuilder.append(inputLine);
			}
			return stringBuilder.toString();
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	public boolean isFull() {
		return full;
	}
}
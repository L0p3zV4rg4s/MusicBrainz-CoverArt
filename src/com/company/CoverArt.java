package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *   It will be search the follow text on the web-site
 *
 *   <a class="artwork-image" href="//coverartarchive.org/release/ac36d9c9-1c56-4a60-81c3-81b13761a2f2/1619745496.jpg" title="">
 *   data-small-thumbnail="//coverartarchive.org/release/ac36d9c9-1c56-4a60-81c3-81b13761a2f2/1619745496-250.jpg" data-title
 */

public class CoverArt {
	private String AHREF = "<a href=\"/release/";
	private String MUSICBRAINZ = "https://musicbrainz.org/";
	private String IMAGEN_LARGE = "<a class=\"artwork-image\" href=";
	private String IMAGEN_SMALL = "data-small-thumbnail=";
	private String HTML_original = "";
	
	private String [] web = {
		"https://musicbrainz.org/taglookup?tag-lookup.artist=",
		"&tag-lookup.release=&tag-lookup.tracknum=&tag-lookup.track=",
		"&tag-lookup.duration=&tag-lookup.filename=&page="
	};
	
	public ArrayList<String> list_page = new ArrayList<>();
	
	public CoverArt() {
	
	}
	
	private String parse(String palabra) {
		//Parse the "blank space" on "%20"
		String resultado = palabra.toLowerCase().trim();
		resultado = resultado.replace(" ", "%20");
		return resultado;
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
	
	public void aHref(String cantante, String cancion, int page) {
		/**
		 * Add in ArrayList list_page the WebSite to search the CoverArt
		 */
		int begin = 0;
		int ende = 0;
		boolean found = true;
		
		HTML_original = getURLSource(web[0] + parse(cantante) + web[1] + parse(cancion) + web[2]);
		//A loop to search the flag AHREF
		for (int x = 1; x <= HTML_original.length() - 18; x++) {
			if (HTML_original.substring(x, x + 18).equals(AHREF)) {
				begin = x;
				ende = x;
				do {
					if (HTML_original.substring(ende - 2, ende).equals("\">")) {
						found = false;
					}
					ende++;
				} while (found);
				found = true;
				list_page.add(MUSICBRAINZ + HTML_original.substring(begin + 9, ende - 3));
			}
		}
	}
	
	//Search IMAGEN_BIG
	public PictureBigSmall coverBig() {
		/**
		 * Add into the object PictureBigSmall the value of the Big Picture and Small picture
		 */
		PictureBigSmall picLink = new PictureBigSmall(list_page.size()*2); //Per HTML-link will be 2 picture-link
		
		for (int i = 0; i < list_page.size(); i++) {
			HTML_original = getURLSource(list_page.get(i));
			int begin = 0;
			int ende = 0;
			for (int x = 1; x <= HTML_original.length() - 30; x++) {
				if (HTML_original.substring(x, x + 30).equals(IMAGEN_LARGE)) {
					begin = x;
					ende = (x + 33);
					//Find the last part of the link.
					ende = find_end(ende);
					//Set the link of the Big Picture into the Object PictureBigSmall
					picLink.setPicLink("http:/" + HTML_original.substring(begin + 32, ende - 2));
					//Set the link of the Big Picture into the Object PictureBigSmall
					picLink.setPicLink(coverSmall(begin));
				}
			}
		}
		return picLink;
	}
	
	//Search IMAGEN_SMALL
	private String coverSmall(int inicio) {
		/**
		 * Return back to coverBig() a String with the link of the Small Picture format
		 */
		int begin = 0; //= inicio;
		int ende = 0;
		String cadena = "";
		
		for (int z = inicio; z <= HTML_original.length()-21; z++) {
			if (HTML_original.substring(z,z+21).equals(IMAGEN_SMALL)) {
				begin = z;
				ende = (z+24);
				
				ende = find_end(ende);
				
				return "http:" + HTML_original.substring(begin+22, ende-2);
			}
		}
		return cadena;
	}
	
	private int find_end(int ende) {
		/**
		 * Find the last part of the link. In this case will be like:
		 * .................xxxxxxx.jpg
		 */
		boolean encontrado = true;
		do {
			if (HTML_original.substring(ende - 1, ende).equals("\"")) {
				encontrado = false;
			}
			ende++;
		} while (encontrado);
		return ende;
	}
	
	//	private void getPageTotalHTML(int i, int m, String text_find) {
	/**
	 * Find the total pages of MUSICBRAINZ
	 *
	 * Need to search for 2 Flags:
	 *      - <ul class="pagination">
	 *      - <span>…</span>
	 * If found, try to search the tag "> and get the number between, just like ">??????</a>
	 *
	 */
//		String PAGINATION_FLAG = "<ul class=\"pagination\">";
//		String SPAN_FLAG = "<span>…</span>";
//		int totalPages = 0;
//		int index = 0;
//		int max = 0;

//		if (HTML_original.substring(i, m).equals(SPAN_FLAG)) {
//			//Flag <span>...</span> found
//			boolean found = true;
//			for (int x = i; x <= HTML_original.length() - 8; x++) {
//				if (HTML_original.substring(x, x + 2).equals("\">")) {
//					//Flag "> found
//					index = x+2;
//					max = index;
//					do {
//						if (HTML_original.substring(max, max+1).equals("<")) {
//							found = false;
//						}
//						max++;
//					} while (found);
//					totalPages = Integer.valueOf(HTML_original.substring(index,max-1));
//					return;
//				}
//			}
//		} else {
//			for (int x = i; x <= HTML_original.length() - m; x++) {
//				if (HTML_original.substring(x, x + text_find.length()).equals(text_find)) {
//					//Flag <ul class="pagination"> found
//					index = x;
//					max = x + text_find.length();
//					System.out.println(index + " - " + max + " - " + HTML_original.substring(index, max));
//					getPageTotalHTML(index, (index+text_find.length()), SPAN_FLAG);
//					return;
//				}
//			}
//		}
//	}
}
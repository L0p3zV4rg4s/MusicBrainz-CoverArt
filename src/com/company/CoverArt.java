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
	private int coverFound = 0;
	private boolean verbose = false;
	private int textHelpingCount = 0;
	
	
	private String [] web = {
		"https://musicbrainz.org/taglookup?tag-lookup.artist=",
		"&tag-lookup.release=&tag-lookup.tracknum=&tag-lookup.track=",
		"&tag-lookup.duration=&tag-lookup.filename=&page="
	};
	
	public ArrayList<String> list_page = new ArrayList<>();
	
	public CoverArt() {
	
	}
	
	public CoverArt(boolean verbose) {
		this.verbose = verbose;
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
		if (verbose)
			getMessage("Downloading the source code from the Website.");
		HTML_original = getURLSource(web[0] + parse(cantante) + web[1] + parse(cancion) + web[2] + page);
		//A loop to search the flag AHREF
		if (verbose)
			getMessage("Searching for Tags on HTML.");
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
				textHelpingCount++;
			}
		}
		if (textHelpingCount > 1 && verbose)
			getMessage("Found " + textHelpingCount + " links with possible Cover Art.");
			
		if (textHelpingCount < 1) {
			getMessage("Something goes wrong.");
		}
	}
	
	private void getMessage(String text) {
		System.out.println(text);
	}
	
	//Search IMAGEN_BIG
	public PictureBigSmall coverBig() {
		if (textHelpingCount < 1) {
			getMessage("Check the page number.");
		} else {
			/**
			 * Add into the object PictureBigSmall the value of the Big Picture and Small picture
			 */
			boolean find; //Flag to know if i found the Cover Art, if not and on verbose mode, it show a message
			PictureBigSmall picLink = new PictureBigSmall(list_page.size() * 2); //Per HTML-link will be 2 picture-link
			
			for (int i = 0; i < list_page.size(); i++) {
				find = false;
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
						coverFound++;
						//Set the link of the Big Picture into the Object PictureBigSmall
						picLink.setPicLink(coverSmall(begin));
						find = true;
					}
				}
				if (find == false) {
					if (verbose)
						getMessage("Cover Art not found on: " + list_page.get(i));
					
					//ToDo: add here to find on amazon website. Only as a BIG Cover Art
					
				}
			}
			if (verbose)
				getMessage("All Cover Art (" + coverFound + ") saved.");
			return picLink;
		}
		
		getMessage("Page not found !!!");
		return null;
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
				coverFound++;
				return "http:" + HTML_original.substring(begin+22, ende-2);
			}
		}
		if (verbose)
			getMessage("Something goes wrong. No Small CoverArt found.");
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
}
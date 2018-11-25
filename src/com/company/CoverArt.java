package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CoverArt {
	private String AHREF = "<a href=\"/release/";
	private String MUSICBRAINZ = "https://musicbrainz.org/";
	private String IMAGEN_LARGE = "<a class=\"artwork-image\" href=";
	private String IMAGEN_SMALL = "data-small-thumbnail=";
	private String HTML_original = "";
	private String web = "";
	
	public CoverArt(String cantante, String cancion) {
		web = "https://musicbrainz.org/taglookup?tag-lookup.artist=" +
			             parse(cantante) + "&tag-lookup.release=&tag-lookup.tracknum=&tag-lookup.track=" +
			             parse(cancion) + "&tag-lookup.duration=&tag-lookup.filename=";
		HTML_original = getURLSource(web);
		aHref();
	}
	
	private String parse(String palabra) {
		String resultado = palabra.toLowerCase();
		resultado = resultado.replace(" ", "+");
//		System.out.println(cantante_trim);
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
	
	private void aHref() {
		List<String> lista = new ArrayList();
		
		int begin = 0;
		int ende = 0;
		boolean encontrado = true;
		
		//Buscar enlace
		for (int x = 1; x <= HTML_original.length()-18; x++) {
			if (HTML_original.substring(x,x+18).equals(AHREF)) {
				begin = x;
				ende = x;
				do {
					if (HTML_original.substring(ende-2, ende).equals("\">")) {
						encontrado = false;
					}
					ende++;
				} while (encontrado);
				encontrado = true;
				lista.add(MUSICBRAINZ + HTML_original.substring(begin+9, ende-3));
			}
		}
//		for(int i=0;i<lista.size();i++) {
//			seguro("aHref", lista.get(i));
//		}
		coverBig(lista);
	}
	
	//Buscar IMAGEN_SMALL
	private void coverBig(List<String> lista) {
		String big = "";
		String small = "";
		for (int i = 0; i < lista.size(); i++) {
			HTML_original = getURLSource(lista.get(i));

			
			int begin = 0;
			int ende = 0;
			//<a class="artwork-image" href="//coverartarchive.org/release/ac36d9c9-1c56-4a60-81c3-81b13761a2f2/1619745496.jpg" title="">
			// data-small-thumbnail="//coverartarchive.org/release/ac36d9c9-1c56-4a60-81c3-81b13761a2f2/1619745496-250.jpg" data-title
			for (int x = 1; x <= HTML_original.length() - 30; x++) {
				//System.out.println(HTML_original.substring(x, x+40));
				if (HTML_original.substring(x, x + 30).equals(IMAGEN_LARGE)) {
					begin = x;
					ende = (x + 33);
					
					ende = find_end(ende);

					System.out.println("http:/" + HTML_original.substring(begin + 32, ende - 2));
					
					small = coverSmall(begin);
				}
			}
		}
	}
	
	//Buscar IMAGEN_SMALL
	private String coverSmall(int inicio) {
		int begin = inicio;
		int ende = 0;
		String cadena = "";
		boolean encontrado = true;
		
		for (int z = inicio; z <= HTML_original.length()-21; z++) {
			if (HTML_original.substring(z,z+21).equals(IMAGEN_SMALL)) {
				begin = z;
				ende = (z+24);
				
				ende = find_end(ende);
				
				encontrado = true;
				System.out.println("http:/" + HTML_original.substring(begin+22, ende-2));
			}
		}
		
		return cadena;
	}
	
	private int find_end(int ende) {
		boolean encontrado = true;
		do {
			if (HTML_original.substring(ende - 1, ende).equals("\"")) {
				encontrado = false;
			}
			ende++;
		} while (encontrado);
		
		return ende;
	}
	
	//Enviar seguro(METODO, TEXTO)
	private void seguro(String metodo, String dato) {
		System.out.println("Metodo: " + metodo + ".\nDato: " + dato);
	}
	
}
package com.company;

public class PictureBigSmall {
	/**
	 * An object to save the link of CoverArt in BIG and SMALL format
	 */
	private String [] combo;
	
	private int count = 0;
	
	public PictureBigSmall(int totalLink) {
		combo = new String[totalLink];
	}
	
	public void setPicLink(String PicLink) {
		combo[count] = PicLink;
		count++;
	}
	
	public int getCount() {
		return (count - 1);
	}
	
	public String getPicLink(int numberLine) {
		if (numberLine < count) {
			return combo[numberLine];
		}
		return "No cover Art found";
	}
	
	public String getAllPictureHtml(String singer, String song) {
		/**
		 * Return the list on a HTML simple format
		 */
		String html = "";
		boolean go_on = true;
		int add = 0;
		html = "<HTML><HEAD>" +
				       "<style>body{background-color: #333333;}h1{color: white;text-align: center;}h2{color: lightgray;text-align: center;}</style>" +
				       "</HEAD><TITLE>" + singer + " - " + song +
				       "</TITLE><BODY><h1>Singer: " + singer + "</h1><p><h2>Song: " + song + "</h2></p><p></p>";
		
		do {
			html = html + "<a href=\"" + getPicLink(add) + "\"><img src=\"" + getPicLink(add+1) + "\" height=\"200\" width=\"200\"></a>";
			add += 2;
		} while (add <= getCount());
		
		html = html + "</body></html>";
		return html;
	}
}

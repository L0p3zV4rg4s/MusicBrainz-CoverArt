package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	    /**
	     * new CoverArt() - Silently search on Website
	     * new CoverArt(true) - Verbose while searching
	     */
	    String singer = "Madonna";
	    String song = "Papa dont't preach";
        CoverArt portadas = new CoverArt(true);
        
        portadas.aHref(singer, song, 1);
	
        PictureBigSmall myPicture =  portadas.coverBig();
	
	    /**
	     * Optional: save the String into a HTML file, and open it.
	     */
	    
	    //For prevent a null obj
	    if (myPicture != null)
	        System.out.println(myPicture.getAllPictureHtml(singer, song));
    }
}

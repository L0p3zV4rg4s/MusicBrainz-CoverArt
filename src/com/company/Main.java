package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
    	int page = 1;
        CoverArt portadas = new CoverArt();
        
        portadas.aHref("Michael Jackson", "Thriller", page);
	
        PictureBigSmall myPicture =  portadas.coverBig();
	
	    /**
	     * Test: get the String from getAllPicture()
	     *
	     * ToDo: save the String into a HTML file, and open it.
	     */
	    System.out.println(myPicture.getAllPictureHtml("Michael Jackson", "Thriller"));
    }
}

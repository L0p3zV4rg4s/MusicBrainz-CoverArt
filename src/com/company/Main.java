package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
    	int page = 1;
        CoverArt portadas = new CoverArt();
        
        //If page = 0, then will search in all pages from artist
        portadas.aHref("Michael Jackson", "Thriller", page);
	
        portadas.coverBig();
    }
}

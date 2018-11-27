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
	
	public int getSizeCombo() {
		return combo.length;
	}
}

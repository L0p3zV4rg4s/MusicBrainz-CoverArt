package com.company;

public class Main {

    public static void main(String[] args) {
        CoverArt cancion = new CoverArt();
        if (cancion.isFull()) { //Hay mas de 200 caracteres
            System.out.println("Esta lleno");
        }
    }
}

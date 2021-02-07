package com.theirongrizzly.primegen;

import java.util.ArrayList;

public class utils {
    public static boolean debug = false;

    public static void printDebug(String text){
        if(debug){
            System.out.println(text);
        }
    }

    public static String messyArrayToString(ArrayList<Integer> potentialPrime){
        int i = potentialPrime.size()-1;
        StringBuilder newPrimeString = new StringBuilder();
        while(i >= 0){
            newPrimeString.append(potentialPrime.get(i));
            i--;
        }
        return newPrimeString.toString();
    }
}

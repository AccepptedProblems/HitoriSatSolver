package com.example.model;

import java.util.HashMap;
import java.util.Map;

public class Converter {
    private Converter(){}

    public static Map<Character, Integer> charToInt() {
        Map<Character, Integer> converter = new HashMap<>();
        converter.put('0', 0);
        converter.put('1', 1);
        converter.put('2', 2);
        converter.put('3', 3);
        converter.put('4', 4);
        converter.put('5', 5);
        converter.put('6', 6);
        converter.put('7', 7);
        converter.put('8', 8);
        converter.put('9', 9);
        converter.put('a', 10);
        converter.put('b', 11);
        converter.put('c', 12);
        converter.put('d', 13);
        converter.put('e', 14);
        converter.put('f', 15);
        converter.put('g', 16);
        converter.put('h', 17);
        converter.put('i', 18);
        converter.put('k', 19);
        converter.put('l', 20);
        return converter;
    }
}

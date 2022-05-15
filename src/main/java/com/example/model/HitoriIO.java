package com.example.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.view.Game;

public class HitoriIO {

    private static final String RESOURCES_PATH = "src/main/resources/com/example/";

    public List<Game> getListGameFromResources() {
        File directory = new File(RESOURCES_PATH + "game");
        int fileCount = directory.list().length;

        List<Game> games = new ArrayList<>();

        for (int i = 0; i < fileCount; i++) 
            games.add(new Game(directory.list()[i], i+1));   
        
        return games;
    }

    public Puzzle loadPuzzleData(String filename) {
        int puzzleSize = 0;
        String filepath = RESOURCES_PATH + "game/" + filename;
        List<Integer> datas = new ArrayList<>();
        Map<Character, Integer> converter = Converter.charToInt();

        try (FileInputStream fis = new FileInputStream(filepath);) {
            int size = 0;
            int content;
            while((content = fis.read()) != -1) {
                if (content == 10 || content == 13) {
                    if (puzzleSize == 0) puzzleSize = size;
                    continue;
                }
                size++;
                datas.add(converter.get((char) content));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } 

        Puzzle puzzle = new Puzzle(puzzleSize);
        int count = 0;
        for (int r = 0; r < puzzleSize; r++) 
            for(int c = 0; c < puzzleSize; c++) 
                puzzle.setValueAt(r, c, datas.get(count++));
        
        return puzzle;
    }

}

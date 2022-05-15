package com.example.model;

public class Puzzle {
    private int size;
    private int[][] val;
    private boolean[][] result;
    

    public Puzzle(int size){
        this.size = size;
        this.val = new int[size][size];
        this.result = new boolean[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) 
                result[i][j] = false;
    }

    public int[][] getVal() {
        return val;
    }

    public void setVal(int[][] val) {
        this.val = val;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getValueAt(int row, int col) {
        return this.val[row][col];
    }

    public void setValueAt(int row, int col, int value) {
        this.val[row][col] = value;
    }

    public boolean isPaint(int row, int col){
        return this.result[row][col];
    }

    public void paintAt(int row, int col) {
        this.result[row][col] = true;
    }

    public boolean[][] getResult() {
        return result;
    }
}

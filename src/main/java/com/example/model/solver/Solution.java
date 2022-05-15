package com.example.model.solver;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private List <int[]> solutions;

    public Solution(){
        solutions = new ArrayList<>();
    }

    public void add(int... clause) {
        solutions.add(clause);
    }

    public List<int[]> getSolutions() {
        return solutions;
    }
}

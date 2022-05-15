package com.example.model.solver;

import java.util.List;

import com.example.model.Puzzle;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public abstract class BaseSolver {
    protected Puzzle puzzle;
    protected double timeCounter;
    protected int size;
    protected int[][] variable;
    protected int nbClauses;
    protected int nbVars;
    protected Solution solution;

    protected BaseSolver(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.timeCounter = 0;
        this.size = puzzle.getSize();
        this.solution = new Solution();
    }

    public double getTimeCounter() {
        return timeCounter;
    }

    public boolean[][] getResult() {
        return puzzle.getResult();
    }

    public int getNbClauses() {
        return nbClauses;
    }

    public int getNbVars() {
        return nbVars;
    }

    public Puzzle getPuzzle(){
        return this.puzzle;
    }

    public void encodeVar() {
        variable = new int[size][size];
        for (int i = 0; i < size; i++) 
            for (int j = 0; j < size; j++) 
                variable[i][j] = i*size + j + 1;
    }

    public void decode(int[] result) {
        for (int value: result) {
            int v = Math.abs(value);
            int row = (v-1) / size;
            int col = (v-1) % size;
            if (value == 0 || value > size*size || value < -size*size) break;
            if (value < 0) puzzle.paintAt(row, col);
        }
    }

    protected abstract void cnfRule1();
    protected abstract void cnfRule2();
    protected abstract void cnfRule3();

    public void solve(){
        long start = System.nanoTime();
        encodeVar();
        cnfRule1();
        cnfRule2();
        cnfRule3();
        satSolver();
        this.timeCounter = ((double)System.nanoTime() - start)/1000000;
        System.out.println(timeCounter);
    }

    private void satSolver(){
        List<int[]> solutions = solution.getSolutions();
        nbVars = size*size;
        nbClauses = solutions.size();

        ISolver solver = SolverFactory.newDefault();
        solver.newVar(nbVars);
        solver.setExpectedNumberOfClauses(nbClauses);
        solver.setTimeout(3600);

        for (int[] clause: solutions) {
            try {
                solver.addClause(new VecInt(clause));
            } catch(ContradictionException e) {
                e.printStackTrace();
            }
        }

        IProblem problem = solver;
        try {
            if(problem.isSatisfiable()) {
                int[] model = problem.model();
                decode(model);
                
            } else {
                System.out.println("Unsastifiable!!!!");
            }
        } catch(TimeoutException e) {
            e.printStackTrace();
        }
    }
}

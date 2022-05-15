package com.example.model.solver;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Puzzle;

public class ChainCycle extends BaseSolver {
    protected boolean[][] isAblePaint;

    public ChainCycle(Puzzle puzzle) {
        super(puzzle);
        isAblePaint = new boolean[size][size];
        for (int i = 0; i < size; i++) 
            for (int j = 0; j < size; j++) 
                isAblePaint[i][j] = false;
    }

    // Cnf Rule 1
    @Override
    protected void cnfRule1() {
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++)
                for(int k = j+1; k < size; k++) 
                    if(puzzle.getValueAt(i, j) == puzzle.getValueAt(i, k)){
                        solution.add(-variable[i][j], -variable[i][k]);
                        isAblePaint[i][j] = true;
                        isAblePaint[i][k] = true;
                    }

        for(int j = 0; j < size; j++) 
            for(int i = 0; i < size; i++) 
                for(int k = i+1; k < size; k++)
                    if(puzzle.getValueAt(i, j) == puzzle.getValueAt(k, j)){
                        solution.add(-variable[i][j], -variable[k][j]);
                        isAblePaint[i][j] = true;
                        isAblePaint[k][j] = true;
                    }
        
        // Loai nhung o khong can phai xoa 
        for(int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) 
                if (!isAblePaint[i][j])
                    solution.add(variable[i][j]);
    }

    // Cnf Rule 2:  Nhung o da bi to den khong the canh nhau 
    @Override
    protected void cnfRule2() {
        for (int i = 0; i < size; i++) 
            for(int j = 0; j < size; j++) {
                if (!isAblePaint[i][j]) continue;

                if (i > 0 && isAblePaint[i-1][j]) 
                    solution.add(variable[i][j] , variable[i-1][j]);

                if (j > 0 && isAblePaint[i][j-1])
                    solution.add(variable[i][j], variable[i][j-1]);
            }
    }

    @Override
    protected void cnfRule3() {
        List<Integer> cycle = new ArrayList<>();
        for (int i = 0; i < size; i++) 
            for(int j = 0; j < size; j++)
                findCycleTmp(i, j, cycle);
            

        List<Integer> chain = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            findChainTmp(i, 0, chain);
            findChainTmp(i, size-1, chain);
        }
        for (int j = 1; j < size; j++) {
            findChainTmp(0, j, chain);
            findChainTmp(size-1, j, chain);
        }
    }

    private boolean isInPuzzle(int x, int y) {
        return (x >= 0 && x < size && y >= 0 && y < size);
    }

    private void findCycleTmp(int x, int y, List<Integer> cycle) {
        if (!isAblePaint[x][y]) return;
        
        cycle.add(variable[x][y]);
        for(int a = -1; a <= 1; a += 2)
            for(int b = -1; b <= 1; b += 2)                            
                findCycle(x+a, y+b, cycle);

        cycle.remove(cycle.indexOf(variable[x][y]));
    }
    
    private void findCycle(int x, int y, List<Integer> cycle) {
        if ( !(cycle.size() != 1 || y > (cycle.get(0)-1)%size)
            || !isInPuzzle(x, y)
            || !isAblePaint[x][y]
            || variable[x][y] < cycle.get(0)
            || cycle.indexOf(variable[x][y]) >= 0
            ) return;

        int c = 0;
        int[] k = {-1, -1, -1};

        for (int dx = -1; dx <= 1; dx += 2)
            for (int dy = -1; dy <= 1; dy += 2) {
                int nx = x + dx;
                int ny = y + dy;
                if (isInPuzzle(nx, ny) && !cycle.get(cycle.size()-1).equals(variable[nx][ny])) 
                    k[c++] = cycle.indexOf(variable[nx][ny]);
            }
        
        for(int np: k) 
            if (np > 0) return;
        
        for (int np: k) 
            if(np == 0){
                int[] cls = new int[cycle.size()+1];
                for (int i = 0; i < cycle.size(); i++) 
                    cls[i] = cycle.get(i);
                cls[cycle.size()] = variable[x][y];
                solution.add(cls);
                return;
            }
        
        cycle.add(variable[x][y]);
        for (int dx = -1; dx <= 1; dx += 2)
            for (int dy = -1; dy <= 1; dy += 2)
                findCycle(x+dx, y+dy, cycle);
        cycle.remove(cycle.indexOf(variable[x][y]));
    }

    private void findChainTmp(int x, int y, List<Integer> chain) {
        if (!isAblePaint[x][y]) return;
        
        chain.add(variable[x][y]);
        for(int a = -1; a < 2; a=a+2)
            for(int b = -1; b < 2; b=b+2)                            
                findChain(x+a, y+b, chain);

        chain.remove(chain.indexOf(variable[x][y]));
    }

    private void findChain(int x, int y, List<Integer> chain) {
        if( !isInPuzzle(x, y)
        ||  !isAblePaint[x][y]
        || chain.indexOf(variable[x][y]) >= 0
        ) return;

        for (int dx = -1; dx <= 1; dx += 2)
            for (int dy = -1; dy <= 1; dy += 2){
                int nx = x + dx;
                int ny = y + dy;
                if(isInPuzzle(nx, ny) && !chain.get(chain.size()-1).equals(variable[nx][ny]) && chain.indexOf(variable[nx][ny]) >= 0) return;
            }
        
        if ((x == 0 || y == 0 || x == size-1 || y == size-1) && variable[x][y] > chain.get(0)){
            int[] cls = new int[chain.size()+1];
            for(int i = 0; i < chain.size(); i++)
                cls[i] = chain.get(i);
            cls[chain.size()] = variable[x][y];
            solution.add(cls);
            return;
        }

        chain.add(variable[x][y]);
        for (int dx = -1; dx <= 1; dx += 2)
            for (int dy = -1; dy <= 1; dy += 2)
                findChain(x+dx, y+dy, chain);
        chain.remove(chain.indexOf(variable[x][y]));

    }
}

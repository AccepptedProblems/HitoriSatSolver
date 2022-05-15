package com.example;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.model.HitoriIO;
import com.example.model.Puzzle;
import com.example.model.solver.ChainCycle;
import com.example.view.Game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HitoriController implements Initializable {

    @FXML
    VBox screen;

    @FXML
    SplitPane gameField;

    @FXML
    MenuButton gameMenu;
    
    @FXML
    Button solveButton;

    @FXML 
    StackPane gameScreen;

    @FXML
    VBox optionScreen;

    @FXML
    Label timeLabel;

    private HitoriIO hitoriIO = new HitoriIO();
    private Game currentGame = null;
    private Puzzle gamePuzzle;
    private int CELL_SIZE = 40;

    private ChainCycle solver;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setupUI();
    }

    private void setupUI() {
        VBox.setVgrow(gameField, Priority.ALWAYS);

        gameMenu.setText("Choose hitori game");
        gameMenu.getItems().clear();

        List<Game> listGame = hitoriIO.getListGameFromResources();
        listGame.forEach(game -> {
            MenuItem item = new MenuItem(game.getDisplayName());
            item.setOnAction(e -> {
                this.currentGame = game;
                this.initializeGame();
                this.gameMenu.setText(game.getDisplayName());
                this.timeLabel.setText("");
            });

            this.gameMenu.getItems().add(item);
        });

        solveButton.setOnAction(e -> {
            solver = new ChainCycle(gamePuzzle);
            solver.solve();
            gamePuzzle = solver.getPuzzle();
            String time = String.format("Time conputed: %.3fms", solver.getTimeCounter());
            timeLabel.setText(time);
            setupPuzzleGame();
        });

        optionScreen.setAlignment(Pos.CENTER);
        timeLabel.setText("");
    }

    private void initializeGame() {
        setupHitoriData();
        setupPuzzleGame();
    }

    private void setupHitoriData() {
        gamePuzzle = hitoriIO.loadPuzzleData(currentGame.getFileName());
    }

    private void setupPuzzleGame() {
        int size = gamePuzzle.getSize();
        GridPane puzzleGrid = new GridPane();
        puzzleGrid.setPrefSize(CELL_SIZE * size, CELL_SIZE * size);
        puzzleGrid.setMaxSize( CELL_SIZE * size, CELL_SIZE * size);
        puzzleGrid.setAlignment(Pos.CENTER);
        puzzleGrid.setPadding(new Insets(10, 10, 10, 10));

        for (int i = 0; i < size; i++) {
            ColumnConstraints columnConstraint = new ColumnConstraints();
            columnConstraint.setPercentWidth(CELL_SIZE);
            puzzleGrid.getColumnConstraints().add(columnConstraint);
        }

        for (int i = 0; i < size; i++) {
            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(CELL_SIZE);
            puzzleGrid.getRowConstraints().add(rowConstraint);
        }

        for(int col = 0; col < size; col++) 
            for(int row = 0; row < size; row++) {
                Pane cell= new Pane();
                if (gamePuzzle.isPaint(row, col))
                    cell.getStyleClass().add("cell-painted");
                else 
                    cell.getStyleClass().add("cell");

                cell.getStyleClass().add("text-field");

                Label label = createTextField(gamePuzzle.getValueAt(row, col));
                label.layoutXProperty().bind(cell.widthProperty().subtract(label.widthProperty()).divide(2));
                label.layoutYProperty().bind(cell.heightProperty().subtract(label.heightProperty()).divide(2));
                cell.getChildren().add(label);

                puzzleGrid.add(cell, col, row);
            }

        gameScreen.getChildren().clear();
        gameScreen.getChildren().add(puzzleGrid);
        StackPane.setAlignment(puzzleGrid, Pos.CENTER);
    }

    private Label createTextField(Integer value) {
        Label label = new Label();

        if (value == 0) return label;

        String text = "";
        text += value;
        label.setText(text);
        return label;
    }

    

    
}

package es.ies.puerto.controller;

import es.ies.puerto.PrincipalApplication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SudokuController {
    
    @FXML
    private Button btnBack; 

    private final int SIZE = 9;
    private int[][] board = new int[SIZE][SIZE];
    private int timeRemaining = 30;  // Tiempo en segundos 300 segundos (5 minutos)

    @FXML private Label timerLabel;

    private Timeline timer;

    @FXML private TextField cell_0_0, cell_0_1, cell_0_2, cell_0_3, cell_0_4, cell_0_5, cell_0_6, cell_0_7, cell_0_8;
    @FXML private TextField cell_1_0, cell_1_1, cell_1_2, cell_1_3, cell_1_4, cell_1_5, cell_1_6, cell_1_7, cell_1_8;
    @FXML private TextField cell_2_0, cell_2_1, cell_2_2, cell_2_3, cell_2_4, cell_2_5, cell_2_6, cell_2_7, cell_2_8;
    @FXML private TextField cell_3_0, cell_3_1, cell_3_2, cell_3_3, cell_3_4, cell_3_5, cell_3_6, cell_3_7, cell_3_8;
    @FXML private TextField cell_4_0, cell_4_1, cell_4_2, cell_4_3, cell_4_4, cell_4_5, cell_4_6, cell_4_7, cell_4_8;
    @FXML private TextField cell_5_0, cell_5_1, cell_5_2, cell_5_3, cell_5_4, cell_5_5, cell_5_6, cell_5_7, cell_5_8;
    @FXML private TextField cell_6_0, cell_6_1, cell_6_2, cell_6_3, cell_6_4, cell_6_5, cell_6_6, cell_6_7, cell_6_8;
    @FXML private TextField cell_7_0, cell_7_1, cell_7_2, cell_7_3, cell_7_4, cell_7_5, cell_7_6, cell_7_7, cell_7_8;
    @FXML private TextField cell_8_0, cell_8_1, cell_8_2, cell_8_3, cell_8_4, cell_8_5, cell_8_6, cell_8_7, cell_8_8;

    @FXML
    public void initialize() {
        int[][] completeBoard = {
            {5,3,4,6,7,8,9,1,2},
            {6,7,2,1,9,5,3,4,8},
            {1,9,8,3,4,2,5,6,7},
            {8,5,9,7,6,1,4,2,3},
            {4,2,6,8,5,3,7,9,1},
            {7,1,3,9,2,4,8,5,6},
            {9,6,1,5,3,7,2,8,4},
            {2,8,7,4,1,9,6,3,5},
            {3,4,5,2,8,6,1,7,9}
        };

        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(completeBoard[i], 0, board[i], 0, SIZE);
        }

        int cellsToRemove = 45;
        while (cellsToRemove > 0) {
            int row = (int)(Math.random() * SIZE);
            int col = (int)(Math.random() * SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                cellsToRemove--;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                TextField cell = getCell(i, j);
                if (board[i][j] != 0) {
                    cell.setText(String.valueOf(board[i][j]));
                    cell.setEditable(false);
                    cell.setStyle("-fx-background-color: #e0e0e0;");
                } else {
                    cell.setText("");
                    cell.setEditable(true);
                }
            }
        }

        timer = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> {
                timeRemaining--;
                updateTimerLabel();
                if (timeRemaining <= 0) {
                    gameOver();
                }
            })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerLabel() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void gameOver() {
        timer.stop(); // detener el temporizador
    
        javafx.application.Platform.runLater(() -> {
            // Mostrar alerta de Game Over
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("¡Se acabó el tiempo! El juego ha terminado.");
            alert.showAndWait();
    
            // Redirigir al perfil
            try {
                FXMLLoader loader = new FXMLLoader(PrincipalApplication.class.getResource("perfil.fxml"));
                Stage stage = (Stage) timerLabel.getScene().getWindow();
                Scene scene = new Scene(loader.load(), 600, 650);
                stage.setTitle("Pantalla Inicio");
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void comprobarSudoku() {
    for (int i = 0; i < SIZE; i++) {
        for (int j = 0; j < SIZE; j++) {
            TextField cell = getCell(i, j);
            String text = cell.getText();
            if (!text.matches("[1-9]")) {
                showAlert("Error", "Hay valores inválidos o vacíos.");
                return;
            }
            board[i][j] = Integer.parseInt(text);
        }
    }

    if (esValido(board)) {
        int puntuacion = timeRemaining * 5;
        showAlert("¡Correcto!", "¡Felicidades! Sudoku resuelto correctamente.\nTu puntuación es: " + puntuacion);
    } else {
        showAlert("Incorrecto", "El Sudoku no está resuelto correctamente.");
    }
}

    private boolean esValido(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            boolean[] row = new boolean[SIZE];
            boolean[] col = new boolean[SIZE];
            boolean[] box = new boolean[SIZE];
            for (int j = 0; j < SIZE; j++) {
                int r = board[i][j] - 1;
                int c = board[j][i] - 1;
                int b = board[3 * (i / 3) + j / 3][3 * (i % 3) + j % 3] - 1;
                if (row[r] || col[c] || box[b]) return false;
                row[r] = col[c] = box[b] = true;
            }
        }
        return true;
    }

    private void showAlert(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private TextField getCell(int row, int col) {
        try {
            return (TextField) getClass().getDeclaredField("cell_" + row + "_" + col).get(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void goBack() {
        try {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("perfil.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 650);
            stage.setTitle("Pantalla Inicio");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

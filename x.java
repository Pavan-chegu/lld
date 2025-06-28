package tic_tac_toe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Game{

    Board board;
    List<Player> players = new ArrayList<>();
    GameStatus gameStatus;

    public Game(Builder builder) {
        this.board = builder.board;
        this.players = builder.players;
        this.gameStatus = builder.gameStatus;
        this.board.game = this;
    }

    public static Builder getBuilder() {
        return new Builder();
    }
    static class Builder{
        Board board;
        List<Player> players = new ArrayList<>();
        GameStatus gameStatus;

        Builder setBoard(int size) {
            this.board = new Board(size);
            return this;
        }

        Builder addPlayer(Player p) {
            this.players.add(p);
            return this;
        }

        public boolean validate() {
            if (this.players.size() < 2) {
                return false;
            }
            return true;
        }

        Builder setGameStatus(GameStatus status) {
            this.gameStatus = status;
            return this;
        }
        Game build() {
            if (validate()) {
                return new Game(this);
            }
            throw new IllegalStateException("Invalid game");
        }
    }

}


class Board{
    Game  game;
    int size;
    List<List<Cell>> grid = new ArrayList<>();
    Board(int size){
        this.size = size;

        for(int i=0;i<size;i++){
            List<Cell> row = new ArrayList<>();
            for(int j=0;j<size;j++){
                Cell cell = new Cell();
                cell.x = i;
                cell.y = j;
                row.add(cell);
            }
            grid.add(row);
        }

//        List<Cell>  columns = Collections.nCopies(size, new Cell());
//        this.grid = Collections.nCopies(size, columns);
    }

    public boolean checkWinCondition() {
        // Check rows
        for (int i = 0; i < size; i++) {
            if (grid.get(i).get(0).Symbol != null &&
                    grid.get(i).get(0).Symbol == grid.get(i).get(1).Symbol &&
                    grid.get(i).get(1).Symbol == grid.get(i).get(2).Symbol) {
                game.gameStatus = GameStatus.WON;
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < size; j++) {
            if (grid.get(0).get(j).Symbol != null &&
                    grid.get(0).get(j).Symbol == grid.get(1).get(j).Symbol &&
                    grid.get(1).get(j).Symbol == grid.get(2).get(j).Symbol) {
                game.gameStatus = GameStatus.WON;
                return true;
            }
        }

        // Check diagonals
        if (grid.get(0).get(0).Symbol != null &&
                grid.get(0).get(0).Symbol == grid.get(1).get(1).Symbol &&
                grid.get(1).get(1).Symbol == grid.get(2).get(2).Symbol) {
            game.gameStatus = GameStatus.WON;
            return true;
        }

        if (grid.get(0).get(2).Symbol != null &&
                grid.get(0).get(2).Symbol == grid.get(1).get(1).Symbol &&
                grid.get(1).get(1).Symbol == grid.get(2).get(0).Symbol) {
            game.gameStatus = GameStatus.WON;
            return true;
        }

        // Check for draw
        boolean isDraw = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid.get(i).get(j).Symbol == null) {
                    isDraw = false;
                    break;
                }
            }
            if (!isDraw) break;
        }

        if (isDraw) {
            game.gameStatus = GameStatus.DRAW;
            return true;
        }

        return false;
    }
    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size &&
                grid.get(x).get(y).Symbol == null;
    }

    public void displayBoard() {
        System.out.println("\nCurrent Board:");
        System.out.println("  0   1   2");

        for (int i = 0; i < size; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < size; j++) {
                Cell cell = grid.get(i).get(j);
                String displaySymbol = (cell.Symbol == null) ? " " : cell.Symbol.toString();
                System.out.print(displaySymbol);

                if (j < size - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();

            // Print horizontal separator line (except after last row)
            if (i < size - 1) {
                System.out.println("  ---------");
            }
        }
        System.out.println();
    }


}

class Cell {
    int x;
    int y;
    symbol Symbol;
}

enum symbol {
    X, O;
}

interface Player {
    void play(Board board);

}

class human implements Player {
    String name;
    symbol Symbol;

    public human(String name, symbol Symbol){
        this.name = name;
        this.Symbol = Symbol;
    }
    @Override
    public void play(Board board) {
        Scanner sc = new Scanner(System.in);
        System.out.println(name + ", enter your move (x,y): ");
        boolean validMove = false;
        while (!validMove) {
            int x = sc.nextInt();
            int y = sc.nextInt();

            if (board.isValidMove(x, y)) {
                board.grid.get(x).get(y).Symbol = this.Symbol;
                validMove = true;
            } else {
                System.out.println("Invalid move! Try again: ");
            }
        }
        board.displayBoard();
        // Check win condition after move
        board.checkWinCondition();
    }

}



enum GameStatus {
    Start,DRAW, WON, LOST;
}



class test{
    public static void main(String[] args) {
        Player p1 = new human("pavan", symbol.X);
        Player p2 = new human("pavan2", symbol.O);
        Game game = Game.getBuilder().setBoard(3).addPlayer(p1).addPlayer(p2).setGameStatus(GameStatus.Start).build();
        System.out.println("Lets play Tic Tac Toe");
        game.board.displayBoard();
        while (game.gameStatus != GameStatus.WON &&
                game.gameStatus != GameStatus.DRAW) {
            for (Player player : game.players) {
                if (game.gameStatus == GameStatus.WON ||
                        game.gameStatus == GameStatus.DRAW) {
                    break;
                }
                player.play(game.board);
            }
        }
        System.out.println(game.gameStatus);
    }
}
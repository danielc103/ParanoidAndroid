package com.fsu.tictacnolebt;

public class TicTacToeCell {

    private final TicTacToeGame.CellPosition pos;
    private TicTacToeGame.Player owner;

    public TicTacToeCell(TicTacToeGame.CellPosition pos, TicTacToeGame.Player owner) {
        this.pos = pos;
        this.owner = owner;
    }

    public TicTacToeGame.CellPosition getPos() {
        return pos;
    }

    public void setOwner(TicTacToeGame.Player owner) {
        this.owner = owner;
    }

    public TicTacToeGame.Player getOwner() {
        return owner;
    }
}

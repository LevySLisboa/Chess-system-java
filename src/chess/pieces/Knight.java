package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }

    private boolean canMove(Position position){
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0,0);

        p.setValues(position.getRows()-1,position.getColumns()-2);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }



        p.setValues(position.getRows()-2,position.getColumns()-1);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }


        p.setValues(position.getRows()-2,position.getColumns()+1);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }


        p.setValues(position.getRows()-1,position.getColumns()+2);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }

        p.setValues(position.getRows()+1,position.getColumns()+2);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }

        p.setValues(position.getRows()+2,position.getColumns()+1);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }

        p.setValues(position.getRows()+2,position.getColumns()-1);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }

        p.setValues(position.getRows()+1,position.getColumns()-2);
        if(getBoard().positionExists(p)&&canMove(p)){
            mat[p.getRows()][p.getColumns()] = true;
        }

        return mat;
    }

    @Override
    public String toString() {
        return "N";
    }
}

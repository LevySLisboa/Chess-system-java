package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    private ChessMatch chessMatch;

    public King(Board board, Color color,ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "K";
    }

    private boolean canMove(Position position){
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        //Cima
        p.setValues(position.getRows() - 1, position.getColumns());

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //Abaixo
        p.setValues(position.getRows() + 1, position.getColumns());

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //Esquerda
        p.setValues(position.getRows(), position.getColumns() - 1);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //Direita
        p.setValues(position.getRows(), position.getColumns() + 1);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //nw
        p.setValues(position.getRows() - 1, position.getColumns() - 1);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //ne
        p.setValues(position.getRows() - 1, position.getColumns() + 1);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //sw
        p.setValues(position.getRows() + 1, position.getColumns() - 1);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //se
        p.setValues(position.getRows() + 1, position.getColumns() + 1);

        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRows()][p.getColumns()] = true;
        }

        //Castling
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            //Kingside
            Position posT1 = new Position(position.getRows(), position.getColumns() + 3);
            if (testRookCastling(posT1)) {
                Position p1 = new Position(position.getRows(), position.getColumns() + 1);
                Position p2 = new Position(position.getRows(), position.getColumns() + 2);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    mat[position.getRows()][position.getColumns() + 2] = true;
                }
            }
                //Queenside
            Position posT2 = new Position(position.getRows(), position.getColumns() - 4);
                if (testRookCastling(posT2)) {
                    Position p1 = new Position(position.getRows(), position.getColumns() - 1);
                    Position p2 = new Position(position.getRows(), position.getColumns() - 2);
                    Position p3 = new Position(position.getRows(), position.getColumns() - 3);
                    if (getBoard().piece(p1) == null && getBoard().piece(p2) == null&& getBoard().piece(p3) == null) {
                        mat[position.getRows()][position.getColumns() - 2] = true;
                    }
                }
            }
            return mat;
        }
        private boolean testRookCastling(Position position){
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p!=null && p instanceof Rook && p.getColor()==getColor() && p.getMoveCount()==0;
    }
}

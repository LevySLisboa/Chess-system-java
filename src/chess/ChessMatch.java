package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private Board board;
    private int turn;
    private Color currantPlayer;
    private boolean check;
    private boolean checkMate;

    private ChessPiece enPassantVulnerable;

    private List<Piece> piecesOnTheBoard= new ArrayList<>();
    private List<Piece> capturedPieces= new ArrayList<>();

    public ChessMatch() {
        board = new Board(8,8);
        turn =1;
        currantPlayer = Color.WHITE;
        initialSetup();
    }

    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for(int i=0;i< board.getRows();i++){
            for(int j=0;j<board.getColumns();j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);
            }
        }
        return mat;
    }

    public int getTurn() {
        return turn;
    }
    public Color getCurrantPlayer() {
        return currantPlayer;
    }
    public boolean getCheck() {
        return check;
    }
    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public boolean [][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition,ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currantPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em check");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        check = testCheck(oppenent(currantPlayer));

        if (testCheckMate(oppenent(currantPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        //En passant
        if(movedPiece instanceof Pawn && (target.getRows()== source.getRows()-2 ||target.getRows()== source.getRows()+2)){
            enPassantVulnerable = movedPiece;
        }else{
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target){
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p,target);

        if(capturedPiece != null){
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        //Castling Kingside Rook
        if(p instanceof King && target.getColumns()==source.getColumns()+2){
            Position sourceT = new Position(source.getRows(), source.getColumns()+3);
            Position targetT = new Position(source.getRows(), source.getColumns()+1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook,targetT);
            rook.increaseMoveCount();
        }
        //Castling Queenside Rook
        if(p instanceof King && target.getColumns()==source.getColumns()-2){
            Position sourceT = new Position(source.getRows(), source.getColumns()-4);
            Position targetT = new Position(source.getRows(), source.getColumns()-1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook,targetT);
            rook.increaseMoveCount();
        }

        //En passant
        if(p instanceof Pawn){
            if(source.getColumns()!=target.getColumns() && capturedPiece == null){
                Position pawnPosition;
                if(p.getColor()==Color.WHITE){
                    pawnPosition = new Position(target.getRows()+1, target.getColumns());
                }else{
                    pawnPosition = new Position(target.getRows()-1, target.getColumns());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }
        return capturedPiece;
    }

    private void undoMove(Position source,Position target,Piece capturedPiece){
        ChessPiece p = (ChessPiece)board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p,source);

        if(capturedPiece!=null){
            board.placePiece(capturedPiece,target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        //Castling Kingside Rook
        if(p instanceof King && target.getColumns()==source.getColumns()+2){
            Position sourceT = new Position(source.getRows(), source.getColumns()+ 3);
            Position targetT = new Position(source.getRows(), source.getColumns()+ 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook,sourceT);
            rook.decreaseMoveCount();
        }
        //Castling Queenside Rook
        if(p instanceof King && target.getColumns()==source.getColumns()-2){
            Position sourceT = new Position(source.getRows(), source.getColumns()-4);
            Position targetT = new Position(source.getRows(), source.getColumns()-1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook,sourceT);
            rook.increaseMoveCount();
        }

        //En passant
        if(p instanceof Pawn){
            if(source.getColumns()!=target.getColumns() && capturedPiece == enPassantVulnerable){
                ChessPiece pawn = (ChessPiece)board.removePiece(target);
                Position pawnPosition;
                if(p.getColor()==Color.WHITE){
                    pawnPosition = new Position(3, target.getColumns());
                }else{
                    pawnPosition = new Position(4, target.getColumns());
                }
                board.placePiece(pawn,pawnPosition);
            }
        }
    }

    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("Não existe peça na posição de origem");
        }
        if(currantPlayer!= ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("A peça escolhida não é sua");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("Não possuem movimentos possíveis para esta peça");
        }
    }

    private void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("A peça escolhida não pode ir para a posição de destino");
        }
    }

    private Color oppenent(Color color){
        return (color == Color.WHITE) ? Color.BLACK: Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Não existe o Rei "+color+" no tabuleiro");
    }

    private boolean testCheck(Color color){
        Position kingPositon = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x->((ChessPiece)x).getColor()== oppenent(color) ).collect(Collectors.toList());
        for (Piece p:opponentPieces){
            boolean [][] mat = p.possibleMoves();
            if(mat[kingPositon.getRows()][kingPositon.getColumns()]){
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color){
        if(!testCheck(color)){
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x->((ChessPiece)x).getColor()== color ).collect(Collectors.toList());

        for(Piece p :list){
            boolean [][] mat = p.possibleMoves();
            for(int i=0; i <board.getRows();i++){
                for(int j=0;j<board.getColumns();j++){
                    if(mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i,j);
                        Piece capturePiece = makeMove(source,target);
                        boolean testCheck= testCheck(color);
                        undoMove(source,target,capturePiece);
                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private void placeNewPiece(char column,int row,ChessPiece piece){
        board.placePiece(piece,new ChessPosition(column,row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void nextTurn(){
        turn ++;
        currantPlayer = (currantPlayer == Color.WHITE)? Color.BLACK : Color.WHITE;
    }
    private void initialSetup() {
        placeNewPiece('e',1,new King(board,Color.WHITE,this));
        placeNewPiece('a',1,new Rook(board,Color.WHITE));
        placeNewPiece('h',1,new Rook(board,Color.WHITE));
        placeNewPiece('a',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('b',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('c',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('d',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('e',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('f',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('g',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('h',2,new Pawn(board,Color.WHITE,this));
        placeNewPiece('c',1,new Bishop(board,Color.WHITE));
        placeNewPiece('f',1,new Bishop(board,Color.WHITE));
        placeNewPiece('b',1,new Knight(board,Color.WHITE));
        placeNewPiece('g',1,new Knight(board,Color.WHITE));
        placeNewPiece('d',1,new Queen(board,Color.WHITE));

        placeNewPiece('a',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('b',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('c',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('d',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('e',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('f',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('g',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('h',7,new Pawn(board,Color.BLACK,this));
        placeNewPiece('e',8,new King(board,Color.BLACK,this));
        placeNewPiece('a',8,new Rook(board,Color.BLACK));
        placeNewPiece('h',8,new Rook(board,Color.BLACK));
        placeNewPiece('c',8,new Bishop(board,Color.BLACK));
        placeNewPiece('f',8,new Bishop(board,Color.BLACK));
        placeNewPiece('b',8,new Knight(board,Color.BLACK));
        placeNewPiece('g',8,new Knight(board,Color.BLACK));
        placeNewPiece('d',8,new Queen(board,Color.BLACK));


    }
}

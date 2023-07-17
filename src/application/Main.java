package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while(true) {
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch,captured);

                System.out.println();
                System.out.print("Peça: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(),possibleMoves);


                System.out.println();
                System.out.print("Para: ");
                ChessPosition target = UI.readChessPosition(sc);
                System.out.println();
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

                if(capturedPiece != null){
                    captured.add(capturedPiece);
                }
            }catch (ChessException e){
                System.out.println(e.getMessage());
                System.out.println("Aperte Enter para continuar");
                sc.nextLine();
            }catch (InputMismatchException e){
                System.out.println(e.getMessage());
                System.out.println("Aperte Enter para continuar");
                sc.nextLine();
            }


        }

    }
}
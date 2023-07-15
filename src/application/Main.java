package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        ChessMatch chessMatch = new ChessMatch();

        while(true) {
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());

                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(sc);

                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(sc);
                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

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
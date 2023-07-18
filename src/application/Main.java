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

        while(!chessMatch.getCheckMate()) {
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

                if(chessMatch.getPromoted()!=null){
                    System.out.print("Digite a peça para promoção (B/N/R/Q): ");
                    String type = sc.nextLine().toUpperCase();

                    while(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")){
                        System.out.print("Valor invalido! Digite a peça para promoção (B/N/R/Q): ");
                         type = sc.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(type);
                }
            }catch (ChessException | InputMismatchException e){
                System.out.println(e.getMessage());
                System.out.println("Aperte Enter para continuar");
                sc.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch,captured);
        sc.close();
    }
}
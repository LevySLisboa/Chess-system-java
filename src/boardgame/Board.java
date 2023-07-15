package boardgame;

public class Board {
    private int rows,columns;
    private Piece[][] pieces;

    public Board(int rows, int columns){
        if(rows < 1 || columns < 1){
            throw new BoardException("Erro ao criar o tabuleiro: é necessário ter pelo menos 1 linha e 1 coluna");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }


    public Piece piece(int row,int column){
        if(!positionExists(row,column)){
            throw new BoardException("Posição fora do tabuleiro");
        }
        return pieces[row][column];
    }
    public Piece piece(Position position){

        if(!positionExists(position)){
            throw new BoardException("Posição fora do tabuleiro");
        }
        return pieces[position.getRows()][position.getColumns()];

    }

    public void placePiece(Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("Já existe uma peça na posição: "+position);
        }

        pieces[position.getRows()][position.getColumns()] = piece;
        piece.position = position;
    }
    private boolean positionExists(int row,int column){
        return row >= 0 && row < rows && column >=0 && column < columns;
    }
    public boolean positionExists(Position position){
        return positionExists(position.getRows(),position.getColumns());
    }
    public boolean thereIsAPiece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Posição fora do tabuleiro");
        }

        return piece(position) != null;
    }
}

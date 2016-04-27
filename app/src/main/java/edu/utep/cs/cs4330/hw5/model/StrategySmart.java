package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import java.util.Random;
import android.util.Log;

/**
 * The class will look for a combination of 3 or 4 to block
 *
 */
public class StrategySmart implements Strategy{

    Coordinates coordinates = new Coordinates();

    @Override
    public Coordinates findCoordinates(char[][]board) {
        //itterate through the board to play defence
        for(int x= 0; x<board.length;x++){
            for(int y = 0; y<board.length;y++){
                if(lookHorizontal(x,y, board))
                    return coordinates;
                if(lookVertical(x, y, board))
                    return coordinates;
                if(lookSouthEast(x, y,board))
                    return coordinates;
                if(lookSouthWest(x, y, board))
                    return coordinates;
                if(lookVertical(x, y, board))
                    return coordinates;
            }
        }
        random(board);
        return coordinates;
    }
    /*
    * Look for any 3 or 4 in a row to block
    * @param x coordinate
    * @param y coordinate
    * @param board a copy of the board*/
    private boolean lookSouthWest(int x, int y, char[][] board){
        if((x-3) >= 0 && (y+3) < board.length ){
            if(board[x][y]=='B' && board[x-1][y+1]=='B' && board[x-2][y+2]=='B'){

                if((x+1) < board.length && (y-1) >= 0){
                    if(board[x+1][y-1]==' '){
                        coordinates.setX(x+1);
                        coordinates.setY(y-1);
                        return true;
                    }
                }
                if((x-3) >=0 && (y+3) < board.length){
                    if(board[x-3][y+3]==' '){
                        coordinates.setX(x-3);
                        coordinates.setY(y+3);
                        return true;
                    }
                }
                if((x-4) >= 0 && (y+4) < board.length){
                    if(board[x-3][y+3]=='B'){
                        if(board[x-4][y+4]==' '){
                            coordinates.setX(x-4);
                            coordinates.setY(y+4);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /*
    * Look for any 3 or 4 in a row to block
    * @param x coordinate
    * @param y coordinate
    * @param board a copy of the board*/
    private boolean lookSouthEast(int x, int y, char[][] board){

        if((x+3) < board.length  && (y+3) < board.length ){
            if(board[x][y]== 'B' && board[x+1][y+1]=='B' && board[x+2][y+2]=='B'){
                if((x-1) >= 0 && (y-1) >= 0){
                    if(board[x-1][y-1]==' '){
                        coordinates.setX(x-1);
                        coordinates.setY(y - 1);
                        return true;
                    }
                }
                if((x+3) < board.length && (y+3) < board.length){
                    if(board[x+3][y+3]==' '){
                        coordinates.setX(x+3);
                        coordinates.setY(y+3);
                        return true;
                    }
                }
                if((x+4) < board.length && (y+4) < board.length){
                    if(board[x+3][y+3]=='B'){
                        if(board[x+4][y+4]==' '){
                            coordinates.setX(x+4);
                            coordinates.setY(y+4);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /*
    * Look for any 3 or 4 in a row to block
    * @param x coordinate
    * @param y coordinate
    * @param board a copy of the board*/
    private boolean lookHorizontal(int x, int y, char[][] board){
        if((x+3) < board.length){
            if(board[x][y]=='B' && board[x+1][y]=='B' && board[x+2][y]=='B'){
                if((x+2) < board.length ){
                    if(board[x+2][y]==' '){
                        coordinates.setX(x+2);
                        coordinates.setY(y);
                        return true;
                    }
                }
                if((x-1) >= 0 ){
                    if(board[x-1][y]==' '){
                        coordinates.setX(x-1);
                        coordinates.setY(y);
                        return true;
                    }
                }
                if((x+4)< board.length){
                    if(board[x+3][y]=='B'){
                        if(board[x+4][y]==' '){
                            coordinates.setX(x+4);
                            coordinates.setY(y);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /*
    * Look for any 3 or 4 in a row to block
    * @param x coordinate
    * @param y coordinate
    * @param board a copy of the board*/
    private boolean lookVertical(int x, int y, char[][] board){

        if((y+3) < board.length){
            if(board[x][y]=='B' && board[x][y+1]=='B' && board[x][y+2]=='B'){
                if((y+2) < board.length ){
                    if(board[x][y+2]==' '){
                        coordinates.setX(x);
                        coordinates.setY(y+2);
                        return true;
                    }
                }
                if((y-1) >= 0 ){
                    if(board[x][y-1]==' '){
                        coordinates.setX(x);
                        coordinates.setY(y-1);
                        return true;
                    }
                }
                if((y+4)< board.length){
                    if(board[x][y+3]=='B'){
                        if(board[x][y+4]==' '){
                            coordinates.setX(x);
                            coordinates.setY(y+4);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /*
    * else play random
    * */
    private void random(char[][] board){
        Random random = new Random();
        int x = random.nextInt(board.length);
        int y = random.nextInt(board.length);
        while(board[x][y]!=' ') {
            x = random.nextInt(board.length);
            y = random.nextInt(board.length);
        }
        coordinates.setX(x);
        coordinates.setY(y);
    }
}


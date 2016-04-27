package edu.utep.cs.cs4330.hw5.model;

/**
 * Created by juanrazo and Genesis Bejarano on 4/26/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Board implements Parcelable {
    private final int BOARDSIZE = 10;
    private char board[][];
    private boolean winner = false;

    public Board() {
        board = new char[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }

    protected Board(Parcel in) {
        winner = in.readByte() != 0;
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                board[i][j] = (char) in.readInt();
            }
        }
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    public boolean placeStone(Player player, Coordinates coordinates) {
        if (coordinates.getX()<BOARDSIZE-1 && coordinates.getY()<BOARDSIZE && board[coordinates.getX()][coordinates.getY()] == ' ') {
            board[coordinates.getX()][coordinates.getY()] = player.getStone();
            return true;
        }
        return false;
    }

    public boolean isPlaceOpen(int x, int y){
        if(board[x][y]==' ')
            return true;
        return false;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean checkWinner(Player player, Coordinates coordinates) {
        //if Horizontal Win
        int west = west(coordinates.getX(), coordinates.getY(), player.getStone());
        int east = east(coordinates.getX(), coordinates.getY(), player.getStone());
        //        Log.i("Check east & west size", "East " + east + " West " + west);
        if ((west + east) >= 6) {
            winner = true;
            return addWinningEastWest(east, west, coordinates.getX(), coordinates.getY(), player.getStone());
        }
        int north = north(coordinates.getX(), coordinates.getY(), player.getStone());
        int south = south(coordinates.getX(), coordinates.getY(), player.getStone());
        //if Vertical Win
        if ((north + south) >= 6) {
            winner = true;
            return addWinningNorthSouth(north, south, coordinates.getX(), coordinates.getY(), player.getStone());
        }
        //if Left-Diagonal Win
        int northWest = northWest(coordinates.getX(), coordinates.getY(), player.getStone());
        int southEast = southEast(coordinates.getX(), coordinates.getY(), player.getStone());
        if ((northWest + southEast) >= 6) {
            winner = true;
            return addWinningLeftDiagonal(northWest, southEast, coordinates.getX(), coordinates.getY(), player.getStone());
        }
        //if Right-Diagonal Win
        int northEast = northEast(coordinates.getX(), coordinates.getY(), player.getStone());
        int southWest = southWest(coordinates.getX(), coordinates.getY(), player.getStone());
        if ((northEast + southWest) >= 6) {
            winner = true;
            return addWinningRightDiagonal(northEast, southWest, coordinates.getX(), coordinates.getY(), player.getStone());
        }
        return false;
    }

    /*
    * Helper methods will go in all directions,
    * check for out of bounds first in the array
    * else use recursion to return an integer
    */
    private int east(int x, int y, char player) {
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0 )
            return 0;
        if (board[x][y] == player) {
            return 1 + east(x + 1, y, player);
        } else return 0;
    }

    private int west(int x, int y, char player) {
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0)
            return 0;
        if (board[x][y] == player) {
            return 1 + west(x - 1, y, player);
        } else return 0;
    }

    private int south(int x, int y, char player) {
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0)
            return 0;
        if (board[x][y] == player) {
            return 1 + south(x, y + 1, player);
        } else return 0;
    }

    private int north(int x, int y, char player) {
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0)
            return 0;
        if (board[x][y] == player) {
            return 1 + north(x, y - 1, player);
        } else return 0;
    }

    private int northWest(int x, int y, char player) {
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0)
            return 0;
        if (board[x][y] == player) {
            return 1 + northWest(x - 1, y - 1, player);
        } else return 0;
    }

    private int northEast(int x, int y, char player) {
        // Check the array for out of bounds in 2D array
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0)
            //if out of bounds then get out with a 0
            return 0;
        // Check if the place is taken and return 1
        if (board[x][y] == player) {
            return 1 + northEast(x + 1, y - 1, player);
        } else return 0;
    }

    private int southWest(int x, int y, char player) {
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0)
            return 0;
        if (board[x][y] == player) {
            return 1 + southWest(x - 1, y + 1, player);
        } else return 0;
    }

    private int southEast(int x, int y, char player) {
        if (x>BOARDSIZE-1 || y>BOARDSIZE-1 || x<0 || y<0)
            return 0;
        if (board[x][y] == player) {
            return 1 + southEast(x + 1, y + 1, player);
        } else return 0;
    }

    //-----------Methods used to denote the winning row-----------//

    /*
    * Based on the same stones found  east and west iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the Horizontal row
    * @Param int east the number of places checked east
    * @Param int west the number of places checked west
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningEastWest(int east, int west, int x, int y, char player) {
        if (east >= 0 & west >= 0) {
            if (east + west > 6)
                east--;
            for (int i = 0; i < west; i++) {
                board[x - i][y] = Character.toLowerCase(board[x - i][y]);
            }
            for (int j = 0; j < east; j++) {
                board[x + j][y] = Character.toLowerCase(board[x + j][y]);
            }
            return true;
        }
        return false;
    }

    /*
    * Based on the same stones found  north and south iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the Vertical row
    * @Param int north the number of places checked north
    * @Param int south the number of places checked south
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningNorthSouth(int north, int south, int x, int y, char player) {
        if (north >= 0 & south >= 0) {
            if (north + south > 6)
                north--;
            for (int i = 0; i < north; i++) {
                board[x][y - i] = Character.toLowerCase(board[x][y - i]);
            }
            for (int j = 0; j < south; j++) {
                board[x][y + j] = Character.toLowerCase(board[x][y + j]);
            }
            return true;
        }
        return false;
    }

    /*
    * Based on the same stones found  northWest and southEast iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the LeftDiagonal row
    * @Param int northWest the number of places checked northWest
    * @Param int southEast the number of places checked southEast
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningLeftDiagonal(int northWest, int southEast, int x, int y, char player) {
        if (northWest >= 0 & southEast >= 0) {
            if (northWest + southEast > 6)
                northWest--;
            for (int i = 0; i < northWest; i++) {
                board[x - i][y - i] = Character.toLowerCase(board[x - i][y - i]);
            }
            for (int j = 0; j < southEast; j++) {
                board[x + j][y + j] = Character.toLowerCase(board[x + j][y + j]);
            }
            return true;
        }
        return false;
    }

    /*
    * Based on the same stones found  northEast and southWest iterate at starting x & y to
    * change the string to lower case. This will in turn be read by the view and change
    * the stone with a contrast color, this method will change the RightDiagonal row
    * @Param int northEast the number of places checked northEast
    * @Param int southWest the number of places checked southWest
    * @Param int x the x position to start changing the string
    * @Param int y the y position to start changing the string
    * @Param String player the player string to change to lowercase*/
    private boolean addWinningRightDiagonal(int northEast, int southWest, int x, int y, char player) {
        if (northEast > 0 & southWest > 0) {
            if (northEast + southWest > 6)
                northEast--;
            for (int i = 0; i < northEast; i++) {
                board[x + i][y - i] = Character.toLowerCase(board[x + i][y - i]);
            }
            for (int j = 0; j < southWest; j++) {
                board[x - j][y + j] = Character.toLowerCase(board[x - j][y + j]);
            }
            return true;
        }
        return false;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (winner ? 1 : 0));
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                dest.writeInt(board[i][j]);
            }
        }
    }
}

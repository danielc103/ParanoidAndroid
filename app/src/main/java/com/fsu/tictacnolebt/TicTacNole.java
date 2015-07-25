/*
    Dylan Sprague
    7/7/15

    Project 1 (Tic Tac Nole) for COP 4656.
    All code is original. FSU seal obtained from https://unicomm.fsu.edu/brand/applying/seal/
    No special environment setup is needed.
    This project was tested on a device with API 19 and a 4.5" screen.

    The choice of human or computer players is hardcoded; see line 215 of this file.
    The app logic as written assumes player 1 is human; player 2 can be either human or computer.
    The AI chooses a random unoccupied cell to move to without any strategy.

    Screen rotation is locked via the manifest, using the attribute android:configChanges.
 */

package com.fsu.tictacnolebt;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

//this class implements the UI for the Tic Tac Toe game and handles the main game loop
public class TicTacNole extends Activity {


    //game logic
    TicTacToeGame game;
    TicTacToePlayer playerOne;
    TicTacToePlayer playerTwo;
    TicTacToePlayer activePlayer;

    //Bluetooth game logic
    boolean btPlay = false;
    BluetoothControl btControl;
    //Recieved thread boolean statement
    boolean running = false;

    //UI elements
    TableLayout board;
    Button[][] boardButtons;
    Button replayButton;
    TextView turnSignifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_nole);

        //initialize UI elements
        board = (TableLayout)findViewById(R.id.game_board);
        boardButtons = new Button[3][3];
        replayButton = (Button)findViewById(R.id.replay_button);
        turnSignifier = (TextView)findViewById(R.id.turn_signifier);

        //wire up click listener for replay_button
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        //set up buttons that represent game board
        int buttonSize;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                boardButtons[i][j] = (Button)((TableRow)board.getChildAt(i)).getChildAt(j);
                boardButtons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeMove(v);
                    }
                });
            }
        }

        //initialize game logic
        newGame();

    }

    private void makeMove(View b) {
        Log.d("myTag", "Making move - " + activePlayer.getTeam());

        TicTacToeGame.CellPosition movePos;

        switch (b.getId()){
            case R.id.game_top_left:
                movePos = TicTacToeGame.CellPosition.TOP_LEFT;
                markButton(boardButtons[0][0], "00");
                break;
            case R.id.game_top_center:
                movePos = TicTacToeGame.CellPosition.TOP_CENTER;
                markButton(boardButtons[0][1], "01");
                break;
            case R.id.game_top_right:
                movePos = TicTacToeGame.CellPosition.TOP_RIGHT;
                markButton(boardButtons[0][2], "02");
                break;
            case R.id.game_mid_left:
                movePos = TicTacToeGame.CellPosition.MID_LEFT;
                markButton(boardButtons[1][0], "10");
                break;
            case R.id.game_mid_center:
                movePos = TicTacToeGame.CellPosition.MID_CENTER;
                markButton(boardButtons[1][1], "11");
                break;
            case R.id.game_mid_right:
                movePos = TicTacToeGame.CellPosition.MID_RIGHT;
                markButton(boardButtons[1][2], "12");
                break;
            case R.id.game_bot_left:
                movePos = TicTacToeGame.CellPosition.BOT_LEFT;
                markButton(boardButtons[2][0], "20");
                break;
            case R.id.game_bot_center:
                movePos = TicTacToeGame.CellPosition.BOT_CENTER;
                markButton(boardButtons[2][1], "21");
                break;
            case R.id.game_bot_right:
                movePos = TicTacToeGame.CellPosition.BOT_RIGHT;
                markButton(boardButtons[2][2], "22");
                break;
            default:
                Log.e("myTag", "Erroneous button press captured");
                return;
        }

        //TODO - this should be ready for deletion, but test that switch statement works first [DS]
        /*
        if (b == findViewById(R.id.game_top_left))
=======
                Log.e("myTag", "Erroneous button press caputred");
                break;
        }

        /*if (b == findViewById(R.id.game_top_left))
>>>>>>> Stashed changes
            movePos = TicTacToeGame.CellPosition.TOP_LEFT;
        else if (b == findViewById(R.id.game_top_center))
            movePos = TicTacToeGame.CellPosition.TOP_CENTER;
        else if (b == findViewById(R.id.game_top_right))
            movePos = TicTacToeGame.CellPosition.TOP_RIGHT;
        else if (b == findViewById(R.id.game_mid_left))
            movePos = TicTacToeGame.CellPosition.MID_LEFT;
        else if (b == findViewById(R.id.game_mid_center))
            movePos = TicTacToeGame.CellPosition.MID_CENTER;
        else if (b == findViewById(R.id.game_mid_right))
            movePos = TicTacToeGame.CellPosition.MID_RIGHT;
        else if (b == findViewById(R.id.game_bot_left))
            movePos = TicTacToeGame.CellPosition.BOT_LEFT;
        else if (b == findViewById(R.id.game_bot_center))
            movePos = TicTacToeGame.CellPosition.BOT_CENTER;
        else if (b == findViewById(R.id.game_bot_right))
            movePos = TicTacToeGame.CellPosition.BOT_RIGHT;
        else {
            Log.e("myTag", "Erroneous button press captured");
            return;
        }
<<<<<<< Updated upstream
        */

        //make move in game's logic
        //the non-UI code from here down should probably be refactored into a separate method

        //TODO - need to wire up Buttons to the game's TicTacToeCells somehow
        game.makeMove(game.getCurrentPlayer(), movePos);


       /* //TODO - create a markButton method
        if (activePlayer.getTeam() == TicTacToeGame.Player.X)
            b.setText(R.string.x_cell);
        else if (activePlayer.getTeam() == TicTacToeGame.Player.O)
            b.setText(R.string.o_cell);

        b.setClickable(false);*/

        game.checkForWin();

        if (game.getWinningPlayer() != TicTacToeGame.Player.NEITHER) {
            handleVictory(game.getWinningPlayer());
        } else {
            game.nextPlayer();
            if (activePlayer == playerOne) {
                activePlayer = playerTwo;
            } else if (activePlayer == playerTwo) {
                activePlayer = playerOne;
            }
            if (activePlayer.getType() == TicTacToeGame.PlayerType.COMPUTER) {
                //handle computer turn

                //this code should probably be refactored into its own method (see above note)
                Log.d("myTag", "Starting computer turn - " + activePlayer.getTeam());


                //find the comp's best move, then recursively call this method with the comp's move;
                //computer move will happen and finish, then the original call will finish
                //should end with control being returned to human player
                TicTacToeCell compMove = game.findBestMove(activePlayer.getTeam());
                makeMove(compMove.getUiCell());
                /*
                int compMoveRow = compMove.getIndex() / 3;
                int compMoveCol = compMove.getIndex() % 3;
                b = boardButtons[compMoveRow][compMoveCol];
                */

                /*
                 * This block of code should be redundant with recursive call to makeMove() above.
                 * TODO - TEST THIS [DS]

                //TODO - get compMove as a TicTacToeCell, then find the appropriate button somehow
                //game.makeMove(game.getCurrentPlayer(), compMove);
                if (activePlayer.getTeam() == TicTacToeGame.Player.X)
                    b.setText(R.string.x_cell);
                else if (activePlayer.getTeam() == TicTacToeGame.Player.O)
                    b.setText(R.string.o_cell);

                b.setClickable(false);

                game.checkForWin();
                if (game.getWinningPlayer() != TicTacToeGame.Player.NEITHER) {
                    handleVictory(game.getWinningPlayer());
                    return; //exit before setting turnSignifier
                } else {
                    game.nextPlayer();
                    if (activePlayer == playerOne) {
                        activePlayer = playerTwo;
                    } else if (activePlayer == playerTwo) {
                        activePlayer = playerOne;
                    }
                }
                */

            } else {
                //wait for human player to make a move
                Log.d("myTag", "Waiting for human turn" + activePlayer.getTeam());
            }

            //set turn_signifier appropriately
            if (activePlayer.getTeam() == TicTacToeGame.Player.X) {
                turnSignifier.setText(R.string.x_prompt);
            } else {
                turnSignifier.setText(R.string.o_prompt);
            }

        }
    } //end makeMove()


    //note: this may not be necessary
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_tic_tac_nole, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newGame () {
        game = new TicTacToeGame();

        /* PLAYER SETTINGS
         *
         */
        //default; player 1 is human, X's
        //player 2 is human, O's
        playerOne = new TicTacToePlayer(TicTacToeGame.PlayerType.HUMAN, TicTacToeGame.Player.X);
        playerTwo = new TicTacToePlayer(TicTacToeGame.PlayerType.COMPUTER, TicTacToeGame.Player.O);

        //X always plays first
        activePlayer = playerOne;
        turnSignifier.setText(R.string.x_prompt);

        //reset game board display
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                boardButtons[i][j].setClickable(true);
                boardButtons[i][j].setText(R.string.blank_cell);
            }
        }
    }

    private void handleVictory(TicTacToeGame.Player winningPlayer) {

        Log.d("myTag", "Victory found for player " + winningPlayer);

        //lock all buttons
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                boardButtons[i][j].setClickable(false);
            }
        }

        //display victory text
        if (winningPlayer == TicTacToeGame.Player.X) {
            turnSignifier.setText(R.string.x_victory);
        } else if (winningPlayer == TicTacToeGame.Player.O){
            turnSignifier.setText(R.string.o_victory);
        } else if (winningPlayer == TicTacToeGame.Player.TIE) {
            turnSignifier.setText(R.string.tie_game);
        } else {
            Log.e("myTag", "Error: handleVictory() called with no winning player");
        }
    }
    //////////////////////////////////////////////////////////////////////////
    //ADDED GAME SUPPORT LOGIC FOR BLUETOOTH - Daniel Carroll
    /////////////////////////////////////////////////////////////////////////

    /**
     * Marks the button with an X or an O and sends move if connected to bluetooth
     * @param btn
     */
    public void markButton(Button btn, String buttonNumber){

        //bt mark
        if (btPlay){
            if (activePlayer.getTeam() == TicTacToeGame.Player.X) {
                btn.setText(R.string.x_cell);
                btControl.sendMove("X," + buttonNumber);
            }
            else if (activePlayer.getTeam() == TicTacToeGame.Player.O) {
                btn.setText("O");
                btControl.sendMove("O," + buttonNumber);
            }

            btn.setClickable(false);


        }
        //non bt mark
        else {
            if (activePlayer.getTeam() == TicTacToeGame.Player.X)
                btn.setText(R.string.x_cell);
            else if (activePlayer.getTeam() == TicTacToeGame.Player.O)
                btn.setText(R.string.o_cell);

            btn.setClickable(false);
        }
    }


    /**
     * thread to actively get move. Splits the move into 2 part array.  1st part = mark / 2nd part =  button number
     * then passes new array to receivedMove method for part handling
     */
    public void receivedThread(){
        running =  true;

        (new Thread() {

            public void run() {

                while (true) {
                    if(running){
                        String[] rMove = new String[1];
                        rMove = btControl.receiveMove().split(",", 2);
                        receivedMove(rMove);
                    }
                    else break;
                }
            }
        }).start();
    }

    /**
     * receives move string array from thread, then marks opponents board and lock button
     * @param move
     */
    public void receivedMove(String[] move){

        String mark = move[0];


        if(move[1].contains("00")){
            boardButtons[0][0].setText(mark);
            boardButtons[0][0].setClickable(false);
        }
        if(move[1].contains("01")){
            boardButtons[0][1].setText(mark);
            boardButtons[0][1].setClickable(false);
        }
        if(move[1].contains("02")){
            boardButtons[0][2].setText(mark);
            boardButtons[0][2].setClickable(false);
        }
        if(move[1].contains("10")){
            boardButtons[1][0].setText(mark);
            boardButtons[1][0].setClickable(false);
        }
        if(move[1].contains("11")){
            boardButtons[1][1].setText(mark);
            boardButtons[1][1].setClickable(false);
        }
        if(move[1].contains("12")){
            boardButtons[1][2].setText(mark);
            boardButtons[1][2].setClickable(false);
        }
        if(move[1].contains("20")){
            boardButtons[2][0].setText(mark);
            boardButtons[2][0].setClickable(false);
        }
        if(move[1].contains("21")){
            boardButtons[2][1].setText(mark);
            boardButtons[2][1].setClickable(false);
        }
        if(move[1].contains("22")){
            boardButtons[2][2].setText(mark);
            boardButtons[2][2].setClickable(false);
        }



    }

    //TODO: create method to end received thread - can't run forever!

}

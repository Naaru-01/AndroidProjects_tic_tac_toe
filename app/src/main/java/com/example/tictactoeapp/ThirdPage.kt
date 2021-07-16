package com.example.tictactoeapp

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_third_page.*
import kotlin.system.exitProcess

var isMyMove = isCodeMaker;

class ThirdPage : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_third_page)
        if (isMyMove == true) {
            textView3.text = "NOW PLAYING : YOU"
            textView3.setTextColor(Color.parseColor("#D22BB804"))
        } else if (!isMyMove) {
            textView3.text = "NOW PLAYING : OPPONENT"
            textView3.setTextColor(Color.parseColor("#EC0C0C"))
        }
        // add the firebase analytics object
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // reset the current view in online mode
        button110.setOnClickListener { reset() }

        // get the instance of firebase data
        FirebaseDatabase.getInstance().reference.child("CURRENT_MOVES").child(code)
            .addChildEventListener(
                object : ChildEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(this.toString(), " Operation Canceled ")

                    }

                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        var data = snapshot.value
                        if (isMyMove) {

                            isMyMove = false
                            moveonline(data.toString(), isMyMove)


                        } else if (!isMyMove) {
                            isMyMove = true
                            moveonline(data.toString(), isMyMove)
                        }


                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        reset()
                        errorMsg("Waiting.....")
                    }

                })
    }


    // this is the function run on both side as user is Plying himself and other is opponent
    fun moveonline(data: String, move: Boolean) {
        // playing the audio
        val audio = MediaPlayer.create(this, R.raw.poutch)
        //Toast.makeText(this, " mo: DataSnapshot(Str) :  $data", Toast.LENGTH_LONG).show()
        //Toast.makeText(this, " mo : DataSnapshot(Int) :  ${data.toInt()}", Toast.LENGTH_LONG).show()

        /*
        if(isMyMove==true){
           // code for checking internet  connectivity of the player`1
        }else if(isMyMove==false){
          // code for checking internet connectivity of the opponent player
        }
        */


        //Toast.makeText(this, " mo :Player Move True/false : $move", Toast.LENGTH_LONG).show()

        if (move) {

            val buttonselected: Button? = when (data.toInt()) {

                // player1 is now selecting his move/ else reset
                1 -> button11
                2 -> button12
                3 -> button13
                4 -> button14
                5 -> button15
                6 -> button16
                7 -> button17
                8 -> button18
                9 -> button19

                // if not selecting any move then reset
                else -> {
                    button11
                }
            }

            if (buttonselected != null) {
                buttonselected.text = "O"
                buttonselected.setTextColor(Color.parseColor("#D22BB804"))

            }

            // now on the player one's screen opponent turn is showing
            textView3.text = "NOW PLAYING : YOU"
            textView3.setTextColor(Color.parseColor("#D22BB804"))


            // PLAYER ONE ROUND IN THE ONLINE MODE ENDED HERE FOR THE FIRST MOVE

            player2.add(data.toInt())
            //Toast.makeText(this, "player2 list :  $player2", Toast.LENGTH_LONG).show()
            emptyCells.add(data.toInt())
            // Toast.makeText(this, "emptycells list :  $emptyCells", Toast.LENGTH_LONG).show()

            //started click sound
            audio.start()
            Handler().postDelayed(Runnable { audio.release() }, 300)


            // if buttton is already selected then disable that button
            if (buttonselected != null) {
                buttonselected.isEnabled = false
            }

            // after every move check the winner
            checkwinner()
        }
    }


    // this is the function which is repsonsible for making the turn of the opposite player

    fun clickfun(view: View) = if (isMyMove) {
        val but = view as Button

        // initially no cell is online
        var cellOnline = 0

        // first no player is playing hence if your turn then playnow in 10 sec window


        cellOnline = when (but.id) {
            R.id.button11 -> 1
            R.id.button12 -> 2
            R.id.button13 -> 3
            R.id.button14 -> 4
            R.id.button15 -> 5
            R.id.button16 -> 6
            R.id.button17 -> 7
            R.id.button18 -> 8
            R.id.button19 -> 9
            else -> {
                0
            }

        }


        playerTurn = false;
        Toast.makeText(this, "Opponent Is Playing Now ...", Toast.LENGTH_LONG).show()
        // in this 1000 mls means in 10 second player 2 is playing on the same board
        // window for player 2 play
        Handler().postDelayed(Runnable { playerTurn = true }, 200)

        playnow(but, cellOnline)

        updateDatabase(cellOnline);

    } else {
        Toast.makeText(this, "Wait for your turn ...", Toast.LENGTH_LONG).show()

    }


    // Arraylists to store the moves of both the players and the to track empty cells
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    private var emptyCells = ArrayList<Int>()

    // still don't know what is active user is doing
    var activeUser = 1

    var player1Count = 0
    var player2Count = 0

    private fun playnow(buttonSelected: Button, currCell: Int) {
        val audio = MediaPlayer.create(this, R.raw.poutch)

        buttonSelected.text = "X"
        emptyCells.remove(currCell)
        textView3.text = "NOW PLAYING : OPPONENT"
        textView3.setTextColor(Color.parseColor("#EC0C0C"))
        buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
        player1.add(currCell)
        emptyCells.add(currCell)
        audio.start()
        //Handler().postDelayed(Runnable { audio.pause() } , 500)
        buttonSelected.isEnabled = false
        Handler().postDelayed(Runnable { audio.release() }, 200)
        checkwinner()
    }


    fun updateDatabase(cellId: Int) {
        FirebaseDatabase.getInstance().reference.child("CURRENT_MOVES").child(code).push()
            .setValue(cellId);
    }

    fun checkwinner(): Int {
        val audio = MediaPlayer.create(this, R.raw.success)
        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) || (player1.contains(
                1
            ) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) || (player1.contains(
                7
            ) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) || (player1.contains(
                1
            ) && player1.contains(
                5
            ) && player1.contains(9)) ||
            player1.contains(3) && player1.contains(5) && player1.contains(7) || (player1.contains(2) && player1.contains(
                5
            ) && player1.contains(8))
        ) {
            player1Count += 1
            buttonDisable()
            audio.start()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("        GAME  OVER")
            build.setMessage("YOU WINS " + "\n\n" + "Do You Want To Play Again ?")
            build.setPositiveButton("YEAH") { dialog, which ->
                reset()
                audio.release()
            }
            build.setNegativeButton("EXIT") { dialog, which ->
                audio.release()
                removeCode()
                exitProcess(1)

            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1


        } else if ((player2.contains(1) && player2.contains(2) && player2.contains(3)) || (player2.contains(
                1
            ) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) || (player2.contains(
                7
            ) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) || (player2.contains(
                1
            ) && player2.contains(
                5
            ) && player2.contains(9)) ||
            player2.contains(3) && player2.contains(5) && player2.contains(7) || (player2.contains(2) && player2.contains(
                5
            ) && player2.contains(8))
        ) {
            player2Count += 1
            audio.start()
            buttonDisable()
            disableReset()
            Handler().postDelayed(Runnable { audio.release() }, 4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("        GAME  OVER")
            build.setMessage("OPPONENT WINS " + "\n\n" + "Do You Want To Play Again ?")
            build.setPositiveButton("YEAH ") { dialog, which ->
                reset()
                audio.release()
            }
            build.setNegativeButton("EXIT") { dialog, which ->
                audio.release()
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 2000)
            return 1
        } else if (emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) && emptyCells.contains(
                4
            ) && emptyCells.contains(5) && emptyCells.contains(6) && emptyCells.contains(7) &&
            emptyCells.contains(8) && emptyCells.contains(9)
        ) {

            val build = AlertDialog.Builder(this)
            build.setTitle("HEY ! IT'S DRAW !")
            build.setMessage("MATCH TIED" + "\n\n" + "Do You Want To Play Again ?")
            build.setPositiveButton("YEAH") { dialog, which ->
                audio.release()
                reset()
            }
            build.setNegativeButton("EXIT") { dialog, which ->
                audio.release()
                removeCode()
                exitProcess(1)
            }
            build.show()
            return 1

        }
        return 0
    }

    fun reset() {
        player1.clear()
        player2.clear()
        if (isMyMove) {
            textView3.text = "NOW PLAYING : YOU"
        } else {
            textView3.text = "NOW PLAYING : OPPONENT"
        }

        emptyCells.clear()

        activeUser = 1;
        for (i in 1..9) {
            var buttonselected: Button? = when (i) {
                1 -> button11
                2 -> button12
                3 -> button13
                4 -> button14
                5 -> button15
                6 -> button16
                7 -> button17
                8 -> button18
                9 -> button19
                else -> {
                    button11
                }
            }
            if (buttonselected != null) {
                buttonselected.isEnabled = true
            }
            if (buttonselected != null) {
                buttonselected.text = ""
            }


            textView.text = "YOU: $player1Count"
            textView2.text = "OPPONENT : $player2Count"
            if (player1Count == 10) {
                player1Count = 0
                val build = AlertDialog.Builder(this)
                build.setTitle("        GAME  OVER")
                build.setMessage("YOU WINS " + "\n\n" + "Do You Want To Play Again ?")
                build.setPositiveButton("YEAH ") { dialog, which ->
                    reset()

                }
                build.setNegativeButton("EXIT") { dialog, which ->
                    removeCode()
                    exitProcess(1)
                }
            } else if (player2Count == 10) {
                player2Count = 0
                val build = AlertDialog.Builder(this)
                build.setTitle("        GAME  OVER")
                build.setMessage("OPPONENT WINS " + "\n\n" + "Do You Want To Play Again ?")
                build.setPositiveButton("YEAH") { dialog, which ->
                    reset()

                }
                build.setNegativeButton("EXIT") { dialog, which ->
                    removeCode()
                    exitProcess(1)
                }
            }
            isMyMove = isCodeMaker
            //startActivity(Intent(this,ThirdPage::class.java))
            if (isCodeMaker) {
                FirebaseDatabase.getInstance().reference.child("CURRENT_MOVES").child(code)
                    .removeValue()
            }


        }
    }

    fun buttonDisable() {
        for (i in 1..9) {
            val buttonSelected = when (i) {
                1 -> button11
                2 -> button12
                3 -> button13
                4 -> button14
                5 -> button15
                6 -> button16
                7 -> button17
                8 -> button18
                9 -> button19
                else -> {
                    button11
                }

            }
            if (buttonSelected.isEnabled)
                buttonSelected.isEnabled = false
        }
    }

    fun removeCode() {
        if (isCodeMaker) {
            FirebaseDatabase.getInstance().reference.child("codes").child(keyValue).removeValue()
        }
    }

    fun errorMsg(value: String) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
    }

    fun disableReset() {
        button110.isEnabled = false
        Handler().postDelayed(Runnable { button110.isEnabled = true }, 2200)
    }

    override fun onBackPressed() {
        removeCode()
        if (isCodeMaker) {
            FirebaseDatabase.getInstance().reference.child("CURRENT_MOVES").child(code)
                .removeValue()
        }

        exitProcess(0)
    }
}

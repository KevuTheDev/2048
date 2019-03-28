package TwoFourEightGame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TFEGameMain {
    // [Time] ------------------------------------------------------------------------------------
    private LocalDateTime myClock  = LocalDateTime.now();
    private DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    // [Playfield] ---------------------------------------------------------------------------------
    private final int myMinLimit = 4;
    private int mySize;
    private TFESquare[][] sqPlayField;
    private int numOfSquares;

    // [Game] ---------------------------------------------------------------------------------
    private boolean isRunning;
    private int gameScore;
    private int highestValue;
    private Scanner myScanner;

    private int squareMoved;

    // [Mode] ---------------------------------------------------------------------------------
    private boolean devMode;

    // [Constructors] ---------------------------------------------------------------------------------

    /*public GameMain() {
        this();
        System.out.println("Hello");
        this.devMode = pramDevMode;
        System.out.println(pramDevMode);
    }*/

    public TFEGameMain() {
        this.init();
        this.getDevMode();
        this.printInDevMode("You have enabled Dev Mode!");

        this.printInDevMode("A " + Integer.toString(this.mySize) + "x" + Integer.toString(this.mySize) + " play-field has been created. ");

        // Interchange between the two below
        //=================================================================
        //[Main Game]
        this.spawnRandomSquare(); //Requires for main game

        //[Testing Game]
        //this.loadGame();// Testing function
        //=================================================================

        this.gameLoop();
    }

    // [Constructor Utility] ---------------------------------------------------------------------------------
    private void isSizeInMinLimit() {
        if(this.mySize < this.myMinLimit) {
            this.mySize = this.myMinLimit;
        }
    }

    private void init() {
        isRunning = true;
        highestValue = 0;
        myScanner = new Scanner(System.in);
        squareMoved = 1;

        this.mySize = this.myMinLimit;
        this.isSizeInMinLimit();
        this.numOfSquares = 0;
        this.gameScore = 0;
        sqPlayField = new TFESquare[this.mySize][this.mySize];
    }

    // [Testing] ---------------------------------------------------------------------------------
    private void getDevMode() {
        System.out.print("Enable Dev Mode?: ");

        String myInput = myScanner.next();
        if(Objects.equals(myInput.toLowerCase().trim(), "yes")) {
            this.devMode = true;
        }
    }

    private boolean isDevMode() {
        return this.devMode;
    }

    private void printInDevMode() {
        if(this.isDevMode()) {
            System.out.println();
        }
    }

    private void printInDevMode(String output) {
        if(this.isDevMode()) {
            myClock = LocalDateTime.now();
            String formattedDate = myClock.format(myFormatObj);
            System.out.println("[" + formattedDate + "][DevMode][System] " + output);
        }
    }

    private void preloadGame() {
        this.sqPlayField = new TFESquare[][] {
                {null, null, null, null},
                {null, new TFESquare(4), new TFESquare(2), new TFESquare(2)},
                {null, new TFESquare(2), null, null},
                {null, new TFESquare(2), null, null}};
    }

    private void assignSquareYXCoord() {
        for(int y = 0; y < this.mySize; y++) {
            for(int x = 0; x < this.mySize; x++) {
                if(this.sqPlayField[y][x] != null) {
                    this.sqPlayField[y][x].assignYXCoord(y,x);
                    this.addNumOfSquare();
                }
            }
        }
    }

    private void loadGame() {
        this.preloadGame();
        this.assignSquareYXCoord();
    }

    // [Utility] ---------------------------------------------------------------------------------
    private boolean isEmptyYXSquare(int y, int x) {
        return this.sqPlayField[y][x] == null;
    }

    private boolean isFieldFull() {
        boolean isFull = false;
        if(this.mySize * this.mySize == this.getNumofSquares()) {
            isFull = true;
        }
        return isFull;
    }

    private int getNumofSquares() {
        return this.numOfSquares;
    }

    private int getScore() {
        return this.gameScore;
    }

    // [Modifiers] -------------------------------------------------------------------------------
    private void addNumOfSquare() {
        this.numOfSquares++;
    }

    private void subNumOfSquare() {
        this.numOfSquares--;
    }

    private void convertAllSquaresFalse() {
        if (this.getNumofSquares() != 0) {
            for (int y = 0; y < this.mySize; y++) {
                for (int x = 0; x < this.mySize; x++) {
                    if (this.sqPlayField[y][x] != null) {
                        this.sqPlayField[y][x].convertFalse();
                    }
                }
            }
        }
    }

    private void addScore(int value) {
        this.gameScore += value;
    }

    private void addMovedSquare() {
        this.squareMoved++;
    }

    // [Game Requirements] ---------------------------------------------------------------------
    private boolean isSquaresAddable(TFESquare sqOne, TFESquare sqTwo) {
        boolean addable = false;
        if(sqOne != null && sqTwo != null) {
            if(sqOne.getMyNumber() == sqTwo.getMyNumber()) {
                if(!sqOne.getConvertBoolean() && !sqTwo.getConvertBoolean()) {
                    addable = true;
                }
            }
        }
        return addable;
    }

    private void addSquares(TFESquare sqOne, TFESquare sqTwo) {
        if(this.isSquaresAddable(sqOne, sqTwo)) {
            this.sqPlayField[sqOne.getMyY()][sqOne.getMyX()].expoMyNumber();
            this.sqPlayField[sqTwo.getMyY()][sqTwo.getMyX()] = null;
            this.subNumOfSquare();
            this.addScore(this.sqPlayField[sqOne.getMyY()][sqOne.getMyX()].getMyNumber());
            this.sqPlayField[sqOne.getMyY()][sqOne.getMyX()].convertTrue();
            this.printInDevMode("Squares Added");
            //System.out.println("Squares ADDED");
            this.addMovedSquare();
        }
    }

    private void spawnRandomSquare() {
        if(this.squareMoved != 0) {
            /*
            Optimization: - Make a list of numbers/points that are not used by an object
                          - Remove point if its being used
                                - During AddingSquare and moving squares
                          - Add point if its not gonna be used
                                - During AddSquare and moving squares
             */
            int y = ThreadLocalRandom.current().nextInt(0, this.mySize);
            int x = ThreadLocalRandom.current().nextInt(0, this.mySize);
            while(!this.isEmptyYXSquare(y,x)) {
                y = ThreadLocalRandom.current().nextInt(0, this.mySize);
                x = ThreadLocalRandom.current().nextInt(0, this.mySize);
            }

            this.sqPlayField[y][x] = new TFESquare(2, y, x);
            this.addNumOfSquare();
            this.printInDevMode("(" + x + ", " + y + ") " + "Square Spawned");
            //System.out.println("Spawned at: ("+Integer.toString((x+1)) +", " + Integer.toString((y+1)) + ")");
            this.squareMoved = 0;
        }
    }

    private void gameLoop() {
        this.printInDevMode("Game Loop is now running.");
        this.printInDevMode();
        System.out.println("Welcome to Kevu's version of 2048.");
        this.printMoveCmd();
        System.out.println();

        this.printInDevMode("Total number of squares on field: " + Integer.toString(this.getNumofSquares()));
        this.printMax();

        while(this.isRunning) {
            this.moveDirection();
            System.out.println();

            this.spawnRandomSquare();
            this.printInDevMode("Total number of squares on field: " + Integer.toString(this.getNumofSquares()));

            this.printMax();

            this.convertAllSquaresFalse();
            this.gameOver();
        }
    }

    private void gameOver() {
        if(this.isFieldFull()) {
            boolean gameOver = true;
            int x = 0;
            int y = 0;
            while(x < (this.mySize-1) && gameOver) {
                while(y < (this.mySize-1) && gameOver) {
                    if(isSquaresAddable(this.sqPlayField[y][x], this.sqPlayField[y][x+1])) {
                        this.printInDevMode("wtf");
                        gameOver = false;
                    }

                    if(isSquaresAddable(this.sqPlayField[y][x], this.sqPlayField[y+1][x])) {
                        this.printInDevMode("wtf2");
                        gameOver = false;
                    }

                    if(isSquaresAddable(this.sqPlayField[y+1][x], this.sqPlayField[y+1][x+1])) {
                        this.printInDevMode("wtf3");
                        gameOver = false;
                    }

                    if(isSquaresAddable(this.sqPlayField[y][x+1], this.sqPlayField[y+1][x+1])) {
                        this.printInDevMode("wtf4");
                        gameOver = false;
                    }
                    y++;
                }
                x++;
            }

            if(gameOver) {
                this.printInDevMode("Game over");
                System.out.println("Game over");
                this.isRunning = false;
                System.out.println("Congratulations!!! You scored: " + this.getScore());
            }
        }
    }

    // [Output] -----------------------------------------------------------------------------------------
    private void printMoveCmd() {
        System.out.println("Shift Squares to corresponding direction...:");
        System.out.println("(w) Up");
        System.out.println("(a) Left");
        System.out.println("(s) Down");
        System.out.println("(d) Right");
    }

    private void printPlayfieldX() {
        /*
        Description: Printing Play-field in it's most advanced way
         */
        System.out.println("---------------------------------------------"); //45 Spaces
        for(int i = 0; i < this.mySize; i++) {
            System.out.println("|          |          |          |          |"); // 10 spaces between |
            System.out.print("|");
            for(int j = 0; j < this.mySize; j++) {
                if(this.sqPlayField[i][j] != null) {
                    int number = this.sqPlayField[i][j].getMyNumber();
                    String strNumber = Integer.toString(number);
                    int length = 10 - strNumber.length();
                    int mid;

                    if(length % 2 == 0) {
                        mid = length / 2;
                    }else {
                        mid = (length + 1) / 2;
                    }

                    for(int k = 0; k < mid; k++) {
                        System.out.print(" ");
                    }
                    System.out.print(number);

                    for(int k = mid; k < length; k++) {
                        System.out.print(" ");
                    }

                }else {
                    System.out.print("          ");
                }
                System.out.print("|");
            }
            System.out.println();
            System.out.println("|          |          |          |          |"); // 10 spaces between |
            System.out.println("---------------------------------------------"); //45
        }
    }

    private void printPlayfield() {
        for(int i = 0; i < this.mySize; i++) {
            for(int j = 0; j < this.mySize; j++) {
                if(this.sqPlayField[i][j] == null) {
                    System.out.print("[0]");
                }else{
                    //int number = this.sqPlayField[i][j].getMyNumber();  // Probably not gonna be used
                    System.out.print("[" + this.sqPlayField[i][j] +  "]");
                }
            }
            System.out.println();
        }
    }

    private void printScore() {
        System.out.println("Score: " + Integer.toString(this.getScore()));
    }

    private void printMax() {
        this.printScore();
        this.printPlayfieldX();
    }

    // [Input] -----------------------------------------------------------------------------------------------
    private void mainInput() {
        String strInput = myScanner.next();
    }

    private void consoleInput() {
        System.out.println("[Console Menu]");
        System.out.println("(1) Developer Mode");
        System.out.println();
        System.out.print("Which option would you like... ");

        String strInput = myScanner.next();
        strInput = strInput.trim().toLowerCase();

        if(Objects.equals(strInput, "1")) {
            System.out.println("Would you like to switch Developer Mode on/off?");
            System.out.println("(1) On");
            System.out.println("(2) Off");
            System.out.println();
            System.out.print("Enter your choice... ");
            strInput = myScanner.next();
            if(Objects.equals(strInput, "1")) {
                this.devMode = true;
                System.out.println("Developer Mode is active!");
            }else if(Objects.equals(strInput, "2")) {
                this.devMode = false;
                System.out.println("Developer Mode has deactivated!");
            }
        }
    }

    // [Movement] -------------------------------------------------------------------------------------
    private void moveDirection() { //INPUT OF SOME SORT
        System.out.print("Next Move: ");
        String strInput = myScanner.next();
        strInput = strInput.trim().toLowerCase();
        while(!Objects.equals(strInput,"w") && !Objects.equals(strInput,"a") && !Objects.equals(strInput,"s") && !Objects.equals(strInput,"d") && !Objects.equals(strInput, "~")) {
            System.out.print("Next Move(w/a/s/d keys only): ");
            strInput = myScanner.next();
        }
        if(Objects.equals(strInput,"w")) {
            this.moveSquaresUp();
        }else if(Objects.equals(strInput,"a")) {
            this.moveSquaresLeft();
        }else if(Objects.equals(strInput,"s")) {
            this.moveSquaresDown();
        }else if(Objects.equals(strInput,"d")){
            this.moveSquaresRight();
        }else if(Objects.equals(strInput, "~")) {
            this.consoleInput();
            this.printMax();
            this.moveDirection();
        }
    }

    private void moveSquaresLeft() {
        //Move all Squares
        //Loop from Left to right, starting at top to bottom
        for(int x = 0; x < this.mySize; x++) {
            for(int y = 0; y < this.mySize; y++) {
                //this.printInDevMode("Squares Shifted Left");
                //System.out.println(Integer.toString(i) + ", "+ Integer.toString(j));
                this.moveSquareLeft(y, x);
            }
        }
    }

    private void moveSquareLeft(int y, int x) {
        if(!this.isEmptyYXSquare(y,x)) {
            while(x > 0 && this.isEmptyYXSquare(y, x-1)) {
                this.sqPlayField[y][x-1] = this.sqPlayField[y][x];
                this.sqPlayField[y][x] = null;

                this.sqPlayField[y][x-1].subMyX();
                x--;
                this.addMovedSquare();
                this.printInDevMode("Square Shifted Left to: " + "(" + x + ", " + y + ")");
                //System.out.println("OH Left");
            }

            if(x > 0) {
                this.addSquares(this.sqPlayField[y][x-1], this.sqPlayField[y][x]);
            }
        }
    }

    private void moveSquaresRight() {
        //Loop from Right to Left, starting at top to bottom
        for(int x = (this.mySize-1); x >= 0; x--) {
            for(int y = 0; y < this.mySize; y++) {
                //this.printInDevMode("Squares Shifted Right");
                //System.out.println(Integer.toString(i) + ", "+ Integer.toString(j));
                this.moveSquareRight(y, x);
            }
        }
    }

    private void moveSquareRight(int y, int x) {
        if(!this.isEmptyYXSquare(y,x)) {
            while(x != (this.mySize - 1) && this.isEmptyYXSquare(y, x+1)) {
                this.sqPlayField[y][x+1] = this.sqPlayField[y][x];
                this.sqPlayField[y][x] = null;

                this.sqPlayField[y][x+1].addMyX();
                x++;
                this.addMovedSquare();
                this.printInDevMode("Square Shifted Right to: " + "(" + x + ", " + y + ")");
                //System.out.println("OH Right");
            }
            if(x < (this.mySize-1)) {
                this.addSquares(this.sqPlayField[y][x+1], this.sqPlayField[y][x]);
            }
        }
    }

    private void moveSquaresUp() {
        //Loop from Top to Bottom, starting at Left to Right
        for(int y = 0; y < this.mySize; y++) {
            for(int x = 0; x < this.mySize; x++) {
                //this.printInDevMode("Squares Shifted Up");
                //System.out.println(Integer.toString(i) + ", "+ Integer.toString(j));
                this.moveSquareUp(y, x);
            }
        }
    }

    private void moveSquareUp(int y, int x) {
        if(!this.isEmptyYXSquare(y,x)) {
            while(y != 0 && this.isEmptyYXSquare(y-1, x)) {
                this.sqPlayField[y-1][x] = this.sqPlayField[y][x];
                this.sqPlayField[y][x] = null;

                this.sqPlayField[y-1][x].subMyY();
                y--;
                this.addMovedSquare();
                this.printInDevMode("Square Shifted Up to: " + "(" + x + ", " + y + ")");
                //System.out.println("OH Up");
            }
            if(y < 0) {
                this.addSquares(this.sqPlayField[y-1][x], this.sqPlayField[y][x]);
            }
        }
    }

    private void moveSquaresDown() {
        //Loop from Bottom to Top, starting at Left to Right
        for(int y = (this.mySize-1); y >= 0; y--) {
            for(int x = 0; x < this.mySize; x++) {
                //this.printInDevMode("Squares Shifted Down");
                //System.out.println(Integer.toString(i) + ", "+ Integer.toString(j));
                this.moveSquareDown(y, x);
            }
        }
    }

    private void moveSquareDown(int y, int x) {
        if(!this.isEmptyYXSquare(y,x)) {
            while(y != (this.mySize - 1) && this.isEmptyYXSquare(y+1, x)) {
                this.sqPlayField[y+1][x] = this.sqPlayField[y][x];
                this.sqPlayField[y][x] = null;

                this.sqPlayField[y+1][x].addMyY();
                y++;
                this.addMovedSquare();
                this.printInDevMode("Square Shifted Down to: " + "(" + x + ", " + y + ")");
                //System.out.println("OH Down");
            }
            if(y < (this.mySize-1)) {
                this.addSquares(this.sqPlayField[y+1][x], this.sqPlayField[y][x]);
            }
        }
    }

    // [Deprecated] -------------------------------------------------------------------------------------

    @Deprecated
    // warning: "Deprecated member is used temporary and will be removed with no other alternative."
    public void spawn() { //Remember to deprecate this function
        this.spawnRandomSquare();
    }

    @Deprecated
    // Warning: "Not in use at all."
    private void spawnSquare(int y, int x) {
    }

    @Deprecated
    // warning: "Renamed from moveDirection to moveIntDirection. Was able to get wasd to work. Now this function will be useless."
    private void moveIntDirection() {
        System.out.print("Next Move: ");
        int intInput = myScanner.nextInt();
        while(intInput < 0 || intInput > 5) {
            intInput = myScanner.nextInt();
        }
        switch (intInput) {
            case 1:
                this.moveSquaresLeft();
                break;
            case 2:
                this.moveSquaresUp();
                break;
            case 3:
                this.moveSquaresDown();
                break;
            case 4:
                this.moveSquaresRight();
                break;
        }
    }
}

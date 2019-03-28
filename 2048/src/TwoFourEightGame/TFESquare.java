package TwoFourEightGame;

import Game.Square;

public class TFESquare extends Square{
    private int myNumber;
    private boolean isConverted = false;



    public TFESquare(int pramNum) {
        this(pramNum, 0, 0);
    }

    public TFESquare(int pramNum, int pramY, int pramX) {
        this.myNumber = pramNum;
        this.checkIfExpoTwo();

        this.myY = pramY;
        this.myX = pramX;
    }


    // [Utility] -------------------------------------------------------------------------------------
    private void checkIfExpoTwo() {
        // 2 4 8 16 32 64 128 256 512 1024 2048
        int counter = 1;

        double result = Math.pow(2, counter);
        while (result < this.myNumber) {
            counter++;
            result = Math.pow(2, counter);
        }

        if(result != this.myNumber) {
            this.toClosestExpoTwo((int)result, (int)Math.pow(2, counter-1));
        }
    }

    private void toClosestExpoTwo(int numOne, int numTwo) {
        if(Math.abs(numOne - this.myNumber) < Math.abs(numTwo - this.myNumber)) {
            this.myNumber = numOne;
        }else{
            this.myNumber = numTwo;
        }
    }

    int getMyNumber() {
        return this.myNumber;
    }

    boolean getConvertBoolean() {
        return this.isConverted;
    }

    // [Square Modifier] -------------------------------------------------------------------------------------
    void expoMyNumber() {
        this.myNumber = this.myNumber*2;
    }

    void convertTrue() {
        this.isConverted = true;
    }

    void convertFalse() {
        this.isConverted = false;
    }

    // [Override] -------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return Integer.toString(this.myNumber);
    }
}

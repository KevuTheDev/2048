package Game;

public class Square {
    public int myY;
    public int myX;

    // [Constructors] ---------------------------------------------------------------------------------
    public Square() {}

    public Square(int pramY, int pramX) {
        this.myY = pramY;
        this.myX = pramX;
    }

    // [Utility] -------------------------------------------------------------------------------------

    public int getMyY(){
        return this.myY;
    }

    public int getMyX() {
        return this.myX;
    }

    // [Coordinates] -------------------------------------------------------------------------------------
    public void addMyY() {
        this.myY += 1;
    }

    public void addMyX() {
        this.myX += 1;
    }

    public void subMyY() {
        this.myY -= 1;
    }

    public void subMyX() {
        this.myX -= 1;
    }

    public void assignYXCoord(int pramY, int pramX) {
        this.myY = pramY;
        this.myX = pramX;
    }
}

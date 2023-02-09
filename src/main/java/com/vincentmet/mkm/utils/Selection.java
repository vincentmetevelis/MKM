package com.vincentmet.mkm.utils;

public class Selection {
    private int maxLength, pos1, pos2;

    public Selection(int maxLength){
        this(maxLength, 0, 0);
    }
    public Selection(int maxLength, int initialPos1, int initialPos2){
        this.maxLength = maxLength;
        this.pos1 = initialPos1;
        this.pos2 = initialPos2;
        swapPosIfNeeded();
        applyConstraints();
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getPos1() {
        return pos1;
    }

    public int getPos2() {
        return pos2;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        applyConstraints();
    }

    public void setPos1(int pos1) {
        this.pos1 = pos1;
        swapPosIfNeeded();
        applyConstraints();
    }

    public void setPos2(int pos2) {
        this.pos2 = pos2;
        swapPosIfNeeded();
        applyConstraints();
    }

    public void setBothPos(int pos1, int pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;
        swapPosIfNeeded();
        applyConstraints();
    }

    public void setBothPos(int pos){
        this.pos1 = pos;
        this.pos2 = pos;
        swapPosIfNeeded();
        applyConstraints();
    }

    public void decreasePos1(){
        setPos1(getPos1()-1);
    }

    public void increasePos1(){
        setPos1(getPos1()+1);
    }

    public void decreasePos2(){
        setPos2(getPos2()-1);
    }

    public void increasePos2(){
        setPos2(getPos2()+1);
    }

    public void reset(){
        this.pos1 = 0;
        this.pos2 = 0;
    }

    public void swapPosIfNeeded(){
        if(pos1 > pos2){
            int temp = this.pos1;
            this.pos1 = this.pos2;
            this.pos2 = temp;
        }
    }

    public void applyConstraints(){
        if (pos1 < 0) pos1 = 0;
        if (pos1 > maxLength) pos1 = Math.max(0, maxLength);
        if (pos2 > maxLength) pos2 = Math.max(0, maxLength);
    }

    public boolean isAnythingSelected(){
        return pos1!=pos2;
    }

    public int getAmountOfCharsSelected(){
        return pos2 - pos1;
    }
}

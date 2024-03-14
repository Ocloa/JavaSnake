package org.example;

public class Bonus {
    private int bonusX = 0;
    private int bonusY = 0;
    private boolean bonusPlaced = false;
    private Bonus bonus;


    public int getBonusX() {
        return bonusX;
    }
    public void setBonusX(int essenX) {
        this.bonusX = essenX;
    }
    public int getBonusY() {
        return bonusY;
    }
    public void setBonusY(int essenY) {
        this.bonusY = essenY;
    }
    public boolean isBonusPlaced() {
        return bonusPlaced;
    }
    public void setBonusPlaced(boolean bonusPlaced) {
        this.bonusPlaced = bonusPlaced;
    }
    public Bonus getBonus() {
        return bonus;
    }
    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }
}

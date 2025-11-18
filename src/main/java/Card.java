public class Card {
    String rank;
    SUITE suit;

    public enum SUITE {Clubs, Diamonds, Hearts, Spades}

    //Constructors
    public Card(){
        this.rank = "";
        this.suit = null;
    }

    public Card(String rank, SUITE suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(Card card){
        this.rank = card.rank;
        this.suit = card.suit;
    }

    //Setters & Getters

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public SUITE getSuit() {
        return suit;
    }

    public void setSuit(SUITE suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    //DISPLAY METHOD
    public void display(){
        System.out.println(this);
    }
}

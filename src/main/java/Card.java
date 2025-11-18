public class Card {
    String rank;
    SUIT suit;

    public enum SUIT {Clubs, Diamonds, Hearts, Spades}

    //Constructors
    public Card(){
        this.rank = "";
        this.suit = null;
    }

    public Card(String rank, SUIT suit) {
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

    public SUIT getSuit() {
        return suit;
    }

    public void setSuit(SUIT suit) {
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

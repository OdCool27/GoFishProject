import java.util.Random;

public class Deck {
    Card[] deck = new Card[52];//An array of 52 Cards
    private int topIndex = 51;
    private Random rand = new Random();

    public static final Card.SUIT[] SUITS = {Card.SUIT.Clubs, Card.SUIT.Hearts, Card.SUIT.Spades, Card.SUIT.Diamonds};
    public static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};


    //Constructors
    public Deck(){
        int index = 0;
        for(Card.SUIT suit : SUITS){
            for(String rank : RANKS){
                deck[index++] = new Card(rank, suit);
            }
        }
    }

    public boolean isEmpty(){
        return topIndex < 0;
    }

    public int remainingCards(){
        return topIndex + 1;
    }

    public void shuffle(){
        for(int i = 0; i < topIndex; i++){
            int index = rand.nextInt(i+1);
            Card temp = deck[index];
            deck[index] = deck[i];
            deck[i] = temp;
        }
        topIndex = 51;//Resets the deck after shuffling
    }


    //Draws the top card like a stack pop
    public Card drawCard(){
        if(isEmpty()){
            return null;
        }
        return deck[topIndex--];
    }


    public void printDeck(){
        for(int i = 0; i < topIndex+1; i++){
            System.out.println(deck[i]);
        }
    }




}

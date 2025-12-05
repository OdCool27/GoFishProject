package model;

public final class Card 
{
    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }

    public enum Rank 
    {
        ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"),
        SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"),
        JACK("J"), QUEEN("Q"), KING("K");

        private final String label;
        Rank(String label) 
        { 
            this.label = label; 
            
        }
        public String label() 
        {
            return label; 
            
        }
        @Override public String toString() 
        { 
            return label; 
            
        }
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit s, Rank r) 
    {
        this.suit = s;
        this.rank = r;
    }

    public Suit getSuit() 
    { 
        return suit; 
        
    }
    public Rank getRank() 
    {
        return rank; 
        
    }

    @Override
    public String toString() 
    {
        return rank.toString() + "-" + suit.toString().charAt(0);
    }

    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card c = (Card) o;
        return this.suit == c.suit && this.rank == c.rank;
    }

    @Override
    public int hashCode() 
    {
        return rank.hashCode() * 31 + suit.hashCode();
    }
}

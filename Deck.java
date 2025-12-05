package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Deck 
{
    private final ArrayList<Card> cards;

    public Deck() 
    {
        cards = new ArrayList<>(52);
        for (Card.Suit s : Card.Suit.values()) 
        {
            for (Card.Rank r : Card.Rank.values()) 
            {
                cards.add(new Card(s, r));
            }
        }
    }

     // shuffle (in-place).
    public void shuffle(Random rnd) 
    {
        for (int i = cards.size() - 1; i > 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            Collections.swap(cards, i, j);
        }
    }

    //Draw the top card (end of list). Returns null if empty.
    public Card drawTop() 
    {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size() - 1);
    }

    //Deal n cards as a List (removes from deck).
    public List<Card> deal(int n) 
    {
        List<Card> out = new ArrayList<>(n);
        for (int i = 0; i < n && !cards.isEmpty(); i++) out.add(drawTop());
        return out;
    }

    public boolean isEmpty() 
    { 
        return cards.isEmpty(); 
        
    }
    public int size() 
    { 
        return cards.size(); 
        
    }

    @Override
    public String toString() 
    { 
        return "Deck(size=" + cards.size() + ")"; 
        
    }
}

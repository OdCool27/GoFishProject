package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Hand 
{
    private final LinkedList<Card> list;

    public Hand() 
    {
        list = new LinkedList<>(); 
        
    }

    public void add(Card c) 
    {
        if (c != null) list.add(c);
    }

    public void addAll(List<Card> cards) 
    {
        if (cards == null) return;
        for (Card c : cards) add(c);
    }


    //Remove and return all cards of the given rank from this hand.
    public List<Card> removeAllOfRank(Card.Rank rank) 
    {
        List<Card> removed = new ArrayList<>();
        Iterator<Card> it = list.iterator();
        while (it.hasNext()) 
        {
            Card c = it.next();
            if (c.getRank() == rank) 
            {
                removed.add(c);
                it.remove();
            }
        }
        return removed;
    }


    //Count how many cards of the given rank are in the hand.
    public int countOfRank(Card.Rank rank) 
    {
        int cnt = 0;
        for (Card c : list) if (c.getRank() == rank) cnt++;
        return cnt;
    }


    //Return a list of unique ranks currently in hand.
    public List<Card.Rank> uniqueRanks() 
    {
        ArrayList<Card.Rank> out = new ArrayList<>();
        for (Card c : list) 
        {
            if (!out.contains(c.getRank())) out.add(c.getRank());
        }
        return out;
    }

    public int size() 
    { 
        return list.size(); 
        
    }
    public boolean isEmpty() 
    { 
        return list.isEmpty(); 
        
    }

    @Override
    public String toString() 
    { 
        return list.toString();
    }
}

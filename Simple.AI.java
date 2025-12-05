package ai;

import java.util.List;
import java.util.Random;

import model.Hand;
import model.Card;


public final class SimpleAI 
{
    private static final Random rng = new Random();

    private SimpleAI()
    {


    //Choose a random rank from the ranks present in the provided hand.
     //Returns null if the hand has no ranks.
     
    public static Card.Rank chooseRandomRankFromHand(Hand hand) 
        {
            if (hand == null) return null;
            java.util.List<Card.Rank> ranks = hand.uniqueRanks();
            if (ranks.isEmpty()) return null;
            return ranks.get(rng.nextInt(ranks.size()));
        }
    }
}

package tests;

import model.Card;
import model.Deck;
import model.Hand;
import ai.SimpleAI;

import java.util.*;


public class Person5TestsMain {

    public static void main(String[] args) 
    {
        System.out.println("=== Running Tests ===");
        testDeckShuffleAndDraw();
        testAskRankTransferWithHand();
        testBookFormationWithHand();
        testSimpleAIWithHand();
        System.out.println("=== All tests passed successfully! ===");
    }

    //Simple assertion helpers
    private static void assertEquals(Object expected, Object actual) 
    {
        if (!Objects.equals(expected, actual)) 
        {
            throw new AssertionError("Assertion failed: expected=" + expected + " actual=" + actual);
        }
    }

    private static void assertTrue(boolean cond, String msg) 
    {
        if (!cond) throw new AssertionError("Assertion failed: " + msg);
    }

    private static void assertNotNull(Object o, String msg) 
    {
        if (o == null) throw new AssertionError("Assertion failed: " + msg);
    }

    // Test 1: Deck shuffle & draw
    private static void testDeckShuffleAndDraw() 
    {
        Deck d1 = new Deck();
        Deck d2 = new Deck();

        // Shuffle with different seeds to reduce chance of equal order
        d1.shuffle(new Random(1));
        d2.shuffle(new Random(2));

        assertEquals(52, d1.size());
        assertEquals(52, d2.size());

        // Draw top card from each; they should typically differ
        Card c1 = d1.drawTop();
        Card c2 = d2.drawTop();
        assertNotNull(c1, "d1 draw should not be null");
        assertNotNull(c2, "d2 draw should not be null");

        //check if they match
        assertEquals(51, d1.size());
        assertEquals(51, d2.size());
        System.out.println("Test Deck Shuffle And Draw passed.");
    }

    // Test 2: Asking ranks and transfer using Hand
    private static void testAskRankTransferWithHand() 
    {
        Hand handA = new Hand();
        Hand handB = new Hand();

        // A has 5,5,7
        handA.add(new Card(Card.Suit.HEARTS, Card.Rank.FIVE));
        handA.add(new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE));
        handA.add(new Card(Card.Suit.CLUBS, Card.Rank.SEVEN));

        // B has 5,9
        handB.add(new Card(Card.Suit.SPADES, Card.Rank.FIVE));
        handB.add(new Card(Card.Suit.HEARTS, Card.Rank.NINE));

        // A asks B for rank FIVE
        List<Card> transferred = handB.removeAllOfRank(Card.Rank.FIVE);
        // A receives transferred cards
        handA.addAll(transferred);

        // Validate
        assertEquals(4, handA.size()); // 3 + 1 transferred
        assertEquals(1, handB.size()); // only the 9 remains
        assertEquals(1, transferred.size());
        System.out.println("Test Ask Rank Transfer With Hand passed.");
    }

    // Test 3: Book formation using Hand
    private static void testBookFormationWithHand() 
    {
        Hand p = new Hand();
        // add four NINES and an extra card
        p.add(new Card(Card.Suit.HEARTS, Card.Rank.NINE));
        p.add(new Card(Card.Suit.DIAMONDS, Card.Rank.NINE));
        p.add(new Card(Card.Suit.CLUBS, Card.Rank.NINE));
        p.add(new Card(Card.Suit.SPADES, Card.Rank.NINE));
        p.add(new Card(Card.Suit.HEARTS, Card.Rank.THREE));

        // Count and detect book
        int cnt = p.countOfRank(Card.Rank.NINE);
        assertEquals(4, cnt);

        // Remove all of that rank (book formed)
        List<Card> removed = p.removeAllOfRank(Card.Rank.NINE);
        assertEquals(4, removed.size());
        assertEquals(1, p.size());
        System.out.println("Test Book Formation With Hand passed.");
    }

    
    private static void testSimpleAIWithHand() 
    {
        Hand botHand = new Hand();
        botHand.add(new Card(Card.Suit.HEARTS, Card.Rank.TWO));
        botHand.add(new Card(Card.Suit.DIAMONDS, Card.Rank.SEVEN));
        botHand.add(new Card(Card.Suit.CLUBS, Card.Rank.KING));

        Card.Rank chosen = SimpleAI.chooseRandomRankFromHand(botHand);
        assertNotNull(chosen, "Random rank must be chosen when hand non-empty");
        assertTrue(botHand.uniqueRanks().contains(chosen), "Random rank must be chpsen when present in its hand");
        System.out.println("Test with Hand passed.");
    }
}

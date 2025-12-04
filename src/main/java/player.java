public class Player {
    private String name;
    private HandLinkedList hand;    // Player's hand using HandLinkedList
    private int booksCount;         // Number of books (pairs) collected
    private boolean isBot;
    
    // Default constructor
    public Player() {
        this.name = "";
        this.hand = new HandLinkedList();
        this.booksCount = 0;
        this.isBot = false;
    }
    
    // Primary constructor
    public Player(String name, boolean isBot) {
        this.name = name;
        this.hand = new HandLinkedList();
        this.booksCount = 0;
        this.isBot = isBot;
    }
    
    // Copy constructor
    public Player(Player player) {
        this.name = player.name;
        this.hand = new HandLinkedList(player.hand);
        this.booksCount = player.booksCount;
        this.isBot = player.isBot;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getBooksCount() {
        return booksCount;
    }
    
    public void setBooksCount(int booksCount) {
        this.booksCount = booksCount;
    }
    
    public boolean isBot() {
        return isBot;
    }
    
    public void setBot(boolean isBot) {
        this.isBot = isBot;
    }
    
    public int getHandSize() {
        return hand.size();
    }
    
    public boolean hasCards() {
        return !hand.isEmpty();
    }
    
    /**
     * Add a card to the player's hand
     * Time Complexity: O(1)
     */
    public void addCard(Card card) {
        if (card != null) {
            hand.insertAtFront(card);
            checkAndFormBooks();
        }
    }
    
    /**
     * Draw initial cards from deck at game start
     * Time Complexity: O(n) where n = number of cards
     */
    public void drawInitialCards(Deck deck, int numCards) {
        for (int i = 0; i < numCards; i++) {
            Card card = deck.drawCard();
            if (card != null) {
                hand.insertAtFront(card);
            }
        }
        checkAndFormBooks();
    }
    
    /**
     * Draw one card from deck
     * Time Complexity: O(1)
     */
    public Card drawCard(Deck deck) {
        Card card = deck.drawCard();
        if (card != null) {
            addCard(card);
        }
        return card;
    }
    
    /**
     * Check if player has any cards of a specific rank
     * Time Complexity: O(n)
     */
    public boolean hasRank(String rank) {
        return hand.hasRank(rank);
    }
    
    /**
     * Give all cards of a rank to another player
     * Time Complexity: O(n)
     */
    public Card[] giveCardsOfRank(String rank) {
        return hand.removeAllOfRank(rank);
    }
    
    /**
     * Choose a random rank from hand (for bot AI)
     * Time Complexity: O(n)
     */
    public String chooseRandomRank() {
        if (hand.isEmpty()) {
            return null;
        }
        
        String[] uniqueRanks = hand.getUniqueRanks();
        if (uniqueRanks.length == 0) {
            return null;
        }
        
        int randomIndex = (int)(Math.random() * uniqueRanks.length);
        return uniqueRanks[randomIndex];
    }
    
    /**
     * Check for pairs and form books automatically
     * Time Complexity: O(n²)
     */
    public int checkAndFormBooks() {
        int newBooks = 0;
        
        // Check each rank for pairs
        for (String rank : Deck.RANKS) {
            while (hand.countRank(rank) >= 2) {
                formBook(rank);
                newBooks++;
            }
        }
        
        return newBooks;
    }
    
    /**
     * Form a book by removing 2 cards of the same rank
     * Time Complexity: O(n)
     */
    private void formBook(String rank) {
        Card[] pair = hand.removePair(rank);
        
        if (pair != null && pair.length == 2) {
            booksCount++;
            System.out.println(name + " formed a book with " + rank + "s!");
        }
    }
    
    /**
     * Display the player's hand
     * Time Complexity: O(n)
     */
    public void displayHand() {
        System.out.println("\n" + name + "'s hand:");
        
        if (hand.isEmpty()) {
            System.out.println("  [Empty]");
        } else {
            hand.display();
        }
        
        System.out.println("Books: " + booksCount + ", Cards in hand: " + hand.size());
    }
    
    @Override
    public String toString() {
        return name + " (Books: " + booksCount + ", Cards: " + hand.size() + ")";
    }
}

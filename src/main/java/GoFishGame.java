package testgamerule;


import java.util.Scanner;

public class GoFishGame {

    private Player[] players;
    private Deck deck;
    private int currentPlayerIndex;
    private int totalBooks; // total books formed by all players

    public GoFishGame(String[] playerNames, boolean[] isBotFlags) {
        if (playerNames.length != isBotFlags.length) {
            throw new IllegalArgumentException("playerNames and isBotFlags must have same length");
        }

        int numPlayers = playerNames.length;
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) 
        {
            players[i] = new Player(playerNames[i], isBotFlags[i]);
        }

        deck = new Deck();
        deck.shuffle();

        dealInitialCards();
        currentPlayerIndex = 0;

        // initial book check was already done inside Player.drawInitialCards()
        updateTotalBooks();
    }

    /**
     * Dealing cards based on number of players:
     *  - 2 or 3 players: 7 cards each
     *  - 4 or more players: 5 cards each
     */
    private void dealInitialCards() 
    {
        int numPlayers = players.length;
        int cardsEach = (numPlayers < 4) ? 7 : 5;

        for (Player p : players) {
            p.drawInitialCards(deck, cardsEach);
        }
    }

    /**
     * Main game loop.
     */
    public void play() 
    {
        Scanner scanner = new Scanner(System.in);

        while (!isGameOver()) {
            Player current = players[currentPlayerIndex];

            System.out.println("\n====================================");
            System.out.println("It's " + current.getName() + "'s turn.");
            System.out.println("Deck has " + deck.remainingCards() + " card(s) left.");

            // If player's hand is empty, draw 4 cards (or as many as possible)
            handleEmptyHandDraw(current);

            // If still no cards and deck empty, skip turn
            if (!current.hasCards() && deck.isEmpty()) {
                System.out.println(current.getName() + " has no cards and cannot draw. Turn is skipped.");
                advanceTurn();
                continue;
            }

            current.displayHand();
            boolean continueTurn = true;

            while (continueTurn && !isGameOver()) {
                // If player lost all cards during turn and deck still has cards, enforce empty-hand draw
                if (!current.hasCards() && !deck.isEmpty()) {
                    handleEmptyHandDraw(current);
                    if (!current.hasCards()) {
                        // Deck might have become empty; if so, break
                        break;
                    }
                }

                int targetIndex = chooseOpponent(scanner, current);
                Player target = players[targetIndex];

                String requestedRank = chooseRankToAsk(scanner, current);
                System.out.println(current.getName() + " asks " + target.getName() +
                                   " for rank \"" + requestedRank + "\"");

                if (target.hasRank(requestedRank)) {
                    System.out.println(target.getName() + " has at least one \"" + requestedRank + "\".");
                    Card[] transferred = target.giveCardsOfRank(requestedRank);
                    if (transferred != null && transferred.length > 0) {
                        for (Card c : transferred) {
                            current.addCard(c);
                        }
                        System.out.println(current.getName() + " received " + transferred.length +
                                           " card(s) of rank \"" + requestedRank + "\".");
                        int newBooks = current.checkAndFormBooks();
                        if (newBooks > 0) {
                            System.out.println(current.getName() + " formed " + newBooks + " new book(s)!");
                        }
                        updateTotalBooks();
                        // Successful ask → player continues
                        continueTurn = true;
                    } else {
                        // Shouldn't happen if hasRank() is correct, but fall back
                        System.out.println("Unexpected: no cards transferred.");
                        continueTurn = false;
                    }
                } else {
                    // Go Fish
                    System.out.println(target.getName() + " says: Go Fish!");
                    Card drawn = current.drawCard(deck);
                    if (drawn == null) {
                        System.out.println("The deck is empty. No card drawn.");
                        continueTurn = false;
                    } else {
                        System.out.println(current.getName() + " draws: " + drawn);
                        int newBooks = current.checkAndFormBooks();
                        if (newBooks > 0) {
                            System.out.println(current.getName() + " formed " + newBooks + " new book(s)!");
                        }
                        updateTotalBooks();

                        // If drawn card matches requested rank → continue, else end turn
                        if (drawn.getRank().equals(requestedRank)) {
                            System.out.println("The drawn card matches the requested rank! " +
                                               current.getName() + " gets another turn.");
                            continueTurn = true;
                        } else {
                            continueTurn = false;
                        }
                    }
                }

                current.displayHand();
            }

            advanceTurn();
        }

        printResults();
        scanner.close();
    }

    /**
     * Draw 4 cards when hand is empty, or as many as possible if fewer than 4 remain.
     */
    private void handleEmptyHandDraw(Player player) {
        if (!player.hasCards() && !deck.isEmpty()) {
            System.out.println(player.getName() + " has no cards and draws up to 4 cards from the deck.");
            int drawnCount = 0;
            for (int i = 0; i < 4 && !deck.isEmpty(); i++) {
                Card card = deck.drawCard();
                if (card != null) {
                    player.addCard(card);
                    drawnCount++;
                }
            }
            System.out.println(player.getName() + " drew " + drawnCount + " card(s).");
            int newBooks = player.checkAndFormBooks();
            if (newBooks > 0) {
                System.out.println(player.getName() + " formed " + newBooks + " new book(s)!");
                updateTotalBooks();
            }
        }
    }

    /**
     * Choose an opponent to ask.
     * If current player is a bot, choose randomly.
     * If human, ask via console.
     */
    private int chooseOpponent(Scanner scanner, Player current) {
        int currentIndex = currentPlayerIndex;

        if (current.isBot()) {
            // choose a random opponent index different from currentIndex
            int numPlayers = players.length;
            int idx;
            do {
                idx = (int)(Math.random() * numPlayers);
            } while (idx == currentIndex);
            System.out.println(current.getName() + " (bot) chooses to ask " + players[idx].getName());
            return idx;
        } else {
            // human
            while (true) {
                System.out.println("Choose a player to ask:");
                for (int i = 0; i < players.length; i++) {
                    if (i == currentIndex) continue;
                    System.out.println("  " + i + " -> " + players[i].getName());
                }
                System.out.print("Enter player index: ");
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    // nextLine() after nextInt() can cause empty lines; handle gracefully
                    continue;
                }
                try {
                    int choice = Integer.parseInt(line);
                    if (choice >= 0 && choice < players.length && choice != currentIndex) {
                        return choice;
                    }
                } catch (NumberFormatException e) {
                    // ignore and re-prompt
                }
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Choose a rank to ask for.
     * For bots: use Player.chooseRandomRank().
     * For humans: show ranks in hand and ask.
     */
    private String chooseRankToAsk(Scanner scanner, Player current) {
        if (current.isBot()) {
            String rank = current.chooseRandomRank();
            if (rank == null) {
                // fallback – should not happen if we checked hasCards()
                rank = Deck.RANKS[0];
            }
            System.out.println(current.getName() + " (bot) chooses rank \"" + rank + "\"");
            return rank;
        } else {
            // human
            while (true) {
                System.out.println("Enter a rank to ask for (must be in your hand).");
                System.out.print("Available ranks in your hand: ");
                String[] uniqueRanks = current.chooseRandomRank() == null
                        ? new String[0]
                        : current.getHandSize() > 0 ? currentHandRanks(current) : new String[0];

                // If we can't easily access HandLinkedList API directly, just tell user the RANKS
                // BUT better: if HandLinkedList exposes getUniqueRanks() via Player, use that instead.
                // Let's assume we can add a helper inside Player to expose unique ranks if needed.
                // For now, we just let user type any string and check with hasRank().

                System.out.print("(You can type something like A, 2, 10, J, Q, K)\n> ");
                String rank = scanner.nextLine().trim();
                if (current.hasRank(rank)) {
                    return rank;
                }
                System.out.println("You don't have any \"" + rank + "\" in your hand. Try again.");
            }
        }
    }

    // Helper to get ranks from Player's hand if you later expose them; stub here to keep code compiling.
    private String[] currentHandRanks(Player current) {
        // If you add a method in Player like:
        // public String[] getUniqueRanksInHand() { return hand.getUniqueRanks(); }
        // you can call it here instead of this stub.
        return new String[0];
    }

    /**
     * Advance to the next player's turn.
     */
    private void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }

    /**
     * Game ends when:
     *  - all books (13 for standard deck) have been formed, OR
     *  - deck is empty AND all players have no cards.
     */
    private boolean isGameOver() {
        if (totalBooks >= 13) {
            return true;
        }

        if (deck.isEmpty()) {
            boolean allEmpty = true;
            for (Player p : players) {
                if (p.hasCards()) {
                    allEmpty = false;
                    break;
                }
            }
            return allEmpty;
        }

        return false;
    }

    private void updateTotalBooks() {
        int sum = 0;
        for (Player p : players) {
            sum += p.getBooksCount();
        }
        totalBooks = sum;
    }

    private void printResults() {
        System.out.println("\n========== GAME OVER ==========");
        System.out.println("Total books formed: " + totalBooks);

        int best = -1;
        for (Player p : players) {
            System.out.println(p);
            if (p.getBooksCount() > best) {
                best = p.getBooksCount();
            }
        }

        System.out.print("Winner(s): ");
        boolean first = true;
        for (Player p : players) {
            if (p.getBooksCount() == best) {
                if (!first) System.out.print(", ");
                System.out.print(p.getName());
                first = false;
            }
        }
        System.out.println();
    }

    // Example main to run a 2-player game: 1 human, 1 bot
    public static void main(String[] args) {
        String[] names = {"You", "Bot"};
        boolean[] bots = {false, true};

        GoFishGame game = new GoFishGame(names, bots);
        game.play();
    }
}

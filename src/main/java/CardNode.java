public class CardNode {
    private Card data;
    private CardNode nextNode;
    
    // Default constructor
    public CardNode() {
        this.data = null;
        this.nextNode = null;
    }
    
    // Primary constructor
    public CardNode(Card data) {
        this.data = data;
        this.nextNode = null;
    }
    
    // Copy constructor
    public CardNode(CardNode node) {
        this.data = node.data;
        this.nextNode = node.nextNode;
    }
    
    // Getters and Setters
    public Card getData() {
        return data;
    }
    
    public void setData(Card data) {
        this.data = data;
    }
    
    public CardNode getNextNode() {
        return nextNode;
    }
    
    public void setNextNode(CardNode nextNode) {
        this.nextNode = nextNode;
    }
}

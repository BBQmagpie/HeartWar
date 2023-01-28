package sample;

/**
 * Allow only 4 regular suits and joker to exist
 *
 * @author Maggie
 *
 */
enum Suit {
    SPADE, CLUB, HEART, DIAMOND;
}

/**
 * Class for a single card
 *
 * @author Maggie
 *
 */
public class Card implements Comparable<Card> {
    public Suit suit;
    private int cardNum;
    private boolean isMain;

    /**
     * Constructor of the card class
     *
     * @param suit    is the suit of the card.
     * @param cardNum is the sign of the card. 10, 11, 12 refer to J, Q, and K. A is 13
     */
    public Card(Suit suit, int cardNum, boolean isMain) {
        this.suit = suit;
        this.cardNum = cardNum;
        this.isMain = isMain;
    }

    /**
     * Return the suit of the card in string to where the class is called.
     *
     * @return the card's suit.
     */
    public String getSuit() {
        if (suit == Suit.CLUB) {
            return "club";
        } else if (suit == Suit.DIAMOND) {
            return "diamond";
        } else if (suit == Suit.HEART) {
            return "heart";
        } else {
            return "spade";
        }
    }

    /**
     * Return the number of the card to where the class is called.
     *
     * @return the card's number.
     */
    public int getCardNum() {
        return cardNum;
    }

    /**
     * Return if the card is main or not to where the class is called.
     *
     * @return true if the card is main card, false otherwise.
     */
    public boolean getIsMain() {
        return isMain;
    }

    /**
     * Set the card as main when called, used in preparation stage in a game.
     */
    public void setIsMain() {
        isMain = true;
    }

    /**
     * Make card comparable for sorting the cards in a player's hand.
     *
     * @param cardToBeCompared is the other card to be compared with.
     * @return 1 if the original card the method is called on has larger card
     *         number, 0 for being the same, and -1 for being smaller
     */
    public int compareTo(Card cardToBeCompared) {
        if (cardNum > cardToBeCompared.cardNum) {
            return 1;
        } else if (cardNum < cardToBeCompared.cardNum) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Check if two cards are the same in both card number and suit.
     *
     * @param firstCard  is one of the card to be validate.
     * @param secondCard is one of the card to be validate, which ought to be the
     *                   same with the first card.
     * @return true if they are exactly the same, else return false.
     */
    public static boolean sameCard(Card firstCard, Card secondCard) {
        if (firstCard.getSuit().equals(secondCard.getSuit())) {
            if (firstCard.getCardNum() == secondCard.getCardNum()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if four cards are in AABB form.
     *
     * @param card1 is one of the card to be validate, which ought to be the same
     *              with one of the other three.
     * @param card2 is one of the card to be validate, which ought to be the same
     *              with one of the other three.
     * @param card3 is one of the card to be validate, which ought to be the same
     *              with one of the other three.
     * @param card4 is one of the card to be validate, which ought to be the same
     *              with one of the other three.
     * @return true if the four cards are in AABB form, else return false.
     */
    public static boolean twoSameInFour(Card card1, Card card2, Card card3, Card card4) {
        if (sameCard(card1, card2)) {
            if (sameCard(card3, card4)) {
                return true;
            }
        } else if (sameCard(card1, card3)) {
            if (sameCard(card2, card4)) {
                return true;
            }
        } else if (sameCard(card1, card4)) {
            if (sameCard(card2, card3)) {
                return true;
            }
        }
        return false;
    }

}

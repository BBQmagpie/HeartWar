package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * An unchangeable default card deck of 108 cards. The card deck is made of two
 * sets of regular 54-large card deck (13 cards for each suit and 2 jokers).
 *
 * @author Maggie
 *
 */
public class CardDeck {
    private ArrayList<Card> deck;

    /**
     * Construct a card deck containing two sets of regular 54-large card deck.
     */
    public CardDeck() {
        deck = new ArrayList<>();
        for (int j = 0; j < 2; j++) {
            deck.add(new Card(Suit.HEART, 15, true));
            deck.add(new Card(Suit.HEART, 14, true));
            for (int i = 1; i < 14; i++) {
                deck.add(new Card(Suit.SPADE, i, false));
                deck.add(new Card(Suit.CLUB, i, false));
                deck.add(new Card(Suit.HEART, i, false));
                deck.add(new Card(Suit.DIAMOND, i, false));
            }
        }
    }

    /**
     * Get method for the deck variable.
     *
     * @return the whole card deck.
     */
    public ArrayList<Card> get() {
        return deck;
    }

    /**
     * Get a random card in the whole deck.
     *
     * @return a random card in the deck.
     */
    public Card randomlyGetACard() {
        int index = (int) (Math.random() * deck.size());
        return deck.remove(index);
    }
}

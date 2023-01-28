package sample;

import java.util.ArrayList;

/**
 * After the preparation, the 25 cards for each player would be confirmed, and
 * the player is then allowed to play a hand.
 *
 * @author Maggie
 *
 */
public class CardsInHand {
    public ArrayList<Card> cards;
    public int[][] cardMatrix;

    /**
     * Construct up the card list for each player.
     *
     * @param cards is the 25 cards for each player set in the preparation.
     */
    public CardsInHand(ArrayList<Card> cards) {
        cards = new ArrayList<>();
        this.cards = cards;
        cardMatrix = new int[4][15];
    }

    /**
     * Add a new card to the cards ArrayList, used in preparation in a game.
     *
     * @param newCard is the card to be added into the cards.
     */
    public void addCard(Card newCard) {
        cards.add(newCard);
    }

    /**
     * Return the ArrayList of the cards.
     *
     * @return cards.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Check if the cards are all played.
     *
     * @return true if there is no card remaining, else, return false.
     */
    public boolean gameDone() {
        return cards.isEmpty();
    }

    public int playCardsFirst(ArrayList<Card> chosenCards) {
        if (!validateFirstPlayedCards(chosenCards)) {
            return -1;
        } else {
            for (Card chosenCard : chosenCards) {
                int count=0;
                for(Card next: cards){
                    if(chosenCard.getSuit().equals(next.getSuit())){
                        if(chosenCard.getCardNum()==next.getCardNum()){
                            cards.remove(count);
                            break;
                        }
                    }
                    count++;
                }
            }
            traceCards();
            return chosenCards.size();
        }
    }

    /**
     *
     * @param chosenCards refers to the cards chosen by the player from the cards
     *                    he/she has.
     * @return true if the cards are played, remove the played cards at the same
     *         time. Else, do nothing and return false.
     */
    public boolean playCardsFollow(ArrayList<Card> chosenCards, ArrayList<Card> firstHand) {
        if (!validateFollowingPlayedCards(chosenCards, firstHand)) {
            return false;
        }
        for (Card chosenCard : chosenCards) {
            int count=0;
            for(Card next: cards){
                if(chosenCard.getSuit().equals(next.getSuit())){
                    if(chosenCard.getCardNum()==next.getCardNum()){
                        cards.remove(count);
                        break;
                    }
                }
                count++;
            }
        }
        traceCards();
        return true;
    }

    /**
     * Sort the cards, jokers at left, then the mains from ace to KQJ to 10 to 1,
     * then the vice in the same order from spade to club to heart to diamond
     */
    public void sortCards(Suit mainSuit) {
        ArrayList<Card> temp = new ArrayList<>();
        ArrayList<Card> spades = new ArrayList<>();
        ArrayList<Card> clubs = new ArrayList<>();
        ArrayList<Card> hearts = new ArrayList<>();
        ArrayList<Card> diamonds = new ArrayList<>();
        for (Card next : cards) {
            if (next.suit == Suit.CLUB) {
                clubs.add(next);
            } else if (next.suit == Suit.DIAMOND) {
                diamonds.add(next);
            } else if (next.suit == Suit.HEART) {
                hearts.add(next);
            } else if (next.suit == Suit.SPADE) {
                spades.add(next);
            }
        }
        clubs = sortCards(clubs);
        diamonds = sortCards(diamonds);
        hearts = sortCards(hearts);
        spades = sortCards(spades);
        temp.addAll(hearts);
        temp.addAll(clubs);
        temp.addAll(diamonds);
        temp.addAll(spades);
        cards = temp;
    }

    /**
     * Check if the first hand cards are played correctly.
     *
     * @param chosenCards refers to the cards chosen by the player from the cards
     *                    he/she has.
     * @return true only if the cards are in correct form.
     */
    private boolean validateFirstPlayedCards(ArrayList<Card> chosenCards) {
        // check if form correct (A, AA, AABB only)
        if (chosenCards.size() == 1) {
            return true;
        } else if (chosenCards.size() == 2) {
            return Card.sameCard(chosenCards.get(0), chosenCards.get(1));
        } else if (chosenCards.size() == 4) {
            return Card.twoSameInFour(chosenCards.get(0), chosenCards.get(1), chosenCards.get(2), chosenCards.get(3));
        }
        return false;
    }

    /**
     * Check if the following cards are played correctly.
     *
     * @param chosenCards refers to the cards chosen by the player from the cards
     *                    he/she has.
     * @param firstHand   refers to the already played first hand cards to be
     *                    compared to.
     * @return true only if the cards are in correct form.
     */
    private boolean validateFollowingPlayedCards(ArrayList<Card> chosenCards, ArrayList<Card> firstHand) {
        // size check done in Game.minorRoundPlayFollow
        // form check already done in validateFirstPlayedCards()
        // first check if the card played is single
        String firstSuit = firstHand.get(0).getSuit();
        if (firstHand.size() == 1) {
            if (chosenCards.get(0).getSuit().equals(firstSuit)) {
                return true;
            } else {
                int r = findRow(firstSuit);
                for (int c = 0; c < 15; c++) {
                    if (cardMatrix[r][c] >= 1) {
                        return false;
                    }
                }
                return true;
            }
        }
        // if AA, then we need to check if there are available AA in the same suit
        // same for AABB
        if (sameForm(firstHand)) {
            int form = firstHand.size();
            // then the player should play in the same form and suit
            if (form == 2 && Card.sameCard(chosenCards.get(0), chosenCards.get(1))) {
                return true;
            } else if (form == 4 && Card.twoSameInFour(chosenCards.get(0), chosenCards.get(1), chosenCards.get(2),
                    chosenCards.get(3))) {
                return true;
            } else {
                System.out.println("You should play in the same form but you didnt");
                return false;
            }
        } else {
            // then the player should first play all the cards in the same suit and then the
            // others
            int remain = restInSameSuit(firstHand);
            if (remain >= firstHand.size()) {
                // enough cards of same suit to play
                for (Card next : chosenCards) {
                    if (!next.getSuit().equals(firstSuit)) {
                        System.out.println("You should play in the same suit but you didnt");
                        return false;
                    }
                }
                return true;
            } else {
                // there aren't enough cards of the same suit to play
                if (chosenHasAll(chosenCards,sameSuitCard(firstSuit))) {
                    return true;
                } else {
                    System.out.println("You should play all in the same suit first but you didnt");
                    return false;
                }
            }
        }
    }

    /**
     * Check if there exists at least one set of same form cards in the same suit of
     * the first hand played cards.
     *
     * @param firstHand refers to the already played first hand cards.
     * @return true if there exists same form cards in the same suit, false
     *         otherwise.
     */
    private boolean sameForm(ArrayList<Card> firstHand) {
        int form = firstHand.size();
        int r = findRow(firstHand.get(0).getSuit());
        int count = 0;
        for (int c = 0; c < 15; c++) {
            if (cardMatrix[r][c] == 2) {
                count++;
            }
        }
        if (form == 2 && count > 0 || form == 4 && count > 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sort the input array list from large card number to smaller ones
     *
     * @param list is the array list of card to be sorted.
     * @return the sorted array list.
     */
    public ArrayList<Card> sortCards(ArrayList<Card> list) {
        Card[] cardArray = new Card[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cardArray[i] = list.get(i);
        }
        Merge.sort(cardArray);
        ArrayList<Card> temp = new ArrayList<>();
        for (int i = cardArray.length - 1; i >= 0; i--) {
            temp.add(cardArray[i]);
        }
        return temp;
    }

    /**
     * Keep track of all the cards in hand, save time for finding cards to play.
     * Called when the cards are successfully played only.
     */
    public void traceCards() {
        cardMatrix=new int[4][15];
        for (Card next : cards) {
            if (next.getSuit().equals("spade")) {
                cardMatrix[0][next.getCardNum() - 1]++;
            } else if (next.getSuit().equals("club")) {
                cardMatrix[1][next.getCardNum() - 1]++;
            } else if (next.getSuit().equals("diamond")) {
                cardMatrix[2][next.getCardNum() - 1]++;
            } else {
                cardMatrix[3][next.getCardNum() - 1]++;
            }
        }
    }

    /**
     * Find in which row of cardMatrix[][] is this suit of cards recorded.
     *
     * @param suit refers to the suit of the cards to be check.
     * @return the row corresponds to the suit in cardMatrix[][].
     */
    public static int findRow(String suit) {
        if (suit.equals("spade")) {
            return 0;
        } else if (suit.equals("club")) {
            return 1;
        } else if (suit.equals("diamond")) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * Get the amount of cards in this player's hand that are of the same suit with
     * the first hand played cards.
     *
     * @param firstHand refers to the already played first hand cards.
     * @return the amount of cards in this player's hand that are of the same suit
     *         with the first hand played cards.
     */
    private int restInSameSuit(ArrayList<Card> firstHand) {
        int r = findRow(firstHand.get(0).getSuit());
        int count = 0;
        for (int c = 0; c < 15; c++) {
            count = count + cardMatrix[r][c];
        }
        return count;
    }

    /**
     * Get an array list of all the cards of the same suit required in player's
     * hands.
     *
     * @param suit is the suit of the cards to check.
     * @return all cards of the same suit remaining in the player's hands.
     */
    private ArrayList<Card> sameSuitCard(String suit) {
        ArrayList<Card> sameSuit = new ArrayList<>();
        int r = findRow(suit);
        for (int c = 0; c < 15; c++) {
            if (cardMatrix[r][c] > 0) {
                sameSuit.add(new Card(toSuit(suit), c + 1, suit.equals("heart")));
            }
        }
        return sameSuit;
    }

    /**
     * Change the String expression of suit to Suit expression.
     *
     * @param suit is the suit of the cards required.
     * @return the Suit expression of parameter suit.
     */
    private Suit toSuit(String suit) {
        if (suit.equals("spade")) {
            return Suit.SPADE;
        } else if (suit.equals("club")) {
            return Suit.CLUB;
        } else if (suit.equals("diamond")) {
            return Suit.DIAMOND;
        } else {
            return Suit.HEART;
        }
    }

    private boolean chosenHasAll(ArrayList<Card> chosenCards, ArrayList<Card> sameSuit){
        //System.out.println(chosenCards.size());
        //System.out.println(sameSuit.size());
        for(Card nextSameSuit: sameSuit){
            for(int i=0;i<chosenCards.size();i++){
                Card nextChosen=chosenCards.get(i);
                //System.out.println("Card in same suit: "+nextSameSuit.getSuit()+nextSameSuit.getCardNum());
                //System.out.println("Card in chosen cards: "+nextChosen.getSuit()+nextChosen.getCardNum());
                if(nextSameSuit.getSuit().equals(nextChosen.getSuit())){
                    if(nextSameSuit.getCardNum()==nextChosen.getCardNum()){
                        //System.out.println("found");
                        //if this card find the same in chosen cards, go to search for the next card
                        break;
                    }
                }
                if(i==chosenCards.size()-1){
                    return false;
                }
            }
        }
        return true;
    }

}

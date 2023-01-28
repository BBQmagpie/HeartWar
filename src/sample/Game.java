package sample;

import java.util.ArrayList;
import java.util.LinkedList;

enum Player {
    YOU, PLAYERA, PLAYERB, PLAYERC;
}

public class Game {
    private Suit main;
    public final Player[] players = { Player.YOU, Player.PLAYERA, Player.PLAYERB, Player.PLAYERC };
    public ComPlayer[] computers;
    public int firstHand; // 0-3
    public ArrayList<Card> base;
    private int score;
    public CardsInHand[] cardsForPlayers;
    public ArrayList<Card>[] cardPool;
    private int host;

    /**
     * Constructor of a single major round of game.
     *
     * @param host is the last winner, which would be the first hand of the first
     *             minor round in this game.
     */
    public Game(int host) {
        this.host=host;
        computers=new ComPlayer[3];
        firstHand = host;
        cardsForPlayers = new CardsInHand[4];
        cardPool = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            cardsForPlayers[i] = new CardsInHand(new ArrayList<>());
            cardPool[i] = new ArrayList<Card>();
        }
        score = 0;
    }

    /**
     * Set cards for all the players and the main.
     */
    public void preparation() {
        CardDeck cardDeck = new CardDeck();
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 4; j++) {
                cardsForPlayers[j].addCard(cardDeck.randomlyGetACard());
            }
        }
        base = cardDeck.get();
        // set main
        main = Suit.HEART;
        for (int i = 0; i < 4; i++) {
            cardsForPlayers[i].traceCards();
            ArrayList<Card> cardsForAPlayer = cardsForPlayers[i].getCards();
            for (Card next : cardsForAPlayer) {
                if (next.getSuit().equals("heart")) {
                    next.setIsMain();
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            cardsForPlayers[i].sortCards(main);
        }
        for(int i=0;i<3;i++){
            computers[i]=new ComPlayer(cardsForPlayers[i+1],MainProgram.diff.get(i));
        }
    }

    /**
     * Let the first hand player try to play a hand.
     *
     * @param cardsToPlay refers to the cards chosen by the player in the interface.
     * @return true if the played cards are valid, false otherwise and the cards
     *         played would not be accepted in the pool.
     */
    public boolean minorRoundFirstPlay(ArrayList<Card> cardsToPlay) {
        int firstPlayer = firstHand;
        if (cardsForPlayers[firstPlayer].playCardsFirst(cardsToPlay) == -1) {
            return false;
        } else {
            cardPool[firstHand].addAll(cardsToPlay);
            return true;
        }
    }

    /**
     * Let one of the following players try to play a hand.
     *
     * @param cardsToPlay refers to the cards chosen by the player in the interface.
     * @param playerNum   refers to the index of the next player.
     * @return true if the played cards are valid, false otherwise and the cards
     *         played would not be accepted in the pool.
     */
    public boolean minorRoundPlayFollow(ArrayList<Card> cardsToPlay, int playerNum) {
        if (cardsToPlay.size() == cardPool[firstHand].size()) {
            if (cardsForPlayers[playerNum].playCardsFollow(cardsToPlay, cardPool[firstHand])) {
                cardPool[playerNum].addAll(cardsToPlay);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Get the score on the attacker-side.
     *
     * @return the score.
     */
    public int getScore() {
        return score;
    }

    /**
     * After each minor round, decide who has played the largest to be the next
     * first hand player. Add scores also.
     */
    public void decideFirstHand() {
        int nextFirst = 0;
        int currentFirst = firstHand;
        // if single
        if (cardPool[firstHand].size() == 1) {
            nextFirst = findMaxInFourSingle(cardPool);
        }
        // if AA
        else if (cardPool[firstHand].size() == 2) {
            nextFirst = findMaxInFourOfTwos(cardPool);
        }
        // if AABB
        else if (cardPool[firstHand].size() == 4) {
            nextFirst = findMaxInFourOfFours(cardPool);
        }
        firstHand = nextFirst;
        //scores are only added to the attacker team
        if(nextFirst!=host && host!=loopInFour(loopInFour(nextFirst))){
            score=score+addUpScores();
            System.out.println(score);
        }
        cardPool[0]=new ArrayList<>();
        cardPool[1]=new ArrayList<>();
        cardPool[2]=new ArrayList<>();
        cardPool[3]=new ArrayList<>();
    }

    /**
     * Find the next integer to the parameter, index. If the result equals to 4, it
     * means that the result should be 0 again.
     *
     * @param index is the integer to find next of.
     * @return the next integer to index in 0-3.
     */
    public static int loopInFour(int index) {
        if (index + 1 > 3) {
            index = 0;
        } else {
            index++;
        }
        return index;
    }

    /**
     * Add up all the points that could be found within the pool. Only 5, 10 and K
     * could give points.
     *
     * @return the total points in the pool.
     */
    private int addUpScores() {
        int scoreInPool = 0;
        for (int i = 0; i < 4; i++) {
            for (Card next : cardPool[i]) {
                if (next.getCardNum() == 12 || next.getCardNum() == 9) {
                    scoreInPool += 10;
                } else if (next.getCardNum() == 4) {
                    scoreInPool += 5;
                }
            }
        }
        return scoreInPool;
    }

    /**
     * Called when a game is finishing. The usage is to add up scores in the base cards for winner team.
     * Only when the winner of the last minor round is the attacker team would the scores be added to total.
     */
    public int baseScores(int winnerOfFinalRound){
        int scoreInBase=0;
        for(Card next : base){
            if (next.getCardNum() == 12 || next.getCardNum() == 9) {
                scoreInBase += 10;
            } else if (next.getCardNum() == 4) {
                scoreInBase += 5;
            }
        }
        if(winnerOfFinalRound!=host && host!=loopInFour(loopInFour(winnerOfFinalRound))){
            score+=scoreInBase;
            return scoreInBase;
        }else{
            return -scoreInBase;
        }
    }

    /**
     * Find the maximum card within four single cards.
     *
     * @param pool is the arrayList that collects the four single cards played in
     *             order of the player.
     * @return the index of the largest card played.
     */
    private int findMaxInFourSingle(ArrayList<Card>[] pool) {
        Card[] poolArray = new Card[4];
        for (int i = 0; i < 4; i++) {
            poolArray[i] = pool[i].get(0);
        }
        // if main, positive card num, if vice and same with firstHand's suit,
        // card num-12 (so that vice A is 0), else (vice and different suit), negative
        // 15
        int[] points = new int[4];
        int maxPoint = -16;
        int maxIndex = 0;
        for (int i = 0; i < 4; i++) {
            if (poolArray[i].getIsMain()) {
                points[i] = poolArray[i].getCardNum();
            } else if (poolArray[i].getSuit().equals(poolArray[firstHand].getSuit())) {
                points[i] = poolArray[i].getCardNum()-13;
            } else {
                points[i] = -15;
            }
            if (points[i] > maxPoint) {
                maxIndex = i;
                maxPoint = points[i];
            }
        }
        return maxIndex;
    }

    /**
     * Find the maximum card set played among four players. The card set in the
     * correct form is considered first.
     *
     * @param pool is the arrayList that collects the four sets of two cards played
     *             in order of the player.
     * @return the index of the largest card set played.
     */
    private int findMaxInFourOfTwos(ArrayList<Card>[] pool) {
        int[] points = new int[4];
        for (int i = 0; i < 4; i++) {
            if (Card.sameCard(pool[i].get(0), pool[i].get(1))) {
                points[i] += 100;
            }
        }
        Card[] poolArray = new Card[4];
        for (int i = 0; i < 4; i++) {
            poolArray[i] = pool[i].get(0);
        }
        int maxPoint = -16;
        int maxIndex = 0;
        for (int i = 0; i < 4; i++) {
            if (poolArray[i].getIsMain()) {
                points[i] += poolArray[i].getCardNum();
            } else if (poolArray[i].getSuit().equals(poolArray[firstHand].getSuit())) {
                points[i] += poolArray[i].getCardNum()-13;
            } else {
                points[i] += -15;
            }
            if (points[i] > maxPoint) {
                maxIndex = i;
                maxPoint = points[i];
            }
        }
        return maxIndex;
    }

    /**
     * Find the maximum card set played among four players. The card set in the
     * correct form is considered first.
     *
     * @param pool is the arrayList that collects the four sets of four cards played
     *             in order of the player.
     * @return the index of the largest card set played.
     */
    private int findMaxInFourOfFours(ArrayList<Card>[] pool) {
        int[] points = new int[4];
        for (int i = 0; i < 4; i++) {
            if (Card.twoSameInFour(pool[i].get(0), pool[i].get(1), pool[i].get(2), pool[i].get(3))) {
                points[i] += 100;
            }
        }
        int maxPoint = -16;
        int maxIndex = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (pool[i].get(j).getIsMain()) {
                    points[i] += pool[i].get(j).getCardNum();
                } else if (pool[i].get(j).getSuit().equals(pool[firstHand].get(0).getSuit())) {
                    points[i] += pool[i].get(j).getCardNum()-13;
                } else {
                    points[i] += -15;
                }
            }
            if (points[i] > maxPoint) {
                maxIndex = i;
                maxPoint = points[i];
            }
        }
        return maxIndex;
    }
}

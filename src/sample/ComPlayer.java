package sample;

import java.util.ArrayList;

enum Difficulty{
    EASY,HARD,EXPERT;
}

public class ComPlayer {
    private CardsInHand cards;
    private Difficulty diff;

    public ComPlayer(CardsInHand cards, String diff){
        if(diff.equals("ez")){
            this.diff=Difficulty.EASY;
        }else if(diff.equals("hd")){
            this.diff=Difficulty.HARD;
        }else{
            this.diff=Difficulty.EXPERT;
        }
        this.cards=cards;
    }

    public ArrayList<Card> mainStation(boolean firstHand, Game game, int count){
        if(diff==Difficulty.EASY){
            return ezPlay(firstHand,game,count);
        }else if(diff==Difficulty.HARD){
            return hdPlay(firstHand,game,count);
        }else{
            return null;
            //expert players have not been developed and this difficulty would never appear in the current version
        }
    }

    private ArrayList<Card> ezPlay(boolean firstHand, Game game, int count){
        if(firstHand){
            ArrayList<Card> chosenCard=new ArrayList<>();
            int randomCard=(int) (Math.random() * cards.cards.size());
            chosenCard.add(cards.cards.get(randomCard));
            return chosenCard;
        }else{
            return ezPlayFollow(game,count);
        }
    }

    private ArrayList<Card> ezPlayFollow(Game game, int count){
        ArrayList<Card> chosenCard=new ArrayList<>();
        ArrayList<Card> firstPlayed=game.cardPool[game.firstHand];
        if(firstPlayed.size()==1){
            int randomCard=(int) (Math.random() * cards.cards.size());
            chosenCard.add(cards.cards.get(randomCard));
            return chosenCard;
        }else {
            //check if there are any AA remaining in the same suit of the first hand
            ArrayList<Card> twoSame=new ArrayList<>();
            int[][] cardMatrix=cards.cardMatrix;
            int row=CardsInHand.findRow(firstPlayed.get(0).getSuit());
            for(int c=0;c<15;c++) {
                if (cardMatrix[row][c] == 2) {
                    twoSame.add(new Card(firstPlayed.get(0).suit, c + 1, firstPlayed.get(0).getIsMain()));
                }
            }
            if (firstPlayed.size() == 2) {
                //if have, play AA
                if(twoSame.size()>0){
                    int randomCard=(int)(Math.random()*twoSame.size());
                    chosenCard.add(twoSame.get(randomCard));
                    chosenCard.add(twoSame.get(randomCard));
                    return chosenCard;
                }else{
                    //do not have, play any others in the same suit first, then any others
                    for(int c=0;c<15;c++){
                        if(cardMatrix[row][c]==1){
                            chosenCard.add(new Card(firstPlayed.get(0).suit, c + 1, firstPlayed.get(0).getIsMain()));
                        }
                        if(chosenCard.size()==2){
                            return chosenCard;
                        }
                    }
                    chosenCard=getRandCards(chosenCard,2-chosenCard.size());
                    return chosenCard;
                }
            } else {
                //if have, play AABB first
                if(twoSame.size()>1){
                    int randomCard=(int)(Math.random()*twoSame.size());
                    chosenCard.add(twoSame.get(randomCard));
                    chosenCard.add(twoSame.get(randomCard));
                    int randomCard2=(int)(Math.random()*twoSame.size());
                    while(randomCard2==randomCard){
                        randomCard2=(int)(Math.random()*twoSame.size());
                    }
                    chosenCard.add(twoSame.get(randomCard2));
                    chosenCard.add(twoSame.get(randomCard2));
                    return chosenCard;
                }else{
                    //do not have, play any others
                    for(int c=0;c<15;c++){
                        if(cardMatrix[row][c]==1){
                            chosenCard.add(new Card(firstPlayed.get(0).suit, c+1 , firstPlayed.get(0).getIsMain()));
                        }
                        if(chosenCard.size()==4){
                            return chosenCard;
                        }
                    }
                    chosenCard=getRandCards(chosenCard,4-chosenCard.size());
                    return chosenCard;
                }
            }
        }
    }

    private ArrayList<Card> hdPlay(boolean firstHand, Game game, int count){
        if(firstHand){
            return hdPlayFirst(game);
        }else{
            return hdPlayFollow(game,count);
        }
    }

    private ArrayList<Card> hdPlayFirst(Game game){
        ArrayList<Card> chosenCard=new ArrayList<>();
        int[][] cardTrace=cards.cardMatrix;
        Suit[] suits={Suit.SPADE,Suit.CLUB,Suit.DIAMOND,Suit.HEART};
        //hard computer player plays all AABB first, then AA, then largest vise card
        for(int r=0;r<4;r++){
            ArrayList<Card> possibleAA=new ArrayList<>();
            for(int c=0;c<15;c++){
                if(cardTrace[r][c]>1&&r<3){
                    possibleAA.add(new Card(suits[r],c+1,false));
                }else if(cardTrace[r][c]>1&&r==3){
                    possibleAA.add(new Card(suits[r],c+1,true));
                }
            }
            //after a suit is searched, return AABB first if possible
            if(possibleAA.size()>1){
                int randomCard=(int)(Math.random()*possibleAA.size());
                chosenCard.add(possibleAA.get(randomCard));
                chosenCard.add(possibleAA.get(randomCard));
                int randomCard2=(int)(Math.random()*possibleAA.size());
                while(randomCard2==randomCard){
                    randomCard2=(int)(Math.random()*possibleAA.size());
                }
                chosenCard.add(possibleAA.get(randomCard2));
                chosenCard.add(possibleAA.get(randomCard2));
                return chosenCard;
            }else if(!possibleAA.isEmpty()){
                //return possible AA next
                chosenCard.add(possibleAA.get(0));
                chosenCard.add(possibleAA.get(0));
                return chosenCard;
            }
        }
        //if no AABB or AA could play, randomly choose a vice suit to play the largest
        int randomSuit=(int)(Math.random()*3);
        for(int c=14;c>=0;c--){
            if(cardTrace[randomSuit][c]==1){
                chosenCard.add(new Card(suits[randomSuit],c+1,false));
                return chosenCard;
            }
        }
        //if no card in that suit, play a small card in main
        //these two strategies are both suitable
        for(int c=0;c<15;c++){
            if(cardTrace[3][c]==1){
                chosenCard.add(new Card(suits[3],c+1,true));
                return chosenCard;
            }
        }
        //if still nothing to play, return a random card in hand
        int randCard=(int)(Math.random()*cards.cards.size());
        chosenCard.add(cards.cards.get(randCard));
        return chosenCard;
    }

    private ArrayList<Card> hdPlayFollow(Game game, int count){
        ArrayList<Card> chosenCard=new ArrayList<>();
        ArrayList<Card> firstPlayed=game.cardPool[game.firstHand];
        if(firstPlayed.size()==1){
            //see if points exist, play large if so
            int randomCard=(int) (Math.random() * cards.cards.size());
            chosenCard.add(cards.cards.get(randomCard));
            return chosenCard;
        }else {
            //check if there are any AA remaining in the same suit of the first hand
            ArrayList<Card> twoSame=new ArrayList<>();
            int[][] cardMatrix=cards.cardMatrix;
            int row=CardsInHand.findRow(firstPlayed.get(0).getSuit());
            for(int c=0;c<15;c++) {
                if (cardMatrix[row][c] == 2) {
                    twoSame.add(new Card(firstPlayed.get(0).suit, c + 1, firstPlayed.get(0).getIsMain()));
                }
            }
            if (firstPlayed.size() == 2) {
                //if have, play AA
                if(twoSame.size()>0){
                    int randomCard=(int)(Math.random()*twoSame.size());
                    chosenCard.add(twoSame.get(randomCard));
                    chosenCard.add(twoSame.get(randomCard));
                    return chosenCard;
                }else{
                    //do not have, play any others in the same suit first, then any others
                    for(int c=0;c<15;c++){
                        if(cardMatrix[row][c]==1){
                            chosenCard.add(new Card(firstPlayed.get(0).suit, c + 1, firstPlayed.get(0).getIsMain()));
                        }
                        if(chosenCard.size()==2){
                            return chosenCard;
                        }
                    }
                    chosenCard=getRandCards(chosenCard,2-chosenCard.size());
                    return chosenCard;
                }
            } else {
                //if have, play AABB first
                if(twoSame.size()>1){
                    int randomCard=(int)(Math.random()*twoSame.size());
                    chosenCard.add(twoSame.get(randomCard));
                    chosenCard.add(twoSame.get(randomCard));
                    int randomCard2=(int)(Math.random()*twoSame.size());
                    while(randomCard2==randomCard){
                        randomCard2=(int)(Math.random()*twoSame.size());
                    }
                    chosenCard.add(twoSame.get(randomCard2));
                    chosenCard.add(twoSame.get(randomCard2));
                    return chosenCard;
                }else{
                    //do not have, play any others
                    for(int c=0;c<15;c++){
                        if(cardMatrix[row][c]==1){
                            chosenCard.add(new Card(firstPlayed.get(0).suit, c+1 , firstPlayed.get(0).getIsMain()));
                        }
                        if(chosenCard.size()==4){
                            return chosenCard;
                        }
                    }
                    chosenCard=getRandCards(chosenCard,4-chosenCard.size());
                    return chosenCard;
                }
            }
        }
    }

    private ArrayList<Card> getRandCards(ArrayList<Card> chosenCards, int difference){
        ArrayList<Card> originalCards=new ArrayList<>();
        originalCards.addAll(cards.cards);
        for(Card nextToTest:chosenCards){
            int count=0;
            for(Card next:originalCards){
                if(next.getCardNum()==nextToTest.getCardNum() && next.getSuit().equals(nextToTest.getSuit())){
                    originalCards.remove(count);
                    break;
                }
                count++;
            }
        }
        if(difference>=1){
            int randomCard=(int) (Math.random() * originalCards.size());
            chosenCards.add(originalCards.get(randomCard));
            originalCards.remove(randomCard);
            if(difference>=2){
                randomCard=(int) (Math.random() * originalCards.size());
                chosenCards.add(originalCards.get(randomCard));
                originalCards.remove(randomCard);
                if(difference>=3){
                    randomCard=(int) (Math.random() * originalCards.size());
                    chosenCards.add(originalCards.get(randomCard));
                    originalCards.remove(randomCard);
                    if(difference==4){
                        randomCard=(int) (Math.random() * originalCards.size());
                        chosenCards.add(originalCards.get(randomCard));
                        originalCards.remove(randomCard);
                    }
                }
            }
        }
        return chosenCards;
    }
}

package sample;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GamePageGUI extends Parent {
    private ArrayList<Card> chosenCards;
    private int currentPlayer;
    private int count;
    private Button play;
    private Button clear;
    private Button next;
    private Pane cardPool;

    public GamePageGUI(Game game, int currentPlayer, int count){
        System.out.println("Stage has been reset.");
        showCardPool(game);

        System.out.println(count);
        this.count=count;

        //return to home page when a game finishes
        if(game.cardsForPlayers[0].cards.isEmpty()&&game.cardsForPlayers[1].cards.isEmpty()
                &&game.cardsForPlayers[2].cards.isEmpty()&&game.cardsForPlayers[3].cards.isEmpty()){
            //System.out.println("Game ends");
            int baseScore=game.baseScores(currentPlayer);
            MainProgram.majorRoundEnd(game);
            //System.out.println("data updated");
            //show final result on the screen
            Pane backGround=new Pane();
            backGround.setPrefSize(960,600);
            backGround.getStylesheets().add("sample/Style.css");
            Label finalScore=new Label("Final Score for the attackers: "+game.getScore());
            finalScore.setLayoutX(350);
            finalScore.setLayoutY(200);
            String finalRoundWinner;
            if(baseScore>0){
                finalRoundWinner=" goes to the attackers";
            }else if(baseScore<0){
                finalRoundWinner=" goes to the defenders";
            }else{
                finalRoundWinner="";
            }
            Label base=new Label("Base score "+Math.abs(baseScore)+finalRoundWinner);
            base.setLayoutX(350);
            base.setLayoutY(250);
            Button returnHome=new Button("Return");
            returnHome.getStyleClass().add("button");
            returnHome.setLayoutX(400);
            returnHome.setLayoutY(300);
            returnHome.setOnMouseClicked(e->{
                Scene home=new Scene(new HomePageGUI(),960,600);
                Main.getStage().setScene(home);
            });
            Pane baseCards=baseCards(game);
            baseCards.setLayoutX(200);
            baseCards.setLayoutY(320);
            backGround.getChildren().addAll(finalScore,base,returnHome,baseCards);
            getChildren().addAll(backGround);
        }else{
            //starting a new minor round
            if(count==0){
                System.out.println("This is a new round, starting from player "+currentPlayer);
                this.currentPlayer=game.firstHand;
            }else{
                this.currentPlayer=currentPlayer;
            }
            chosenCards=new ArrayList<>();

            Pane backGround=new Pane();
            backGround.setPrefSize(960,600);
            backGround.getStylesheets().add("sample/Style.css");

            //display all the cards on main controller's hand
            Pane MCcards=MCcards(game);
            MCcards.setLayoutX(50);
            MCcards.setLayoutY(480);
            backGround.getChildren().add(MCcards);

            //let the current player play
            boolean first=false;
            if(currentPlayer==0&&count<4){
                System.out.println("Player is playing "+currentPlayer);
                if(count==0) {
                    first = true;
                }
            }
            play=playButton(game,first);
            clear=clearButton(game);
            next=nextButton(game);
            if(currentPlayer==0&&count<4){
                backGround.getChildren().addAll(play,clear);
            }else if(count<=4){
                backGround.getChildren().add(next);
            }

            if(currentPlayer!=0&&count==0){
                System.out.println("Computer is playing "+currentPlayer);
                comPlay(true,game,currentPlayer);
            }else if(currentPlayer!=0&&count<4){
                System.out.println("Computer is playing "+currentPlayer);
                comPlay(false,game,currentPlayer);
            }

            //gaming instruction
            Button hint=new Button();
            hint.setPrefSize(20,15);
            hint.setText("?");
            hint.setLayoutX(920);
            hint.setLayoutY(10);
            hint.setOnMouseClicked(e->{
                StackPane instructionPage=new StackPane();
                Label instru=new Label("Rules:\n" +
                        "1.\tAll players would have 25 cards randomly drawn from two decks of \ncards, while the 8 remaining cards serve as the base cards.\n" +
                        "2.\tFor each minor round you would either play as the first-hand player \nor just play following the others in a counterclockwise order:\n" +
                        "i.\tIf you are the first-hand player, you should play a single card, \ntwo same cards (a double set), or two double sets of the same suit.\n" +
                        "ii.\tIf you play follow, firstly you should play out all the possible \ncards in the same form and suit as the first-hand; secondly, \nyou should at least play cards in the same suit; if none could be achieved, \nplay any cards you have until the amount played equals to the first-hand’s size.\n" +
                        "3.\tThe first-hand player of each next minor round is the player who \nplayed the largest set during last round.\n" +
                        "4.\tAll scores in the pool (5 for 5 points, 10 and K for 10 points) \ngo to the winner of the minor round.\n" +
                        "5.\tIf the attackers gain over 80 points in total, the attacker team’s \nmember next to the current host would be the next host. \nFor each 40 points over 80 points, the ranking of attacker team increases by 1.\n" +
                        "6.\tIf the attackers gain less than 30 points, the defender team’s \nranking would increase by 1. 0 point gained leads to an additional increment of 1.\n");
                instructionPage.getChildren().add(instru);
                Scene instructionScene=new Scene(instructionPage,500,400);
                Stage newWindow=new Stage();
                newWindow.setTitle("Instruction");
                newWindow.setScene(instructionScene);
                newWindow.show();
            });
            backGround.getChildren().add(hint);

            //show cards in the pool
            cardPool=new Pane();
            cardPool.setPrefSize(760,215);
            cardPool.setLayoutX(100);
            cardPool.setLayoutY(100);
            showCardPoolPane(game);
            backGround.getChildren().add(cardPool);

            //show round information
            int currentPlayerInt=currentPlayer+1;
            int firstHandInt=game.firstHand+1;
            int hostInt=MainProgram.host+1;
            int scoreInt=game.getScore();
            Label currentPlayerInfo=new Label("Currently Playing: Player "+currentPlayerInt+" | Round Beginner: Player "+firstHandInt+" | Host: Player"+hostInt+" | Attackers' Score: "+scoreInt);
            currentPlayerInfo.setLayoutX(250);
            currentPlayerInfo.setLayoutY(445);
            backGround.getChildren().addAll(currentPlayerInfo);

            //show cards in computers' hands and how many are left for them
            Image stackImage=new Image("img/backStack3.png");
            Rectangle backStack1=new Rectangle(150,375);
            Label com1Cards=new Label("Player 2: "+game.cardsForPlayers[1].cards.size());
            backStack1.setFill(new ImagePattern(stackImage));
            backStack1.setLayoutX(860);
            backStack1.setLayoutY(80);
            com1Cards.setLayoutX(860);
            com1Cards.setLayoutY(65);
            backGround.getChildren().addAll(backStack1,com1Cards);
            stackImage=new Image("img/backStack2.png");
            Rectangle backStack2=new Rectangle(500,200);
            Label com2Cards=new Label("Player 3: "+game.cardsForPlayers[2].cards.size());
            backStack2.setFill(new ImagePattern(stackImage));
            backStack2.setLayoutX(200);
            backStack2.setLayoutY(-100);
            com2Cards.setLayoutX(200);
            com2Cards.setLayoutY(100);
            backGround.getChildren().addAll(backStack2,com2Cards);
            stackImage=new Image("img/backStack1.png");
            Rectangle backStack3=new Rectangle(150,375);
            Label com3Cards=new Label("Player 4: "+game.cardsForPlayers[3].cards.size());
            backStack3.setFill(new ImagePattern(stackImage));
            backStack3.setLayoutX(-50);
            backStack3.setLayoutY(40);
            com3Cards.setLayoutX(0);
            com3Cards.setLayoutY(25);
            backGround.getChildren().addAll(backStack3,com3Cards);


            getChildren().addAll(backGround);
        }

    }

    private Pane MCcards(Game game){
        Pane board=new Pane();
        board.setPrefSize(500,200);
        ArrayList<Card> cards=game.cardsForPlayers[0].cards;
        int count=0;
        for(Card next:cards){
            Image cardImage=new Image("img/"+next.getSuit()+","+next.getCardNum()+".png");
            Rectangle card=new Rectangle(150,200);
            card.setFill(new ImagePattern(cardImage));
            card.setLayoutX(count*35);
            card.setLayoutY(0);
            card.setOnMouseClicked(mouseEvent -> {
                chosenCards.add(next);
                card.setLayoutY(-25);
                card.setOnMouseClicked(e->{});
            });
            board.getChildren().add(card);
            count++;
        }

        return board;
    }

    private Pane baseCards(Game game){
        Pane board=new Pane();
        board.setPrefSize(600,300);
        ArrayList<Card> baseCards=game.base;
        for(int i=0;i<8;i++){
            Card next=baseCards.get(i);
            Image cardImage=new Image("img/"+next.getSuit()+","+next.getCardNum()+".png");
            Rectangle card=new Rectangle(150,200);
            card.setFill(new ImagePattern(cardImage));
            card.setLayoutX(50*i);
            card.setLayoutY(50+20*Math.pow(-1,i)*Math.random());
            board.getChildren().add(card);
        }
        return board;
    }

    private Button playButton(Game game,boolean first){
        Button play=new Button();
        play.setPrefSize(50,20);
        play.setText("Play");
        play.setLayoutX(100);
        play.setLayoutY(440);
        play.setOnMouseClicked(e->{
            int counter=count+1;
            if(first){
                if(game.minorRoundFirstPlay(chosenCards)){
                    GamePageGUI page=new GamePageGUI(game,1,1);
                    Scene newScene = new Scene(page,960,600);
                    Main.getStage().setScene(newScene);
                }else{
                    chosenCards.clear();
                    GamePageGUI page=new GamePageGUI(game,currentPlayer,count);
                    Scene newScene=new Scene(page,960,600);
                    Main.getStage().setScene(newScene);
                }
            }else{
                if(game.minorRoundPlayFollow(chosenCards,0)){
                    GamePageGUI page=new GamePageGUI(game,1,counter);
                    Scene newScene = new Scene(page,960,600);
                    Main.getStage().setScene(newScene);
                }else{
                    chosenCards.clear();
                    GamePageGUI page=new GamePageGUI(game,currentPlayer,count);
                    Scene newScene=new Scene(page,960,600);
                    Main.getStage().setScene(newScene);
                }
            }
        });
        play.getStyleClass().add("button");
        return play;
    }

    private Button clearButton(Game game){
        Button clear=new Button();
        clear.setPrefSize(50,20);
        clear.setText("Clear");
        clear.setLayoutX(170);
        clear.setLayoutY(440);
        clear.setOnMouseClicked(e->{
            chosenCards.clear();
            GamePageGUI page=new GamePageGUI(game,currentPlayer,count);
            Scene newScene=new Scene(page,960,600);
            Main.getStage().setScene(newScene);
        });
        clear.getStyleClass().add("button");
        return clear;
    }

    private Button nextButton(Game game){
        Button next=new Button();
        next.setPrefSize(50,20);
        next.setText("Next");
        next.setLayoutX(100);
        next.setLayoutY(440);
        int counter=count+1;
        next.setOnMouseClicked(e->{
            if(counter<4){
                Scene newScene=new Scene(new GamePageGUI(game,MainProgram.loopInFour(currentPlayer),counter),960,600);
                Main.getStage().setScene(newScene);
            }else{
                game.decideFirstHand();
                Scene newScene=new Scene(new GamePageGUI(game,game.firstHand,0),960,600);
                Main.getStage().setScene(newScene);
            }
        });
        next.getStyleClass().add("button");
        return next;
    }

    private ArrayList<Card> comPlay(boolean first,Game game,int playerNum){
        ArrayList<Card> comChosenCards=game.computers[playerNum-1].mainStation(first,game,count);
        //printCards(comChosenCards);
        if(first){
            //wait(1000);
            while(!game.minorRoundFirstPlay(comChosenCards)){
                comChosenCards=game.computers[playerNum-1].mainStation(true,game,count);
                //System.out.println("tried again:");
                //printCards(comChosenCards);
            }
            //System.out.println(comChosenCards.get(0).getCardNum()+comChosenCards.get(0).getSuit());
        }else{
            //wait(1000);
            while(!game.minorRoundPlayFollow(comChosenCards,playerNum)){
                comChosenCards=game.computers[playerNum-1].mainStation(false,game,count);
                //System.out.println("tried again:");
                //printCards(comChosenCards);
            }
            //System.out.println(comChosenCards.get(0).getCardNum()+comChosenCards.get(0).getSuit());
        }
        return comChosenCards;
    }

    private void showCardPool(Game game){
        for(int i=0;i<4;i++){
            ArrayList<Card> cards=game.cardPool[i];
            if(cards.isEmpty()){
                System.out.print("null / ");
            }else{
                System.out.print(cards.get(0).getCardNum()+cards.get(0).getSuit()+" / ");
            }
            System.out.println(game.cardsForPlayers[i].cards.size());
        }
    }

    private void showCardPoolPane(Game game){
        for(int i=0;i<4;i++){
            ArrayList<Card> cards=game.cardPool[i];
            if(!cards.isEmpty()){
                Pane individualPool=new Pane();
                individualPool.setPrefSize(240,160);
                if(i==1){
                    individualPool.setLayoutX(400);
                    individualPool.setLayoutY(55);
                }else if(i==2){
                    individualPool.setLayoutX(220);
                    individualPool.setLayoutY(30);
                }else if(i==3){
                    individualPool.setLayoutX(150);
                    individualPool.setLayoutY(120);
                }else{
                    individualPool.setLayoutX(320);
                    individualPool.setLayoutY(145);
                }

                int count=0;
                for(Card next:cards){
                    Image cardImage=new Image("img/"+next.getSuit()+","+next.getCardNum()+".png");
                    Rectangle card=new Rectangle(120,160);
                    card.setFill(new ImagePattern(cardImage));
                    card.setLayoutX(count*30);
                    card.setLayoutY(0);
                    count++;
                    individualPool.getChildren().add(card);
                }
                cardPool.getChildren().add(individualPool);
            }
        }
    }
}

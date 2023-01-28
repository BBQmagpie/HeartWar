package sample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class HomePageGUI extends Parent {

    public HomePageGUI(){
        Pane backGround=new Pane();
        backGround.setPrefSize(960,600);
        backGround.getStylesheets().add("sample/Style.css");

        Image cardImage=new Image("img/Starter.png");
        Rectangle back=new Rectangle(960,682);
        back.setFill(new ImagePattern(cardImage));
        backGround.getChildren().add(back);

        Label playerWinning=new Label("Player Team Ranking: "+(MainProgram.playerTeamRanking.size()-1));
        playerWinning.setLayoutX(5);
        playerWinning.setLayoutY(540);
        Label oppoWinning=new Label("Opposite Team Ranking: "+(MainProgram.oppositeTeamRanking.size()-1));
        oppoWinning.setLayoutX(5);
        oppoWinning.setLayoutY(560);
        Label nextHost=new Label("Next Host: player no."+(MainProgram.host+1));
        nextHost.setLayoutX(5);
        nextHost.setLayoutY(580);
        backGround.getChildren().addAll(playerWinning,oppoWinning,nextHost);

        Button newGame=new Button();
        newGame.setPrefSize(160,40);
        newGame.setText("Start New Game");
        newGame.setLayoutX(640);
        newGame.setLayoutY(500);
        newGame.setOnMouseClicked(e->{
            MainProgram.newGame();
            Scene newScene = new Scene(new SetDifficultyPage(),960,600);
            Main.getStage().setScene(newScene);
        });
        newGame.getStyleClass().add("button");
        backGround.getChildren().add(newGame);

        Button continuePlay=new Button();
        continuePlay.setPrefSize(160,40);
        continuePlay.setText("Continue to Play");
        continuePlay.setLayoutX(640);
        continuePlay.setLayoutY(440);
        continuePlay.setOnMouseClicked(e->{
            Game game=new Game(MainProgram.host);
            game.preparation();
            Scene newScene = new Scene(new GamePageGUI(game,game.firstHand,0),960,600);
            Main.getStage().setScene(newScene);
        });
        continuePlay.getStyleClass().add("button");
        backGround.getChildren().add(continuePlay);

        getChildren().addAll(backGround);
    }

}

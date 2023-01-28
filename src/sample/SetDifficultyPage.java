package sample;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class SetDifficultyPage extends Parent {

    public SetDifficultyPage(){
        Pane backGround=new Pane();
        backGround.setPrefSize(960,600);
        backGround.getStylesheets().add("sample/Style.css");

        Image backImage=new Image("img/NewGame.png");
        Rectangle back=new Rectangle(672,477);
        back.setLayoutY(50);
        back.setLayoutX(144);
        back.setFill(new ImagePattern(backImage));
        backGround.getChildren().add(back);

        Image pointerImg=new Image("img/pointer.png");
        Rectangle pointer1=new Rectangle(7,7);
        pointer1.setFill(new ImagePattern(pointerImg));
        pointer1.setLayoutX(468);
        pointer1.setLayoutY(150);
        Rectangle pointer2=new Rectangle(7,7);
        pointer2.setFill(new ImagePattern(pointerImg));
        pointer2.setLayoutX(468);
        pointer2.setLayoutY(250);
        Rectangle pointer3=new Rectangle(7,7);
        pointer3.setFill(new ImagePattern(pointerImg));
        pointer3.setLayoutX(468);
        pointer3.setLayoutY(355);
        backGround.getChildren().addAll(pointer1,pointer2,pointer3);

        Button com1Ez=new Button();
        com1Ez.setLayoutX(450);
        com1Ez.setLayoutY(120);
        com1Ez.setText("Easy");
        Button com1Hd=new Button();
        com1Hd.setLayoutX(550);
        com1Hd.setLayoutY(120);
        com1Hd.setText("Hard");
        com1Ez.setOnMouseClicked(e->{
            MainProgram.diff.remove(0);
            MainProgram.diff.add(0,"ez");
            pointer1.setLayoutX(468);
            pointer1.setLayoutY(150);
        });
        com1Hd.setOnMouseClicked(e->{
            MainProgram.diff.remove(0);
            MainProgram.diff.add(0,"hd");
            pointer1.setLayoutX(568);
            pointer1.setLayoutY(150);
        });

        Button com2Ez=new Button();
        com2Ez.setLayoutX(450);
        com2Ez.setLayoutY(220);
        com2Ez.setText("Easy");
        Button com2Hd=new Button();
        com2Hd.setLayoutX(550);
        com2Hd.setLayoutY(220);
        com2Hd.setText("Hard");
        com2Ez.setOnMouseClicked(e->{
            MainProgram.diff.remove(1);
            MainProgram.diff.add(1,"ez");
            pointer2.setLayoutX(468);
            pointer2.setLayoutY(250);
        });
        com2Hd.setOnMouseClicked(e->{
            MainProgram.diff.remove(1);
            MainProgram.diff.add(1,"hd");
            pointer2.setLayoutX(568);
            pointer2.setLayoutY(250);
        });

        Button com3Ez=new Button();
        com3Ez.setLayoutX(450);
        com3Ez.setLayoutY(325);
        com3Ez.setText("Easy");
        Button com3Hd=new Button();
        com3Hd.setLayoutX(550);
        com3Hd.setLayoutY(325);
        com3Hd.setText("Hard");
        com3Ez.setOnMouseClicked(e->{
            MainProgram.diff.remove(2);
            MainProgram.diff.add(2,"ez");
            pointer3.setLayoutX(468);
            pointer3.setLayoutY(355);
        });
        com3Hd.setOnMouseClicked(e->{
            MainProgram.diff.remove(2);
            MainProgram.diff.add(2,"hd");
            pointer3.setLayoutX(568);
            pointer3.setLayoutY(355);
        });

        backGround.getChildren().addAll(com1Ez,com1Hd,com2Ez,com2Hd,com3Ez,com3Hd);

        System.out.println(MainProgram.diff.get(0)+MainProgram.diff.get(1)+MainProgram.diff.get(2));

        Button confirm=new Button();
        confirm.setPrefSize(120,40);
        confirm.setText("Start the Game");
        confirm.setLayoutX(420);
        confirm.setLayoutY(450);
        confirm.setOnMouseClicked(e->{
            System.out.println(MainProgram.diff.get(0)+MainProgram.diff.get(1)+MainProgram.diff.get(2));
            Game game=new Game(0);
            game.preparation();
            Scene newScene=new Scene(new GamePageGUI(game,0,0),960,600);
            Main.getStage().setScene(newScene);
        });
        confirm.getStyleClass().add("button");
        backGround.getChildren().add(confirm);


        getChildren().addAll(backGround);
    }
}

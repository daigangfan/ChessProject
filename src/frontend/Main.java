package frontend;


import backend.Executor;
import backend.Man;
import backend.Move;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Optional;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox box = new VBox();
        Label message = new Label("chess v0.0, please select your side.");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("white", "black"));
        cb.setValue("white");
        Button button = new Button("ok,lets' start");
        box.getChildren().addAll(message, cb, button);
        box.setSpacing(30);
        Scene startScene = new Scene(box);
        button.setOnMouseClicked(event -> {
            int side;
            if (cb.getValue() == "white") side = 1;
            else side = 0;
            Scene playScene = new Scene(new ChessBoardPane(side));
            primaryStage.setScene(playScene);
        });
        primaryStage.setScene(startScene);
        primaryStage.show();

    }
}

class ChessBoardPane extends GridPane {
    Executor executor = new Executor();
    boolean selecting = false;
    int selectedID = -1;
    int targetID = -1;
    int side;
    public ChessBoardPane(int side) {
        super();
        this.side = side;
        executor.setSide(side);
        if (side == 0) executor.makeMove();
        renderPane();

    }

    public void renderPane() {

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                Group g = new Group();
                Rectangle rect = new Rectangle(60, 60);
                rect.setId(Integer.toString(i * 8 + j));
                rect.setFill(Color.BLUE);
                if (i % 2 == 0 && j % 2 == 0) rect.setFill(Color.WHITE);
                if (i % 2 == 1 && j % 2 == 1) rect.setFill(Color.WHITE);


                int man = executor.getChessBoard().get(i, j);
                if (man > 0) {
                    Image image = new Image("resource/" + Integer.toString(man) + ".png");
                    ImageViewWithSide iv = new ImageViewWithSide(image);
                    if (executor.isWhite(man)) {
                        iv.setSide(0);
                    } else {
                        iv.setSide(1);
                    }
                    iv.setFitHeight(60);
                    iv.setFitWidth(60);
                    g.getChildren().addAll(rect, iv);
                    this.add(g, j, i);
                    if (iv.getSide() != side) {
                        g.setOnMouseClicked(
                                event -> {
                                    if (!selecting) {
                                        selectedID = Integer.valueOf(((Rectangle) g.getChildren().get(0)).getId());
                                        ((Rectangle) g.getChildren().get(0)).setFill(Color.RED);

                                        selecting = true;
                                    } else handleStep(g);
                                }
                        );
                    } else {
                        g.setOnMouseClicked(
                                event -> {
                                    handleStep(g);
                                }
                        );
                    }
                    continue;
                }
                this.add(rect, j, i);
                rect.setOnMouseClicked(event -> {
                    handleStep(rect);
                });

            }

    }

    private void handleStep(Node node) {

        if (selecting) {
            if (node instanceof Rectangle) {
                Rectangle target = (Rectangle) node;
                targetID = Integer.valueOf(target.getId());
            } else {
                if (node instanceof Group) {
                    Group target = (Group) node;
                    targetID = Integer.valueOf(((Rectangle) target.getChildren().get(0)).getId());
                }
            }
            if (selectedID >= 0) {
                int fromX = selectedID % 8;
                int fromY = selectedID / 8;
                int toX = targetID % 8;
                int toY = targetID / 8;
                Move move = new Move(fromX, fromY, toX, toY);
                //add promotion
                if (executor.getChessBoard().get(fromY, fromX) == Man.B_PAWN && side == 0 && fromY == 6 && toY == 7) {
                    move.setPromotion(true);
                    Optional<String> result = new PromotionSelectWindow().showAndWait();
                    while (!result.isPresent()) {
                        result = new PromotionSelectWindow().showAndWait();
                    }
                    switch (result.get()) {
                        case "Queen":
                            move.setPromotionType(Man.B_QUEEN);
                            break;
                        case "Rook":
                            move.setPromotionType(Man.B_ROOK);
                            break;
                        case "Bishop":
                            move.setPromotionType(Man.B_BISHOP);
                            break;
                        case "Knight":
                            move.setPromotionType(Man.B_KNIGHT);
                            break;
                        default:
                            break;
                    }
                }
                if (executor.getChessBoard().get(fromY, fromX) == Man.W_PAWN && side == 1 && fromY == 1 && toY == 0) {
                    move.setPromotion(true);
                    Optional<String> result = new PromotionSelectWindow().showAndWait();
                    while (!result.isPresent()) {
                        result = new PromotionSelectWindow().showAndWait();
                    }
                    switch (result.get()) {
                        case "Queen":
                            move.setPromotionType(Man.W_QUEEN);
                            break;
                        case "Rook":
                            move.setPromotionType(Man.W_ROOK);
                            break;
                        case "Bishop":
                            move.setPromotionType(Man.W_BISHOP);
                            break;
                        case "Knight":
                            move.setPromotionType(Man.W_KNIGHT);
                            break;
                        default:
                            break;
                    }
                }


                if (!executor.isValidMove(move)) {
                    if (executor.isWhite(executor.getChessBoard().get(toY, toX)) && side == 1) renderPane();
                    else if (executor.isBlack(executor.getChessBoard().get(toY, toX)) && side == 0) renderPane();

                    else {
                        new Alert(Alert.AlertType.ERROR, "invalid move").show();
                        renderPane();

                    }
                    selecting = false;
                }
                if (executor.isValidMove(move)) {
                    executor.execute(move);
                    renderPane();

                    executor.makeMove();
                    renderPane();
                    selecting = false;
                }
            }
        }
    }

    }


class ImageViewWithSide extends ImageView {
    private int side;

    public ImageViewWithSide(Image image) {
        super(image);
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }
}

class PromotionSelectWindow extends ChoiceDialog {

    public PromotionSelectWindow() {
        super("Queen", FXCollections.observableArrayList("Queen", "Rook", "Knight", "Bishop"));
        this.setTitle("Promotion Select Window");
        this.setContentText("please select the type to promotion");

    }
}

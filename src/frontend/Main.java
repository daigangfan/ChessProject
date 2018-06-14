package frontend;


import backend.Executor;
import backend.Man;
import backend.Move;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Optional;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
//        VBox box = new VBox();
//        Label message = new Label("chess v0.0, please select your side.");
//        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("white", "black"));
//        cb.setValue("white");
//        Button button = new Button("ok,lets' start");
//        box.getChildren().addAll(message, cb, button);
//        box.setSpacing(30);
//        Scene startScene = new Scene(box);
//        button.setOnMouseClicked(event -> {
//            int side;
//            if (cb.getValue() == "white") side = 1;
//            else side = 0;
//            ChessBoardPane chessBoardPane = new ChessBoardPane(side);
//            HBox newHBox = new HBox();
//            Button longCastling = new Button("长易位");
//            longCastling.setOnMouseClicked(event1 -> {
//                longCastling.fireEvent(new CastlingEvent(CastlingEvent.longCastlingEvent));
//            });
//
//            Button shortCastling = new Button("短易位");
//            shortCastling.setOnMouseClicked(event1 -> {
//                shortCastling.fireEvent(new CastlingEvent(CastlingEvent.shortCastlingEvent));
//            });
//            VBox vBox = new VBox(longCastling, shortCastling);
//            newHBox.getChildren().addAll(chessBoardPane, vBox);
//            newHBox.addEventHandler(CastlingEvent.CastlingEventType, event1 -> {
//                chessBoardPane.HandlingCastleEvent(event1);
//            });
//            Scene playScene = new Scene(newHBox);
//            primaryStage.setScene(playScene);
//        });
        StartBox startBox = new StartBox(primaryStage);
        Scene startScene = new Scene(startBox);
        primaryStage.setScene(startScene);
        primaryStage.show();
        primaryStage.setTitle("chess v1.0.0");
        primaryStage.setAlwaysOnTop(true);
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

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                Group g = new Group();
                Rectangle rect = new Rectangle(60, 60);
                rect.setId(Integer.toString(i * 8 + j));
                rect.setFill(Color.valueOf("#1ABDE6"));
                if (i % 2 == 0 && j % 2 == 0) rect.setFill(Color.WHITE);
                if (i % 2 == 1 && j % 2 == 1) rect.setFill(Color.WHITE);
                ImageViewWithSide iv = new ImageViewWithSide();
                iv.setFitHeight(60);
                iv.setFitWidth(60);
                g.getChildren().addAll(rect, iv);

                g.setOnMouseClicked(
                        event -> {

                            if (!selecting) {

                                if (((ImageViewWithSide) g.getChildren().get(1)).getSide() != this.side && ((ImageViewWithSide) g.getChildren().get(1)).getSide() >= 0) {
                                    selectedID = Integer.valueOf(((Rectangle) g.getChildren().get(0)).getId());
                                    ((Rectangle) g.getChildren().get(0)).setFill(Color.RED);

                                    selecting = true;
                                }

                            } else handleStep(g);
                        }
                );
                this.add(g, j, i);


            }
        if (side == 0) executor.makeMove();
        renderPane(false);
        this.addEventHandler(StepFinishedEvent.STEP_FINISHED_EVENT_EVENT_TYPE, event -> {
            Platform.runLater(
                    new Runnable() {
                        @Override
                        public void run() {

                            boolean terminated = checkStatus(false);
                            if (terminated) return;
                            Stage current = (Stage) getScene().getWindow();
                            current.setTitle("thinking...");
                            executor.makeMove();

                            renderPane(false);
                            current.setTitle("chess v1.0.0");
                            checkStatus(true);
                        }
                    }
            );

        });


    }

    public void renderPane(boolean isAfterStep) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (executor.getChessBoard().get(i, j) > 0) {
                    ((ImageViewWithSide) ((Group) this.getChildren().get(i * 8 + j)).getChildren().get(1)).setSide(executor.getChessBoard().get(i, j) >= Man.B_PAWN ? 1 : 0);
                    ((ImageViewWithSide) ((Group) this.getChildren().get(i * 8 + j)).getChildren().get(1)).setImage(new Image("resource/" + Integer.toString(executor.getChessBoard().get(i, j)) + ".png"));

                    }
                if (executor.getChessBoard().get(i, j) == 0) {
                    ((ImageViewWithSide) ((Group) this.getChildren().get(i * 8 + j)).getChildren().get(1)).setImage(null);
                    }
                ((Rectangle) ((Group) this.getChildren().get(i * 8 + j)).getChildren().get(0)).setFill(Color.valueOf("#1ABDE6"));
                if (i % 2 == 0 && j % 2 == 0)
                    ((Rectangle) ((Group) this.getChildren().get(i * 8 + j)).getChildren().get(0)).setFill(Color.WHITE);
                if (i % 2 == 1 && j % 2 == 1)
                    ((Rectangle) ((Group) this.getChildren().get(i * 8 + j)).getChildren().get(0)).setFill(Color.WHITE);
                }
        super.updateBounds();

        if (isAfterStep) this.fireEvent(new StepFinishedEvent());

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
                if (executor.getChessBoard().get(fromY, fromX) == Man.B_PAWN && side == 0 && fromY == 4 && Math.abs(fromX - toX) == 1 && executor.getChessBoard().get(toY, toX) == Man.NONE) {
                    move.setEP(true);
                }
                if (executor.getChessBoard().get(fromY, fromX) == Man.W_PAWN && side == 1 && fromY == 3 && Math.abs(fromX - toX) == 1 && executor.getChessBoard().get(toY, toX) == Man.NONE) {
                    move.setEP(true);
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
                    if (executor.isWhite(executor.getChessBoard().get(toY, toX)) && side == 1) renderPane(false);
                    else if (executor.isBlack(executor.getChessBoard().get(toY, toX)) && side == 0) renderPane(false);

                    else {

                        new Alert(Alert.AlertType.ERROR, "invalid move").show();
                        renderPane(false);
                        ;
                    }
                    selecting = false;
                }
                if (executor.isValidMove(move)) {
                    executor.execute(move);

                    renderPane(true);
                    selecting = false;
                }
            }
        }
    }

    public void HandlingCastleEvent(CastlingEvent event) {
        EventType<? extends CastlingEvent> type = event.getEventType();

        if (type.getName().equals("longCastling")) {
            if (side == 0) {
                Move move = new Move(4, 0, 2, 0);
                move.setLongCastling(true);
                if (executor.isValidMove(move)) executor.execute(move);
                else {
                    new Alert(Alert.AlertType.ERROR, "现在不可以进行易位！").show();

                    return;
                }
                renderPane(true);

                selecting = false;
            }
            if (side == 1) {
                Move move = new Move(4, 7, 2, 7);
                move.setLongCastling(true);
                if (executor.isValidMove(move)) executor.execute(move);
                else {
                    new Alert(Alert.AlertType.ERROR, "现在不可以进行易位！").show();
                    return;
                }
                renderPane(true);

            }
        } else if (type.getName().equals("shortCastling")) {
            if (side == 0) {
                Move move = new Move(4, 0, 6, 0);
                move.setShortCastling(true);
                if (executor.isValidMove(move)) executor.execute(move);
                else {
                    new Alert(Alert.AlertType.ERROR, "现在不可以进行易位！").show();
                    return;
                }
                renderPane(true);

                selecting = false;
            }
            if (side == 1) {
                Move move = new Move(4, 7, 6, 7);
                move.setShortCastling(true);
                if (executor.isValidMove(move)) executor.execute(move);
                else {
                    new Alert(Alert.AlertType.ERROR, "现在不可以进行易位！").show();
                    return;
                }
                renderPane(true);
                selecting = false;
            }
        }
    }

    private boolean checkStatus(boolean isAfterComputer) {
        if (side == 0 && isAfterComputer) {
            String info = this.executor.isBlackLose();
            if (info == "Black Lost!") {
                this.executor.getLogger().info("#");
                Alert x = new Alert(Alert.AlertType.INFORMATION, "You lose!");
                x.show();
                x.setOnCloseRequest(event -> {
                    Stage a = (Stage) (this.getScene().getWindow());
                    a.setScene(new Scene(new StartBox(a)));
                });
                return true;
            }
        }
        if (side == 1 && isAfterComputer) {
            String info = this.executor.isWhiteLose();
            if (info == "White Lost!") {
                this.executor.getLogger().info("#");
                Alert x = new Alert(Alert.AlertType.INFORMATION, "You lose!");
                x.show();
                x.setOnCloseRequest(event -> {

                    Stage a = (Stage) (this.getScene().getWindow());
                    a.setScene(new Scene(new StartBox(a)));

                });
                return true;
            }
        }
        if (side == 0 && !isAfterComputer) {
            String info = this.executor.isWhiteLose();
            if (info == "White Lost!") {
                this.executor.getLogger().info("#");
                Alert x = new Alert(Alert.AlertType.INFORMATION, "You win!");
                x.show();
                x.setOnCloseRequest(event -> {
                    Stage a = (Stage) (this.getScene().getWindow());
                    a.setScene(new Scene(new StartBox(a)));
                });
                return true;
            }
        }
        if (side == 1 && !isAfterComputer) {
            String info = this.executor.isBlackLose();
            if (info == "Black Lost!") {
                this.executor.getLogger().info("#");
                Alert x = new Alert(Alert.AlertType.INFORMATION, "You win!");
                x.show();
                x.setOnCloseRequest(event -> {
                    Stage a = (Stage) (this.getScene().getWindow());
                    a.setScene(new Scene(new StartBox(a)));
                });
                return true;
            }
        }
        return false;
    }

}


class ImageViewWithSide extends ImageView {
    private int side = -1;

    public ImageViewWithSide(Image image) {
        super(image);
    }

    public ImageViewWithSide() {
        super();
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

class CastlingEvent extends Event {
    public static final EventType<CastlingEvent> CastlingEventType = new EventType<>(EventType.ROOT);
    public static final EventType<CastlingEvent> shortCastlingEvent = new EventType<>(CastlingEventType, "shortCastling");
    public static final EventType<CastlingEvent> longCastlingEvent = new EventType<>(CastlingEventType, "longCastling");

    public CastlingEvent(EventType<? extends CastlingEvent> eventType) {
        super(eventType);

    }

    @Override
    public EventType<? extends CastlingEvent> getEventType() {
        return (EventType<? extends CastlingEvent>) super.getEventType();
    }
}

class StepFinishedEvent extends Event {
    public static final EventType<StepFinishedEvent> STEP_FINISHED_EVENT_EVENT_TYPE = new EventType<>(EventType.ROOT, "STEP_FINISHED");

    public StepFinishedEvent() {
        super(STEP_FINISHED_EVENT_EVENT_TYPE);
    }
}

class StartBox extends VBox {
    public StartBox(Stage primaryStage) {
        Label message = new Label("chess v0.0, please select your side.");
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("white", "black"));
        cb.setValue("white");
        Button button = new Button("ok,lets' start");
        this.getChildren().addAll(message, cb, button);
        this.setSpacing(30);
        button.setOnMouseClicked(event -> {
            int side;
            if (cb.getValue() == "white") side = 1;
            else side = 0;
            ChessBoardPane chessBoardPane = new ChessBoardPane(side);
            HBox newHBox = new HBox();
            Button longCastling = new Button("长易位");
            longCastling.setOnMouseClicked(event1 -> {
                longCastling.fireEvent(new CastlingEvent(CastlingEvent.longCastlingEvent));
            });

            Button shortCastling = new Button("短易位");
            shortCastling.setOnMouseClicked(event1 -> {
                shortCastling.fireEvent(new CastlingEvent(CastlingEvent.shortCastlingEvent));
            });
            VBox vBox = new VBox(longCastling, shortCastling);
            newHBox.getChildren().addAll(chessBoardPane, vBox);
            newHBox.addEventHandler(CastlingEvent.CastlingEventType, event1 -> {
                chessBoardPane.HandlingCastleEvent(event1);
            });
            Scene playScene = new Scene(newHBox);
            primaryStage.setScene(playScene);
        });
    }

}
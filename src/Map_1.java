import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Map_1 extends Application
{
 private static final int FRAME_WIDTH  = 640;
 private static final int FRAME_HEIGHT = 480;

 boolean woods = true;
 boolean rocks = true;

 GraphicsContext gc;
 Canvas canvas;

 Parser_1 parser1 = new Parser_1();

 Image image = new Image("map.jpg");

 public static void main(String[] args) { launch(args); }


 @Override
 public void start(Stage primaryStage)
 {
  parser1.run();

  AnchorPane root = new AnchorPane();
  primaryStage.setTitle("Map");

  canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT);
  canvas.setOnMousePressed(this::mouse);

  gc = canvas.getGraphicsContext2D();

  redraw();

  drawWoods();
  drawRocks();

  root.getChildren().add(canvas);

  RadioButton rbtn1 = new RadioButton();
  rbtn1.setText("Woods");
  rbtn1.setSelected(true);
  rbtn1.setOnAction(this::woods);

  root.getChildren().add(rbtn1);
  AnchorPane.setBottomAnchor( rbtn1, 5.0d );
  AnchorPane.setLeftAnchor( rbtn1, 50.0d );


  RadioButton rbtn2 = new RadioButton();
  rbtn2.setText("Rocks");
  rbtn2.setSelected(true);
  rbtn2.setOnAction(this::rocks);

  root.getChildren().add(rbtn2);
  AnchorPane.setBottomAnchor( rbtn2, 5.0d );
  AnchorPane.setLeftAnchor( rbtn2, 200.0d );




  Scene scene = new Scene(root);
  primaryStage.setTitle("Dolina Będkowska");
  primaryStage.setScene( scene );
  primaryStage.setWidth(FRAME_WIDTH + 10);
  primaryStage.setHeight(FRAME_HEIGHT+ 80);
  primaryStage.show();
 }

 private void redraw() {
  gc.clearRect(0,0,canvas.getWidth(),canvas.getWidth());
  gc.setGlobalAlpha(1);
  gc.drawImage(image, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
 }

 private void drawWoods()
 {
  gc.setGlobalAlpha(0.5);
  gc.setFill(Color.GREEN);

  double x_p[];
  double y_p[];
  int nrLas = parser1.wood1.obj;

  for (int i=0; i<nrLas; i++)
  {
   x_p = parser1.wood1.getX(i);
   y_p = parser1.wood1.getY(i);

   int size = parser1.wood1.getX(i).length;

   gc.fillPolygon(x_p, y_p, size);
  }

  gc.setFill(Color.WHITE);

  int nrM = parser1.meadow1.obj;

  for (int i=0;i<nrM; i++)
  {
   x_p = parser1.meadow1.getX(i);
   y_p = parser1.meadow1.getY(i);

   int size = parser1.meadow1.getX(i).length;

   gc.fillPolygon(x_p, y_p, size);
  }

  gc.setGlobalAlpha(1);
  woods = true;
 }

 private void woods(ActionEvent e)
 {
  System.out.println("woods");
  if (!woods) {
   drawWoods();
  } else {
   redraw();
   if (rocks)
    drawRocks();
   woods = false;
  }
 }

 private void drawRocks()
 {
  gc.setGlobalAlpha(0.5);

  double x_p[];
  double y_p[];
  int size;
  int nrRock;

  // czarne skaly
  gc.setFill(Color.BLACK);
  size = parser1.rock1cz.rockLen;
  nrRock = parser1.rock1cz.rock;
  for (int i=0; i<nrRock; i++) {
   x_p = parser1.rock1cz.getX(i);
   y_p = parser1.rock1cz.getY(i);
   gc.fillPolygon(x_p, y_p, size);
  }

   // szare skaly
   size = parser1.rock1sz.rockLen;
   nrRock = parser1.rock1sz.rock;
   for (int i=0; i<nrRock; i++) {
    x_p = parser1.rock1sz.getX(i);
    y_p = parser1.rock1sz.getY(i);
    gc.setFill(Color.DARKGRAY);
    gc.fillPolygon(x_p, y_p, size);
    gc.setFill(Color.BLACK);
    gc.strokePolyline(x_p, y_p, size);
  }

  gc.setGlobalAlpha(1);
  rocks = true;
 }

 private void rocks(ActionEvent e)
 {
  System.out.println("rocks");
  if (!rocks) {
   drawRocks();
  } else {
   redraw();
   if(woods)
    drawWoods();
   rocks = false;
  }
 }

 private void mouse(MouseEvent e)
 {
  System.out.println("X=" + e.getX());
  System.out.println("Y=" + e.getY());
 }

}	

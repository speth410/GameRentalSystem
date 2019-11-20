package GameRentalSystem;

import com.sun.jdi.LongValue;
import com.sun.xml.internal.xsom.impl.scd.Iterators;
import javafx.animation.*;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.Delayed;

public class FeatureController {

  @FXML private Label titleLbl;
  @FXML private ImageView featureImg;
  @FXML private ImageView selectImg1;
  @FXML private ImageView selectimg2;
  @FXML private ImageView leftArrow;
  @FXML private ImageView rightArrow;
  @FXML private Blend hovereffect = new Blend();
  @FXML private Blend noHoverEffect = new Blend();
  Image img1 = new Image("GameRentalSystem/images/feature1.jpg");
  Image img2 = new Image("GameRentalSystem/images/feature2.jpg");

  private void makeFade() {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setNode(featureImg);
    fadeTransition.setDuration(Duration.seconds(1));
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.play();
  }

  public void featuredGames() {
    Image[] img = {img1, img2};
    Random rand = new Random();
    int limit = rand.nextInt(2) + 1;

    if (limit == 1) {
      featureImg.setImage(img1);
      selectImg1.setEffect(hovereffect);
    } else {
      selectImg1.setEffect(noHoverEffect);
    }

    if (limit == 2) {
      featureImg.setImage(img2);
      selectimg2.setEffect(hovereffect);
    } else {
      selectimg2.setEffect(noHoverEffect);
    }

    makeFade();
  }

  @FXML
  public void initialize() {
    featuredGames();
    hovereffect.setMode(BlendMode.ADD);
    hovereffect.setOpacity(0.6);
    noHoverEffect.setOpacity(0);
  }

  @FXML
  void rightClicked(MouseEvent event) {
    featuredGames();
  }

  @FXML
  void leftClicked(MouseEvent event) {
    featuredGames();
  }

  /** Effects */
  @FXML
  void leftHover(MouseEvent event) {
    leftArrow.setEffect(hovereffect);
  }

  @FXML
  void leftHoverOff(MouseEvent event) {
    leftArrow.setEffect(noHoverEffect);
  }

  @FXML
  void rightHover(MouseEvent event) {
    rightArrow.setEffect(hovereffect);
  }

  @FXML
  void rightHoverOff(MouseEvent event) {
    rightArrow.setEffect(noHoverEffect);
  }
}

package GameRentalSystem;

import com.sun.jdi.LongValue;
import javafx.animation.*;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.concurrent.Delayed;

public class FeatureController {

  @FXML private Label titleLbl;

  @FXML private ImageView featureImg;

  @FXML private ImageView featureImg2;
  @FXML private ImageView leftArrow;

  @FXML
  private AnchorPane rootPane;

  @FXML private ImageView rightArrow;
  @FXML private Blend hovereffect = new Blend();
  @FXML private Blend noHoverEffect = new Blend();

  @FXML
  public void initialize() {
    hovereffect.setMode(BlendMode.ADD);
    hovereffect.setOpacity(0.4);
    noHoverEffect.setOpacity(0);
    makeFadeAuto();
  }

  private void makeFade() {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.seconds(1));
    fadeTransition.setNode(featureImg);
    fadeTransition.setFromValue(1);
    fadeTransition.setToValue(0);
    fadeTransition.play();
  }

  private void makeFadeAuto() {

    FadeTransition fadeTransitionAuto = new FadeTransition();
    fadeTransitionAuto.setDuration(Duration.seconds(4));
    fadeTransitionAuto.setNode(featureImg);
    fadeTransitionAuto.setFromValue(1);
    fadeTransitionAuto.setToValue(0);
    fadeTransitionAuto.play();
  }


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

  @FXML
  void rightClicked(MouseEvent event) {
    makeFade();
  }

  @FXML
  void leftClicked(MouseEvent event) {
    makeFade();
  }
}

package GameRentalSystem;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.Random;

public class FeatureController extends Mediator{

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
  Image[] img = {img1, img2};
  private Mediator mediator;
  @FXML
  public void initialize() {
    selectImg1.setEffect(hovereffect);
    Random rand = new Random(2);//always start on COD(replace w/ 0 for random)
    featureImg.setImage(img[rand.nextInt(img.length)]);
    hovereffect.setMode(BlendMode.ADD);
    hovereffect.setOpacity(0.6);
    noHoverEffect.setOpacity(0);
  }

  @FXML
  void rightClicked(MouseEvent event) {
    mediator.clickedOnRight();
  }

  @FXML
  void leftClicked(MouseEvent event) {
    mediator.clickedOnLeft();
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

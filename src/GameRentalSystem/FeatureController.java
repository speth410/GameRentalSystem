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
  Image[] img = {img1, img2};

  private void makeFade() {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setNode(featureImg);
    fadeTransition.setDuration(Duration.seconds(1));
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1);
    fadeTransition.play();
  }

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
    if (featureImg.getImage() != img[0]) {
      featureImg.setImage(img[0]);
      selectimg2.setEffect(hovereffect);
    }
    else if(featureImg.getImage() != img[1]) {
      selectImg1.setEffect(hovereffect);
      featureImg.setImage(img[1]);
      }

    if (featureImg.getImage() == img[1]) {
      selectimg2.setEffect(noHoverEffect);
    }
    else if(featureImg.getImage() == img[0]) {
      selectImg1.setEffect(noHoverEffect);
    }

      makeFade();
  }

  @FXML
  void leftClicked(MouseEvent event) {
    if (featureImg.getImage() != img[1]) {
      featureImg.setImage(img[1]);
      selectImg1.setEffect(hovereffect);
    }
    else if(featureImg.getImage() != img[0]) {
        selectimg2.setEffect(hovereffect);
        featureImg.setImage(img[0]);
    }
      if (featureImg.getImage() == img[0]) {
          selectImg1.setEffect(noHoverEffect);
      }
      else if(featureImg.getImage() == img[1]) {
          selectimg2.setEffect(noHoverEffect);
      }
    makeFade();
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

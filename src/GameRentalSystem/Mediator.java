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

public class Mediator {
    private FeatureController featureController;
    @FXML
    private ImageView featureImg;
    @FXML private Label titleLbl;
    @FXML private ImageView selectImg1;
    @FXML private ImageView selectimg2;
    @FXML private ImageView leftArrow;
    @FXML private ImageView rightArrow;
    @FXML private Blend hovereffect = new Blend();
    @FXML private Blend noHoverEffect = new Blend();
    Image img1 = new Image("GameRentalSystem/images/feature1.jpg");
    Image img2 = new Image("GameRentalSystem/images/feature2.jpg");
    Image[] img = {img1, img2};

    public void makeFader(){
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setNode(featureImg);
        fadeTransition.setDuration(Duration.seconds(1));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    public void clickedOnRight(){
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

        makeFader();
    }

    public void clickedOnLeft(){
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

        makeFader();
    }
}

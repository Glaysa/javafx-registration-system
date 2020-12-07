package cssOverriding;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class CustomAlertDialogs {
    public void modify(Alert success, Alert confirm, Alert warning){
        // MAKES THE ALERT DIALOG STAY ON TOP OF THE MAIN WINDOW, ONLY CLOSES AFTER RESPONDING TO IT
        // THIS FUNCTIONALITY IS GONE WHEN I CHANGED THE STAGE STYLE TO TRANSPARENT
        success.initModality(Modality.APPLICATION_MODAL);
        warning.initModality(Modality.APPLICATION_MODAL);
        confirm.initModality(Modality.APPLICATION_MODAL);

        // REMOVES THE DEFAULT TITLE BAR
        success.initStyle(StageStyle.TRANSPARENT);
        warning.initStyle(StageStyle.TRANSPARENT);
        confirm.initStyle(StageStyle.TRANSPARENT);

        // GET THE DIALOG PANES OF EACH ALERT
        DialogPane warningPane = warning.getDialogPane();
        DialogPane successPane = success.getDialogPane();
        DialogPane confirmPane = confirm.getDialogPane();

        // GET YOUR CUSTOM ICONS AND SET IT AS A NEW IMAGE VIEW
        ImageView warningIcon = new ImageView(getClass().getResource("_warning.png").toExternalForm());
        ImageView successIcon = new ImageView(getClass().getResource("_success.png").toExternalForm());
        ImageView confirmIcon = new ImageView(getClass().getResource("_confirm.png").toExternalForm());

        // RESIZE THE ICONS TO FIT NICELY ON THE ALERT DIALOGS
        warningIcon.setFitHeight(50.0);
        warningIcon.setFitWidth(50.0);
        successIcon.setFitHeight(50.0);
        successIcon.setFitWidth(50.0);
        confirmIcon.setFitHeight(50.0);
        confirmIcon.setFitWidth(50.0);

        // SET THE CUSTOM ICONS AS THE NEW ICONS OF EACH ALERT DIALOG
        warningPane.setGraphic((warningIcon));
        successPane.setGraphic((successIcon));
        confirmPane.setGraphic((confirmIcon));

        // CONNECT THE ALERT DIALOGS TO A STYLESHEET TO FURTHER CUSTOMIZED IT
        warningPane.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        successPane.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        confirmPane.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());

        // GIVE THE ALERT DIALOGS A CLASS NAME, SO THAT WE CAN USE IT AS A REFERENCE IN THE CSS FILE
        warningPane.getStyleClass().add("warning");
        successPane.getStyleClass().add("success");
        confirmPane.getStyleClass().add("confirm");

    }
}

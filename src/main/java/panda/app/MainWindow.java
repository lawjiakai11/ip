package panda.app;

import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controller for Panda's main chat window.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;

    private final PandaService panda = new PandaService();
    private final Image poImage = loadAvatar("/images/po-avatar.png");
    private final Image platypusImage = loadAvatar("/images/platypus-avatar.png");

    /** Binds the chat history to the scroll position. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        addPandaDialog("Hello! I'm Panda.\nWhat can I do for you?");
    }

    /** Sends the current text to Panda when Enter or Send is pressed. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }
        dialogContainer.getChildren().add(new DialogBox(input, platypusImage, false));
        String response = panda.getResponse(input);
        if (response.equals(PandaService.BYE_RESPONSE)) {
            addPandaDialog("Bye. Hope to see you again soon!");
            userInput.setDisable(true);
        } else {
            addPandaDialog(response);
        }
        userInput.clear();
    }

    private void addPandaDialog(String response) {
        dialogContainer.getChildren().add(new DialogBox(response, poImage, true));
    }

    private Image loadAvatar(String resourcePath) {
        return new Image(MainWindow.class.getResourceAsStream(resourcePath));
    }
}

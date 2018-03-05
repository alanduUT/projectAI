package ut.university.projectAI;

import ut.university.projectAI.processor.MainProcessor;

import javafx.scene.control.TextArea;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class App extends Application{ 

	private static final MainProcessor mainProcessor = new MainProcessor();
	private static final int LIMIT = 140;

    public static void main( String[] args ){
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		 primaryStage.setTitle("Dialogue System");
		 BorderPane pane = new BorderPane();
		 
		 TextArea chat = new TextArea();
		 chat.setEditable(false);
		 chat.setMinHeight(600);
		 chat.appendText("Robot: Hello! How may I help You?\n");
		 pane.setTop(chat);
		 
		 TextArea input = new TextArea();
		 Button button = new Button("READY");
		 button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				input.setEditable(false);
				input.setMouseTransparent(true);
				input.setFocusTraversable(false);
				chat.appendText("Me: ");smartAppend(input.getText());
				chat.appendText("\n");
				chat.appendText("Robot: ");smartAppend(mainProcessor.getResponse(input.getText()));
				chat.appendText("\n");
				chat.setScrollTop(Double.MAX_VALUE);
				input.setText("");
				input.setEditable(true);
				input.setMouseTransparent(false);
				input.setFocusTraversable(true);
			}
			
			private void smartAppend(String text){
				while(text.length() > LIMIT){
					chat.appendText(text.substring(0, LIMIT));
					text = text.substring(LIMIT, text.length());
					chat.appendText("\n");
				}
				chat.appendText(text);
			}
			 
		 });
		 
		 pane.setCenter(input);
		 pane.setBottom(button);
		 
		 primaryStage.setScene(new Scene(pane, 1000, 700));
		 primaryStage.show();
	}
}

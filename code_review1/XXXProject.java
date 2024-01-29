
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class XXXProject extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("f_files/Login_screen.fxml"));
        stage.setTitle("XX Project");
        stage.setScene(new Scene(root));
        stage.show();

    }
}

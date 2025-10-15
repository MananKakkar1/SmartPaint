module ca.utoronto.utm.paint {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;
    requires java.dotenv;


    opens com.manankakkar.smartpaint to javafx.fxml;
    exports com.manankakkar.smartpaint;
}
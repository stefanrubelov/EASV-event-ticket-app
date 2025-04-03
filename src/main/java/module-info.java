module easv.ticketapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires com.google.zxing;
    requires javafx.swing;
    requires jakarta.mail;
    requires jdk.jdi;
    requires net.synedra.validatorfx;
    requires tornadofx.controls;
    requires itextpdf;
    requires com.mailjet.api;
    requires org.json;

    opens easv.ticketapp to javafx.fxml;
    opens easv.ticketapp.be to javafx.base;
    opens easv.ticketapp.gui to javafx.fxml;

    exports easv.ticketapp;
    exports easv.ticketapp.gui;
}

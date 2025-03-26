module easv.ticketapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires net.synedra.validatorfx;
    requires java.desktop;

    opens easv.ticketapp to javafx.fxml;
    opens easv.ticketapp.be to javafx.base;
    opens easv.ticketapp.gui to javafx.fxml;

    exports easv.ticketapp;
    exports easv.ticketapp.gui;

    opens easv.ticketapp.gui to javafx.fxml;
}


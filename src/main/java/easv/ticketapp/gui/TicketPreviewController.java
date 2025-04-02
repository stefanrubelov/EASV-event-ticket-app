package easv.ticketapp.gui;

import easv.ticketapp.be.Event;
import easv.ticketapp.be.ticket.Ticket;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.text.DecimalFormat;

public class TicketPreviewController {
    @FXML
    private Label eventNameLbl1;
    @FXML
    private Label eventNameLbl2;
    @FXML
    private Label eventDateLbl1;
    @FXML
    private Label eventDateLbl2;
    @FXML
    private Label eventLocationLbl1;
    @FXML
    private Label eventLocationLbl2;
    @FXML
    private Label ticketPriceLbl;
    @FXML
    private Label ticketDescriptionLbl;
    @FXML
    private Label eventDescriptionLbl;
    @FXML
    private Pane rootPane;

    public void updatePreview(Ticket ticket) {
        eventNameLbl1.setText(ticket.getEvent().getName());
        eventNameLbl2.setText(ticket.getEvent().getName());
        eventDateLbl1.setText(ticket.getEvent().getDate().toString());
        eventDateLbl2.setText(ticket.getEvent().getDate().toString());
        eventLocationLbl1.setText(ticket.getEvent().getLocation());
        eventLocationLbl2.setText(ticket.getEvent().getLocation());

        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println(df.format(ticket.getPrice()));
        ticketPriceLbl.setText(df.format(ticket.getPrice()));

        ticketDescriptionLbl.setText(ticket.getDescription());
        eventDescriptionLbl.setText(ticket.getEvent().getDescription());
    }

    public void setBackgroundImage(String imagePath) {
        rootPane.setStyle("-fx-background-image: url(");
    }

    public Pane getRootPane() {
        return rootPane;
    }


}

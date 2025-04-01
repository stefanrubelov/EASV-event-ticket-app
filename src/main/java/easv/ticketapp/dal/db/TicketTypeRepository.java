package easv.ticketapp.dal.db;

import easv.ticketapp.be.ticket.Ticket;
import easv.ticketapp.be.ticket.TicketType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketTypeRepository {
    final private QueryBuilder queryBuilder = new QueryBuilder();
    final private Logger logger = Logger.getAnonymousLogger();

    public List<TicketType> getTicketTypes() {
        List<TicketType> tickets = new ArrayList<>();

        try (ResultSet rs = queryBuilder
                .select("*")
                .from("ticket_types")
                .get()) {

            while (rs != null && rs.next()) {
                TicketType ticket = mapModel(rs, rs.getInt("id"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return tickets;
    }

    private TicketType mapModel(ResultSet rs, int id) throws SQLException {
        String type = rs.getString("type");

        return new TicketType(id, type);
    }
}

package easv.ticketapp.dal.db;

import easv.ticketapp.be.Event;

import java.util.List;

public interface BaseRepository<T> {
    List<T> getById(int id);
    List<T> getAll();
    void delete(int id);
    void update(T entity);
    T create(T entity);
}

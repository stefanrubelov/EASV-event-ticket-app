package easv.ticketapp.dal.db.interaces;

import java.util.List;

public interface BaseRepository<T> {
    T getById(int id);

    List<T> getAll();

    void delete(int id);

    void update(T entity);

    T create(T entity);
}

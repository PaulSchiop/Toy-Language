package model.adt;
import java.util.List;

public interface IMyList<Elem> {
    void add(Elem elem);
    List<Elem> getAll();
}

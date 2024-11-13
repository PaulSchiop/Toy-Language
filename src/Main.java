import repository.IRepo;
import controller.*;
import repository.Repo;
import view.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        IRepo repo = new Repo();
        Controller controller = new Controller(repo, true);
        View view = new View(controller);
        view.run();
    }
}
package view;

import view.commands.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private Map<String, Command> map;

    public TextMenu() {
        this.map = new HashMap<>();
    }

    public void addCommand(Command command) {
        this.map.put(command.getKey(), command);
    }

    public void printMenu() {
        for (Command command : this.map.values()) {
            String line = String.format("%s: %s", command.getKey(), command.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            this.printMenu();
            System.out.println("Input the option: ");
            try {
                String line = scanner.nextLine();
                Command command = this.map.get(line);
                if (command == null) {
                    System.out.println("Invalid option");
                    continue;
                }
                command.execute();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

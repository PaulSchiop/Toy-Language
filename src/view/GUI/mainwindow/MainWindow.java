package view.GUI.mainwindow;

import controller.Controller;
import exceptions.ControllerException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import model.adt.IMyBarrier;
import model.adt.IMyStack;
import model.adt.MyPair;
import model.state.PrgState;
import model.statements.IStatement;
import model.values.IValue;
import model.values.StringValue;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class MainWindow {

    private Controller controller;

    @FXML
    private TextField nrProgramStatesTextField;

    @FXML
    private TableView<MyPair<Integer, IValue>> heapTableView;
    @FXML
    private TableColumn<MyPair<Integer, IValue>, Integer> addressColumn;
    @FXML
    private TableColumn<MyPair<Integer, IValue>, IValue> valueColumn;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<String> outListView;

    @FXML
    private ListView<Integer> prgStateIdListView;

    @FXML
    private TableView<MyPair<String, IValue>> symTableView;
    @FXML
    private TableColumn<MyPair<String, IValue>, String> varNameColumn;
    @FXML
    private TableColumn<MyPair<String, IValue>, IValue> valueSymColumn;

    @FXML
    private ListView<String> exeStackListView;

    @FXML
    private TableView<Map.Entry<Integer, Pair<Integer, List<Integer>>>> barrierTableView;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, Integer> barrierIndex;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, Integer> barrierValue;
    @FXML
    private TableColumn<Map.Entry<Integer, Pair<Integer, List<Integer>>>, String> valuesList;

    @FXML
    private Button oneStepButton;

    @FXML
    public void initialize(IStatement selectedStatement) {
        prgStateIdListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateExecutionStack();
                populateSymTable();
            }
        });
    }

    public void setController(Controller controller) {
        this.controller = controller;
        if (controller != null) {
            populateIdentifiers();
            populateTables();
        }
    }


    private void populateTables() {
        if (controller != null) {
            populateHeapTable();
            populateSymTable();
            populateExecutionStack();
            populateOutput();
            populateFileTable();
            populateIdentifiers();
            populateNumberProgramStates();
            populateBarrierTable();
        }
    }

    private void populateBarrierTable() {
        if (controller.getProgramStateList().isEmpty()) {
            System.out.println("No program state available.");
            return;
        }

        ObservableList<Map.Entry<Integer, Pair<Integer, List<Integer>>>> barrierData = FXCollections.observableArrayList(
                controller.getProgramStateList().get(0)
                        .getBarrier()
                        .getBarrierTable()
                        .entrySet()
                        .stream()
                        .toList()
        );

        barrierIndex.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getKey()));
        barrierValue.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValue().getKey()));
        valuesList.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValue().getValue().toString()));

        barrierTableView.setItems(barrierData);
        barrierTableView.refresh();
    }

    private PrgState getSelectedProgramState() {
        if (controller.getProgramStateList().isEmpty()) {
            return null;
        }
        int selectedIndex = prgStateIdListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1 || selectedIndex >= controller.getProgramStateList().size()) {
            return controller.getProgramStateList().get(0); // Return the first program state
        }
        return controller.getProgramStateList().get(selectedIndex);
    }


    private void populateNumberProgramStates() {
        nrProgramStatesTextField.setText(String.valueOf(controller.getProgramStateList().size()));
    }

    private void populateIdentifiers() {
        ObservableList<Integer> identifiers = FXCollections.observableArrayList();
        for (PrgState prgState : controller.getProgramStateList()) {
            if (!prgState.getExeStack().isEmpty())
                identifiers.add(prgState.getId());
        }
        prgStateIdListView.setItems(identifiers);

        // Select the first program state by default
        if (!identifiers.isEmpty()) {
            prgStateIdListView.getSelectionModel().selectFirst();
        }
    }


    private void populateFileTable() {
        PrgState currentProgramState = getSelectedProgramState();
        var fileTable = Objects.requireNonNull(currentProgramState).getFileTable();
        ArrayList<MyPair<StringValue, BufferedReader>> fileTableContent = new ArrayList<>();
        for (StringValue key : fileTable.getContent().keySet())
            fileTableContent.add(new MyPair<>(key, fileTable.getContent().get(key)));
        fileTableListView.getItems().clear();
        for (MyPair<StringValue, BufferedReader> entry : fileTableContent) {
            fileTableListView.getItems().add(entry.getFirst().toString());
            fileTableListView.getItems().add(entry.getSecond().toString());
        }
    }

    private void populateOutput() {
        PrgState currentProgramState = getSelectedProgramState();
        var output = Objects.requireNonNull(currentProgramState).getOut();
        ArrayList<String> outputContent = new ArrayList<>();
        for (String entry : output.getAll())
            outputContent.add(entry);
        outListView.getItems().clear();
        for (String entry : outputContent)
            outListView.getItems().add(entry);
    }

    private void populateHeapTable() {
        if (controller.getProgramStateList().isEmpty()) {
            System.out.println("No program state available.");
            return;
        }

        ObservableList<MyPair<Integer, IValue>> heapData = FXCollections.observableArrayList(
                controller.getProgramStateList().get(0)
                        .getHeap()
                        .getMap()
                        .entrySet()
                        .stream()
                        .map(entry -> new MyPair<>(entry.getKey(), entry.getValue())) // Map to MyPair
                        .toList()
        );

        addressColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getFirst()).asObject());
        valueColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSecond()));
        heapTableView.setItems(heapData);
    }

    private void populateSymTable() {
        int selectedProgramStateIndex = prgStateIdListView.getSelectionModel().getSelectedIndex();
        if (selectedProgramStateIndex == -1 || selectedProgramStateIndex >= controller.getProgramStateList().size()) {
            System.out.println("No program state selected.");
            return;
        }
        ObservableList<MyPair<String, IValue>> symTableData = FXCollections.observableArrayList(
                controller.getProgramStateList().get(selectedProgramStateIndex)
                        .getSymTable()
                        .getContent()
                        .entrySet()
                        .stream()
                        .map(entry -> new MyPair<>(entry.getKey(), entry.getValue())) // Map to MyPair
                        .toList()
        );

        varNameColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFirst()));
        valueSymColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSecond()));
        symTableView.setItems(symTableData);
        symTableView.refresh();
    }

    private void populateExecutionStack() {
        PrgState currentProgramState = getSelectedProgramState();
        if (currentProgramState == null) {
            exeStackListView.getItems().clear();
            return;
        }
        IMyStack<IStatement> executionStack = currentProgramState.getExeStack();
        ArrayList<IStatement> executionStackContent = new ArrayList<>(executionStack.getStack());
        exeStackListView.getItems().clear();
        for (IStatement stmt : executionStackContent) {
            exeStackListView.getItems().add(stmt.toString());
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void oneStepButtonHandler() {
        try {
            if (controller.getProgramStateList().isEmpty()) {
                showErrorMessage("No program states available.");
                return;
            }

            int selectedIndex = prgStateIdListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1 || selectedIndex >= controller.getProgramStateList().size()) {
                showErrorMessage("Invalid program state selected.");
                return;
            }

            controller.oneStepForAllPrg(controller.getProgramStateList());
            populateTables();
        } catch (IOException | InterruptedException | ControllerException e) {
            showErrorMessage(e.getMessage());
        }
    }
}
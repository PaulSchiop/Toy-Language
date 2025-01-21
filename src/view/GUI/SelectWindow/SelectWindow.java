package view.GUI.SelectWindow;

import controller.Controller;
import exceptions.ExpressionExceptions;
import exceptions.StatementException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import model.expressions.*;
import model.state.PrgState;
import model.statements.*;
import model.types.*;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;
import model.adt.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import view.GUI.mainwindow.MainWindow;

public class SelectWindow implements Initializable {

    private MainWindow mainWindowController;
    IStatement selectedStatement;
    List<IStatement> statementList = new ArrayList<>();

    @FXML
    private ListView<IStatement> statementsListView;

    @FXML
    private Label selectedStatementLabel;

    @FXML
    private Button executeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateStatements();
        for (IStatement st : statementList)
            statementsListView.getItems().add(st);
        this.statementsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.selectedStatement = statementsListView.getSelectionModel().getSelectedItem();
            this.selectedStatementLabel.setText("Selected statement: " + this.selectedStatement);
        });
    }

    @FXML
    public void executeButtonHandler(ActionEvent actionEvent) {
        int selectedProgram = statementsListView.getSelectionModel().getSelectedIndex();
        if (selectedProgram < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No program selected", ButtonType.OK);
            alert.showAndWait();
            return;
        } else {
            IStatement selectedStatement = statementList.get(selectedProgram);
            try {
                IMyDict<String, IType> typeEnv = new MyDictionary<>();
                selectedStatement.typeCheck(typeEnv);
            } catch (StatementException | ExpressionExceptions e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Type check failed: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
                return;
            }
            selectedProgram++;
            IRepo repo = new Repo("log" + selectedProgram + ".txt");
            Controller controller = new Controller(repo, true);
            controller.addProgram(selectedStatement);
            showMainWindow(selectedStatement, controller);
        }
    }

    private void showMainWindow(IStatement selectedStatement, Controller controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainwindow/MainWindow.fxml"));
            Parent root = loader.load();

            MainWindow mainWindowController = loader.getController();
            mainWindowController.setController(controller);
            mainWindowController.initialize(selectedStatement);

            Stage stage = new Stage();
            stage.setTitle("MainWindow");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateStatements() {
        IStatement ex1 = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );
        statementList.add(ex1);

        IStatement ex2 = new CompStatement(
                new VariableDeclarationStatement("a", new IntType()),
                new CompStatement(
                        new VariableDeclarationStatement("b", new IntType()),
                        new CompStatement(
                                new AssignStatement("a", new AritmeticalExpression(
                                        new ValueExpression(new IntValue(2)),
                                        new AritmeticalExpression(
                                                new ValueExpression(new IntValue(3)),
                                                new ValueExpression(new IntValue(5)),
                                                AritmeticalOperator.MUL
                                        ),
                                        AritmeticalOperator.ADD
                                )),
                                new CompStatement(
                                        new AssignStatement("b", new AritmeticalExpression(
                                                new VariableExpression("a"),
                                                new ValueExpression(new IntValue(1)),
                                                AritmeticalOperator.ADD
                                        )),
                                        new PrintStatement(new VariableExpression("b"))
                                )
                        )
                )
        );
        statementList.add(ex2);

        IStatement ex3 = new CompStatement(
                new VariableDeclarationStatement("a", new model.types.BoolType()),
                new CompStatement(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompStatement(
                                new AssignStatement("a", new ValueExpression(new model.values.BoolValue(true))),
                                new CompStatement(
                                        new IfStatement(
                                                new VariableExpression("a"),
                                                new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                                new AssignStatement("v", new ValueExpression(new IntValue(3)))
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        );
        statementList.add(ex3);

        //test RFile
        //int varf; varf="test.in"; openRFile(varf); int varc; readRFile(varf, varc); print(varc); readRFile(varf, varc); print(varc); closeRFile(varf)
        IStatement ex4 = new CompStatement(
                new VariableDeclarationStatement("varf", new StringType()),
                new CompStatement(
                        new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompStatement(
                                new OpenRFileStatement(new VariableExpression("varf")),
                                new CompStatement(
                                        new VariableDeclarationStatement("varc", new IntType()),
                                        new CompStatement(
                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompStatement(
                                                        new PrintStatement(new VariableExpression("varc")),
                                                        new CompStatement(
                                                                new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompStatement(
                                                                        new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseReadFile(new VariableExpression("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex4);

        /// Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
        IStatement ex5 = new CompStatement(
                new VariableDeclarationStatement("v", new model.types.RefType(new IntType())),
                new CompStatement(
                        new HeapAllocStatement("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VariableDeclarationStatement("a", new model.types.RefType(new model.types.RefType(new IntType()))),
                                new CompStatement(
                                        new HeapAllocStatement("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a"))
                                        )
                                )
                        )
                )
        );
        statementList.add(ex5);

        //Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)
        IStatement ex6 = new CompStatement(
                new VariableDeclarationStatement("v", new model.types.RefType(new IntType())),
                new CompStatement(
                        new HeapAllocStatement("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VariableDeclarationStatement("a", new model.types.RefType(new model.types.RefType(new IntType()))),
                                new CompStatement(
                                        new HeapAllocStatement("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
                                                new PrintStatement(
                                                        new AritmeticalExpression(
                                                                new HeapReadExpression(
                                                                        new HeapReadExpression(new VariableExpression("a"))
                                                                ),
                                                                new ValueExpression(new IntValue(5)),
                                                                AritmeticalOperator.ADD
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex6);

        //Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);
        IStatement ex7 = new CompStatement(
                new VariableDeclarationStatement("v", new model.types.RefType(new IntType())),
                new CompStatement(
                        new HeapAllocStatement("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new PrintStatement(new HeapReadExpression(new VariableExpression("v"))),
                                new CompStatement(
                                        new HeapWriteStatement("v", new ValueExpression(new IntValue(30))),
                                        new PrintStatement(
                                                new AritmeticalExpression(
                                                        new HeapReadExpression(new VariableExpression("v")),
                                                        new ValueExpression(new IntValue(5)),
                                                        AritmeticalOperator.ADD
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex7);

        //Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); Ref int k; new(k, 15); print(rH(rH(a)))
        IStatement ex8 = new CompStatement(
                new VariableDeclarationStatement("v", new model.types.RefType(new IntType())),
                new CompStatement(
                        new HeapAllocStatement("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VariableDeclarationStatement("a", new model.types.RefType(new model.types.RefType(new IntType()))),
                                new CompStatement(
                                        new HeapAllocStatement("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new HeapAllocStatement("v", new ValueExpression(new IntValue(30))),
                                                new CompStatement(
                                                        new VariableDeclarationStatement("k", new model.types.RefType(new IntType())),
                                                        new PrintStatement(
                                                                new HeapReadExpression(
                                                                        new HeapReadExpression(new VariableExpression("a"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex8);

        //int v = 4; (while (v > 0) print(v); v = v - 1); print(v)
        IStatement ex9 = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()), // int v;
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(4))), // v = 4;
                        new CompStatement(
                                new WhileStatement(
                                        new RelationalExperssion( // while (v > 0)
                                                new VariableExpression("v"),
                                                new ValueExpression(new IntValue(0)),
                                                RelationalOperator.GREATER
                                        ),
                                        new CompStatement(
                                                new PrintStatement(new VariableExpression("v")), // print(v);
                                                new AssignStatement(
                                                        "v",
                                                        new AritmeticalExpression( // v = v - 1;
                                                                new VariableExpression("v"),
                                                                new ValueExpression(new BoolValue(true)),
                                                                AritmeticalOperator.SUB
                                                        )
                                                )
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v")) // print(v);
                        )
                )
        );
        statementList.add(ex9);

        // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); v = 0; print(rH(rH(a)))
        IStatement ex10 = new CompStatement(
                new VariableDeclarationStatement("v", new model.types.RefType(new IntType())),
                new CompStatement(
                        new HeapAllocStatement("v", new ValueExpression(new IntValue(20))),
                        new CompStatement(
                                new VariableDeclarationStatement("a", new model.types.RefType(new model.types.RefType(new IntType()))),
                                new CompStatement(
                                        new HeapAllocStatement("a", new VariableExpression("v")),
                                        new CompStatement(
                                                new HeapAllocStatement("v", new ValueExpression(new IntValue(30))),
                                                new CompStatement(
                                                        new AssignStatement("v", new ValueExpression(new model.values.IntValue(0))),
                                                        new PrintStatement(
                                                                new HeapReadExpression(
                                                                        new HeapReadExpression(new VariableExpression("a"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex10);

        IStatement ex11 = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStatement(
                        new VariableDeclarationStatement("a", new model.types.RefType(new IntType())),
                        new CompStatement(
                                new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompStatement(
                                        new HeapAllocStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompStatement(
                                                new ForkStatement(
                                                        new CompStatement(
                                                                new HeapWriteStatement("a", new ValueExpression(new IntValue(30))),
                                                                new CompStatement(
                                                                        new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                                                        new CompStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex11);

        IStatement ex12 = new ForStatement(
                "i",
                new ValueExpression(new IntValue(0)),
                new RelationalExperssion(
                        new VariableExpression("i"),
                        new ValueExpression(new IntValue(3)),
                        RelationalOperator.SMALLER
                ),
                new AritmeticalExpression(
                        new VariableExpression("i"),
                        new ValueExpression(new IntValue(1)),
                        AritmeticalOperator.ADD
                ),
                new PrintStatement(new VariableExpression("i"))
        );
        statementList.add(ex12);

        IStatement ex13 = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStatement(
                        new VariableDeclarationStatement("x", new IntType()),
                        new CompStatement(
                                new VariableDeclarationStatement("y", new IntType()),
                                new CompStatement(
                                        new AssignStatement("v", new ValueExpression(new IntValue(0))),
                                        new CompStatement(
                                                new RepeatUntillStatement(
                                                        new CompStatement(
                                                                new ForkStatement(
                                                                        new CompStatement(
                                                                                new PrintStatement(new VariableExpression("v")),
                                                                                new AssignStatement(
                                                                                        "v",
                                                                                        new AritmeticalExpression(
                                                                                                new VariableExpression("v"),
                                                                                                new ValueExpression(new IntValue(1)),
                                                                                                AritmeticalOperator.SUB
                                                                                        )
                                                                                )
                                                                        )
                                                                ),
                                                                new AssignStatement(
                                                                        "v",
                                                                        new AritmeticalExpression(
                                                                                new VariableExpression("v"),
                                                                                new ValueExpression(new IntValue(1)),
                                                                                AritmeticalOperator.ADD
                                                                        )
                                                                )
                                                        ),
                                                        new RelationalExperssion(
                                                                new VariableExpression("v"),
                                                                new ValueExpression(new IntValue(3)),
                                                                RelationalOperator.EQUAL
                                                        )
                                                ),
                                                new CompStatement(
                                                        new AssignStatement("x", new ValueExpression(new IntValue(1))),
                                                        new CompStatement(
                                                                new NopStatement(),
                                                                new CompStatement(
                                                                        new AssignStatement("y", new ValueExpression(new IntValue(3))),
                                                                        new CompStatement(
                                                                                new NopStatement(),
                                                                                new PrintStatement(
                                                                                        new AritmeticalExpression(
                                                                                                new VariableExpression("v"),
                                                                                                new ValueExpression(new IntValue(10)),
                                                                                                AritmeticalOperator.MUL
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex13);

        IStatement ex14 = new CompStatement(
                new VariableDeclarationStatement("cnt", new IntType()),
                new CompStatement(
                        new AssignStatement("cnt", new ValueExpression(new IntValue(5))),
                        new CreateBarrierStatement("cnt", new VariableExpression("cnt"))
                )
        );
        statementList.add(ex14);

        IStatement ex15 = new CompStatement(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompStatement(
                        new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompStatement(
                                new VariableDeclarationStatement("v3", new RefType(new IntType())),
                                new CompStatement(
                                        new VariableDeclarationStatement("cnt", new IntType()),
                                        new CompStatement(
                                                new HeapAllocStatement("v1", new ValueExpression(new IntValue(2))),
                                                new CompStatement(
                                                        new HeapAllocStatement("v2", new ValueExpression(new IntValue(3))),
                                                        new CompStatement(
                                                                new HeapAllocStatement("v3", new ValueExpression(new IntValue(4))),
                                                                new CompStatement(
                                                                        new CreateBarrierStatement("cnt", new HeapReadExpression(new VariableExpression("v2"))),
                                                                        new CompStatement(
                                                                                new ForkStatement(
                                                                                        new CompStatement(
                                                                                                new AwaitStatement("cnt"),
                                                                                                new CompStatement(
                                                                                                        new HeapWriteStatement("v1", new AritmeticalExpression(
                                                                                                                new HeapReadExpression(new VariableExpression("v1")),
                                                                                                                new ValueExpression(new IntValue(10)),
                                                                                                                AritmeticalOperator.MUL
                                                                                                        )),
                                                                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("v1")))
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                new CompStatement(
                                                                                        new ForkStatement(
                                                                                                new CompStatement(
                                                                                                        new AwaitStatement("cnt"),
                                                                                                        new CompStatement(
                                                                                                                new HeapWriteStatement("v2", new AritmeticalExpression(
                                                                                                                        new HeapReadExpression(new VariableExpression("v2")),
                                                                                                                        new ValueExpression(new IntValue(10)),
                                                                                                                        AritmeticalOperator.MUL
                                                                                                                )),
                                                                                                                new CompStatement(
                                                                                                                        new HeapWriteStatement("v2", new AritmeticalExpression(
                                                                                                                                new HeapReadExpression(new VariableExpression("v2")),
                                                                                                                                new ValueExpression(new IntValue(10)),
                                                                                                                                AritmeticalOperator.MUL
                                                                                                                        )),
                                                                                                                        new PrintStatement(new HeapReadExpression(new VariableExpression("v2")))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompStatement(
                                                                                                new AwaitStatement("cnt"),
                                                                                                new PrintStatement(new HeapReadExpression(new VariableExpression("v3")))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        statementList.add(ex15);

        IStatement testForkProgram = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()), // Declare variable v
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(10))), // Assign 10 to v
                        new CompStatement(
                                new ForkStatement( // Fork a new thread
                                        new CompStatement(
                                                new AssignStatement("v", new ValueExpression(new IntValue(20))), // Change v in the forked thread
                                                new PrintStatement(new VariableExpression("v")) // Print v in the forked thread
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v")) // Print v in the main thread
                        )
                )
        );
        statementList.add(testForkProgram);
    }
}
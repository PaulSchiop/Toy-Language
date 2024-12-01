package view;

import model.expressions.AritmeticalOperator;
import model.expressions.ValueExpression;
import model.expressions.VariableExpression;
import model.state.PrgState;
import model.statements.*;
import model.types.IntType;
import model.types.StringType;
import model.values.IntValue;
import model.values.StringValue;
import repository.IRepo;
import repository.Repo;
import controller.Controller;
import model.expressions.AritmeticalExpression;
import view.commands.ExitCommand;
import view.commands.RunExample;
import model.expressions.*;

public class Interpreter {
    public static void main(String[] args) {
        // int v; v=2; Print(v)
        IStatement ex1 = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))
                )
        );
        PrgState prg1 = new PrgState(ex1);
        IRepo repo1 = new Repo("log1.txt");
        Controller ctrl1 = new Controller(repo1, true);
        ctrl1.addProgram(ex1);

        // int a; int b; a=2+3*5; b=a+1; Print(b)
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
        PrgState prg2 = new PrgState(ex2);
        IRepo repo2 = new Repo("log2.txt");
        Controller ctrl2 = new Controller(repo2, true);
        ctrl2.addProgram(ex2);

        // bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
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
        PrgState prg3 = new PrgState(ex3);
        IRepo repo3 = new Repo("log3.txt");
        Controller ctrl3 = new Controller(repo3, true);
        ctrl3.addProgram(ex3);

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

        PrgState prg4 = new PrgState(ex4);
        IRepo repo4 = new Repo("log4.txt");
        Controller ctrl4 = new Controller(repo4, true);
        ctrl4.addProgram(ex4);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), ctrl1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctrl2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctrl3));
        menu.addCommand(new RunExample("4", ex4.toString(), ctrl4));

        menu.show();
    }
}
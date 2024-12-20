package view;

import model.expressions.AritmeticalOperator;
import model.expressions.ValueExpression;
import model.expressions.VariableExpression;
import model.state.PrgState;
import model.statements.*;
import model.types.IntType;
import model.types.StringType;
import model.values.BoolValue;
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
        PrgState prg5 = new PrgState(ex5);
        IRepo repo5 = new Repo("log5.txt");
        Controller ctrl5 = new Controller(repo5, true);
        ctrl5.addProgram(ex5);

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
        PrgState prg6 = new PrgState(ex6);
        IRepo repo6 = new Repo("log6.txt");
        Controller ctrl6 = new Controller(repo6, true);
        ctrl6.addProgram(ex6);

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
        PrgState prg7 = new PrgState(ex7);
        IRepo repo7 = new Repo("log7.txt");
        Controller ctrl7 = new Controller(repo7, true);
        ctrl7.addProgram(ex7);

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
        PrgState prg8 = new PrgState(ex8);
        IRepo repo8 = new Repo("log8.txt");
        Controller ctrl8 = new Controller(repo8, true);
        ctrl8.addProgram(ex8);

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
                                                                new ValueExpression(new IntValue(1)),
                                                                AritmeticalOperator.SUB
                                                        )
                                                )
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v")) // print(v);
                        )
                )
        );

        PrgState prg9 = new PrgState(ex6);
        IRepo repo9 = new Repo("log9.txt");
        Controller ctrl9 = new Controller(repo9, true);
        ctrl9.addProgram(ex9);

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

        PrgState prg10 = new PrgState(ex10);
        IRepo repo10 = new Repo("log10.txt");
        Controller ctrl10 = new Controller(repo10, true);
        ctrl10.addProgram(ex10);

        IStatement ex11 = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStatement(
                        new VariableDeclarationStatement("a", new model.types.RefType(new IntType())),
                        new CompStatement(
                                new AssignStatement("v", new ValueExpression(new BoolValue(false))),
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

        IRepo repo11 = new Repo("log11.txt");
        Controller ctrl11 = new Controller(repo11, true);
        ctrl11.addProgram(ex11);

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

// Set up the repository and controller
        IRepo repo = new Repo("testForkLog.txt"); // Repository for logging
        Controller controller = new Controller(repo, true); // Controller to manage the program
        controller.addProgram(testForkProgram);


        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), ctrl1));
        menu.addCommand(new RunExample("2", ex2.toString(), ctrl2));
        menu.addCommand(new RunExample("3", ex3.toString(), ctrl3));
        menu.addCommand(new RunExample("4", ex4.toString(), ctrl4));
        menu.addCommand(new RunExample("5", ex5.toString(), ctrl5));
        menu.addCommand(new RunExample("6", ex6.toString(), ctrl6));
        menu.addCommand(new RunExample("7", ex7.toString(), ctrl7));
        menu.addCommand(new RunExample("8", ex8.toString(), ctrl8));
        menu.addCommand(new RunExample("9", ex9.toString(), ctrl9));
        menu.addCommand(new RunExample("10", ex10.toString(), ctrl10));
        menu.addCommand(new RunExample("11", ex11.toString(), ctrl11));
        menu.addCommand(new RunExample("12", testForkProgram.toString(), controller));
        menu.show();
    }
}
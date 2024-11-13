package view;

import controller.Controller;
import exceptions.EmptyStackExc;
import exceptions.ADTException;
import exceptions.ExpressionExceptions;
import exceptions.KeyNotFoundExc;
import exceptions.RepoException;
import exceptions.StatementException;
import model.statements.CompStatement;
import model.statements.IStatement;
import model.statements.*;
import model.expressions.*;
import model.adt.*;
import model.types.*;
import model.values.*;

import java.io.IOException;
import java.util.Scanner;

public class View {
    private final Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void run() {
        try{
            int input;
            Scanner scanner = new Scanner(System.in);
            while (true){
                System.out.println("Choose a program to run: ");
                System.out.println("1. v=2; print(v)");
                System.out.println("2. a=2*(3+5); b=a+1; print(b)");
                System.out.println("3. result=3*5; print(result)");
                System.out.println("0. Exit");
                input = scanner.nextInt();
                switch (input){
                    case 1:
                        runProgram1();
                        break;
                    case 2:
                        runProgram2();
                        break;
                    case 3:
                        runProgram3();
                        break;
                    case 4:
                        this.controller.executeOneStep(this.controller.getRepo().getCurrentProgram());
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid input");
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void runProgram1() throws EmptyStackExc {
        IStatement ex1 = new CompStatement(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        runStatement(ex1);
    }

    private void runProgram2() throws EmptyStackExc {
        IStatement ex2 = new CompStatement(
                new VariableDeclarationStatement("a", new IntType()),
                new CompStatement(
                        new VariableDeclarationStatement("b", new IntType()),
                        new CompStatement(
                                new AssignStatement("a", new AritmeticalExpression(new ValueExpression(new IntValue(2)),
                                        new AritmeticalExpression(new ValueExpression(new IntValue(3)),
                                                new ValueExpression(new IntValue(5)), AritmeticalOperator.ADD), AritmeticalOperator.MUL)),
                                new CompStatement(
                                        new AssignStatement("b", new AritmeticalExpression(new VariableExpression("a"),
                                                new ValueExpression(new IntValue(1)), AritmeticalOperator.ADD)),
                                        new PrintStatement(new VariableExpression("b"))))));
        runStatement(ex2);
    }

    private void runProgram3() throws EmptyStackExc {
        IStatement exMultiply = new CompStatement(
                new VariableDeclarationStatement("result", new IntType()),
                new CompStatement(
                        new AssignStatement("result", new AritmeticalExpression(
                                new ValueExpression(new IntValue(3)),
                                new ValueExpression(new IntValue(5)),
                                AritmeticalOperator.MUL)),
                        new PrintStatement(new VariableExpression("result"))
                )
        );
        runStatement(exMultiply);
    }


    private void runStatement(IStatement statement) throws EmptyStackExc {
        this.controller.addProgram(statement);
        try {
            this.controller.executeAllSteps();
            System.out.println("Result: " + this.controller.getRepo().getCurrentProgram().getOut().toString());
        } catch (EmptyStackExc | ADTException | ExpressionExceptions | KeyNotFoundExc | RepoException |
                 StatementException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

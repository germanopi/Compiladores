package br.ufpe.cin.if688;

import br.ufpe.cin.if688.ast.Stm;
import br.ufpe.cin.if688.grammar.MyVisitor;
import br.ufpe.cin.if688.grammar.GrammarLexer;
import br.ufpe.cin.if688.grammar.GrammarParser;
import br.ufpe.cin.if688.symboltable.Table;
import br.ufpe.cin.if688.visitor.Interpreter;
import br.ufpe.cin.if688.visitor.MaxArgsVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Scanner;

class Main {
	public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        StringBuilder input = new StringBuilder();
        while (in.hasNext())
            input.append(in.nextLine());

        MyVisitor grammarVisitor = new MyVisitor();
        Stm test = (Stm) grammarVisitor.visit((new GrammarParser(new CommonTokenStream(new GrammarLexer(CharStreams.fromString(input.toString()))))).goal());

        MaxArgsVisitor maxArgs = new MaxArgsVisitor();
        System.out.println("MaxArgs: " + maxArgs.visit(test));

        Interpreter interpreter = new Interpreter(null);
        Table t = interpreter.visit(test);
        System.out.println(t);
	}
}

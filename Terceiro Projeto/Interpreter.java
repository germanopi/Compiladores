package br.ufpe.cin.if688.visitor;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;
import br.ufpe.cin.if688.symboltable.IntAndTable;
import br.ufpe.cin.if688.symboltable.Table;

public class Interpreter implements IVisitor<Table> {

	// a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;

	public Interpreter(Table t) {
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		s.accept(this);
		return t;
	}

	@Override
	public Table visit(AssignStm s) {
		String id = s.getId();
		IntAndTableVisitor Visitor = new IntAndTableVisitor(t);
		IntAndTable auxiliar = s.getExp().accept(Visitor);
		double valor = auxiliar.result;
		t = new Table(id, valor, auxiliar.table);
		return t;
	}

	@Override
	public Table visit(CompoundStm s) {
		Table tabela1 = s.getStm1().accept(this);
		Table tabela2 = s.getStm2().accept(this);
		return t;
	}

	@Override
	public Table visit(PrintStm s) {
		s.getExps().accept(this);
		return t;
	}

	@Override
	public Table visit(Exp e) {
		IntAndTableVisitor Visitor = new IntAndTableVisitor(t);
		t = e.accept(Visitor).table;
		return t;
	}

	@Override
	public Table visit(EseqExp e) {
		IntAndTableVisitor Visitor = new IntAndTableVisitor(t);
		t = e.accept(Visitor).table;
		return t;
	}

	@Override
	public Table visit(IdExp e) {
		IntAndTableVisitor Visitor = new IntAndTableVisitor(t);
		t = e.accept(Visitor).table;
		return t;
	}

	@Override
	public Table visit(NumExp e) {
		return t;
	}

	@Override
	public Table visit(OpExp e) {
		IntAndTableVisitor Visitor = new IntAndTableVisitor(t);
		t = e.accept(Visitor).table;
		return t;
	}

	@Override
	public Table visit(ExpList el) {
		el.accept(this);
		return t;
	}

	@Override
	public Table visit(PairExpList el) {
		IntAndTableVisitor Visitor = new IntAndTableVisitor(t);
		IntAndTable auxiliar = el.getHead().accept(Visitor);
		t = auxiliar.table;
		System.out.print(auxiliar.result + "\n");
		el.getTail().accept(this);
		return t;
	}

	@Override
	public Table visit(LastExpList el) {
		IntAndTableVisitor Visitor = new IntAndTableVisitor(t);
		IntAndTable auxiliar = el.getHead().accept(Visitor);
		t = auxiliar.table;
		System.out.print(auxiliar.result + "\n");
		return t;
	}

}

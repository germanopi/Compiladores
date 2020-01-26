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

public class IntAndTableVisitor implements IVisitor<IntAndTable> {
	private Table t;

	public IntAndTableVisitor(Table t) {
		this.t = t;
	}

	@Override
	public IntAndTable visit(Stm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(AssignStm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(CompoundStm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(PrintStm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public IntAndTable visit(EseqExp e) {
		Interpreter Visitor = new Interpreter(t);
		t = e.getStm().accept(Visitor);
		return e.getExp().accept(this);
	}

	@Override
	public IntAndTable visit(IdExp e) {
		String id = e.getId();
		Table tabela = t;
		while (tabela.id != id && tabela.tail != null) {
			tabela = tabela.tail;
		}
		double valor = tabela.value;
		IntAndTable auxiliar = new IntAndTable(valor, t);
		return auxiliar;
	}

	@Override
	public IntAndTable visit(NumExp e) {
		IntAndTable auxiliar = new IntAndTable(e.getNum(), t);
		return auxiliar;
	}

	@Override
	public IntAndTable visit(OpExp e) {
		Exp esq = e.getLeft();
		Exp dir = e.getRight();
		double ResultEsq = esq.accept(this).result;
		double ResultDir = dir.accept(this).result;
		int operador = e.getOper();
		IntAndTable auxiliar;
		double resultado = 0;
		if (operador == 1) {
			resultado = ResultEsq + ResultDir;
		} else if (operador == 2) {
			resultado = ResultEsq - ResultDir;
		} else if (operador == 3) {
			resultado = ResultEsq * ResultDir;
		} else {
			resultado = ResultEsq / ResultDir;
		}
		auxiliar = new IntAndTable(resultado, t);
		return auxiliar;
	}

	@Override
	public IntAndTable visit(ExpList el) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(PairExpList el) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(LastExpList el) {
		// TODO Auto-generated method stub
		return null;
	}

}

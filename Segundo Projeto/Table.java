package br.ufpe.cin.if688.table;

import br.ufpe.cin.if688.parsing.analysis.*;
import br.ufpe.cin.if688.parsing.grammar.*;
import br.ufpe.cin.if688.table.LL1Key;

import java.util.*;

public final class Table {
	private Table() {
	}

	public static Map<LL1Key, List<GeneralSymbol>> createTable(Grammar g) throws NotLL1Exception {
		if (g == null)
			throw new NullPointerException();
		Map<Nonterminal, Set<GeneralSymbol>> first = SetGenerator.getFirst(g);
		Map<Nonterminal, Set<GeneralSymbol>> follow = SetGenerator.getFollow(g, first);
		Map<LL1Key, List<GeneralSymbol>> parsingTable = new HashMap<LL1Key, List<GeneralSymbol>>();

		/*
		 * Implemente sua table aqui.
		 */

		Collection<Production> REGRAS = g.getProductions();
		Set<GeneralSymbol> Temp;
		List<GeneralSymbol> Lista;
		List<Integer> indices;
		int i;
		for (Production producao : REGRAS) {
			if (producao.getProduction().contains(SpecialSymbol.EPSILON)) {
				Temp = follow.get(producao.getNonterminal());
				for (GeneralSymbol simbolo : Temp) {
					parsingTable.put(new LL1Key(producao.getNonterminal(), simbolo), producao.getProduction());
				}

			} else {
				Lista = new ArrayList<>();
				indices = new ArrayList<>();
				Lista.add(producao.getProduction().get(0));
				indices.add(0);
				i = 0;
				while (!(i >= Lista.size())) {
					if (Lista.get(i) instanceof Nonterminal) {
						for (Production producaoInicial : REGRAS) {
							if (producaoInicial.getNonterminal() == Lista.get(i)) {
								Lista.add(producaoInicial.getProduction().get(0));
								indices.add(0);
							}
						}
						i++;
					} else if (Lista.get(i) instanceof SpecialSymbol) {
						i++;
					} else {
						parsingTable.put(new LL1Key(producao.getNonterminal(), Lista.get(i)), producao.getProduction());
						i = Lista.size();
					}
				}
			}
		}

		// ------------------------------ Aqui já tem os prints para voce ------------

		System.out.print(g);
		System.out.println(first);
		System.out.println(follow);
		System.out.println(parsingTable);

		return sortTable(parsingTable);
	}

	static private Map<LL1Key, List<GeneralSymbol>> sortTable(Map<LL1Key, List<GeneralSymbol>> parsingTable) {
		// This sorts only the key, as the values in the set must be in the order of the
		// rule
		Map<LL1Key, List<GeneralSymbol>> sortedMap = new TreeMap<LL1Key, List<GeneralSymbol>>(new Comparator<LL1Key>() {
			@Override
			public int compare(LL1Key t1, LL1Key t2) {
				return t1.toString().compareTo(t2.toString());
			}
		});

		parsingTable.forEach((k, v) -> {
			sortedMap.put(k, v);
		});

		return sortedMap;
	}
}
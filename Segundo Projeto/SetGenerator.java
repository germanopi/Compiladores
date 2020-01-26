package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import br.ufpe.cin.if688.parsing.analysis.GeneralSymbol;
import br.ufpe.cin.if688.parsing.grammar.*;

public final class SetGenerator {

	public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {

		if (g == null)
			throw new NullPointerException("g nao pode ser nula.");

		Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);

		/*
		 * Implemente aqui o método para retornar o conjunto first
		 */

		Set<GeneralSymbol> TempFirst;
		Collection<Production> REGRAS = g.getProductions();
		List<GeneralSymbol> Lista;
		List<Integer> indices;
		int i;

		for (Production producaoInicial : REGRAS) {
			TempFirst = new HashSet<>();
			Lista = new ArrayList<>();
			indices = new ArrayList<>();
			Lista.add(producaoInicial.getNonterminal());
			indices.add(0);
			Lista.add(producaoInicial.getProduction().get(0));
			indices.add(0);
			i = 1;
			while (!(i >= Lista.size())) {
				if (Lista.get(i) instanceof Nonterminal) {
					if (!first.get(Lista.get(i)).isEmpty()) {
						TempFirst.addAll(first.get(producaoInicial.getNonterminal()));
					} else {
						for (Production producao : REGRAS) {
							if (producao.getNonterminal() == Lista.get(i)) {
								Lista.add(producao.getProduction().get(0));
								indices.add(0);
							}
						}
					}
					i++;
				} else {
					if (!first.get(producaoInicial.getNonterminal()).isEmpty()) {
						TempFirst.addAll(first.get(producaoInicial.getNonterminal()));
					}
					TempFirst.add(Lista.get(i));
					i++;
					if (i >= Lista.size()) {
						if (TempFirst.contains(SpecialSymbol.EPSILON)) {
							GeneralSymbol aux;
							int contador;
							do {
								aux = Lista.remove(Lista.size() - 1);
								contador = indices.remove(indices.size() - 1);
								i--;
							} while (!(aux instanceof Nonterminal));
							if (aux != producaoInicial.getNonterminal()) {
								for (Production producao : REGRAS) {
									if (contador < producao.getProduction().size()
											&& producao.getProduction().get(contador) == aux) {
										contador++;
										aux = producao.getNonterminal();
										for (Production producaoAux : REGRAS) {
											if (producaoAux.getNonterminal() == aux) {
												if (contador >= producaoAux.getProduction().size()) {
													break;
												} else {
													TempFirst.remove(SpecialSymbol.EPSILON);
													Lista.add(producaoAux.getProduction().get(contador));
													indices.add(contador);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			first.put(producaoInicial.getNonterminal(), TempFirst);
		}
		return sortList(first);
	}

	public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g,
			Map<Nonterminal, Set<GeneralSymbol>> first) {
		if (g == null || first == null)
			throw new NullPointerException();
		Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);
		/*
		 * implemente aqui o método para retornar o conjunto follow
		 */

		Set<GeneralSymbol> TempFollow;
		Collection<Production> REGRAS = g.getProductions();
		List<Production> Lista;
		List<Integer> indices;
		int i;

		for (Production producaoInicial : REGRAS) {
			TempFollow = new HashSet<>();
			Lista = new ArrayList<>();
			indices = new ArrayList<>();
			i = 0;

			if (g.getStartSymbol() == producaoInicial.getNonterminal()) {
				TempFollow.add(SpecialSymbol.EOF);
			}

			for (Production producao : REGRAS) {
				if (producao.getProduction().contains(producaoInicial.getNonterminal())) {
					indices.add(producao.getProduction().indexOf(producaoInicial.getNonterminal()));
					Lista.add(producao);
				}

			}

			while (i < Lista.size()) {
				if (indices.get(i) >= (Lista.get(i).getProduction().size() - 1)) {

					if (!(Lista.get(i).getProduction().get(indices.get(i)) instanceof Nonterminal)) {

						TempFollow.add(Lista.get(i).getProduction().get(indices.get(i)));
					} else {
						if ((Lista.get(i).getProduction().get(indices.get(i)) != Lista.get(i).getNonterminal())) {
							if (follow.get(Lista.get(i).getNonterminal()).isEmpty()) {
								for (Production producao : REGRAS) {
									if (producao.getProduction().contains(Lista.get(i).getNonterminal())) {
										indices.add(producao.getProduction().indexOf(Lista.get(i).getNonterminal()));
										Lista.add(producao);
									}
								}
							} else {
								TempFollow.addAll(follow.get(Lista.get(i).getNonterminal()));
							}
						}
						i++;
					}
				} else {
					GeneralSymbol contador = Lista.get(i).getProduction().get(indices.get(i) + 1);
					if (contador instanceof Nonterminal) {
						if (first.get(contador).contains(SpecialSymbol.EPSILON)) {
							if ((indices.get(i) + 2) > (Lista.get(i).getProduction().size() - 1)) {
								for (Production producao : REGRAS) {
									if (producao.getProduction().contains(Lista.get(i).getNonterminal())) {
										indices.add(producao.getProduction().indexOf(Lista.get(i).getNonterminal()));
										Lista.add(producao);
									}
								}
							} else {
								indices.add(indices.get(i) + 1);
								Lista.add(Lista.get(i));
							}
							TempFollow.addAll(first.get(contador));
							TempFollow.remove(SpecialSymbol.EPSILON);
						} else {
							TempFollow.addAll(first.get(contador));
						}
					} else {
						TempFollow.add(contador);
					}
				}

				i++;
			}
			follow.put(producaoInicial.getNonterminal(), TempFollow);
		}
		return sortList(follow);
	}

	static private Map<Nonterminal, Set<GeneralSymbol>> sortList(Map<Nonterminal, Set<GeneralSymbol>> firstOrFollow) {

		Map<Nonterminal, Set<GeneralSymbol>> sortedList = new TreeMap<Nonterminal, Set<GeneralSymbol>>(
				new Comparator<Nonterminal>() {
					@Override
					public int compare(Nonterminal o1, Nonterminal o2) {
						return o1.toString().compareTo(o2.toString());
					}
				});

		firstOrFollow.forEach((k, v) -> {
			List<GeneralSymbol> list = new ArrayList<>(v);
			Collections.sort(list, new Comparator<GeneralSymbol>() {
				@Override
				public int compare(GeneralSymbol t1, GeneralSymbol t2) {
					return t1.toString().compareTo(t2.toString());
				}
			});
			Set<GeneralSymbol> sortedSet = new HashSet<>();
			for (GeneralSymbol gs : list) {
				sortedSet.add(gs);
			}
			sortedList.put(k, sortedSet);
		});

		Map<Nonterminal, Set<GeneralSymbol>> sortedMap = new TreeMap<>(new Comparator<Nonterminal>() {
			@Override
			public int compare(Nonterminal t1, Nonterminal t2) {
				return t1.toString().compareTo(t2.toString());
			}
		});

		sortedMap = sortedList;

		return sortedMap;
	}

	// método para inicializar mapeamento nãoterminais -> conjunto de símbolos
	private static Map<Nonterminal, Set<GeneralSymbol>> initializeNonterminalMapping(Grammar g) {
		Map<Nonterminal, Set<GeneralSymbol>> result = new HashMap<Nonterminal, Set<GeneralSymbol>>();

		for (Nonterminal nt : g.getNonterminals())
			result.put(nt, new HashSet<GeneralSymbol>());

		return result;
	}

}
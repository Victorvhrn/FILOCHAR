import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import org.javatuples.Pair;
import org.javatuples.Quartet;

public class Filo implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int ESQUERDA = 13;
	private static final int DIREITA = 45;
	public static int htus = -1;
	public static int numTax;
	public static int numChar;
	public static int[][] matrizChar;
	public int indice;
	public int[] CC;
	public int[] CE;
	public int[] CCAux;
	public Filo pai;
	public Filo esq;
	public Filo dir;
	public String nome;
	public int parcimonia;

	public Filo(int indice) {
		if (indice >= 0)
			this.indice = indice;
		else {
			this.indice = htus;
			htus -= 1;
		}
		CC = new int[numChar];
		CE = new int[numChar];
		CCAux = new int[numChar];
		if (indice >= 0) {
			for (int i = 0; i < numChar; i++) {
				CC[i] = matrizChar[indice][i];
				CCAux[i] = (CC[i] == 2) ? 2 : 0;
			}
			nome = Integer.toString(this.indice);
		}
		pai = null;
		esq = null;
		dir = null;
	}

	public boolean isRaiz() {
		return this.pai == null;
	}

	public boolean isFolha() {
		return this.esq == null && this.dir == null;
	}

	@Override
	public String toString() {
		return Integer.toString(this.indice);
	}

	public int getPosicaoFilho() {
		return (this.pai.esq == this) ? ESQUERDA : DIREITA;
	}

	public int calcularCC() {
		int custo = 0;
		Pair<Filo, Integer> temp;
		Stack<Pair<Filo, Integer>> Q = new Stack<>();
		Q.push(new Pair<>(this, 0));
		while (!Q.isEmpty()) {
			temp = Q.pop();
			Filo N = temp.getValue0();
			int t = temp.getValue1();
			while (t != 1) {
				Q.push(new Pair<Filo, Integer>(N, 1));
				if (!N.isFolha()) {
					Q.push(new Pair<Filo, Integer>(N.dir, 0));
					Q.push(new Pair<Filo, Integer>(N.esq, 0));
				}
				temp = Q.pop();
				N = temp.getValue0();
				t = temp.getValue1();
			}
			if (!N.isFolha()) {
				int[] CCFD = N.dir.CC;
				int[] CCFE = N.esq.CC;
				int[] CCAuxFD = N.dir.CCAux;
				int[] CCAuxFE = N.esq.CCAux;
				for (int i = 0; i < Filo.numChar; i++) {
					if (CCAuxFD[i] == 2 && CCAuxFE[i] == 2)
						N.CCAux[i] = 2;
					else
						N.CCAux[i] = 0;

					if (CCFD[i] == CCFE[i])
						N.CC[i] = CCFD[i];
					else if (CCFD[i] != 2 && CCFE[i] != 2) {
						N.CC[i] = 2;
						custo += 1;
					} else if (CCFD[i] == 2) {
						N.CC[i] = CCFE[i];
					} else {
						N.CC[i] = CCFD[i];
					}
				}
			}
		}
		return custo;
	}

	public void calcularCE() {
		Stack<Filo> Q = new Stack<Filo>();
		Q.push(this);
		int[] CEPai, CCFD, CCFE;
		while (!Q.isEmpty()) {
			Filo N = Q.pop();
			if (N.isRaiz())
				CEPai = N.CC;
			else
				CEPai = N.pai.CE;
			if (N.isFolha()) {
				CCFD = N.CC;
				CCFE = N.CC;
			} else {
				CCFD = N.dir.CC;
				CCFE = N.esq.CC;
			}
			for (int i = 0; i < Filo.numChar; i++) {
				if (N.CC[i] == 2 || CEPai[i] == N.CC[i])
					N.CE[i] = CEPai[i];
				else if (CCFD[i] == CCFE[i]) {
					N.CE[i] = CCFD[i];
				} else
					N.CE[i] = 2;
			}
			if (!N.isFolha()) {
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
	}

	public int calcularValorInsercao(Filo u, Filo v) {
		int custo = 0;
		for (int i = 0; i < Filo.numChar; i++) {
			if (u.CE[i] == v.CE[i] && u.CE[i] != this.CE[i] && this.CE[i] != 2
					&& u.CE[i] != 2 && v.CE[i] != 2) {
				custo += 1;
			}
		}
		return custo;
	}

	public int calcularValorRemocao(Filo irmao, Filo avo) {
		int reducao = 0;
		for (int i = 0; i < Filo.numChar; i++) {
			if (avo.CE[i] == 2 && irmao.CE[i] == 2) {
				if (avo.CCAux[i] != 2 && this.CE[i] != 2) {
					reducao += 1;
				}
			} else if (avo.CE[i] == irmao.CE[i] && avo.CE[i] != this.CE[i]
					&& this.CE[i] != 2 && avo.CE[i] != 2 && irmao.CE[i] != 2) {
				reducao += 1;
			}
		}
		return reducao;
	}

	public Filo getIrmao() {
		return (this.pai.dir == this) ? this.pai.esq : this.pai.dir;
	}

	public static Filo removeNo(Filo raiz, Filo z) {
		if (z.pai == null) {
			z = null;
			raiz = z;
			return raiz;
		}
		Filo irmao_z = z.getIrmao();
		if (z.pai.pai != null) {
			Filo avo_z = z.pai.pai;
			irmao_z.pai = avo_z;
			if (avo_z.dir == z.pai)
				avo_z.dir = irmao_z;
			else
				avo_z.esq = irmao_z;
		} else {
			raiz = irmao_z;
			irmao_z.pai = null;
		}
		z.pai = null;
		return raiz;
	}

	public static void insereNo(Filo z, Filo u, Filo v) {
		Filo pai = new Filo(-1);
		pai.esq = z;
		pai.dir = v;
		z.pai = pai;
		v.pai = pai;
		if (u.dir == v)
			u.dir = pai;
		else
			u.esq = pai;
		pai.pai = u;
	}

	public Filo movimentar(Pair<Filo, Filo> aresta) {
		Filo u = aresta.getValue0();
		Filo v = aresta.getValue1();
		Filo pai = new Filo(-1);
		pai.pai = u;
		if (u.dir == v) {
			u.dir = pai;
		} else if (u.esq == v) {
			u.esq = pai;
		}
		pai.esq = this;
		pai.dir = v;
		this.pai = pai;
		v.pai = pai;
		return pai;
	}

	public HashSet<Integer> getFolhas() {
		HashSet<Integer> resultado = new HashSet<>();
		Stack<Filo> Q = new Stack<>();
		Filo N;
		Q.push(this);
		while (!Q.isEmpty()) {
			N = Q.pop();
			if (!N.isFolha()) {
				Q.push(N.dir);
				Q.push(N.esq);
			} else {
				resultado.add(N.indice);
			}
		}
		return resultado;
	}

	public ArrayList<Pair<Filo, Filo>> getArestas() {
		ArrayList<Pair<Filo, Filo>> arestas = new ArrayList<>();
		Stack<Filo> Q = new Stack<>();
		Q.push(this);
		Filo N;
		while (!Q.isEmpty()) {
			N = Q.pop();
			if (!N.isFolha()) {
				arestas.add(new Pair<Filo, Filo>(N, N.esq));
				arestas.add(new Pair<Filo, Filo>(N, N.dir));
				Q.push(N.dir);
				Q.push(N.esq);
			}

		}
		return arestas;
	}

	public ArrayList<Filo> getNos() {
		ArrayList<Filo> nos = new ArrayList<>();
		Stack<Filo> Q = new Stack<>();
		Filo N;
		Q.push(this);
		while (!Q.isEmpty()) {
			N = Q.pop();
			nos.add(N);
			if (!N.isFolha()) {
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
		return nos;
	}
	
	public ArrayList<Filo> getNosInternos() {
		ArrayList<Filo> nos = new ArrayList<>();
		Stack<Filo> Q = new Stack<>();
		Filo N;
		Q.push(this);
		while (!Q.isEmpty()) {
			N = Q.pop();
			if (!N.isFolha()) {
				nos.add(N);
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
		return nos;
	}

	public void debugaArvore() {
		Stack<Filo> Q = new Stack<>();
		Filo N;
		Q.push(this);
		System.out.println("++++++++++ÁRVORE+++++++++++++++");
		while (!Q.isEmpty()) {
			N = Q.pop();
			System.out.println("=====================");
			System.out.println("N.indice: " + N.indice);
			if (!N.isRaiz())
				System.out.println("N.pai: " + N.pai.indice);
			else
				System.out.println("Nó Raiz");
			if (!N.isFolha()) {
				System.out.println("Filhos N: " + N.esq.indice + "/"
						+ N.dir.indice);
			} else
				System.out.println("Nó Folha");
			System.out.println("=====================");
			if (!N.isFolha()) {
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
		System.out.println("++++++++++ FIM ÁRVORE+++++++++++++++");
	}

	public String printaArvore() {
		Stack<Pair<Filo, Integer>> Q = new Stack<>();
		Pair<Filo, Integer> temp;
		Q.push(new Pair<Filo, Integer>(this, 0));
		while (!Q.isEmpty()) {
			temp = Q.pop();
			Filo N = temp.getValue0();
			int t = temp.getValue1();
			while (t != 1) {
				Q.push(new Pair<Filo, Integer>(N, 1));
				if (!N.isFolha()) {
					Q.push(new Pair<Filo, Integer>(N.dir, 0));
					Q.push(new Pair<Filo, Integer>(N.esq, 0));
				}
				temp = Q.pop();
				N = temp.getValue0();
				t = temp.getValue1();
			}
			if (!N.isFolha()) {
				N.nome = "(" + N.indice + " " + N.esq.nome + " " + N.dir.nome
						+ ")";
			}
		}
		return this.nome;
	}

	public Pair<Filo, Filo> findArestaMelhorInsercao(Filo raiz) {
		Pair<Filo, Filo> melhorAresta = null;
		Filo N;
		int incEsq, incDir, incremento = Integer.MAX_VALUE;
		Stack<Filo> Q = new Stack<>();
		Q.push(raiz);
		while (!Q.isEmpty()) {
			N = Q.pop();
			if (!N.isFolha()) {
				incEsq = this.calcularValorInsercao(N, N.esq);
				incDir = this.calcularValorInsercao(N, N.dir);

				if (incEsq < incremento) {
					incremento = incEsq;
					melhorAresta = new Pair<Filo, Filo>(N, N.esq);
				}
				if (incDir < incremento) {
					incremento = incDir;
					melhorAresta = new Pair<Filo, Filo>(N, N.dir);
				}
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
		return melhorAresta;
	}

	public static Filo firstRotuGbr() {
		Filo raiz = new Filo(-1);
		Filo t;
		Pair<Filo, Filo> aresta;
		int parcimonia = 0;
		ArrayList<Integer> taxons = new ArrayList<>();
		for (int i = 0; i < numTax; i++) {
			taxons.add(i);
		}
		Collections.shuffle(taxons);
		for (int i = 0; i < numTax; i++) {
			t = new Filo(taxons.get(i));
			t.calcularCE();
			if (i == 0) {
				raiz.esq = t;
				t.pai = raiz;
			} else if (i == 1) {
				raiz.dir = t;
				t.pai = raiz;
				parcimonia = raiz.calcularCC();
				raiz.calcularCE();
			} else {
				aresta = t.findArestaMelhorInsercao(raiz);
				Filo.insereNo(t, aresta.getValue0(), aresta.getValue1());
				parcimonia = raiz.calcularCC();
				raiz.calcularCE();
			}
		}
		raiz.parcimonia = parcimonia;
		return raiz;
	}

	public static Filo gStepWR() {
		Random rd = new Random();
		Filo raiz = new Filo(-1), N;
		Filo t;
		int incremento, inc, incEsq, incDir;
		Pair<Filo, Integer> melhor;
		ArrayList<Pair<Filo, Integer>> melhores = new ArrayList<Pair<Filo, Integer>>();
		Quartet<Filo, Integer, Filo, Filo> melhor2 = null;
		ArrayList<Quartet<Filo, Integer, Filo, Filo>> melhores2 = new ArrayList<>();
		ArrayList<Integer> taxons = new ArrayList<>();
		for (int i = 0; i < numTax; i++)
			taxons.add(i);
		Collections.shuffle(taxons);
		while (!taxons.isEmpty()) {
			if (taxons.size() == numTax) {
				t = new Filo(taxons.get(0));
				t.calcularCE();
				raiz.dir = t;
				t.pai = raiz;
				taxons.remove(0);
			} else if (taxons.size() == numTax-1) {
				incremento = Integer.MAX_VALUE;
				for (int i = 0; i < taxons.size(); i++) {
					t = new Filo(taxons.get(i));
					t.calcularCE();
					inc = t.calcularValorInsercao(raiz.dir, raiz.dir);
					if (inc < incremento) {
						incremento = inc;
						melhores.clear();
						melhores.add(new Pair<Filo, Integer>(t, i));
					} else if (inc == incremento) {
						melhores.add(new Pair<Filo, Integer>(t, i));
					}
				}
				melhor = melhores.get((int) rd.nextInt(melhores.size()));
				t = melhor.getValue0();
				raiz.esq = t;
				t.pai = raiz;
				raiz.parcimonia = raiz.calcularCC();
				raiz.calcularCE();
				taxons.remove((int) melhor.getValue1());
			} else {
				incremento = Integer.MAX_VALUE;
				for (int i = 0; i < taxons.size(); i++) {
					t = new Filo(taxons.get(i));
					t.calcularCE();
					Stack<Filo> Q = new Stack<>();
					Q.push(raiz);
					while (!Q.isEmpty()) {
						N = Q.pop();
						if (!N.isFolha()) {
							incEsq = t.calcularValorInsercao(N, N.esq);
							incDir = t.calcularValorInsercao(N, N.dir);
							if (incEsq < incremento) {
								incremento = incEsq;
								melhores2.clear();
								melhores2.add(new Quartet<Filo, Integer, Filo, Filo>(
												t, i, N, N.esq));
							} else if (incEsq == incremento) {
								melhores2.add(new Quartet<Filo, Integer, Filo, Filo>(
												t, i, N, N.esq));
							}
							if (incDir < incremento) {
								incremento = incDir;
								melhores2.clear();
								melhores2.add(new Quartet<Filo, Integer, Filo, Filo>(
												t, i, N, N.dir));
							} else if (incDir == incremento) {
								melhores2.add(new Quartet<Filo, Integer, Filo, Filo>(
												t, i, N, N.dir));
							}
							Q.push(N.dir);
							Q.push(N.esq);
						}
					}
				}
				melhor2 = melhores2.get((int) rd.nextInt(melhores2.size()));
				Filo.insereNo(melhor2.getValue0(), melhor2.getValue2(),
						melhor2.getValue3());
				taxons.remove((int) melhor2.getValue1());
				raiz.parcimonia = raiz.calcularCC();
				raiz.calcularCE();
			}
		}
		raiz.parcimonia = raiz.calcularCC();
		raiz.calcularCE();
		return raiz;
	}

	public static Filo buscaLocal(Filo raiz) {
		// raiz.debugaArvore();
		boolean esq1, esq2;
		Filo s1, s2, q0, q1, movRaiz, s2Irmao, s2Avo;
		int custo1, custo2, melhorCusto, custoInsercao, custoMov;
		Pair<Filo, Filo> melhorR;
		ArrayList<Pair<Filo, Filo>> arestas = raiz.getArestas();
		ArrayList<Pair<Filo, Filo>> arestasS;
		for (Pair<Filo, Filo> q : arestas) {
			esq1 = false;
			q0 = q.getValue0();
			q1 = q.getValue1();
			esq2 = (q1.getPosicaoFilho() == DIREITA);
			s2 = q1.getIrmao();
			s2.pai = null;
			if (q0.isRaiz()) {
				s1 = q1;
				s1.pai = null;
			} else if (q0.getPosicaoFilho() == ESQUERDA) {
				esq1 = true;
				q1.pai = q0.pai;
				q0.pai.esq = q1;
				s1 = raiz;
			} else {
				q1.pai = q0.pai;
				q0.pai.dir = q1;
				s1 = raiz;
			}
			custo1 = s1.calcularCC();
			s1.calcularCE();
			custo2 = s2.calcularCC();
			s2.calcularCE();
			melhorCusto = Integer.MAX_VALUE;
			melhorR = null;
			if (!s1.isFolha()) {
				arestasS = s1.getArestas();
				for (Pair<Filo, Filo> r : arestasS) {
					custoInsercao = s2.calcularValorInsercao(r.getValue0(),
							r.getValue1());
					if (custoInsercao < melhorCusto) {
						melhorCusto = custoInsercao;
						melhorR = r;
					}
				}
			}
			if (melhorR != null) {
				movRaiz = s2.movimentar(melhorR);
				custoMov = custo1 + custo2 + melhorCusto;
				if (custoMov < raiz.parcimonia) {
					s1.parcimonia = s1.calcularCC();
					s1.calcularCE();
					return s1;
				}
			} else {
				q0.pai = q1.pai;
				if (esq2) {
					q0.esq = s2;
					q0.dir = q1;
				} else {
					q0.esq = q1;
					q0.dir = s2;
				}
				s2.pai = q0;
				q1.pai = q0;
				if (esq1 && !q0.isRaiz())
					q0.pai.esq = q0;
				else if (!esq1 && !q0.isRaiz())
					q0.pai.dir = q0;
				raiz = (q0.isRaiz()) ? q0 : s1;
				continue;
			}
			if (q1.indice == melhorR.getValue1().indice) {
				if (movRaiz.getPosicaoFilho() == ESQUERDA)
					movRaiz.pai.esq = q0;
				else
					movRaiz.pai.dir = q0;
			} else {
				s2Irmao = s2.pai.dir; // inserido em movimentar estï¿½ sempre
										// ï¿½ esquerda
				s2Avo = s2.pai.pai;
				s2Irmao.pai = s2Avo;
				if (s2Avo.esq == s2.pai)
					s2Avo.esq = s2Irmao;
				else
					s2Avo.dir = s2Irmao;
				if (esq2) {
					q0.esq = s2;
					q0.dir = q1;
				} else {
					q0.esq = q1;
					q0.dir = s2;
				}
			}
			q1.pai = q0;
			s2.pai = q0;
			if (esq1 && !q0.isRaiz())
				q0.pai.esq = q0;
			else if (!esq1 && !q0.isRaiz())
				q0.pai.dir = q0;
			raiz = (q0.isRaiz()) ? q0 : s1;
		}
		return raiz;
	}

	public Filo copy() {
		Filo filo = null;
		try {
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(this);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			filo = (Filo) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return filo;
	}

	public Filo findTaxon(int indice){
		Filo N;
		Stack<Filo> Q = new Stack<>();
		Q.push(this);
		while(!Q.isEmpty()){
			N = Q.pop();
			if(N.indice == indice)
				return N;
			else if(!N.isFolha()){
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
		return null;
	}
	
	public static void carregaInstancia(String path) throws IOException {
		BufferedReader br = new BufferedReader( new FileReader(path));
		String linha,chars;
		String temp[] = br.readLine().split("\\s+");
		int num_tax = Integer.parseInt(temp[1]);
		int num_char = Integer.parseInt(temp[0]);
		Filo.numTax = num_tax;
		Filo.numChar = num_char;
		int matriz[][] =  new int[num_tax][num_char];
		int i = 0;
		while((linha = br.readLine()) != null){
			if(linha.trim().equals("tread")){
				break;
			}
			temp = linha.split("\\s+");
			chars = temp[1];
			for(int j = 0; j < num_char; j++){
				if(chars.charAt(j) == '0')
					matriz[i][j] = 0;
				else if(chars.charAt(j) == '1')
					matriz[i][j] = 1;
				else if(chars.charAt(j) == '?')
					matriz[i][j] = 2;
			}
			i += 1;
		}
		br.close();
		Filo.matrizChar = matriz;
	}
	
	public static String printaVetor(String titulo,int[] vet){
		String saida = titulo+": ";
		for(int i = 0; i < numChar; i++)
			saida += vet[i]+" ";
		return saida;
	}
}

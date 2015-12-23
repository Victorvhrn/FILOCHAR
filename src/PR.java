import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import org.javatuples.Pair;


public class PR {
	public static HashSet<Integer> intersection(HashSet<Integer> a, HashSet<Integer> b){
		HashSet<Integer> c = new HashSet<>();
		int sizeA = a.size(),sizeB = b.size();
		if(sizeA < sizeB){
			for (Integer n : a) {
				if(b.contains(n)){
					c.add(n);
				}
			}
		}else{
			for (Integer n : b) {
				if(a.contains(n)){
					c.add(n);
				}
			}
		}
		return c;
	}
	
	public static Pair<Filo,Boolean> melhorRemocao(HashSet<Integer> W, HashSet<Integer> fEsq,
		HashSet<Integer> fDir, Filo esq, Filo dir){
		Pair<Filo,Boolean> resultado = null;
		Filo x = null,avo;
		boolean neto,esquerda = false;
		int melhorRed = Integer.MIN_VALUE,red;
		for (Integer k : W) {
			neto = false;
			if(fEsq.contains(k)){
				esquerda = true;
				x = esq.findTaxon(k);
				if(x.pai == esq.pai)
					neto = true;
			}else if(fDir.contains(k)){
				esquerda = false;
				x = dir.findTaxon(k);
				if (x.pai == dir.pai)
					neto = true;
			}
			avo = (x.pai.pai != null) ? x.pai.pai : x.pai;
			red = x.calcularValorRemocao(x.getIrmao(), avo);
			if(neto)
				red -= 1000; //netos de N só podem ser removidos em ultimo caso.
			if (red > melhorRed){
				melhorRed = red;
				resultado = new Pair<Filo,Boolean>(x, esquerda);
			}
				
		}
		return resultado;
	}
	
	public static Pair<Filo,Filo> melhorInsercao(Filo z, Filo raiz){
		if(raiz.isFolha())
			return new Pair<Filo, Filo>(raiz.pai, raiz);
		Pair<Filo, Filo> retorno = null;
		Filo N;
		int melhorInsercao = Integer.MAX_VALUE,ins;
		Stack<Filo> Q = new Stack<>();
		Q.push(raiz);
		while(!Q.isEmpty()){
			N = Q.pop();
			if(!N.isFolha()){
				ins = z.calcularValorInsercao(N, N.esq);
				if(ins < melhorInsercao){
					melhorInsercao = ins;
					retorno = new Pair<Filo, Filo>(N, N.esq);
				}
				ins = z.calcularValorInsercao(N, N.dir);
				if(ins < melhorInsercao){
					melhorInsercao = ins;
					retorno = new Pair<Filo, Filo>(N, N.dir);
				}
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
		return retorno;
		
	}
	
	public static void trocaNos(Filo n1,Filo n2){
		Filo p1,p2;
		p1 = n1.pai;
		p2 = n2.pai;
		n1.pai = p2;
		n2.pai = p1;
		if(p2.esq == n2)
			p2.esq = n1;
		else
			p2.dir = n1;
		if(p1.esq == n1)
			p1.esq = n2;
		else
			p1.dir = n2;
	}
	
	public static Filo removeNo(Filo raiz,Filo z){
		if(z.pai == null){
			z = null;
			raiz = z;
			return raiz;
		}
		Filo irmaoZ = z.getIrmao();
		Filo avoZ = z.pai.pai;
		if(avoZ != null){
			if(raiz == z){
				if(avoZ.esq == z.pai)
					avoZ.esq = irmaoZ;
				else if(avoZ.dir == z.pai)
					avoZ.dir = irmaoZ;
				irmaoZ.pai = avoZ;
				raiz = irmaoZ;
				z.pai = null;
				return raiz;
			}
			if (avoZ == raiz.pai){
				if(avoZ.esq == raiz)
					avoZ.esq = irmaoZ;
				else if(avoZ.dir == raiz)
					avoZ.dir = irmaoZ;
				irmaoZ.pai = avoZ;
				raiz = irmaoZ;
			}else{
				irmaoZ.pai = avoZ;
				if(avoZ.dir == z.pai)
					avoZ.dir = irmaoZ;
				else if(avoZ.esq == z.pai)
					avoZ.esq = irmaoZ;
			}
		}else{
			raiz = irmaoZ;
			irmaoZ.pai = null;
		}
		z.pai = null;
		return raiz;
	}
	
	public static Filo pathRelinking(Filo s1, Filo s2){
		int melhorCusto = Integer.MAX_VALUE,comp1,comp2;
		Filo melhor = null,ini,guia,s,N1,N2,esqN1,esqN2,dirN1,dirN2,remocao;
		Pair <Filo,Boolean> remocaoEsq = null;
		Pair <Filo,Filo> insercao = null;
		boolean podeDescer,esq,trocaN1;
		HashSet<Integer> folhasEsqN1,folhasEsqN2,folhasDirN1,folhasDirN2,W,temp;
		Stack<Filo> Q1 = new Stack<Filo>(),Q2 = new Stack<Filo>();
		for(int i = 0; i < 2; i++){
			if(i == 0){
				ini = s1;
				guia = s2;
			}else{
				ini = s2;
				guia = s1;
			}
			s = ini.copy();
//			System.out.println("s: "+s.printaArvore());
//			System.out.println("guia: "+guia.printaArvore());
			Q1.push(s);
			Q2.push(guia);
			while(!Q1.isEmpty()){
				N1 = Q1.pop();
				N2 = Q2.pop();
				esqN1 = N1.esq; dirN1 = N1.dir;
				esqN2 = N2.esq; dirN2 = N2.dir;
				folhasEsqN1 = (esqN1 != null) ? esqN1.getFolhas() : new HashSet<Integer>();
				folhasDirN1 = (dirN1 != null) ? dirN1.getFolhas() : new HashSet<Integer>();
				folhasEsqN2 = (esqN2 != null) ? esqN2.getFolhas() : new HashSet<Integer>();
				folhasDirN2 = (dirN2 != null) ? dirN2.getFolhas() : new HashSet<Integer>();
				temp = PR.intersection(folhasEsqN1,folhasEsqN2);
				temp.addAll(PR.intersection(folhasDirN1,folhasDirN2));
				comp1 = temp.size();
				temp = PR.intersection(folhasEsqN1,folhasDirN2);
				temp.addAll(PR.intersection(folhasEsqN1,folhasDirN2));
				comp2 = temp.size();
				if(comp2 > comp1){
					PR.trocaNos(esqN2, dirN2);
					esqN2 = N2.esq; dirN2 = N2.dir;
					folhasEsqN2 = esqN2.getFolhas();
					folhasDirN2 = dirN2.getFolhas();
				}
				W = PR.intersection(folhasEsqN1,folhasDirN2);
				W.addAll(PR.intersection(folhasDirN1,folhasEsqN2));
//				System.out.println("W: "+W);
				podeDescer = false;
				while(!podeDescer){
					if(W.size() == 0)
						break;
					remocaoEsq = PR.melhorRemocao(W, folhasEsqN1, folhasDirN1, esqN1, dirN1);
					remocao = remocaoEsq.getValue0(); esq = remocaoEsq.getValue1();
					trocaN1 = remocao.pai == N1;
					if(esq){
						esqN1 = PR.removeNo(esqN1, remocao);
						if(trocaN1){
							N1 = esqN1;
							esqN1 = N1.esq; dirN1 = N1.dir;
						}
						insercao = PR.melhorInsercao(remocao, dirN1);
					}else{
						dirN1 = PR.removeNo(dirN1, remocao);
						if(trocaN1){
							N1 = dirN1;
							esqN1 = N1.esq; dirN1 = N1.dir;
						}
						insercao = PR.melhorInsercao(remocao, esqN1);
					}
					Filo.insereNo(remocao, insercao.getValue0(), insercao.getValue1());
					s.parcimonia = s.calcularCC();
					s.calcularCE();
					if(s.parcimonia < melhorCusto){
						melhorCusto = s.parcimonia;
						melhor = s.copy();
					}
					esqN1 = N1.esq; dirN1 = N1.dir;
					folhasEsqN1 = esqN1.getFolhas();
					folhasDirN1 = dirN1.getFolhas();
					temp = PR.intersection(folhasEsqN1,folhasEsqN2);
					temp.addAll(PR.intersection(folhasDirN1,folhasDirN2));
					comp1 = temp.size();
					temp = PR.intersection(folhasEsqN1,folhasDirN2);
					temp.addAll(PR.intersection(folhasEsqN1,folhasDirN2));
					comp2 = temp.size();
					if(comp2 > comp1){
						PR.trocaNos(esqN2, dirN2);
						esqN2 = N2.esq; dirN2 = N2.dir;
						folhasEsqN2 = esqN2.getFolhas();
						folhasDirN2 = dirN2.getFolhas();
					}
					W = PR.intersection(folhasEsqN1,folhasDirN2);
					W.addAll(PR.intersection(folhasDirN1,folhasEsqN2));
				}
				if(!N1.isFolha()){
					Q1.push(N1.dir);
					Q1.push(N1.esq);
					Q2.push(N2.dir);
					Q2.push(N2.esq);
				}
			}
		}
		if(melhor == null){
			if(s1.parcimonia <= s2.parcimonia)
				melhor = s1;
			else
				melhor = s2;
		}
		return melhor;
	}

	public static void insereElite(int maxElite, Filo bar,
			ArrayList<Filo> elitePool) {
		if(elitePool.size() >= maxElite)
			return;
		else if(elitePool.isEmpty()){
			elitePool.add(bar);
			return;
		}
		
		int maior = Integer.MIN_VALUE, indice = 0,i = 0;
		for (Filo filo : elitePool) {
			if(filo.parcimonia > maior){
				maior = filo.parcimonia;
				indice = i; 
			}else if(filo.parcimonia == bar.parcimonia)
				return;
			i++;
		}
		if (bar.parcimonia < maior)
			elitePool.add(bar);
		if(i == maxElite)
			elitePool.remove(indice);
	}
	
}

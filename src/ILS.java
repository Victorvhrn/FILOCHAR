import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import org.javatuples.Pair;

public class ILS {
	public static Filo perturbacao(Filo raiz) {
		Random rd = new Random();
		ArrayList<Filo> internos = raiz.getNosInternos();
		Filo sub = internos.get(rd.nextInt(internos.size()));
		ArrayList<Filo> subs1 = sub.esq.getNos();
		ArrayList<Filo> subs2 = sub.dir.getNos();
		Filo N1 = subs1.get(rd.nextInt(subs1.size()));
		Filo N2 = subs2.get(rd.nextInt(subs2.size()));
		Filo P1 = N1.pai;
		Filo P2 = N2.pai;
		P1 = N1.pai;
		P2 = N2.pai;
		N1.pai = P2;
		N2.pai = P1;
		if(P2.esq == N2)
			P2.esq = N1;
		else
			P2.dir = N1;
		if(P1.esq == N1)
			P1.esq = N2;
		else
			P1.dir = N2;
		raiz.parcimonia = raiz.calcularCC();
		raiz.calcularCE();
		return raiz;
	}
	
	public static Filo perturbacaoN(Filo raiz, int n){
		Random rd = new Random();
		ArrayList<Filo> subs1,subs2,internos;
		Filo N1,N2,P1,P2,sub;
		for(int i = 0; i < n; i++){
			internos = raiz.getNosInternos();
			sub = internos.get(rd.nextInt(internos.size()));
			subs1 = sub.esq.getNos();
			subs2 = sub.dir.getNos();
			N1 = subs1.get(rd.nextInt(subs1.size()));
			N2 = subs2.get(rd.nextInt(subs2.size()));
			N1 = subs1.get(rd.nextInt(subs1.size()));
			N2 = subs2.get(rd.nextInt(subs2.size()));
			P1 = N1.pai;
			P2 = N2.pai;
			P1 = N1.pai;
			P2 = N2.pai;
			N1.pai = P2;
			N2.pai = P1;
			if(P2.esq == N2)
				P2.esq = N1;
			else
				P2.dir = N1;
			if(P1.esq == N1)
				P1.esq = N2;
			else
				P1.dir = N2;
		}
		raiz.parcimonia = raiz.calcularCC();
		raiz.calcularCE();
		return raiz;
	}
	
	static Filo perturbacaoGula(Filo raiz){
		Random rd = new Random();
		Filo temp = raiz.copy();
		Filo s1,s2;
		if(rd.nextBoolean()){
			s1 = temp.esq;
			s2 = temp.dir;
		}else{
			s2 = temp.esq;
			s1 = temp.dir;
		}
		s1.pai = null;
		s2.pai = null;
		s1.calcularCC(); s2.calcularCC();
		s1.calcularCE(); s2.calcularCE();
		int melhor = Integer.MAX_VALUE, inc;
		Filo u = null,v = null,N;
		Stack<Filo> Q = new Stack<>();
		Q.push(s1);
		while(!Q.isEmpty()){
			N = Q.pop();
			if(!N.isFolha()){
				inc = s2.calcularValorInsercao(N, N.esq);
				if(inc < melhor){
					melhor = inc;
					u = N;
					v = N.esq;
				}
				inc = s2.calcularValorInsercao(N, N.dir);
				if(inc < melhor){
					melhor = inc;
					u = N;
					v = N.dir;
				}
				Q.push(N.dir);
				Q.push(N.esq);
			}
		}
		if(u == null || v == null)
			s1 = perturbacaoN(raiz, 1);
		else
			Filo.insereNo(s2, u, v);
		s1.parcimonia = s1.calcularCC();
		s1.calcularCE();
		return s1;
	}
	
	public static Filo aceitacao(Filo star, Filo lin){
		return (lin.parcimonia < star.parcimonia) ? lin : star;
	}
	
	public static Filo executeRotu(int maxIter){
		Filo star = Filo.firstRotuGbr();
		Filo bar,lin = null;
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
			i++;
		}
		return star;
	}
	
	public static Filo executeGStep(int maxIter){
		Filo star = Filo.gStepWR();
		Filo bar,lin = null;
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
			i++;
		}
		return star;
	}
	
	public static Filo executeTargetRotu(int alvo){
		Filo star = Filo.firstRotuGbr();
		Filo bar,lin = null;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
		}
		return star;
	}
	
	
	public static Filo executeTargetGStep(int alvo){
		Filo star = Filo.gStepWR();
		Filo bar,lin = null;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
		}
		return star;
	}
	
	public static Filo executeRotuPR(int maxIter,int freqPR,int maxElite){
		Filo star = Filo.firstRotuGbr();
		Filo bar,lin = null,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
			PR.insereElite(maxElite,bar,elitePool);
			if(i % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
				}
				PR.insereElite(maxElite,bar,elitePool);
			}
			i++;
		}
		return star;
	}
	
	public static Filo executeGStepPR(int maxIter,int freqPR,int maxElite){
		Filo star = Filo.gStepWR();
		Filo bar,lin = null,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
			PR.insereElite(maxElite,bar,elitePool);
			if(i % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
				}
				PR.insereElite(maxElite,bar,elitePool);
			}
			i++;
		}
		return star;
	}
	
	public static Filo executeTargetRotuPR(int alvo,int freqPR,int maxElite){
		Filo star = Filo.firstRotuGbr();
		Filo bar,lin = null,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		int k = 0;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
			PR.insereElite(maxElite,bar,elitePool);
			if(k % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
				}
				PR.insereElite(maxElite,bar,elitePool);
			}
			k++;
		}
		return star;
	}
	
	public static Filo executeTargetGStepPR(int alvo,int freqPR,int maxElite){
		Filo star = Filo.gStepWR();
		Filo bar,lin = null,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		int k = 0;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			star = aceitacao(star, lin);
			PR.insereElite(maxElite,bar,elitePool);
			if(k % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
				}
				PR.insereElite(maxElite,bar,elitePool);
			}
			k++;
		}
		return star;
	}
	
	public static Pair<Filo,Boolean> accept(Filo star, Filo lin){
		return (lin.parcimonia < star.parcimonia) ? new Pair<Filo, Boolean>(lin, true) :
			new Pair<Filo, Boolean>(star, false);
	}
	
	public static Filo executeFullRotu(int semMelhora){
		Filo star = Filo.firstRotuGbr();
		Filo bar,lin = null;
		boolean melhorou;
		boolean podeSair = false;
		int i = 0;
		Pair<Filo,Boolean> temp;
		while(!podeSair){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			temp = accept(star, lin);
			star = temp.getValue0();
			if(temp.getValue1()){
				i = 0;
			}else{
				i++;
			}
			if(i == semMelhora){
				podeSair = true;
			}
				
		}
		return star;
	}
	
	public static Filo executeFullGStep(int semMelhora){
		Filo star = Filo.gStepWR();
		Filo bar,lin = null;
		boolean melhorou;
		boolean podeSair = false;
		int i = 0;
		Pair<Filo,Boolean> temp;
		while(!podeSair){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			temp = accept(star, lin);
			star = temp.getValue0();
			if(temp.getValue1()){
				i = 0;
			}else{
				i++;
			}
			if(i == semMelhora){
				podeSair = true;
			}
				
		}
		return star;
	}
	
	
	public static Filo executeFullRotuPR(int semMelhora,int freqPR,int maxElite){
		Filo star = Filo.firstRotuGbr();
		Filo bar,lin = null,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou;
		boolean podeSair = false;
		Pair <Filo,Boolean> temp;
		int i = 0,k = 0;
		while(!podeSair){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			temp = accept(star, lin);
			star = temp.getValue0();
			if(temp.getValue1()){
				k = 0;
			}
			PR.insereElite(maxElite,bar,elitePool);
			if(i % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
					k = 0;
				}else if(!temp.getValue1()){
					k++;
				}
				PR.insereElite(maxElite,bar,elitePool);
			}
			if(k == semMelhora){
				podeSair = true;
			}
			i++;
		}
		return star;
	}
	
	public static Filo executeFullGStepPR(int semMelhora,int freqPR,int maxElite){
		Filo star = Filo.gStepWR();
		Filo bar,lin = null,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou;
		boolean podeSair = false;
		Pair <Filo,Boolean> temp;
		int i = 0,k = 0;
		while(!podeSair){
			bar = star.copy();
			bar = perturbacaoN(bar,1);
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else
					melhorou = false;
			}
			temp = accept(star, lin);
			star = temp.getValue0();
			if(temp.getValue1()){
				k = 0;
			}
			PR.insereElite(maxElite,bar,elitePool);
			if(i % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
					k = 0;
				}else if(!temp.getValue1()){
					k++;
				}
				PR.insereElite(maxElite,bar,elitePool);
			}
			if(k == semMelhora){
				podeSair = true;
			}
			i++;
		}
		return star;
	}
}

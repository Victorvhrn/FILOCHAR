import java.util.ArrayList;
import java.util.Random;


public class GRASP {
	public static Filo executeTargetGStep(int alvo){
		Filo star = Filo.gStepWR(),bar,lin;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = Filo.gStepWR();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
		}
		return star;
	}
	
	public static Filo executeTargetRotu(int alvo){
		Filo star = Filo.firstRotuGbr(),bar,lin;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = Filo.firstRotuGbr();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
		}
		return star;
	}
	
	public static Filo executeRotu(int maxIter){
		Filo star = Filo.firstRotuGbr(),bar,lin;
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = Filo.firstRotuGbr();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
			i++;
		}
		return star;
	}
	
	public static Filo executeGStep(int maxIter){
		Filo star = Filo.gStepWR(),bar,lin;
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = Filo.gStepWR();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
			i++;
		}
		return star;
	}
	
	public static Filo executeTargetRotuPR(int alvo,int freqPR, int maxElite){
		Filo star = Filo.firstRotuGbr(),bar,lin,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		int k = 0;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = Filo.firstRotuGbr();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
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
	
	
	public static Filo executeTargetGStepPR(int alvo,int freqPR, int maxElite){
		Filo star = Filo.gStepWR(),bar,lin,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		int k = 0;
		boolean melhorou;
		int i = 0;
		while(star.parcimonia > alvo && i < 300){
			i++;
			bar = Filo.gStepWR();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
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
	
	public static Filo executeRotuPR(int maxIter, int freqPR,int maxElite){
		Filo star = Filo.firstRotuGbr(),bar,lin,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = Filo.firstRotuGbr();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
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
	
	public static Filo executeGStepPR(int maxIter,int freqPR, int maxElite){
		Filo star = Filo.gStepWR(),bar,lin,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou;
		int i = 0;
		while(i < maxIter){
			bar = Filo.gStepWR();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
			}
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
	
	public static Filo executeFullRotu(int semMelhora){
		Filo star = Filo.firstRotuGbr(),bar,lin;
		boolean melhorou;
		int i = 0;
		boolean podeSair = false;
		while(!podeSair){
			bar = Filo.firstRotuGbr();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
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
		Filo star = Filo.gStepWR(),bar,lin;
		boolean melhorou;
		boolean podeSair = false;
		int i = 0;
		while(!podeSair){
			bar = Filo.gStepWR();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			if(bar.parcimonia < star.parcimonia){
				star = bar;
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
	
	
	public static Filo executeFullRotuPR(int semMelhora, int freqPR,int maxElite){
		Filo star = Filo.firstRotuGbr(),bar,lin,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou,melhorouOtimo;
		boolean podeSair = false;
		int i = 0,k = 0;
		while(!podeSair){
			bar = Filo.firstRotuGbr();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			melhorouOtimo = false;
			if(bar.parcimonia < star.parcimonia){
				star = bar;
				k = 0;
				melhorouOtimo = true;
			}
			PR.insereElite(maxElite,bar,elitePool);
			if(i % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
					k = 0;
				}else if(!melhorouOtimo){
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
	
	public static Filo executeFullGStepPR(int semMelhora,int freqPR, int maxElite){
		Filo star = Filo.gStepWR(),bar,lin,poolMember;
		ArrayList<Filo> elitePool = new ArrayList<>();
		Random rd = new Random();
		boolean melhorou,melhorouOtimo;
		boolean podeSair = false;
		int i = 0,k = 0;
		while(!podeSair){
			bar = Filo.gStepWR();
			melhorou = true;
			while(melhorou){
				lin = Filo.buscaLocal(bar);
				if (lin.parcimonia < bar.parcimonia){
					bar = lin;
				}else{
					melhorou = false;
				}
			}
			melhorouOtimo = false;
			if(bar.parcimonia < star.parcimonia){
				star = bar;
				k = 0;
				melhorouOtimo = true;
			}
			PR.insereElite(maxElite,bar,elitePool);
			if(i % freqPR == 0 && !elitePool.isEmpty()){
				poolMember = elitePool.get(rd.nextInt(elitePool.size()));
				bar = PR.pathRelinking(bar, poolMember);
				if(bar.parcimonia < star.parcimonia){
					star = bar;
					k = 0;
				}else if(!melhorouOtimo){
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

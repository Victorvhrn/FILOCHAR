import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;


public class Teste {
	public static final double BILHAO = Math.pow(10.0, 9.0); 
	
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime( ) : 0L;
	}
	
	public static long getUserTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadUserTime( ) : 0L;
	}
	
	public static long getSystemTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        (bean.getCurrentThreadCpuTime( ) - bean.getCurrentThreadUserTime( )) : 0L;
	}
	
	public static String testeConstrucao(String guia) throws IOException{
		long t0, dt,tempos;
		int valores;
		double mediaValRotu,mediaValGStep,mediaTempoRotu,mediaTempoGStep;
		Filo result;
		int iters = 100;
		String saida = "Instancia\tFirstRotuGbr(Tempo(s)/Valor)\tGStepWR(Tempo(s)/Valor)\n";
		String linha = "";
		BufferedReader br = new BufferedReader(new FileReader(guia));
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/resultados_construcao.txt"));
		while((linha = br.readLine()) != null){
			linha = linha.trim().split("\\s+")[0];
			Filo.carregaInstancia(linha);
			tempos = 0; valores = 0; 
			for(int i = 0; i < iters; i++){
				t0 = getCpuTime();
				result = Filo.firstRotuGbr();
				dt = getCpuTime() - t0;
				tempos += dt;
				valores += result.parcimonia;
			}
			mediaTempoRotu = tempos/iters;
			mediaValRotu = valores/iters;
			tempos = 0; valores = 0;
			for(int i = 0; i < iters; i++){
				t0 = getCpuTime();
				result = Filo.gStepWR();
				dt = getCpuTime() - t0;
				tempos += dt;
				valores += result.parcimonia;
			}
			mediaTempoGStep = tempos/iters;
			mediaValGStep = valores/iters;
			saida += String.format("%s\t\t%.3f/%.1f\t\t%.3f/%.1f\n", linha,mediaTempoRotu/BILHAO,
					mediaValRotu,mediaTempoGStep/BILHAO,mediaValGStep);
		}
		bw.write(saida);
		br.close();
		bw.close();
		return saida;
	}

	
	public static String bateriaTestes(String guia) throws IOException{
		String saida = "";
		BufferedReader br = new BufferedReader(new FileReader(guia));
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/results_metaheuristicas.txt"));
		String linha;
		int iters = 10;
		int freqPR = 20;
		int maxElite = 10;
		while((linha = br.readLine()) != null){
			linha = linha.trim();
			try{
				saida += teste1(linha,iters);
				saida += teste2(linha,iters);
				saida += teste3(linha,iters,freqPR,maxElite);
				saida += teste4(linha,iters,freqPR,maxElite);
				saida += teste5(linha,iters);
				saida += teste6(linha,iters);
				saida += teste7(linha,iters,freqPR,maxElite);
				saida += teste8(linha,iters,freqPR,maxElite);
			}catch (Exception ex){
				break;
			}
		}
		br.close();
		bw.write(saida);
		bw.close();
		return saida;
	}
	
	public static String teste1(String linha,int iters) throws IOException{
		String saida = "\t\t\t\tGRASP FirstRotuGbr com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetRotu(alvo).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	
	public static String teste2(String linha, int iters) throws IOException{
		String saida = "\t\t\t\tGRASP GStepWR com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStep(alvo).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	
	public static String teste3(String linha,int iters, int freqPR, int maxElite) throws IOException{
		String saida = "\t\t\t\tGRASP FirstRotuGbr + PR com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetRotuPR(alvo, freqPR, maxElite).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	
	public static String teste4(String linha,int iters, int freqPR, int maxElite) throws IOException{
		String saida = "\t\t\t\tGRASP GStepWR + PR com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStepPR(alvo, freqPR, maxElite).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	public static String teste5(String linha,int iters) throws IOException{
		String saida = "\t\t\tILS FirstRotuGbr com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetRotu(alvo).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	
	
	public static String teste6(String linha,int iters) throws IOException{
		String saida = "\t\t\tILS GStepWR com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStep(alvo).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	
	public static String teste7(String linha,int iters,int freqPR, int maxElite) throws IOException{
		String saida = "\t\t\t\tILS FirstRotuGbr + PR com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetRotuPR(alvo, freqPR, maxElite).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	
	public static String teste8(String linha,int iters,int freqPR, int maxElite) throws IOException{
		String saida = "\t\t\t\tILS GStepWR + PR com valor alvo:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor Final\n";
		String[] temp = linha.split("\\s+");
		Filo.carregaInstancia(temp[0]);
		int alvo = Integer.parseInt(temp[1]);
		int val;
		long t0,dt;
		double mediaTempos;
		double mediaValores;
		long tempos = 0; int valores = 0;
		for(int i  = 0; i < iters; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStepPR(alvo, freqPR, maxElite).parcimonia;
			dt = getCpuTime() - t0;
			valores += val;
			tempos += dt;
		}
		mediaTempos = tempos/iters;
		mediaValores = valores/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n",temp[0],mediaTempos/BILHAO,mediaValores);
		System.out.println(saida);
		return saida;
	}
	
	public static void testeGraspTTTPlots() throws IOException{
		BufferedWriter bw1 = new BufferedWriter(new FileWriter("ANGI_GRASP.txt"));
		BufferedWriter bw11 = new BufferedWriter(new FileWriter("ANGI_GRASP_PR.txt"));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter("ETHE_GRASP.txt"));
		BufferedWriter bw21 = new BufferedWriter(new FileWriter("ETHE_GRASP_PR.txt"));
		BufferedWriter bw3 = new BufferedWriter(new FileWriter("CARP_GRASP.txt"));
		BufferedWriter bw31 = new BufferedWriter(new FileWriter("CARP_GRASP_PR.txt"));
		int val;
		long t0,dt;
		Filo.carregaInstancia("instancias/ANGI.txt");
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStep(222).parcimonia;
			dt = getCpuTime() - t0;
			bw1.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw1.close();
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStepPR(222,20,10).parcimonia;
			dt = getCpuTime() - t0;
			bw11.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw11.close();
		System.out.println("ANGI FOI");
		Filo.carregaInstancia("instancias/ETHE.txt");
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStep(378).parcimonia;
			dt = getCpuTime() - t0;
			bw2.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw2.close();
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStepPR(378,20,10).parcimonia;
			dt = getCpuTime() - t0;
			bw21.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw21.close();
		System.out.println("ETHE FOI");
		Filo.carregaInstancia("instancias/CARP.txt");
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStep(567).parcimonia;
			dt = getCpuTime() - t0;
			bw3.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw3.close();
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = GRASP.executeTargetGStepPR(567,20,10).parcimonia;
			dt = getCpuTime() - t0;
			bw31.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw31.close();
		System.out.println("CARP FOI");
		
	}
	public static void testeILSTTTPlots() throws IOException{
		BufferedWriter bw1 = new BufferedWriter(new FileWriter("ANGI_ILS.txt"));
		BufferedWriter bw11 = new BufferedWriter(new FileWriter("ANGI_ILS_PR.txt"));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter("ETHE_ILS.txt"));
		BufferedWriter bw21 = new BufferedWriter(new FileWriter("ETHE_ILS_PR.txt"));
		BufferedWriter bw3 = new BufferedWriter(new FileWriter("CARP_ILS.txt"));
		BufferedWriter bw31 = new BufferedWriter(new FileWriter("CARP_ILS_PR.txt"));
		int val;
		long t0,dt;
		Filo.carregaInstancia("instancias/ANGI.txt");
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStep(222).parcimonia;
			dt = getCpuTime() - t0;
			bw1.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw1.close();
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStepPR(222,20,10).parcimonia;
			dt = getCpuTime() - t0;
			bw11.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw11.close();
		System.out.println("ANGI FOI");
		Filo.carregaInstancia("instancias/ETHE.txt");
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStep(378).parcimonia;
			dt = getCpuTime() - t0;
			bw2.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw2.close();
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStepPR(378,20,10).parcimonia;
			dt = getCpuTime() - t0;
			bw21.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw21.close();
		System.out.println("ETHE FOI");
		Filo.carregaInstancia("instancias/CARP.txt");
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStep(567).parcimonia;
			dt = getCpuTime() - t0;
			bw3.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw3.close();
		for(int i = 0; i < 100; i++){
			t0 = getCpuTime();
			val = ILS.executeTargetGStepPR(567,20,10).parcimonia;
			dt = getCpuTime() - t0;
			bw31.write(Double.toString(dt/BILHAO)+"\n");
			if(i % 20 == 0)
				System.out.println(i);
		}
		bw31.close();
		System.out.println("CARP FOI");
	}
	
	public static String testeFull(String guia) throws IOException{
		String saida = "";
		BufferedReader br = new BufferedReader(new FileReader(guia));
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/results_metaheuristicas_full.txt"));
		String linha;
		int iters = 3;
		int freqPR = 20;
		int maxElite = 10;
		int semMelhora = 100;
		while((linha = br.readLine()) != null){
			linha = linha.trim();
			try{
				saida += testeGraspRotuFull(linha,iters,semMelhora);
				saida += testeGraspGStepFull(linha,iters,semMelhora);
				saida += testeGraspRotuPRFull(linha,iters,semMelhora,freqPR,maxElite);
				saida += testeGraspGStepPRFull(linha,iters,semMelhora,freqPR,maxElite);
				saida += testeILSRotuFull(linha,iters,semMelhora);
				saida += testeILSGStepFull(linha,iters,semMelhora);
				saida += testeILSRotuPRFull(linha,iters,semMelhora,freqPR,maxElite);
				saida += testeILSGStepPRFull(linha,iters,semMelhora,freqPR,maxElite);
			}catch (IOException ex){
				break;
			}
		}
		br.close();
		bw.write(saida);
		bw.close();
		return saida;
	}

	private static String testeILSGStepPRFull(String linha, int iters,
			int semMelhora,int freqPR, int maxElite) throws IOException {
		String saida = "Teste ILS GStep + PR Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = ILS.executeFullGStepPR(semMelhora,freqPR,maxElite);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/ils_gstep_pr_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}

	private static String testeILSRotuPRFull(String linha, int iters,
			int semMelhora, int freqPR, int maxElite) throws IOException {
		String saida = "Teste ILS Rotu + PR Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = ILS.executeFullRotuPR(semMelhora,freqPR,maxElite);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/ils_rotu_pr_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}

	private static String testeILSGStepFull(String linha, int iters,
			int semMelhora) throws IOException {
		String saida = "Teste ILS GStep Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = ILS.executeFullGStep(semMelhora);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/ils_gstep_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}

	private static String testeILSRotuFull(String linha, int iters,
			int semMelhora) throws IOException {
		String saida = "Teste ILS Rotu Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = ILS.executeFullRotu(semMelhora);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/ils_rotu_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}

	private static String testeGraspRotuFull(String linha, int iters, int semMelhora) throws IOException {
		String saida = "Teste GRASP Rotu Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = GRASP.executeFullRotu(semMelhora);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/grasp_rotu_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}

	private static String testeGraspGStepFull(String linha, int iters, int semMelhora) throws IOException {
		String saida = "Teste GRASP Gstep Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = GRASP.executeFullGStep(semMelhora);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/grasp_gstep_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}

	private static String testeGraspRotuPRFull(String linha, int iters, int semMelhora,
			int freqPR,int maxElite) throws IOException {
		String saida = "Teste GRASP Rotu + PR Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = GRASP.executeFullRotuPR(semMelhora,freqPR,maxElite);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/grasp_rotu_pr_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}

	private static String testeGraspGStepPRFull(String linha, int iters, int semMelhora,
			int freqPR, int maxElite) throws IOException {
		String saida = "Teste GRASP Gstep + PR Full:\n";
		saida += "Instancia\t\tTempo(s)\t\tValor\n";
		Filo.carregaInstancia(linha.split("\\s+")[0]);
		String nome = linha.split("\\s+")[0].split("/")[1].split("\\.")[0];
		Filo s,melhor = Filo.firstRotuGbr();
		long t0,dt;
		double mediaVal,mediaTempo;
		long tempos = 0; int valores = 0;
		for(int i = 0; i < iters; i++){
			t0 = getCpuTime();
			s = GRASP.executeFullGStepPR(semMelhora,freqPR,maxElite);
			dt = getCpuTime() - t0;
			valores += s.parcimonia;
			tempos += dt;
			if(s.parcimonia < melhor.parcimonia){
				melhor = s;
			}
		}
		mediaVal = valores/iters; mediaTempo = tempos/iters;
		saida += String.format("%s\t\t%.3f\t\t%.1f\n", linha.split("\\s+")[0],mediaTempo/BILHAO,
				mediaVal);
		BufferedWriter bw = new BufferedWriter(new FileWriter("resultados/melhores/grasp_gstep_pr_"+nome+".txt"));
		bw.write(melhor.parcimonia+": "+melhor.printaArvore());
		bw.close();
		System.out.println(saida);
		return saida;
	}
}


import java.io.IOException;

public class Principal {

	public static void main(String[] args) throws IOException,InterruptedException {
//		long t0, dt;
		String path = "instancias/ANGI.txt";
		Filo.carregaInstancia(path);
//		Filo result = Filo.gStepWR();
//		System.out.println(result.parcimonia);
//		result = Filo.firstRotuGbr();
//		System.out.println(result.parcimonia);
//		Teste.testeConstrucao("guia_testes_construcao.txt");
//		System.out.println(Teste.bateriaTestes("guia_testes_construcao.txt"));
//		Filo result = ILS.executeTargetRotu(217);
//		System.out.println(Teste.testeFull("guia_testes_construcao.txt"));
//		Teste.testeGraspTTTPlots();
		Teste.testeILSTTTPlots();
		Teste.testeGraspTTTPlots();
	}
}

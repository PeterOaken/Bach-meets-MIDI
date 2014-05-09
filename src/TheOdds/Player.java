package TheOdds;


public class Player {
	
	public static void main(String[] argv){
		FileNameResolver fnr = new FileNameResolver("data");
		
		if (argv[0].equals("-complex")){
			ComplexAnalyzer complexAnalyzer = new ComplexAnalyzer(fnr.getFiles());
			complexAnalyzer.analyze();
			
			ComplexComposer complexComposer = new ComplexComposer();
			complexComposer.play(0, complexAnalyzer);
		}else if(argv[0].equals("-simple")){
			SimpleAnalyzer analyzer = new SimpleAnalyzer(fnr.getFiles());
			analyzer.analyze();
			
			SimpleComposer simpleComposer = new SimpleComposer();
			simpleComposer.play(0, analyzer);
		}else if(argv[0].equals("-random")){		
			RandomAnalyzer randomAnalyzer = new RandomAnalyzer();
			randomAnalyzer.analyze();
	
			RandomComposer randomComposer = new RandomComposer();
			randomComposer.play(0, randomAnalyzer);
		}else{
			System.out.println("Please specify the input correctly");
		}
		
	}	

}

package TheOdds;


public class Player {
	
	public static void main(String[] argv){
		FileNameResolver fnr = new FileNameResolver("data");
		String path = "";
		boolean toWrite = false;
		if(argv.length==2){
			path = argv[1];
			toWrite = true;
		}
		
		if (argv[0].equals("-complex")){
			ComplexAnalyzer complexAnalyzer = new ComplexAnalyzer(fnr.getFiles());
			complexAnalyzer.analyze();
			
			ComplexComposer complexComposer = new ComplexComposer();
			complexComposer.play(0, complexAnalyzer);
			if(toWrite){
				complexComposer.write(path);
			}
		}else if(argv[0].equals("-simple")){
			SimpleAnalyzer analyzer = new SimpleAnalyzer(fnr.getFiles());
			analyzer.analyze();
			
			SimpleComposer simpleComposer = new SimpleComposer();
			simpleComposer.play(0, analyzer);
			if(toWrite){
				simpleComposer.write(path);
			}
		}else if(argv[0].equals("-random")){		
			RandomAnalyzer randomAnalyzer = new RandomAnalyzer();
			randomAnalyzer.analyze();
	
			RandomComposer randomComposer = new RandomComposer();
			randomComposer.play(0, randomAnalyzer);
			if(toWrite){
				randomComposer.write(path);
			}
		}else{
			System.out.println("Please specify the input correctly");
		}
		
	}	

}

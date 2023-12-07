public class ritztest {

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		PositionalIndex ps = new PositionalIndex("/Users/rambekar/CS_435/CS_435_PA3/data/IR");
		System.out.println(((System.currentTimeMillis() - timeStart)/1000) + " seconds"); // seconds to create inverted index
		System.out.println(ps.termFrequency("striking", "Zach_Duke.txt"));
		System.out.println(ps.docFrequency("striking"));
        System.out.println(ps.TPScore("striking out nine and receiving pirates ball hit", "Zach_Duke.txt"));
        System.out.println(ps.VSScore("striking out nine and receiving pirates ball hit", "ach_Duke.txt"));
        System.out.println(ps.Relevance("striking out nine and receiving pirates ball hit", "Zach_Duke.txt"));


		//System.out.println(ps.postingsList("s"));
	}
	
}


public class Term {
	
	/*
	 * each terms has
	 *  name
	 *  number of documents it is in
	 *  number of know relevent documents term is in
	 */
	
	private String name;
	private double termDocCount; //n(i)
	private int termFreq; // r(i)
	private double weight;
	
	private static final double N = 0.500000;
	
	Term(String name){
		this.name = name;
		termFreq = 1;
	}
	
	public void getNi(double idf){
		termDocCount = N / idf;
	}
	
	public boolean isTerm(String name){
		return this.name.equals(name);
	}
	
	public void addFreq(){
		termFreq++;
	}
	
	public void setWeight(int releventDocs){
		weight = Math.log10(((termFreq + 5.0) * ( N - termDocCount - releventDocs + termFreq +0.5))/((termDocCount - termFreq + 0.5) * (releventDocs - termFreq + 0.5)));
	}
	
	public double getWeight(){
		return weight;
	}
	
	public double getRank(){
		return termFreq * weight;
	}
	public String getName(){
		return name;
	}
}

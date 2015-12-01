import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

public class Process {
	
	public static void main(String [] args){
				
		getResultPage("cosmic%20event","10");//405
		System.out.println();
		getResultPage("Parkinson's%20disease","10");//406
		System.out.println();
		getResultPage("tropical%20storms","10");//408
		
	}
	
	static void getResultPage(String query, String numberOfDocs){
        
		try{
		    
			String line = "http://136.206.115.117:8080/IRModelGenerator/SearchServlet?query="+query+"&simf=BM25&k=1.2&b=0.75&numwanted="+numberOfDocs+"";
		    
			URL url = new URL(line);

			URLConnection connection = url.openConnection();
	       	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	       	StringBuilder contentBuilder = new StringBuilder();
			String str;
			
			while ((str = in.readLine()) != null) {
				str = str.replace("\n", "").replace("\r", ""); //remove new lines
	    	
		    	if(str.contains("<div class=\"ResultURLStyle\">")){
		    		str = str.substring(str.indexOf("<br>") + 4);
		    		while(!str.contains("</div><div class=\"ResultSnippetStyle\">") && str != null){
		    			str = str.replace("\n", "").replace("\r", "");
		    			
		    			contentBuilder.append(str);
		    			// System.out.print(str);
		    			str = in.readLine();    			
		    		}
		    		
		    		str = str.substring(0, str.indexOf("</div><div class=\"ResultSnippetStyle\">"));
		    		str = str.replace("\n", "").replace("\r", "");
		    		str = str.replace("  ", ",");
		    		str+=" ";
		    		contentBuilder.append(str);
		    	}
			}
		    in.close();
		    
		    String content = contentBuilder.toString();
		
		    String [] allTerm = content.split(" ");
		
		    HashMap<String,Term> terms = new HashMap<>();
		    for(int i = 0, j = 1; j < allTerm.length; i=j+1, j=i+1){
		    	String term = allTerm[i].substring(0, allTerm[i].indexOf(":"));
		    	double idf = Double.parseDouble(allTerm[j]);
		    	if(terms.containsKey(term)){
		    		Term  temp= terms.get(term);
		    		temp.addFreq();
		    		terms.put(term, temp);
		    	}else{/* Add a new */
		    		Term temp = new Term(term);
		    		temp.getNi(idf);
		    		terms.put(term,temp);
		    	}
		    }
		
		    String [] cycleTerms = terms.keySet().toArray(new String[terms.size()]);
		
		    //fine the top ten results.
		
		    //get all results for everything
		    for(String term:cycleTerms){
		    	Term temp = terms.get(term);
		    	temp.setWeight(10);
		    	temp.getRank();
		    	terms.put(term,temp);
		    }
		
		    //fine the top ten results.
		    double [] values = new double[10];
		    String [] topTerms = new String[10]; 
		
		    //just to be safe pop top ten with first 10 values.
		
		    for(int i = 0; i<10;i++){
		    	values[i]= terms.get(cycleTerms[i]).getRank();
		    	topTerms[i]=terms.get(cycleTerms[i]).getName();
		    }
		
		    for(String term:cycleTerms){
		    	Term temp = terms.get(term);
		    	double value = temp.getRank();
			
		    	for(int i =0; i<values.length;i++){
				
		    		/* We check to see if the value is greater then the value at the top of the list 
		    		 * and move through each making sure nothing is smaller then this value rank.*/
		    		if(value > values[i]){//move everything that is in this slot down one.
					
		    			double tempValue = values[i];
		    			String tempTerm = topTerms[i];
					
		    			for(int j=i+1; j<values.length;j++){
						
		    				double temp2 = values[j];
		    				String temp3 = topTerms[j];
						
		    				values[j] = tempValue;
		    				topTerms[j] = tempTerm;
						
							tempValue = temp2;
							tempTerm = temp3;
						
						}
					
						values[i]=value;
						topTerms[i]=temp.getName();
						break;
					}
				}
			}
		
			for(int i=0; i<values.length;i++){
				System.out.println(topTerms[i] + ": " + values[i] );
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	}
}

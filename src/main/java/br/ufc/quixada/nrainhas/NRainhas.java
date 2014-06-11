package br.ufc.quixada.nrainhas;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;


public class NRainhas {

	public static int n;
	public static int[] par = new int[2];
	public static PrintWriter writer;
	public static ArrayList<String> formulas;
     

    public static void main(String[] args){
        try{
        	if(args.length==1){
        		n=Integer.parseInt(args[0]);
        	}
        	else{
		    	Scanner scan=new Scanner(System.in);
		        System.out.println("Entre com o valor de N: ");
		        n=scan.nextInt();

				scan.close();
        	}
	        writer=new PrintWriter(n+"rainhas.cnf");
	        formulas=new ArrayList<String>();

        	criarFormulas();
        	escreverArquivo();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private static void criarFormulas() throws Exception{		
    	linhas();
    	colunas();
    	diagonalED();
    	diagonalDE();
    }
    
    private static void diagonalDE() {
    	int x=0;
    	for(int i=n; i>0; i--){
	    	int[] diagonal=new int[x+1];
	    	int y=0;
	    	//4,7,10,13.....3,6,9
			for(int j=i; j<=n*(x+1); j+=n+1){
				diagonal[y]=j;	
				y++;
			}
			x++;	
			addTodasClausulasDaLinha(diagonal, true);
    	}
    	int y=n-1;
    	
    	for(int i=n+1; i<n*n; i+=n){
	    	int[] diagonal=new int[y];
	    	x=0;
			for(int j=i; j<n*n; j+=n+1){
				diagonal[x]=j;	
				x++;
			}
			y--;
			addTodasClausulasDaLinha(diagonal, true);
    	}
	}
    
	public static void linhas(){
    	for(int i=0; i<n; i++){
	    	int[] todaLin=new int[n];
			for(int j=0;j<n;j++){
				todaLin[j]=(j+1)+(i*n);
			}
			addTodasClausulasDaLinha(todaLin, false);
    	}
    }
    public static void colunas(){

    	for(int i=1; i<=n; i++){
	    	int[] todaCol=new int[n];
			for(int j=0;j<n;j++){
				todaCol[j]=(j*n)+i;
			}
			addTodasClausulasDaLinha(todaCol, false);
    	}
    }
    
    public static void diagonalED(){
    	for(int i=n; i>0; i--){
	    	int[] diagonal=new int[i];
	    	int x=0;
			for(int j=i; x<i; j+=n-1){
				diagonal[x]=j;	
				x++;
			}
			addTodasClausulasDaLinha(diagonal, true);
    	}
    	
    	
    	int y=n-1;
    	//Get the diagonals from the right side
    	for(int i=n*2; i<n*n; i+=n){
    		
	    	int[] diagonal=new int[y];
	    	int x=0;
	    	//4,7,10,13.....3,6,9
			for(int j=i; j<n*n; j+=n-1){
				diagonal[x]=j;	
				x++;
			}
			y--;
			addTodasClausulasDaLinha(diagonal, true);
    	}
    }
    
    public static void addTodasClausulasDaLinha(int[] x, boolean diagonal) {
    	if(x.length==1){
    		//Ignora
    	}
    	else{
    		if(!diagonal){
    			
    			String s=new String();
    			for(int i=0; i<x.length; i++){
    				s+=x[i]+" ";
    			} 
    			s+="0";
    			formulas.add(s);
    		}

	    	for(int k=0; k<x.length; k++){
				for(int l=k+1; l<x.length;l++){
					formulas.add("-"+x[k]+" -"+x[l]+" 0");
					par[0]=x[k] * -1;
					par[1]=x[l] * -1;
				}
			}
    	}
    }
    
    public static void escreverArquivo() throws FileNotFoundException, UnsupportedEncodingException{
		writer.println("p cnf "+n+" "+formulas.size());

		for(int i=0;i<formulas.size();i++){
			writer.println(formulas.get(i));
		}

		writer.close();
    }
    
}
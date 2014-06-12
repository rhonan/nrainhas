package br.ufc.quixada.nrainhas;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class NRainhas {
	public static int[][] tabuleiro;
	public static int n;
	public static int variaveis;
	public static int clausulas;
	public static StringBuilder builder;
	public static String nomeArquivoCNF;
	
	
    public static void main(String[] args){
    	
    	if(args.length == 0){
        	Scanner scan=new Scanner(System.in);
            System.out.println("Entre com o valor de N: ");
            n=scan.nextInt();
            scan.close();
    	}
    	else{
    		n=Integer.parseInt(args[0]);
    	}
		tabuleiro = new int[n][n];
		preencherTabuleiro(n);
		builder = new StringBuilder();
		nomeArquivoCNF = n+"rainhas.cnf";
		gerarCNF();

    }

	public static void preencherTabuleiro(int n) {
		int label = 1;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				tabuleiro[i][j] = label;
				label++;
			}
		}
		variaveis = --label;
	}
	
	public static StringBuilder gerarCNF() {
		gerarClausulasLinhas();
		gerarClausulasColunas();
		gerarClausulasDiagonal();
		gerarArquivoCNF();
		return builder;
	}

	public static  void gerarClausulasLinhas() {
		String clausula = "";
		int[] linha = new int[n];
		int k = 0;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				linha[k] = tabuleiro[i][j];
				clausula += tabuleiro[i][j] +" ";
				k++;
			}
			addClausula(clausula);
			clausula = "";
			for(int l=0; l<n; l++) {
				for(int m=l+1; m<n; m++) {
					clausula += "-"+linha[l]+" -"+linha[m]+" ";
					addClausula(clausula);
					clausula = "";
				}
			}

			k = 0;
		}
	}
	public static void gerarClausulasColunas() {
		String clausula = "";
		int[] coluna = new int[n];
		int k = 0;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				coluna[k] = tabuleiro[j][i];
				clausula += tabuleiro[j][i] +" ";
				k++;
			}
			addClausula(clausula);
			clausula = "";
			for(int l=0; l<n; l++) {
				for(int m=l+1; m<n; m++) {
					clausula += "-"+coluna[l]+" -"+coluna[m]+" ";
					addClausula(clausula);
					clausula = "";
				}
			}
			k = 0;
		}
	}
	public static void gerarClausulasDiagonal() {
		String clausula = "";
		int[] diagonal = new int[n];
		int k = 0;
		int l = 0;
		for(int i=1; i<n*2-2; i++) {
			for(int j = 0; j<=i; j++) {
				k = i - j;
				if(k<n && j< n) {
					diagonal[l] = tabuleiro[k][j];
					l++;
				}
			}
			for(int p=0; p<l; p++) {
				for(int q=p+1; q<l; q++) {
					clausula += "-"+diagonal[p]+" -"+diagonal[q]+" ";
					addClausula(clausula);
					clausula = "";
				}
			}
			l = 0;
		}

		for(int i=n-2; i>=0; i--) {
			for(int j = i+n-1; j>=0; j--) {
				k = i + j;
				if(k<n && j< n) {
					diagonal[l] = tabuleiro[k][j];
					l++;
				}
		 }

		for(int p=0; p<l; p++) {
			for(int q=p+1; q<l; q++) {
				clausula += "-"+diagonal[p]+" -"+diagonal[q]+" ";
				addClausula(clausula);
				clausula = "";
			}
		}
		l = 0;
		}
		int iter = 2;
		int step = 1;
		int i = 0;
		int j = i+step;
		while(iter>=2) {
			iter = 0;
			while(j<n) {
					diagonal[l] = tabuleiro[i][j];
					j++;
					i++;
					iter++;
					l++;
			}
			for(int p=0; p<l; p++) {
				for(int q=p+1; q<l; q++) {
					clausula += "-"+diagonal[p]+" -"+diagonal[q]+" ";
					addClausula(clausula);
					clausula = "";
				}
			}
			l = 0;
			i = 0;
			step++;
			j = i+step;
			if(j==n-1) break;
		}
	}
	
	public static void addClausula(String clausula) {
		builder.append(clausula);	
		builder.append("0\n");
		clausulas++;
	}
	
	/**
	 * Adiciona a CNF ao arquivo.
	 */
	public static void gerarArquivoCNF()  {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(nomeArquivoCNF, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writer.println("p cnf "+variaveis+" "+clausulas);
		String[] cnfContent = builder.toString().split("\\n");
		for(String clausula:cnfContent) {
			writer.println(clausula);
		}
		writer.close();
	}
	
}
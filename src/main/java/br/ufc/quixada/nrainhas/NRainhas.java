package br.ufc.quixada.nrainhas;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Converts NQueens problem into CNF for a particular value of n.
 * @author mon
 *
 */
public class NRainhas {
	public static int[][] board;
	public static int n;
	public static int variables;
	public static int clauses;
	public static StringBuilder builder;
	public static String cnfFileName;
	
	
    public static void main(String[] args){
    	
    	Scanner scan=new Scanner(System.in);
        System.out.println("Entre com o valor de N: ");
        n=scan.nextInt();
        scan.close();
		// init the board
		board = new int[n][n];
		// label logical variables on the board
		labelBoard(n);
		// init clause builder for cnf
		builder = new StringBuilder();
		cnfFileName = n+"rainhas.cnf";
		generateCNF();

    }

    

	/**
	 * Label the board 1..n^2 where n is the desired dimension 
	 * (size of the problem).
	 * @param n : size of the problem. 
	 */
	public static void labelBoard(int n) {
		int label = 1;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				board[i][j] = label;
				label++;
			}
		}
		variables = --label;
	}
	/**
	 * Generates a cnf String corresponding to the problem (with a particular 
	 * value of n).
	 * @return: StringBuilder containing cnf.
	 */
	public static StringBuilder generateCNF() {
		generateRowClauses();
		generateColumnClauses();
		generateDiagonalClauses();
		cnfToFile();
		return builder;
	}

	public static  void generateRowClauses() {
		String clause = "";
		int[] row = new int[n];
		int k = 0;
		for(int i=0; i<n; i++) {
			// construct statement of form (x1 or x2 or... or xn)
			for(int j=0; j<n; j++) {
				row[k] = board[i][j];
				clause += board[i][j] +" ";
				k++;
			}
			appendClause(clause);
			clause = "";
			// construct statements of form (!x1 or !x2); (!x1 or !x3); (!x1 or !x2) etc.
			for(int l=0; l<n; l++) {
				for(int m=l+1; m<n; m++) {
					clause += "-"+row[l]+" -"+row[m]+" ";
					appendClause(clause);
					clause = "";
				}
			}

			k = 0;
		}
	}
	public static void generateColumnClauses() {
		String clause = "";
		int[] column = new int[n];
		int k = 0;
		for(int i=0; i<n; i++) {
			// construct statement of form (x1 or x2 or... or xn)
			for(int j=0; j<n; j++) {
				column[k] = board[j][i];
				clause += board[j][i] +" ";
				k++;
			}
			appendClause(clause);
			clause = "";
			// construct statements of form (!x1 or !x2); (!x1 or !x3); (!x1 or !x2) etc.
			for(int l=0; l<n; l++) {
				for(int m=l+1; m<n; m++) {
					clause += "-"+column[l]+" -"+column[m]+" ";
					appendClause(clause);
					clause = "";
				}
			}
			k = 0;
		}
	}
	public static void generateDiagonalClauses() {
		String clause = "";
		// bottom-up left right part
		int[] diagonal = new int[n];
		// index for diagonal
		int k = 0;
		int l = 0;
		for(int i=1; i<n*2-2; i++) {
			for(int j = 0; j<=i; j++) {
				k = i - j;
				if(k<n && j< n) {
					diagonal[l] = board[k][j];
					l++;
				}
			}
			for(int p=0; p<l; p++) {
				for(int q=p+1; q<l; q++) {
					clause += "-"+diagonal[p]+" -"+diagonal[q]+" ";
					appendClause(clause);
					clause = "";
				}
			}
			l = 0;
		}

		for(int i=n-2; i>=0; i--) {
			for(int j = i+n-1; j>=0; j--) {
				k = i + j;
				if(k<n && j< n) {
					diagonal[l] = board[k][j];
					l++;
				}
		 }

		for(int p=0; p<l; p++) {
			for(int q=p+1; q<l; q++) {
				clause += "-"+diagonal[p]+" -"+diagonal[q]+" ";
				appendClause(clause);
				clause = "";
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
					diagonal[l] = board[i][j];
					j++;
					i++;
					iter++;
					l++;
			}
			for(int p=0; p<l; p++) {
				for(int q=p+1; q<l; q++) {
					clause += "-"+diagonal[p]+" -"+diagonal[q]+" ";
					appendClause(clause);
					clause = "";
				}
			}
			l = 0;
			i = 0;
			step++;
			j = i+step;
			if(j==n-1) break;
		}
	}
	public static void appendClause(String clause) {
		builder.append(clause);	
		// AND
		builder.append("0\n");
		clauses++;
	}
	/**
	 * Writes cnf to file.
	 */
	public static void cnfToFile()  {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(cnfFileName, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// adhere to cnf convension
		writer.println("c logic in conjunctive normal form");
		writer.println("c for n-queens problem, with n = "+n);
		writer.println("p cnf "+variables+" "+clauses);
		String[] cnfContent = builder.toString().split("\\n");
		for(String clause:cnfContent) {
			writer.println(clause);
		}
		writer.close();
	}
	public static String getCnfFilename() {
		return cnfFileName;
	}
}
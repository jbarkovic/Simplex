package simplex;

import java.util.Arrays;

public class Start {

	public static void main (String [] args) {
		double [][] problem0 = new double [][] {
				{1   ,0   ,0   ,0   ,0   ,0   ,0   ,-1  ,-1  ,0  },
				{0   ,0   ,3/2 ,1   ,1   ,1/2 ,0   ,1   ,-1/2,9  },
				{0   ,1   ,-1/2,0   ,0   ,-1/2,0   ,0   ,1/2 ,1  },
				{0   ,0   ,-3/2,1   ,0   ,1/2 ,1   ,0   ,-1/2,5  },				
		};
		double [][] problem1 = new double [][] {
				{1  ,-1  ,-2   ,1   ,0   ,0   ,0   ,0   },
				{0   ,2   ,1   ,1   ,1   ,0   ,0   ,14  },
				{0   ,4   ,2   ,3   ,0   ,1   ,0   ,28  },
				{0   ,2   ,5   ,5   ,0   ,0   ,1   ,30  },
		};
		double [][] A2_prob = new double [][] {
				{-1  ,3   ,1   ,-1  ,0   ,0   ,0   ,0   ,0   },
				{0   ,1   ,1   ,1   ,1   ,0   ,0   ,1   ,10  },
				{0   ,2   ,-1  ,0   ,0   ,-1  ,0   ,0   ,2  },
				{0   ,1   ,-2  ,1   ,0   ,0   ,1   ,0   ,6  },
		};
		double [][] prob = A2_prob;
		printProblemFraction(prob,"Original Problem:");
		Simplex simp = new Simplex ();
		Simplex.State problemState = simp.solve(prob,new int [] {1,1,0});
		//prob = simp.solve(prob);
		printProblemFraction(prob,"Final Tableau:");
		System.out.println("\n\nSystem is: " + problemState.toString());
		System.out.println("\n\nSolution:\n");
		for (double value : simp.getSolution(prob)) {
			System.out.print("[" + Simplex.toFraction(value, 100) + "], ");
		}
	}
	public static void printProblemFraction (double [][] in,String msg) {
		System.out.println(msg);
		
		//System.out.println(message);
		String [][] values = new String [in.length][0];
		int maxLength = 0;
		for (int i=0;i<in.length;i++) {
			values[i] = new String [in[i].length];
			for (int j=0;j<in[i].length;j++) {
				values [i][j] = Simplex.toFraction(in [i][j], 100);
				maxLength = Math.max(maxLength,values[i][j].length());
			}
		}
		maxLength = Math.max(8, maxLength);
		for (int i=0;i<in.length;i++) {
			for (int j=0;j<in[i].length;j++) {
				String element = Simplex.toFraction(in [i][j], 100);				
				int lMarg = Math.max(maxLength-element.length (),0) / 2;
				int rMarg = Math.max(maxLength-(lMarg + element.length()),0); 
				System.out.print("[");
				for (int k=0;k<lMarg;k++) {
					System.out.print(" ");
				}
				System.out.print(element);
				for (int k=0;k<rMarg;k++) {
					System.out.print(" ");
				}
				System.out.print("]");
			}
			System.out.println();
		}
	}
	public static void printProblemFloat (double [][] in,String msg) {
		System.out.println(msg);
		
		//System.out.println(message);
		for (int i=0;i<in.length;i++) {
			for (int j=0;j<in[i].length;j++) {			
				System.out.format("[%+02.4f]" + ((j == in[0].length-1) ? "\n" : ","),in[i][j]);
			}
			//System.out.println();
		}
	}
}

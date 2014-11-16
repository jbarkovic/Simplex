package simplex;

import java.util.ArrayList;

public class Simplex {
	public enum State {
		VALID, NOTVALID, UNBOUNDED
	}	
	public State solve (double [][] in) {
		int count = 0;
		final int fullWidth = in [0].length;		
		while (true) {
			String message = "Iteration: " + count + ". ";
			count ++;
			boolean optimal = true;
			for (int col=1;col<fullWidth-1;col++) {
				if ((in [0][col] < 0 && in [0][0] > 0) || (in [0][col] > 0 && in [0][0] < 0)) {
					optimal = false;
					int rowPivot = getRowOfMinRatio (col, in);
					double rowScaleFactor = 1 / in [rowPivot][col];

					scaleRow (rowScaleFactor, rowPivot, in);
					message += "variable " + col + " entered."; 
					pivot (in, rowPivot,col);
					break;
				}			
			}
			State currentState = checkState (in);
			switch (currentState) {
			case NOTVALID : return currentState;
			case UNBOUNDED: return currentState;
			case VALID 	  : break;
			default 	  : return currentState;
			}
			if (optimal) break;
			if (count > 10) break;
			Start.printProblemFraction(in, message);
		}
		return checkState (in);
	}
	public State solve (double [][] in, int [] Y) {
		final int originalWidth  = in [0].length;
		final int originalHeight = in.length;
		
		double [][] phase1 = new double [originalHeight][originalWidth+Y.length];
		for (int row=1;row<originalHeight;row++) {
			for (int col=0;col<originalWidth-1;col++) {
				phase1 [row] [col] = in [row] [col];
			}
			phase1 [row] [phase1 [row].length-1] = in [row] [originalWidth-1];
		}
		phase1 [0][0] = -1;
		for (int i=originalWidth-1,j=0;j<Y.length;i++,j++) {
			phase1 [0]   [i] = -Y[j];
			phase1 [j+1] [i] =  Y[j];
		}		
		Start.printProblemFraction(phase1, "Phase 1:");
		ArrayList <Integer> basicColumns = new ArrayList <Integer> ();
		for (int i=0;i<Y.length;i++) {
			if (Y [i] == 1) {
				basicColumns.add (originalWidth + i - 1);
			}
		}
		restoreBasicVariables (phase1, basicColumns.toArray(new Integer [0]));
		Start.printProblemFraction(phase1, "Phase 1: Basic Variables Restored:");		
		solve (phase1);
		for (int row = 1; row < in.length; row++) {
			for (int col = 1; col < in [0] .length-1; col++) {
				in [row][col] = phase1 [row][col];
			}
			in [row][originalWidth-1] = phase1[row][phase1[0].length-1];
		}
		Start.printProblemFraction(in, "Phase 2: ");
		return solve (in);
	}
	private int restoreBasicVariables (double [][] in, Integer [] columns) {
		for (int col : columns) {
			System.out.print("COL: " + col);
			for (int row=1;row<in.length;row++) {
				if (in [row][col] == 1) {
					while (in [0][col] != 0) {						
						int factor = (in [0][col] > 0) ? -1 : 1;
						for (int subCol = 1; subCol < in[0].length;subCol++) {
							in [0] [subCol] += factor*in [row] [subCol];
						}
					}
				}
			}
		}
		System.out.println();
		return 0;
	}	
	private State checkState (double in [][]) {
		final int fullWidth = in [0].length;
		for (int col=1;col<fullWidth-1;col++) {
			int numPositive = 0;
			int numNegative = 0;
			for (int row=1;row<in.length;row++) {
				if (in [row][col] >= 0) numPositive++;
				else numNegative ++;
			}
			if (col >= 1 && col < fullWidth-1) {
				if (numPositive == 0) return State.UNBOUNDED;
			} else if (col == fullWidth-1) {
				if (numNegative > 0) return State.NOTVALID;
			}
		}
		return State.VALID;
	}
	private void pivot (double [][] in, int pivotRow, int pivotCol) {
		final int fullWidth = in [0].length;
		for (int row = 0; row<in.length;row++) {
			if (row == pivotRow) continue;		
			double localScaleFactor = (-1.0) * in [row][pivotCol] / in [pivotRow][pivotCol];
			for (int col=0;col<fullWidth;col++) {
				in [row][col] += in [pivotRow][col] * localScaleFactor;
			}
		}
	}
	private void scaleRow (double factor, int row, double [][] in) {
		final int fullWidth = in [0].length;
		for (int subCol = 0; subCol < fullWidth; subCol++) {
			in [row][subCol] *= factor;
		}
	}
	private int getRowOfMinRatio (int col, double [][] in) {
		final int fullWidth = in [0].length;
		int minRatioRow = 1;
		double minRatio = in [1][fullWidth-1] / in [1][col]; 
		for (int row=2;row<in.length;row++) {
			double localRatio = in [row][fullWidth-1] / in [row][col];
			if (localRatio < minRatio && localRatio > 0) {
				minRatio = localRatio;
				minRatioRow = row;
			}
		}
		return minRatioRow;
		
	}
	public double [] getSolution (double [][] in) {
		final int fullWidth = in [0].length;	
		double [] out = new double [in [0].length-2];
		for (int col=1;col<in[0].length-1;col++) {
			int count = 0;
			double value = 0;
			for (int row = 1;row<in.length;row++) {
				if (in [row][col] != 0) {
					count ++;				
					value = (count == 1) ? in[row][fullWidth-1] : 0;
				}
			}
			out [ col -1] = value;
		}
		return out;
	}
	public static String toFraction(double d, int factor) {
		/** Borrowed From:
		 * http://stackoverflow.com/questions/5968636/converting-a-float-into-a-string-fraction-representation
		 * 
		 */
	    StringBuilder sb = new StringBuilder();
	    if (d == 0) return "0";
	    if (d < 0) {
	        sb.append('-');
	        d = -d;
	    }
	    long l = (long) d;
	    if (l != 0) sb.append(l);
	    d -= l;
	    double error = Math.abs(d);
	    int bestDenominator = 1;
	    for(int i=2;i<=factor;i++) {
	        double error2 = Math.abs(d - (double) Math.round(d * i) / i);
	        if (error2 < error) {
	            error = error2;
	            bestDenominator = i;
	        }
	    }
	    if (bestDenominator > 1)
	        sb.append(' ').append(Math.round(d * bestDenominator)).append('/') .append(bestDenominator);
	    return sb.toString();
	}
}

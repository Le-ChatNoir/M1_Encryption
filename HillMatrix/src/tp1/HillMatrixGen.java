package tp1;

public class HillMatrixGen {
	
	//Generates the key matrix from the key word and the size of the matrix wanted
	static void getKeyMatrix(String key, int keySize, int keyMatrix[][]) { 
		
	    int k = 0; 
	    for (int i = 0; i < keySize; i++)  
	    { 
	        for (int j = 0; j < keySize; j++)  
	        { 
	        	if(k < key.length()) {
	        		keyMatrix[i][j] = (key.charAt(k)) % 65; 
	        	} else {
	        		keyMatrix[i][j] = 0;
	        	}
	            k++; 
	        }
	    }
	}
	
	static String[] getMessageCut(String text, int keySize) {
		
		//Cutting text 2 by 2 using Regex
		text = text.replaceAll(" ", "");
				
				
		//Adding "." m times for the regex
		String separator = "";
		for(int i = 0; i < keySize; i++) {
			separator += ".";
		}
						
		//Splitting text using regex
		String[] cutText = text.split("(?<=\\G" + separator + ")");
				
		//Adding X at the end of unfinished strings
		if(cutText[cutText.length-1].length() != keySize) {
			while(cutText[cutText.length-1].length() != keySize) {
				cutText[cutText.length-1] = cutText[cutText.length-1] + 'X';
			}
		}
		
		return cutText;
	}
	
	static void printMatrix(int[][] m) {
		for (int i = 0; i < m.length; i++) {
		    for (int j = 0; j < m[i].length; j++) {
		        System.out.print(m[i][j] + " ");
		    }
		    System.out.println();
		}
	}
	
	static void printMatrix(float[][] m) {
		for (int i = 0; i < m.length; i++) {
		    for (int j = 0; j < m[i].length; j++) {
		        System.out.print(m[i][j] + " ");
		    }
		    System.out.println();
		}
	}
	
	static void getCofactor(int mat[][],  
            int temp[][], int p, int q, int n) 
   { 
       int i = 0, j = 0; 
     
       // Looping for each element of  
       // the matrix 
       for (int row = 0; row < n; row++) 
       { 
           for (int col = 0; col < n; col++) 
           { 
                 
               // Copying into temporary matrix  
               // only those element which are  
               // not in given row and column 
               if (row != p && col != q) 
               { 
                   temp[i][j++] = mat[row][col]; 
     
                   // Row is filled, so increase  
                   // row index and reset col  
                   //index 
                   if (j == n - 1) 
                   { 
                       j = 0; 
                       i++; 
                   } 
               } 
           } 
       } 
   } 
	
	static int getDeterminant(int keySize, int[][] keyMatrix) {
		int res = 0;
		
		//In case matrix is of size 1
		if(keySize == 1)
			return keyMatrix[0][0];
		
		 // To store cofactors 
        int temp[][] = new int[keySize][keySize];  
          
        // To store sign multiplier 
        int sign = 1;  
      
        // Iterate for each element of first row 
        for (int f = 0; f < keySize; f++) 
        { 
            // Getting Cofactor of mat[0][f] 
            getCofactor(keyMatrix, temp, 0, f, keySize); 
            res += sign * keyMatrix[0][f]  
               * getDeterminant(keySize - 1, temp); 
      
            // terms are to be added with  
            // alternate sign 
            sign = -sign; 
        } 
      
        return res; 
    } 
	
	static void getAdjoint(int A[][],int [][]adj, int keySize) 
	{ 
	    if (keySize == 1) 
	    { 
	        adj[0][0] = 1; 
	        return; 
	    } 
	  
	    // temp is used to store cofactors of A[][] 
	    int sign = 1; 
	    int [][]temp = new int[keySize][keySize]; 
	  
	    for (int i = 0; i < keySize; i++) 
	    { 
	        for (int j = 0; j < keySize; j++) 
	        { 
	            // Get cofactor of A[i][j] 
	            getCofactor(A, temp, i, j, keySize); 
	  
	            // sign of adj[j][i] positive if sum of row 
	            // and column indexes is even. 
	            sign = ((i + j) % 2 == 0)? 1: -1; 
	  
	            // Interchanging rows and columns to get the 
	            // transpose of the cofactor matrix 
	            adj[j][i] = (sign)*(getDeterminant(keySize-1, temp)); 
	        } 
	    } 
	} 
	  
	// Function to calculate and store inverse, returns false if 
	// matrix is singular 
	static boolean getInverse(int A[][], float [][]inverse, int keySize) 
	{ 
	    // Find determinant of A[][] 
	    int det = getDeterminant(keySize, A); 
	    if (det == 0) 
	    { 
	        System.out.print("Singular matrix, can't find its inverse"); 
	        return false; 
	    } 
	  
	    // Find adjoint 
	    int [][]adj = new int[keySize][keySize]; 
	    getAdjoint(A, adj, keySize); 
	  
	    // Find Inverse using formula "inverse(A) = adj(A)/det(A)" 
	    for (int i = 0; i < keySize; i++) 
	        for (int j = 0; j < keySize; j++) 
	            inverse[i][j] = adj[i][j]/(float)det; 
	  
	    return true; 
	} 
}

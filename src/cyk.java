import java.util.ArrayList;
import java.util.Scanner;

public class cyk {
    /**
     * Finds the index of the symbol The purpose of this is to use each symbol
     * as a unique id with a specific index and truth value on the P table.
     * 
     * @param Grammar
     * @param symbol
     * @return
     */
    public static int findIndex (Grammar Grammar, String symbol) {
        for (int i = 0; i < Grammar.size (); i++) {
            String[] rule = Grammar.get (i);
            if (rule.length == 2) {
                if (symbol.equals (Grammar.get (i)[0]))
                    return i;
            }
        }
        for (int i = 0; i < Grammar.size (); i++) {
            if (symbol.equals (Grammar.get (i)[0]))
                return i;
        }
        return -1;

    }

    /**
     * Finds the starting symbol which is S or the first symbol of the grammar
     * 
     * @param Grammar
     * @return
     */
    public static Integer[] findStartingSymbols (Grammar Grammar) {
        /*
         * for (int i = 0; i < Grammar.size (); i++) { String[] rule =
         * Grammar.get (i); if (rule[0].equals ("S_0")) return i; }
         */
        ArrayList<Integer> startSymbols = new ArrayList<Integer> ();
        for (int i = 0; i < Grammar.size (); i++) {
            String[] rule = Grammar.get (i);
            if (rule[0].equals ("S")) {
                startSymbols.add (i);
                if (rule.length == 2) {
                    startSymbols.add (findIndex (Grammar, rule[1]));
                }
            }
        }

        Integer[] output = new Integer[startSymbols.size ()];
        output = startSymbols.toArray (output);
        return output;
    }

    /**
     * Returns whether or not the string S belongs to Grammar
     * 
     * @param S
     * @param Grammar
     * @return
     */
    public static boolean cyk (String S, Grammar Grammar) {
        int n = S.length ();
        int r = Grammar.size ();
        Integer[] startSymbols = findStartingSymbols (Grammar);
        boolean P[][][] = new boolean[n][n][r];
        // let P[n,n,r] be an array of booleans. Initialize all elements of P to
        // false.
        initializeP (P, n, r);
        // for each i = 1 to n, we do -1 because it is java and arrays start
        // from 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < r; j++) {
                // for each unit production Rj -> ai
                String[] rule = (String[]) Grammar.get (j);
                if (rule.length == 2) {
                    if (rule[1].equals (String.valueOf (S.charAt (i)))) {

                        int A = findIndex (Grammar, rule[0]);
                        // set P[i,1,j] = true
                        P[i][0][A] = true;

                    }
                }
            }
        }
        // for each i = 2 to n -- Length of span
        for (int i = 1; i < n; i++) {
            // for each j = 1 to n-i+1 -- Start of span
            for (int j = 0; j < n - i; j++) {
                // for each k = 1 to i-1 -- Partition of span
                for (int k = 0; k < i; k++) {
                    // for each production RA -> RB RC
                    for (int m = 0; m < r; m++) {
                        String[] rule = Grammar.get (m);
                        if (rule.length > 2) {

                            int A = findIndex (Grammar, rule[0]);
                            int B = findIndex (Grammar, rule[1]);
                            int C = findIndex (Grammar, rule[2]);
                            // System.out.println(rule[2]+" "+C);
                            // if P[j,k,B] and P[j+k,i-k,C] then set P[j,i,A] =
                            // true
                            if (P[j][k][B] && P[j + k + 1][i - k - 1][C])
                                P[j][i][A] = true;

                        }
                    }
                }
            }
        }

        System.out.println ("###Parse Tree###");
        for (int i = 0; i < S.length (); i++) {
            System.out.print (S.charAt (i) + " ");
        }

        System.out.println ();
        printP (P, n, r, Grammar);
        // if any of P[1,n,x] is true (x is iterated over the set s, where s are
        // all the indices for Rs) then
        for (int i = 0; i < startSymbols.length; i++) {
            int x = startSymbols[i];
            if (x >= 0)
                if (P[0][n - 1][x])
                    return true; // S is member of language
        }

        // else
        return false; // S is not member of language
    }

    /**
     * Prints the parse table.
     * 
     * @param P
     * @param n
     * @param r
     * @param Grammar
     */
    public static void printP (boolean P[][][], int n, int r, Grammar Grammar) {
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n - i; k++) {
                System.out.print ("{");
                for (int m = 0; m < r; m++) {
                    if (P[k][i][m]) {
                        System.out.print (Grammar.get (m)[0] + " ");
                    }

                }

                System.out.print ("} ");
            }
            System.out.println ();
        }
    }

    /**
     * Initializes the P array
     * 
     * @param P
     * @param n
     * @param r
     */
    public static void initializeP (boolean P[][][], int n, int r) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < r; k++) {
                    P[i][j][k] = false;
                }
            }
        }
    }

    /**
     * Main
     * 
     * @param args
     */
    public static void main (String args[]) {
        String input;
        Scanner in = new Scanner (System.in);
        Grammar Grammar = new Grammar ("grammar.txt");
        if (!Grammar.isRead ()) {
            System.out.println ("Grammar could not be read.");
            return;
        }
        while (in.hasNext ()) {
            input = in.next ();
            if (cyk (input, Grammar))
                System.out.println ("String is part of the language");
            else
                System.out.println ("String is NOT part of the language");

        }
    }
}

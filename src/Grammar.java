import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Grammar {

    ArrayList<String[]> rules;

    /**
     * Constructor, the Grammar File is saved in an ArrayList of type String[].
     * So that rules can be accessed as a list and each part of the rule can be
     * accessed inidividually in the String array.
     * 
     * @param grammarFile
     */
    public Grammar(String grammarFile) {
        this.rules = convertrules (readrules (grammarFile));
    }

    /**
     * Size of the array list
     * 
     * @return
     */
    public int size () {
        return this.rules.size ();
    }

    /**
     * returns the ith element of the array list
     * 
     * @param i
     * @return
     */
    public String[] get (int i) {
        return this.rules.get (i);
    }

    /**
     * Converts the rules according to the steps
     * http://www.cs.nyu.edu/courses/fall07/V22.0453-001/cnf.pdf returns the
     * rules in CNF format
     * 
     * @param rules
     * @return
     */
    public static ArrayList<String[]> convertrules (ArrayList<String[]> rules) {
        stepOne (rules);
        stepTwo (rules);
        stepThree (rules);
        stepFour (rules);
        // UNCOMMENT VERSION OF STEP U WANT TO USE if CYK is WRONG!
        // if CYK is wrong comment step five and try again.
         stepFive (rules);
        // stepFiveV2 (rules);
        return rules;

    }

    /**
     * ############## STEP FIVE ################## 1st version done by Iordanis
     * Fostiropoulos - NOT 100% correct needs 2 hours more of debugging.
     * 
     * @param rules
     */
    public static void stepFive (ArrayList<String[]> rules) {
        ArrayList<String[]> unitProductions = findUnitProductions (rules);

        // find strong graph components.

        for (int i = 0; i < unitProductions.size (); i++) {
            String[] production = unitProductions.get (i);
            for (int j = 0; j < unitProductions.size (); j++) {
                String[] tempProduction = unitProductions.get (j);
                if (production[0] == tempProduction[1]
                        && production[1] == tempProduction[0]) {
                    // it is strong graph component since they nodes point to
                    // each other
                    // replace all rules that have the second symbol with the
                    // first symbol
                    // S'->X => S'->S'
                    for (int k = 0; k < rules.size (); k++) {
                        String[] rule = rules.get (k);
                        for (int l = 0; l < rule.length; l++) {
                            if (rule[l].equals (production[1])) {
                                rule[l] = production[0];
                            }
                        }
                        rules.set (k, rule);
                    }
                }
            }
        }

        // remove rules of the form X->X
        for (int i = 0; i < rules.size (); i++) {
            String[] rule = rules.get (i);
            if (rule[0].equals (rule[1]) && rule.length == 2) {
                rules.remove (i);
                i--;
            }
        }

        unitProductions = findUnitProductions (rules);
        // we now have a reduced graph
        // we traverse through the graph
        for (int i = 0; i < unitProductions.size (); i++) {
            String[] production = unitProductions.get (i);
            step5Recursion (rules, production, i);
        }
        // remove duplicate rules
        for (int i = 0; i < rules.size (); i++) {
            String[] rule = rules.get (i);
            for (int j = 0; j < rules.size (); j++) {
                if (rules.get (j).length==rules.get (i).length) {
                    boolean isEqual = true;

                    for (int k = 0; k < rule.length; k++) {
                        if (!rules.get (j)[k].equals (rule[k])) {
                            isEqual = false;
                        }
                    }
                    if (i != j && isEqual) {
                        rules.remove (j);
                        j--;
                    }
                }
            }
        }

    }

    public static void step5Recursion (ArrayList<String[]> rules,
            String[] production, int oldProductionIndex) {
        for (int j = 0; j < rules.size (); j++) {
            // System.out.println(rules.get (j)[0]+" "); // debugging

            if (rules.get (j)[0].equals (production[1])) {
                if (rules.get (j).length == 2
                        && Character.isLowerCase (rules.get (j)[1].charAt (0))) {
                    // case where letter is lowercase
                    // S -> a
                    String[] rule = { production[0], rules.get (j)[1] };
                    rules.add (rule);
                    int ruleCount = 0;
                    for (int k = 0; k < rules.size (); k++) {
                        if (rules.get (k)[0].equals (production[1])) {
                            ruleCount++;
                        }
                    }
                    if (ruleCount > 1) {
                        rules.remove (j);
                        j--;
                    }

                } else if (rules.get (j).length == 2
                        && Character.isUpperCase (rules.get (j)[1].charAt (0))) {

                    // this part hasn't been debugged its for complicated cfgs
                    // grammars.
                    /*
                     * It won't work exactly correct for grammar S A A a A A e
                     */
                    String[] newProduction = { production[0], rules.get (j)[1] };
                    // check conditions before removing
                    rules.remove (oldProductionIndex);
                    if (oldProductionIndex < j) {
                        j--;
                    }
                    step5Recursion (rules, newProduction, j);
                    j--;
                    // re-avaluate it for | transitions
                } else if (rules.get (j).length == 3) {

                    String[] rule = { production[0], rules.get (j)[1],
                            rules.get (j)[2] };
                    rules.add (rule);
                   // rules.remove (j);
                   // j--;
                }

            }
        }
    }

    public static ArrayList<String[]> findUnitProductions (
            ArrayList<String[]> rules) {
        ArrayList<String[]> unitProductions = new ArrayList<String[]> ();
        // create graph of unit productions
        for (int i = 0; i < rules.size (); i++) {
            if (rules.get (i).length == 2
                    && Character.isUpperCase (rules.get (i)[1].charAt (0))) {
                // it is a unit production
                // create a graph
                unitProductions.add (rules.get (i));
            }
        }
        return unitProductions;
    }

    /**
     * ############## STEP FIVE VERSION 2 ################## 2nd version done by
     * BOXIONG ZHAO - NOT 100% correct couldn't not work for rules A->A1 A1->A2
     * 
     * @param rules
     */
    public static void stepFiveV2 (ArrayList<String[]> rules) {
        int con = 1;
        for (int ij = 0; ij < rules.size (); ij++) {
            if (rules.get (ij).length == 2) {
                con++;
            }
        }

        for (int ij = 0; ij < con; ij++) {
            int si = rules.size ();
            for (int m = 0; m < si; m++) {
                if (rules.get (m).length == 2
                        && Character.isUpperCase (rules.get (m)[1].charAt (0))) {
                    for (int n = 0; n < si; n++) {
                        if (rules.get (n).length == 2
                                && Character.isUpperCase (rules.get (n)[1]
                                        .charAt (0))) {
                            if (rules.get (m)[1].compareTo (rules.get (n)[0]) == 0) {
                                String[] str0 = new String[2];
                                str0[0] = rules.get (m)[0];
                                str0[1] = rules.get (n)[1];
                                rules.add (str0);
                                // System.out.println(str0[0]+str0[1]+str0[2]);
                            }

                        }

                    }
                }
            }

            for (int ijj = 0; ijj < si; ijj++) {
                for (int jji = 0; jji < si; jji++) {
                    if (rules.get (ijj).length == 2
                            && rules.get (jji).length == 2
                            && rules.get (ijj)[0]
                                    .compareTo (rules.get (jji)[0]) == 0
                            && rules.get (ijj)[1]
                                    .compareTo (rules.get (jji)[1]) == 0
                            && ijj != jji) {
                        rules.remove (ijj);

                    }

                }

            }
        }

        for (int i1 = 0; i1 < rules.size (); i1++) {
            for (int j1 = 0; j1 < rules.size (); j1++) {
                if (rules.get (i1).length == 2 && rules.get (j1).length == 2
                        && rules.get (i1)[0].compareTo (rules.get (j1)[1]) == 0
                        && rules.get (i1)[1].compareTo (rules.get (j1)[0]) == 0) {
                    for (int k1 = 0; k1 < rules.size (); k1++) {
                        for (int l1 = 0; l1 < rules.get (k1).length; l1++) {
                            if (rules.get (k1)[l1]
                                    .compareTo (rules.get (i1)[0]) == 0) {
                                rules.get (k1)[l1] = rules.get (j1)[1];
                                // System.out.println("Got one AB BA");
                            }
                        }

                    }

                }

            }

        }
        int rs = rules.size ();
        for (int i2 = 0; i2 < rs; i2++) {
            if (rules.get (i2).length == 2
                    && Character.isUpperCase (rules.get (i2)[1].charAt (0))) {
                for (int j2 = 0; j2 < rs; j2++) {
                    if (rules.get (j2).length == 3
                            && rules.get (j2)[0].compareTo (rules.get (i2)[1]) == 0) {
                        String[] str1 = new String[3];
                        str1[0] = rules.get (i2)[0];
                        str1[1] = rules.get (j2)[1];
                        str1[2] = rules.get (j2)[2];
                        rules.add (str1);
                        // System.out.println("Got one A->B B->BC");
                    }
                }
            }

        }
        int rsize = rules.size ();
        for (int i3 = 0; i3 < rsize; i3++) {
            if (rules.get (i3).length == 2
                    && Character.isUpperCase (rules.get (i3)[1].charAt (0))) {
                for (int j3 = 0; j3 < rsize; j3++) {
                    if (rules.get (j3).length == 2
                            && Character.isLowerCase (rules.get (j3)[1]
                                    .charAt (0))
                            && rules.get (i3)[1].compareTo (rules.get (j3)[0]) == 0) {
                        String[] str3 = new String[2];
                        str3[0] = rules.get (i3)[0];
                        str3[1] = rules.get (j3)[1];
                        // System.out.println("got one A->B B->b");
                        rules.add (str3);
                    }
                }

            }
        }

        boolean bo = true;
        while (bo) {
            for (int z = 0; z < rules.size (); z++) {
                if (rules.get (z).length == 2
                        && Character.isUpperCase (rules.get (z)[1].charAt (0))) {
                    rules.remove (z);

                }

            }
            for (int i0 = 0; i0 < rules.size (); i0++) {
                if (rules.get (i0).length == 2
                        && Character.isUpperCase (rules.get (i0)[1].charAt (0))) {
                    bo = true;
                    break;

                } else
                    bo = false;
            }
        }
    }

    /*
     * BOXIONG ZHAO code which didn't work so well public static void
     * stepFour(ArrayList<String[]> rules) { for (int l = 0; l < rules.size();
     * l++) { if (rules.get(l).length == 2 && rules.get(l)[1].charAt(0) == 'e')
     * { for (int o = 0; o < rules.size(); o++) { boolean bbb; if
     * (rules.get(l)[0].compareTo(rules.get(o)[1]) == 0) { bbb = true; } else
     * bbb = false; if (rules.get(o).length == 2 && bbb) { String[] chch = new
     * String[2]; chch[0] = rules.get(o)[0]; chch[1] = "e"; rules.add(chch);
     * //System.out.println("Got one A->B"); } } } } for (int i = 0; i <
     * rules.size(); i++) { if (rules.get(i).length == 2) { if
     * (rules.get(i)[1].charAt(0) == 'e') { for (int m = 0; m < rules.size();
     * m++) { if (rules.get(m).length == 3) { boolean b, c; if
     * (rules.get(i)[0].compareTo(rules.get(m)[1]) == 0) { b = true; } else b =
     * false; if (rules.get(i)[0].compareTo(rules.get(m)[2]) == 0) { c = true; }
     * else c = false; if (b && !c) { String[] kk = new String[2]; kk[0] =
     * rules.get(m)[0]; kk[1] = rules.get(m)[2]; rules.add(kk); } else if (c &&
     * !b) { String[] kk = new String[rules.get(m).length - 1]; kk[0] =
     * rules.get(m)[0]; kk[1] = rules.get(m)[1]; rules.add(kk); } else if (b &&
     * c) { String[] kk = new String[rules.get(m).length - 1]; kk[0] =
     * rules.get(m)[0]; kk[1] = rules.get(m)[1]; rules.add(kk); } } }
     * rules.remove(i); } } } }
     */
    /**
     * ############## STEP FOUR ##################
     * 
     * @param rules
     */
    public static void stepFour (ArrayList<String[]> rules) {
        for (int i = 0; i < rules.size (); i++) {
            if (rules.get (i)[1].equals ("e")) {
                // made into a function
                String nullNonTerminal = rules.get (i)[0];
                rules.remove (i);

                removeEpsilon (rules, nullNonTerminal);
            }
            //
        }
    }

    public static void removeEpsilon (ArrayList<String[]> rules,
            String nullNonTerminal) {

        for (int j = 0; j < rules.size (); j++) {

            if (rules.get (j)[1].equals (nullNonTerminal)) {
                if (rules.get (j).length == 3) {
                    if (rules.get (j)[2].equals (nullNonTerminal)) {
                        // case1
                        String newNullNonTerminal = rules.get (j)[0];
                        // reecursion on newNull

                        if (!isDoubleNonTerminal (rules, nullNonTerminal)) {
                            rules.remove (j);

                            removeEpsilon (rules, newNullNonTerminal);
                        }
                    } else {
                        // case 3
                        String[] newRule = { rules.get (j)[0], rules.get (j)[2] };
                        if (isDoubleNonTerminal (rules, nullNonTerminal)) {
                            rules.add (j, newRule);
                            j++;
                        } else
                            rules.set (j, newRule);
                    }
                } else {
                    // case 2
                    String newNullNonTerminal = rules.get (j)[0];
                    // recursion
                    if (!isDoubleNonTerminal (rules, nullNonTerminal)) {

                        rules.remove (j);

                        removeEpsilon (rules, newNullNonTerminal);
                    }
                }
                // String[] newRule={rules.get(j)[0])
            } else if (rules.get (j).length == 3) {

                if (rules.get (j)[2].equals (nullNonTerminal)) {

                    // case 4
                    // S-> B A
                    // A-> e
                    // = S -> B
                    String[] newRule = { rules.get (j)[0], rules.get (j)[1] };
                    if (isDoubleNonTerminal (rules, nullNonTerminal)) {
                        rules.add (j, newRule);
                        // interesting problem because when u add u keep adding
                        // and the loop never ends.
                        // System.out.println(newRule[1]);
                        // break;
                        j++;
                    } else
                        rules.set (j, newRule);
                }
            }
        }

    }

    public static boolean isDoubleNonTerminal (ArrayList<String[]> rules,
            String NonTerminal) {
        // we removed the 1st one
        int count = 1;
        for (int i = 0; i < rules.size (); i++) {
            if (rules.get (i)[0].equals (NonTerminal))
                count++;
        }
        // System.out.println(count);
        if (count > 1)
            return true;
        return false;
    }

    /**
     * ############## STEP THREE ##################
     * 
     * @param rules
     */
    public static void stepThree (ArrayList<String[]> rules) {
        boolean thereIsS = false;
        for (int i = 0; i < rules.size (); i++) {
            String[] rule = rules.get (i);
            for (int j = 1; j < rule.length; j++) {
                if (rule[j].equals ("S")) {
                    thereIsS = true;
                    break;
                }
            }
        }
        if (thereIsS) {
            for (int i = 0; i < rules.size (); i++) {
                String[] rule = rules.get (i);
                for (int j = 0; j < rule.length; j++) {
                    if (rule[j].equals ("S")) {
                        rule[j] = "S_0";
                    }
                }
            }
            String[] SigmaRule = { "S", "S_0" };
            rules.add (SigmaRule);
        }
    }

    /**
     * ############## STEP TWO ##################
     * 
     * @param rules
     */
    public static void stepTwo (ArrayList<String[]> rules) {

        int count = 0;

        for (int i = 0; i < rules.size (); i++) {

            while (rules.get (i).length > 3) {

                int n = rules.get (i).length;

                String[] g = new String[3];

                g[0] = "P" + count;

                g[1] = rules.get (i)[n - 2];

                g[2] = rules.get (i)[n - 1];

                rules.add (g);

                String[] h = new String[n - 1];

                for (int j = 0; j < n - 2; j++)

                {

                    h[j] = rules.get (i)[j];

                }

                h[n - 2] = "P" + count;

                count++;

                rules.remove (i);

                rules.add (h);

            }

        }

    }

    /**
     * ############## STEP ONE ##################
     * 
     * @param rules
     */
    public static void stepOne (ArrayList<String[]> rules) {

        int s = rules.size ();

        for (int i = 0; i < s; i++) {

            if (rules.get (i).length > 2) {

                for (int j = 0; j < rules.get (i).length; j++) {

                    char n = rules.get (i)[j].charAt (0);

                    if (Character.isLowerCase (n)) {

                        String[] g = new String[2];

                        g[0] = rules.get (i)[j].toUpperCase () + "_0";// add one
                                                                      // more

                        g[1] = rules.get (i)[j];
                        boolean isAlreadyDefined = false;
                        for (int k = 0; k < rules.size (); k++) {
                            if (g[0].equals (rules.get (k)[0]))
                                isAlreadyDefined = true;
                        }
                        if (!isAlreadyDefined)
                            rules.add (g);

                        rules.get (i)[j] = rules.get (i)[j].toUpperCase ()
                                + "_0"; // change
                                        // the
                                        // grammar

                    }

                }

            }

        }

    }

    /**
     * are the rules read or just null?
     * 
     * @return
     */
    public boolean isRead () {
        if (this.rules != null)
            return true;
        return false;
    }

    /**
     * Reads the rules
     * 
     * @param grammarFile
     * @return
     */
    public ArrayList<String[]> readrules (String grammarFile) {
        ArrayList<String[]> rules = new ArrayList<String[]> ();
        try {
            FileReader fr = new FileReader (grammarFile);
            BufferedReader br = new BufferedReader (fr);
            String line = br.readLine ();
            while (line != null) {
                String[] rule = line.toString ().split (" ");
                rules.add (rule);
                line = br.readLine ();
            }

        } catch (Exception e) {
            System.out.println ("Can't find file grammar.txt");
            return null;
        }
        return rules;

    }

    /**
     * Prints the CNF rules
     * 
     * @param args
     */
    public static void main (String[] args) {
        Grammar test = new Grammar ("grammar.txt");
        for (int i = 0; i < test.size (); i++) {
            String[] rule = test.get (i);
            for (int j = 0; j < rule.length; j++) {
                System.out.print (rule[j] + " ");
            }
            System.out.println ();
        }
    }
}

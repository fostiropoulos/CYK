# CYK implemented in Java

## Description

The project is based on the requirements set by Dr. Mitra within his Syllabus. We were asked
to write a program, for an input CFG, and then a string, be ready to generate/reject the
later. The objective of the program was to create a string parser given a Context Free
Grammar. A string parser is a program used by compilers to identify program syntax and be
able to convert the given program to machine code. Application of string parsers can be
found on Artificial Intelligence where the natural language is converted into digital
commands. Furthermore the application of such parser was obviously an intriguing project
with potentialfor expansion. After the research done on parsers, we concluded the scope of
our program.

The scope of our program is defined by the inputs and the outputs. After research we
concluded that the simplest grammar to use as input is CNF (Chomsky Normal Form).

![Alt text](http://s16.postimg.org/4lykdyfhd/Untitled_2.png "Grammar Example")

It is simple to understand and parse. We also realized that most of the Context Free
Grammar that Dr. Mitra would ask us to input would be invalid or too hard to convert to
CNF. Furthermore we made a grammar converter that would convert any Context Free
Grammar to Chomsky Normal Form.

In order to make debugging easier for us and make the process of parsing more
understandable to us, Dr. Mitra or any other future user, we output a CYK table. The CYK
Table is the table created during the use of the CYK Algorithm. The CYK Table is really
similar to a parsing tree. The inputted string is on the top of the output window and through
a series of steps it is converted to the starting symbol S. At the end of the table there is a
conclusion of whether or not the string belongs to the language described by the Context
Free Grammar.

## Algorithm PseudoCode

### CYK [1]

	let the input be a string S consisting of n characters: a1 ... an.
	let the grammar contain r nonterminal symbols R1 ... Rr.
	This grammar contains the subset Rs which is the set of start symbols.
	let P[n,n,r] be an array of booleans. Initialize all elements of P to false.
	for each i = 1 to n
	  for each unit production Rj -> ai
	    set P[i,1,j] = true
	for each i = 2 to n -- Length of span
	  for each j = 1 to n-i+1 -- Start of span
	    for each k = 1 to i-1 -- Partition of span
	      for each production RA -> RB RC
	        if P[j,k,B] and P[j+k,i-k,C] then set P[j,i,A] = true
	if any of P[1,n,x] is true (x is iterated over the set s, where s are all the indices for Rs) then
	  S is member of language
	else
	  S is not member of language


###Chomsky Normal Form to Context Free Grammar [2]

	1. Remove all nonterminal from the right hand side of all productions except the unit
	productions.
	2. Replace any rule that has three or more nonterminals with the equivalent rules of size
	two.
	3. Replace every rule S with S�. Add a new rule S?S�
	4. Remove all epsilon transitions by iterating their equivalent form. For each production
	that includes a terminal that is equal to epsilon, add another rule equal to the initial
	rule excluding the terminal that is equal to epsilon.
	5. Remove all the unit rules of the form A ? B

## Issues

The project is not completely finished. Step 5 of Chomsky Normal form converter algorithm, a not so crucial but important step
for the algorithm, is not working. Step 5 is reponsible for simplifying the grammar to
match CNF format. 

## How-to-use

Move grammars to executables folder and rename them to grammar.txt

Open command line and type

	java C:\path to .class (without the .class)

Grammar.class 
	will output the CNF to CFG

cyk.class 
	Will output the table and transitions. 

to compile use java or eclipse. 

### Examples of Use

![Alt text](http://s24.postimg.org/e2xfdq7md/Untitled_4.png "Grammar Example")
![Alt text](http://s24.postimg.org/56mn9sh05/Untitled_5.png "Grammar Example")
![Alt text](http://s24.postimg.org/fff4fm51x/Untitled_6.png "Grammar Example")


## IMPORANT 
Because CYK relies on the final symbols to determine if the string belongs or not to the Language
it is possible that the final symbol is there but not recognized by the CYK because of the lack
of step 5. 

Try compiling by comenting out the step5 and uncommenting step5v2 if that doesn't work try commenting both.

If a symbol is on the final cell it might be the final so assume string is accepted. 

Also note that the documented input format is CNF, according to the specifications provided,
we could input a language in any format. But we tried to go the extra mile and make it better. 

## Refferences
[1] Lange, Martin, and Hans Leiß. "To CNF or Not to CNF? An Efficient Yet Presentable
Version of the CYK Algorithm." Thesis.Ludwig-Maximilians-Universit¨atM¨unchen,
Germany, 2009. To CNF or Not to CNF? An Efficient Yet Presentable Version of the CYK
Algorithm. http://www.informatica-didactica.de, 29 June 2009. Web.6 Apr. 2013.

[2] Cole, Richard. "Converting CFGs to CNF (Chomsky Normal Form)." - Chomsky
Normal Form. New York University, 17 Oct. 2007. Web.6 Apr. 2013.
<http://www.cs.nyu.edu/courses/fall07/V22.0453-001/cnf.pdf>.

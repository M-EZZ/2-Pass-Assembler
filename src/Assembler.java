import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Assembler {

    static List<Instruction> OPTAB = new ArrayList<Instruction>();
    static List <Literals> LITTAB=new ArrayList<Literals>();
    static Map<String , Integer > SYMTAB = new HashMap<String , Integer>();
    static List <CodeLine> Assembly= new ArrayList<CodeLine>();
    static int LOCCTR = 0 , startAddress = 0 , first_executable = -1 ; // initialized ot -1 to be updated only once
    static  int progLength = 0 ;
    static String [] assembler_directives = {"START" , "END" , "BASE" ,"NOBASE"}; //TODO {"BYTE" , "WORD" , "RESB" , "RESW" }
    //TODO make a register table using a dictionary if neeaded
    public static void main(String []args) throws IOException {
        read_ISA();
        Pass1();

    }

    public static void read_ISA () throws IOException {
        BufferedReader optab_buffer = new BufferedReader(new FileReader("OPTAB.txt"));
        String str;
        String []parts;

        // reading the instruction set from text file

        while((str = optab_buffer.readLine()) != null){
            parts= str.split(" ");
            Instruction instruction=new Instruction(parts[0],parts[1],parts[2]);
            OPTAB.add(instruction);
            Arrays.fill(parts,null);
        }
        optab_buffer.close();
    }

    public static void Pass1 () throws IOException  {
        String str ;
        String [] lmo ; //label mnemonic operands

        // reading assembly code from text file
        BufferedReader asm = new BufferedReader(new FileReader("code.txt"));
        // writing to the intermediate text file
        BufferedWriter intermediate = new BufferedWriter(new FileWriter("intermediate.txt"));
        BufferedWriter symbol_table = new BufferedWriter(new FileWriter( "symbol_table.txt"));
        symbol_table.write("Symbol" +"\t"+"Address"+"\t"+"\n");


        while((str = asm.readLine()) != null){
            if (isComment(str)) { continue; }
            CodeLine line = CodeLine.parse(str);
            line.address = LOCCTR;

            if(line.symbol != null) {
                if (SYMTAB.containsKey(line.symbol)) {
                    System.out.println("Duplicate Symbol ERROR");
                } else {
                    SYMTAB.put(line.symbol, LOCCTR);
                }
            }


            switch (line.mnemonic){
                case "START":
                    startAddress = Integer.parseInt(line.operands[0]);
                    LOCCTR = startAddress;
                    line.address = LOCCTR;
                    //SYMTAB.put(line.symbol,LOCCTR);
                    break;

                case "END":
                    break;

                case "RESW":
                    LOCCTR += 3 * Integer.parseInt(line.operands[0]);
                    break;

                case "RESB":
                    LOCCTR += Integer.parseInt(line.operands[0]);
                    break;

                case "BYTE":             //TODO literal table needs to be considered
                    String s = line.operands[0];  //Operand 1
                    switch (s.charAt(0)) {
                        case 'C':
                            int length= s.length()-3;
                            LOCCTR += (length);
                            // C'EOF' -> EOF -> 3 bytes
                            Literals literal=new Literals(s,length,s.substring(1,s.length()),LOCCTR,0);
                            LITTAB.add(literal);
                            break;
                        case 'X':
                            length = (s.length()-3)/2;
                            LOCCTR += (s.length() - 3) / 2; // X'05' -> 05 -> 2 half bytes
                             literal=new Literals(s,length,s.substring(1,s.length()),LOCCTR,0);
                            LITTAB.add(literal);
                            break;
                    }
                    break;

                case "WORD":
                    LOCCTR += 3 ;
                    break;

                case "BASE":
                    break;

                case "NOBASE":
                    break;

                default:
                    if(search(line.mnemonic)!= -1){
                        if(first_executable < 0){
                            first_executable = LOCCTR;
                        }
                        switch (search(line.mnemonic)){  //switch( format of the mnemonic )
                            case 1:
                                LOCCTR += 1;
                                break;
                            case 2:
                                LOCCTR += 2;
                                break;
                            case 7:         //case 3/4 :   named 7 for ease of use in the code
                                LOCCTR += 3 + ((line.extended)? 1:0);
                                break;
                        }
                    }
                    else{
                        System.out.println("INVALID OPERATION : " +line.toString());
                    }
            }
            // System.out.println(line);          //Uncomment to show the address and Source CodeLine

            intermediate.write(line.toString()+"\n");
            if(line.symbol != null){symbol_table.write(line.symbol+" \t"+line.address+"\n");}
        }


        progLength = LOCCTR - startAddress ; //TODO needs checking
        intermediate.close();
        asm.close();
        symbol_table.close();
    }


    public static boolean isComment (String str) {
        return str.startsWith(".");
    }

    public static int search (String mnemonic)
    {
        int found=0;
        int i;
        for ( i=0 ; i< OPTAB.size() ; i++)
        {
            if(OPTAB.get(i).mnemonic.equals(mnemonic)) {

                found = 1;
                break;
            }
        }
        if(found == 1)
            return OPTAB.get(i).format;
        else
            return -1;
    }



}

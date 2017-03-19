import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Assembler {


    static List<Instruction> OPTAB = new ArrayList<Instruction>();
    static List<Literals> LITTAB=new ArrayList<Literals>();
    static Map< String , String > SYMTAB = new HashMap<String , String>();
    static List <CodeLine> Assembly= new ArrayList<CodeLine>();
    static int LOCCTR = 0 , startAddress = 0 , first_executable = -1 ; // initialized ot -1 to be updated only once
    static  int progLength = 0 ;
    static String [] assembler_directives = {"START" , "END" , "BASE" ,"NOBASE" ,"BYTE" , "WORD" , "RESB" , "RESW" };


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
            Instruction instruction=new Instruction(parts[0],parts[1], hex2decimal(parts[2]));
            OPTAB.add(instruction);
            Arrays.fill(parts,null);
        }
        optab_buffer.close();
    }

    public static void Pass1 () throws IOException  {
        String str ;
        String [] lmo ; //label mnemonic operands

        // reading assembly code from text file
        BufferedReader asm = new BufferedReader(new FileReader("input_palindrome.txt"));
        // writing to the intermediate text file
        BufferedWriter intermediate = new BufferedWriter(new FileWriter("intermediate.txt"));
        BufferedWriter symbol_table = new BufferedWriter(new FileWriter( "symbol_table.txt"));
        symbol_table.write("Symbol" +"\t"+"Address"+"\t"+"\n");
        BufferedWriter literal_table = new BufferedWriter(new FileWriter("literal_table.txt"));
        literal_table.write("Literal" + " \t"+ "Length" +" \t"+"Value" +" \t"+ "Address"+"\n" );

        while((str = asm.readLine()) != null){
            if (isComment(str)) { continue; }
            CodeLine line = CodeLine.parse(str);
            line.address = Integer.toHexString(LOCCTR);

            if(line.symbol != null) {
                if (SYMTAB.containsKey(line.symbol)) {
                    System.out.println("Duplicate Symbol ERROR");
                } else {
                    SYMTAB.put(line.symbol, Integer.toHexString(LOCCTR));
                }
            }


            switch (line.mnemonic){
                case "START":
                    startAddress = hex2decimal(line.operands[0]);
                    LOCCTR = startAddress;
                    line.address = Integer.toHexString(LOCCTR);
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

                case "BYTE":
                    String s = line.operands[0];  //Operand 1
                    switch (s.charAt(0)) {
                        case 'C':
                            int length= s.length()-3;
                            LOCCTR += (length);
                            // C'EOF' -> EOF -> 3 bytes
                            Literals literal=new Literals(s,length,s.substring(2,s.length()-1),Integer.toHexString(LOCCTR),0);
                            LITTAB.add(literal);
                            literal_table.write( literal.name+ "\t\t"+ literal.length +"\t\t"+literal.value +"\t\t"+ literal.address+"\n" );
                            break;
                        case 'X':
                            length = (s.length()-3)/2;
                            LOCCTR += (s.length() - 3) / 2; // X'05' -> 05 -> 2 half bytes
                             literal=new Literals(s,length,s.substring(2,s.length()-1),Integer.toHexString(LOCCTR),0);
                            LITTAB.add(literal);
                            literal_table.write( literal.name+ "\t\t"+ literal.length +"\t\t"+literal.value +"\t\t"+ literal.address+"\n" );
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
                        break;
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
        literal_table.close();
    }

    public static void pass2 () throws IOException {

        String str;
        TextRecord textRecord = new TextRecord(startAddress);
        BufferedReader asm = new BufferedReader(new FileReader("input_palindrome.txt"));
        BufferedWriter objectProgram = new BufferedWriter(new FileWriter("ObjectCode.txt"));
        while((str = asm.readLine()) != null) {
            CodeLine line = CodeLine.parse(str);
            if(line.symbol == "START"){
                objectProgram.write(new HeaderRecord(line.symbol , startAddress , progLength ).toObjectProgram());
            }
            else if (line.symbol == "END"){break ;}
            else {
                String objectCode = assembleInstruction (line);      //TODO
                if (textRecord.add(objectCode) == false){
                    objectProgram.write(textRecord.toObjectProgram() + '\n');
                    textRecord = new TextRecord(Integer.parseInt(line.address));
                    textRecord.add(objectCode);
                }
            }
        }
        objectProgram.write(textRecord.toObjectProgram() + '\n');
        objectProgram.write(new EndRecord(first_executable).toObjectProgram() + '\n');
    }

    public static boolean isComment (String str) {
        return str.startsWith(".");
    }

    public static int search (String mnemonic) {
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

    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
}

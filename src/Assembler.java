import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assembler {

    public static void main(String []args) throws IOException {
        BufferedReader in1 = new BufferedReader(new FileReader("C:\\Users\\Omar Ahmed\\Documents\\GitHub\\2-Pass-Assembler\\ISA.txt"));
        String str;
        String []parts;

      // reading the instruction set from text file
        List<Instruction> ISA = new ArrayList<Instruction>();
        while((str = in1.readLine()) != null){
            Instruction instruction=new Instruction();
            parts= str.split(" ");
            instruction.name = parts[0];
            instruction.opCode= parts[1];
            ISA.add(instruction);
            Arrays.fill(parts,null);
        }
       // System.out.print(ISA.toString());
      // reading assembly code from text file
        BufferedReader in2 = new BufferedReader(new FileReader("C:\\Users\\Omar Ahmed\\Documents\\GitHub\\2-Pass-Assembler\\CODE.txt"));
        List <CodeLine> Assembly= new ArrayList<CodeLine>();
        while((str = in2.readLine()) != null){
            CodeLine code=new CodeLine();
            code.line= str;
            Assembly.add(code);
        }




    }
    public static int Pass1(List <CodeLine> code)
    {
        int locationCounter;
         return 0;
    }


}

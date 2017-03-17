
public class Instruction {
 public  String opCode;
    public String name;
    public String format;
    @Override
    public String toString() {
        return ("  name:"+this.name+
                "  opcode: "+ this.opCode);
    }


}

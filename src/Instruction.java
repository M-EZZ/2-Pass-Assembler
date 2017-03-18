public class Instruction {

    public String mnemonic , objcode;
    public int format ;

    // address symbol mnemonic operand      Example : 203F loop1 ADD foo

    public Instruction (String mnemonic, String format, String opcode) {
        this.mnemonic = mnemonic;
        this.objcode = opcode;
        this.format = Integer.parseInt(format);
    }

    @Override
    public String toString() {
        return ("  Mnemonic:"+this.mnemonic+
                "  Format: "+ this.format+
                "  Object Code: "+ this.objcode);
    }

}

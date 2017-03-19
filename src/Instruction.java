public class Instruction {

    public String mnemonic ;
    public int format , opcode;

    // address symbol mnemonic operand      Example : 203F loop1 ADD foo

    public Instruction (String mnemonic, String format, int opcode) {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.format = Integer.parseInt(format);
    }

    @Override
    public String toString() {
        return ("  Mnemonic:"+this.mnemonic+
                "  Format: "+ this.format+
                "  Object Code: "+ this.opcode);
    }

}

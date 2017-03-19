import java.util.ArrayList;
import java.util.Arrays;

public class CodeLine {

    public String symbol;
    public String mnemonic;
    public String[] operands;
    public boolean extended;
    public int address ;


    private CodeLine(String symbol, String mnemonic, boolean extended, String[] operands) {
        this.symbol = symbol;
        this.mnemonic = mnemonic;
        this.extended = extended;
        this.operands = operands;
    }

    @Override
    public String toString() {
        return (this.address + "\t" + (this.symbol != null? this.symbol:"") + "\t" + this.mnemonic + "\t" + (this.operands[0] != null? this.operands[0]:"") + (this.operands[1] != null? (','+this.operands[1]) :"")) ;
    }

    public static CodeLine parse(String statement) {
        //String[] tokens = statement.trim().split("  ");
        ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(statement.split(" ")));
        tokens.removeAll(Arrays.asList("" ,null));

        String symbol, mnemonic;
        String[] Operands;
        boolean extended = false;
        int index = 0;

        if (tokens.size() == 3) {
            symbol = tokens.get(index++); //tokens[index++];
            symbol = symbol.replace(" " ,"");
        } else {
            symbol = null;
        }

        mnemonic = tokens.get(index++); //tokens[index++];
        mnemonic = mnemonic.replace(" ", "");
        try{
        if (mnemonic.charAt(0) == '+') {
            extended = true;
            mnemonic = mnemonic.substring(1);
        }}catch (StringIndexOutOfBoundsException e){
            //TODO
        }

        Operands = new String[2];
        if (index < tokens.size()) {
            int pos = tokens.get(index).indexOf(','); //tokens[index].indexOf(',');
            if (pos > 0) {
                Operands[0] = tokens.get(index).substring(0,pos); //tokens[index].substring(0, pos);
                Operands[0] = Operands[0].replace(" " ,"");
                Operands[1] = tokens.get(index).substring(pos+1); //tokens[index].substring(pos + 1);
                Operands[1] = Operands[1].replace(" " ,"");
            } else {
                Operands[0] = tokens.get(index); //tokens[index];
                Operands[0] = Operands[0].replace(" " ,"");
                Operands[1] = null;
            }
        } else {
            Operands[0] = Operands[1] = null;
        }

        return new CodeLine(symbol, mnemonic, extended, Operands);
    }
}

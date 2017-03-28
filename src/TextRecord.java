import java.util.ArrayList;
import java.util.List;

public class TextRecord implements Record {
    public int startAddress;
    public int length;
    public List <String> objectCodes;

    public static int MAX_LENGTH = 0x1E;        //TODO needs to be in alignment with the output length of objectCode from assembleInstruction()

    public TextRecord(int startAddress) {
        this.startAddress = startAddress;
        this.length = 0;
        this.objectCodes = new ArrayList<>();
    }

    public boolean add(String objectCode) {
        if (objectCode.length() == 0) {
            return true;                    // assembler directives case

        } else if (length + objectCode.length() / 2 <= MAX_LENGTH) {
            objectCodes.add(objectCode);
            length += objectCode.length() / 2;
            return true;

        } else {
            return false;
        }
    }

    @Override
    public String toObjectProgram() {
        String temp = String.format("T %06X %02X ", startAddress, length);

        for (String s : objectCodes) {
            temp += s + " ";
        }
        return temp;
    }


}


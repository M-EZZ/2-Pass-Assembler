import java.util.ArrayList;
import java.util.List;

public class TextRecord implements Record {
    private int startAddress;
    public int length;
    List <String> objectCodes;

    private static int MAX_LENGTH = 0x1E;        //TODO needs to be in alignment with the output length of objectCode from assembleInstruction()

    TextRecord(int startAddress) {
        this.startAddress = startAddress;
        this.length = 0;
        this.objectCodes = new ArrayList<>();
    }

    boolean add(String objectCode) {
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


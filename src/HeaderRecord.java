public class HeaderRecord implements Record{
    public String programName;
    public int startAddress;
    public int programLength;

    public HeaderRecord(String name, int startAddress, int length) {
        this.programName = name;
        this.startAddress = startAddress;
        this.programLength = length;
    }

    @Override
    public String toObjectProgram() {
        return String.format("H %1$-6s %2$06X %3$06X", programName, startAddress, programLength);
    }
}

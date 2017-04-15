public class ModificationRecord implements Record {

    private int location;
    public  int length;

    ModificationRecord(int modifiedLocation, int modifiedLength) {
        this.location = modifiedLocation;
        this.length = modifiedLength;
    }

    @Override
    public String toObjectProgram() {
        return String.format("M %06X %02X", location, length);
    }

}
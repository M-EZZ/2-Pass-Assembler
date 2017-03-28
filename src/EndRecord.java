public class EndRecord implements Record {

    public int firstExecutable;

    public EndRecord(int firstExecutable) {
        this.firstExecutable = firstExecutable;
    }

    @Override
    public String toObjectProgram() {
        return String.format("E %1$06X", firstExecutable);
    }

}

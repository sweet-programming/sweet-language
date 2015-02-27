public class StringValue implements Value {
    private String strValue;

    public StringValue(String strValue) {
        this.strValue = strValue;
    }

    public String getType() {
        return "String";
    }

    public String toString() {
        return strValue;
    }
}

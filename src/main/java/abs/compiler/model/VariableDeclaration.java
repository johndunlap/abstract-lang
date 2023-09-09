package abs.compiler.model;

public class VariableDeclaration {
    private String name;
    private PrimitiveDataType primitiveDataType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrimitiveDataType getPrimitiveDataType() {
        return primitiveDataType;
    }

    public void setPrimitiveDataType(PrimitiveDataType primitiveDataType) {
        this.primitiveDataType = primitiveDataType;
    }
}

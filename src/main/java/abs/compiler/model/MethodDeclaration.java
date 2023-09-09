package abs.compiler.model;

import java.util.List;

public class MethodDeclaration {
    private PrimitiveDataType returnPrimitiveDataType;
    private List<PrimitiveDataType> signature;

    public PrimitiveDataType getReturnPrimitiveDataType() {
        return returnPrimitiveDataType;
    }

    public void setReturnPrimitiveDataType(PrimitiveDataType returnPrimitiveDataType) {
        this.returnPrimitiveDataType = returnPrimitiveDataType;
    }

    public List<PrimitiveDataType> getSignature() {
        return signature;
    }

    public void setSignature(List<PrimitiveDataType> signature) {
        this.signature = signature;
    }
}

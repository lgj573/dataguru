
package dataguru.codec.sentence.message;

import dataguru.codec.sentence.BinArray;
import dataguru.codec.sentence.SixbitEncoder;
import dataguru.codec.sentence.SixbitException;
import dataguru.codec.sentence.Vdm;

public class AisMessage1 extends AisPositionMessage {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * SOTDMA Slot Timeout Specifies frames remaining until a new slot is selected 0 means that this was the last
     * transmission in this slot 1-7 means that 1 to 7 frames respectively are left until slot change
     */
    protected int slotTimeout; // 3 bits

    /**
     * SOTDMA sub-message This field has many purposes see class description for help:
     */
    protected int subMessage; // 14 bits

    public AisMessage1() {
        super(1);
    }

    AisMessage1(int msgType) {
        super(msgType);
    }

    public AisMessage1(Vdm vdm) throws AisMessageException, SixbitException {
        super(vdm);
        parse();
    }

    public void parse() throws AisMessageException, SixbitException {
        BinArray binArray = vdm.getBinArray();
        super.parse(binArray);
        this.slotTimeout = (int) binArray.getVal(3);
        this.subMessage = (int) binArray.getVal(14);
    }

    @Override
    public SixbitEncoder getEncoded() {
        SixbitEncoder encoder = super.encode();
        encoder.addVal(slotTimeout, 3);
        encoder.addVal(subMessage, 14);
        return encoder;
    }

    public int getSlotTimeout() {
        return slotTimeout;
    }

    public void setSlotTimeout(int slotTimeout) {
        this.slotTimeout = slotTimeout;
    }

    public int getSubMessage() {
        return subMessage;
    }

    public void setSubMessage(int subMessage) {
        this.subMessage = subMessage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(", slotTimeout=");
        builder.append(slotTimeout);
        builder.append(", subMessage=");
        builder.append(subMessage);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean isPositionValid() {
        return false;
    }
}


package dataguru.codec.sentence.message;


/**
 * Interface for all messages carrying a the position of the sending target.
 */
public interface IPositionMessage {

    /**
     * Get position
     * 
     * @return
     */
    AisPosition getPos();

    /**
     * Position accuracy
     * 
     * @return
     */
    int getPosAcc();

}

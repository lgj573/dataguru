/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dataguru.codec.sentence.message;


import dataguru.codec.sentence.BinArray;
import dataguru.codec.sentence.SixbitEncoder;
import dataguru.codec.sentence.SixbitException;
import dataguru.codec.sentence.Vdm;

import java.io.Serializable;
import java.util.*;

/**
 * Abstract base class for all AIS messages
 */
public abstract class AisMessage implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** A set of all valid AIS message types. */
    public static final Set<Integer> VALID_MESSAGE_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(1,
            2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 17, 18, 19, 21, 24)));

    protected int msgId; // 6 bit: message id
    protected int repeat; // 2 bit: How many times message has been repeated
    protected int userId; // 30 bit: MMSI number
    protected transient Vdm vdm; // The VDM encapsulating the AIS message

    /**
     * Constructor given message id
     * 
     * @param msgId
     */
    public AisMessage(int msgId) {
        this.msgId = msgId;
        this.repeat = 0;
    }

    /**
     * Constructor given VDM with AIS message
     * 
     * @param vdm
     */
    public AisMessage(Vdm vdm) {
        this.vdm = vdm;
        this.msgId = vdm.getMsgId();
    }

    /**
     * Base parse method to be called by all extending classes
     * 
     * @param binArray
     * @throws AisMessageException
     * @throws SixbitException
     */
    protected void parse(BinArray binArray) throws AisMessageException, SixbitException {
        this.repeat = (int) binArray.getVal(2);
        this.userId = (int) binArray.getVal(30);
    }

    /**
     * Base encode method to be called by all extending classes
     * 
     * @return SixbitEncoder
     */
    protected SixbitEncoder encode() {
        SixbitEncoder encoder = new SixbitEncoder();
        encoder.addVal(msgId, 6);
        encoder.addVal(repeat, 2);
        encoder.addVal(userId, 30);
        return encoder;
    }

    /**
     * Abstract method to be implemented by all extending classes
     * 
     * @return SixbitEncoder
     */
    public abstract SixbitEncoder getEncoded();

    public int getMsgId() {
        return msgId;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    /**
     * Get VDM this message was encapsulated in
     * 
     * @return Vdm
     */
    public Vdm getVdm() {
        return vdm;
    }


    /**
     * Given VDM return the encapsulated AIS message. To determine which message is returned use instanceof operator or
     * getMsgId() before casting.
     * 
     * Example: AisMessage aisMessage = AisMessage.getInstance(vmd); if (aisMessage instanceof AisPositionMessage) {
     * AisPositionMessage posMessage = (AisPositionMessage)aisMessage; } ...
     * 
     * @param vdm
     * @return AisMessage
     * @throws AisMessageException
     * @throws SixbitException
     */
    public static AisMessage getInstance(Vdm vdm) throws AisMessageException, SixbitException {
        AisMessage message = null;

        switch (vdm.getMsgId()) {
        case 1:
            message = new AisMessage1(vdm);
            break;
        case 2:
            break;
        case 3:
            break;
        case 4:
            break;
        case 5:
            message = new AisMessage5(vdm);
            break;
        case 6:
            break;
        case 7:
            break;
        case 8:
            break;
        case 9:
            break;
        case 10:
            break;
        case 11:
            break;
        case 12:
            break;
        case 13:
            break;
        case 14:
            break;
        case 15:
            break;
        case 16:
            break;
        case 17:
            break;
        case 18:
            break;
        case 19:
            break;
        default:
            throw new AisMessageException("Unknown AIS message id " + vdm.getMsgId());
        }

        return message;
    }

    /**
     * Utility to trim text from AIS message
     * 
     * @param text
     * @return
     */
    public static String trimText(String text) {
        if (text == null) {
            return null;
        }
        // Remove @
        int firstAt = text.indexOf("@");
        if (firstAt >= 0) {
            text = text.substring(0, firstAt);
        }
        // Trim leading and trailing spaces
        return text.trim();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[msgId=");
        builder.append(msgId);
        builder.append(", repeat=");
        builder.append(repeat);
        builder.append(", userId=");
        builder.append(userId);
        return builder.toString();
    }

}

import java.io.Serializable;

/**
 * @author Chana Rosenbluth and Rachel Nemesure
 *
 * Class simulating what gets sent between Server and Client
 * There are three types of Packets: Character Packets, Message Packets, and Integer Array Packets
 */
public class Packet implements Serializable
{
    private Character character;
    private Message message;
    private Integer packetNumber;
    private Integer totalPacketsNumber;
    private Integer[] requestedPacketsNumbers;

    /**
     * Character Packets are sent from Server to Client
     * @param character - one character of the message
     * @param packetNumber - the index for the character in the message
     * @param totalPacketsNumber - the total number of message characters
     */
    public Packet (Character character, Integer packetNumber, Integer totalPacketsNumber)
    {
        // constructor for a character packet
        this.character = character;
        this.packetNumber = packetNumber;
        this.totalPacketsNumber = totalPacketsNumber;

        message = Message.NULL;
        requestedPacketsNumbers = null;
    }

    /**
     * Message Packets are sent between Server and Client
     * @param message - a Message value to be sent from Server to Client and Client to Server
     */
    public Packet (Message message)
    {
        // constructor for a message packet
        this.message = message;

        character = null;
        packetNumber = null;
        totalPacketsNumber = null;
        requestedPacketsNumbers = null;
    }

    /**
     * Integer Array Packets are sent from the Client to the Server
     * @param requestedPacketsNumbers - an array of Integers containing missing indices
     */
    public Packet (Integer[] requestedPacketsNumbers)
    {
        // constructor for a missing packets request
        this.requestedPacketsNumbers = requestedPacketsNumbers;

        message = Message.NULL;

        character = null;
        packetNumber = null;
        totalPacketsNumber = null;
    }

    public void setTotalPacketsNumber(int total)
    {
        totalPacketsNumber = total;
    }

    public Character getCharacter()
    {
        return character;
    }

    public Message getMessage()
    {
        return message;
    }

    public Integer getPacketNumber()
    {
        return packetNumber;
    }

    public Integer getTotalPacketsNumber()
    {
        return totalPacketsNumber;
    }

    public Integer[] getRequestedPacketsNumbers()
    {
        return requestedPacketsNumbers;
    }
}

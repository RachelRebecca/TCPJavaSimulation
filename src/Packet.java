import java.io.Serializable;

public class Packet implements Serializable
{
    private Character character;
    private Message message;
    private Integer packetNumber;
    private Integer totalPacketsNumber;
    private Integer[] requestedPacketsNumbers;

    public Packet (Character character, Integer packetNumber, Integer totalPacketsNumber)
    {
        // constructor for a character packet
        this.character = character;
        this.packetNumber = packetNumber;
        this.totalPacketsNumber = totalPacketsNumber;

        message = null;
    }

    public Packet (Message message)
    {
        // constructor for a message packet
        this.message = message;

        character = null;
        packetNumber = null;
        totalPacketsNumber = null;
    }

    public Packet (Integer[] requestedPacketsNumbers)
    {
        // constructor for a missing packets request
        this.requestedPacketsNumbers = requestedPacketsNumbers;

        character = null;
        message = null;
        packetNumber = null;
        totalPacketsNumber = null;
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

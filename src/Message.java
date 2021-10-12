/**
 * @author Chana Rosenbluth and Rachel Nemesure
 *
 * Enum class with the different types of messages (sent as Packets)
 */
public enum Message
{
    ALL_SENT,       // server tells client all packages have been sent
    READY,          // client tells server it's ready to receive packets
    ALL_RECEIVED,   // client tells server it has gotten all packets
    DO_NOT_SEND,    // client tells server not to send packets (as per user request)
    NULL            // client's condition getMessage() is checked without raising null exception (replaces message = null)
}

package lesson31;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private String name;
    private List<Card> cardsList;

    public Client(String name, List<Card> cardsList) {
        this.name = name;
        this.cardsList = cardsList;
    }

    

    @Override
    public String toString() {
        return "Client [name=" + name + ", cardsList=" + cardsList + "]";
    }



    public void addCard(Card card){
        if (card == null) return;
        cardsList.add(card);
    }

    public List<Card> getCardsList() {
        return cardsList;
    }

    public String getName() {
        return name;
    }

    public ByteBuffer toByteBuffer(){
        byte[] nameBytes = name.getBytes();
        byte[] enterNewLineBytes = "\n".getBytes();
        int bufferSize = nameBytes.length + enterNewLineBytes.length;
        for (Card card : cardsList){
            bufferSize += card.toByteBuffer().remaining();
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        byteBuffer.put(nameBytes);
        byteBuffer.put(enterNewLineBytes);
        for (Card card: cardsList){
            byteBuffer.put(card.toByteBuffer());
        }
        byteBuffer.flip();
        return byteBuffer;
    }
}

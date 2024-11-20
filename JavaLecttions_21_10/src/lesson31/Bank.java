package lesson31;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Bank {

    private final List<Client> clientList;

    public Bank() {
        this.clientList = new ArrayList<>();
    }

    public void addClient(Client client) {
        if (client == null) return;
        clientList.add(client);
    }

    public Card findCardByNumber(String cardNumber) {
        Card card = null;
        for (Client client : clientList) {
            for (Card clientCard : client.getCardsList()) {
                if (cardNumber.equals(clientCard.getCardNumber())) {
                    card = clientCard;
                }
            }
        }
        return card;
    }

    //повинен повертати картки у яких balance більше 10_000 гривень
    public List<Card> findUAHCardsWithHighBalance() {
        List<Card> cards = new ArrayList<>();
        //        for (Client client : clientList) {
//            for (Card clientCard : client.getCardsList()) {
//                if (clientCard.getBalance() >= 10_000 && clientCard.getCurrency() == Currency.UAH) {
//                    cards.add(clientCard);
//                }
//            }
//        }
        cards = clientList.stream()
                .flatMap(client -> client.getCardsList().stream())
                .filter(card -> card.getCurrency() == Currency.UAH && card.getBalance() >= 10_000)
                .collect(Collectors.toList());

        return cards;
    }

    public void saveClientToFile() {
        try (FileChannel channel = FileChannel.open(Path.of(CardUtils.CLIENTS_FILE_PATH),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE)) {
            for (Client client : clientList) {
                ByteBuffer clientBuffer = client.toByteBuffer();
                channel.write(clientBuffer);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Cannot save clients to file");
        }
    }
    
    public List<Client> loadClientsFromFile(){
        List<Client> clients = new ArrayList<>();
        List<Card> cards = new ArrayList<>();
        String currentName = null;
        String currentCard = null;


        try (FileChannel channel = (FileChannel) Files.newByteChannel(Paths.get("clients.txt"))){
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            StringBuilder builder = new StringBuilder("");
            for(int i = 0; i < channel.size(); i++){
                builder.append((char) buffer.get());
            }

            String[] words = builder.toString().split("\n");
            
            Pattern patternForNames = Pattern.compile("\\b[a-zA-Z]+\\b");
            Pattern patternForCards = Pattern.compile("\\d{4} \\d{4} \\d{4} \\d{4}[A-Z]+\\d+\\.\\d+");


            for (String string : words) {
                Matcher matcherForNames = patternForNames.matcher(string);
                Matcher matcherForCards = patternForCards.matcher(string);
                if (matcherForNames.find()) {
                    currentName = matcherForNames.group();
                    System.out.println(currentName);
                    cards.clear();

                    while(matcherForCards.find()){
                        currentCard = matcherForCards.group();
                        System.out.println(currentCard);
                        cards.add(new Card(currentCard));
                    }
                    
                    clients.add(new Client(currentName, new ArrayList<>(cards)));
                }
            }

                
            } catch (IOException exception) {
                System.out.println("Error.");
                exception.printStackTrace();
            }
        // System.out.println(cards);
        System.out.println(clients);
        System.out.println(cards.size());
        System.out.println(clients.size());
        return clients;
    }
}

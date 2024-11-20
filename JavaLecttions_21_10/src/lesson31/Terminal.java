package lesson31;

import lesson31.exceptions.InsufficientFundsException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Terminal {

    public static void main(String[] args) {
        if(Files.exists(Path.of(CardUtils.CLIENTS_FILE_PATH))){
            System.out.println("True");
            loadClientsFromFile();
        } else {
            createNewClients();
        }

    }

    public static void createNewClients(){
        Bank bank = new Bank();
        Card cardJohn1 = new Card("4444 4444 4444 4444",100.33,Currency.UAH);
        Card cardJohn2 = new Card("4445 4445 4445 4445",400.01,Currency.USD);
        Client clientJohn = new Client("John", List.of(cardJohn1, cardJohn2));
        Card cardSuzi1 = new Card("5555 5555 5555 5555",270, Currency.UAH);
        Card cardSuzi2 = new Card("5556 5556 5556 5556",700, Currency.UAH);
        Card cardSuzi3 = new Card("5556 5556 5556 5556",100, Currency.EUR);
        Client clientSuzi = new Client("Suzi", List.of(cardSuzi1,cardSuzi2,cardSuzi3));
        Card cardSmith = new Card("7777 7777 7777 7777",10_000.00,Currency.UAH);
        Client clientSmith = new Client("Smith",List.of(cardSmith));
        bank.addClient(clientJohn);
        bank.addClient(clientSuzi);
        bank.addClient(clientSmith);
        List<Card> cards = bank.findUAHCardsWithHighBalance();
        System.out.println(cards);
        Card card = bank.findCardByNumber("5556 5556 5556 5556");
        if (card != null){
            //doSomething
        }
        System.out.println(card);
        Card cardNonExist = bank.findCardByNumber("0000 0000 0000 0000");
        System.out.println(cardNonExist);
        bank.saveClientToFile();
    }

    public static List<Client> loadClientsFromFile(){
        Bank bank = new Bank();
        return bank.loadClientsFromFile();
    }
}
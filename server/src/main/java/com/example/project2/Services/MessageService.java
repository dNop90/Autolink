package com.example.project2.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Message;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Repositories.MessageRepository;

@Service
public class MessageService {
    private final KafkaService kafkaService;
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;


    public MessageService(MessageRepository messageRepository, KafkaService kafkaService, AccountRepository accountRepository)
    {
        this.messageRepository = messageRepository;
        this.kafkaService = kafkaService;
        this.accountRepository = accountRepository;
    }

    /**
     * When we create a new message we will confirm if the user IDs are correct.
     * Then we will save to the table and use kafka producer to send the message.
     * @param message The message we will be using for check for the user IDs and sending them.
     * @return True if success. otherwise, false
     */
    public boolean createMessage(Message message)
    {
        Long fromID = message.getFromAccountID();
        Long toID = message.getToAccountID();

        //Check if account ID exist
        Account fromAccount = accountRepository.findAccountByAccountId(fromID);
        Account toAccount = accountRepository.findAccountByAccountId(toID);
        if(fromAccount == null || toAccount == null)
        {
            return false;
        }

        //Add username to the Message
        message.setFromUsername(fromAccount.getUsername());
        message.setToUsername(toAccount.getUsername());

        //Save the message to the table
        Message newmessage = messageRepository.save(message);

        //Using kafka to send the message
        kafkaService.send(newmessage);

        return true;
    }

    /**
     * Get all old message related to the fromAccountID and toAccountID using specific offset for the limit
     * @param fromAccountID The message from Account ID
     * @param toAccountID The message to Account ID
     * @param offset The offset for the limit
     * @return A list of old messages
     */
    public List<Message> getOldMessages(Long fromAccountID, Long toAccountID, int offset)
    {
        return messageRepository.findAllByFromAccountIDAndToAccountID(fromAccountID, toAccountID, offset);
    }

    /**
     * Get unique account that the user has interact with
     * @param fromAccountID Account ID from the user
     * @return A list of messages with unique of account
     */
    public List<?> getUniqueAccountInteractWith(Long fromAccountID)
    {
        return messageRepository.findAllUniqueAccountUserInteractWith(fromAccountID);
    }
}

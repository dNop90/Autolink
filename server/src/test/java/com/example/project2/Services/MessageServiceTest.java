package com.example.project2.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.project2.Entities.Account;
import com.example.project2.Entities.Message;
import com.example.project2.Repositories.AccountRepository;
import com.example.project2.Repositories.MessageRepository;

public class MessageServiceTest {
    @Mock
    AccountRepository accountRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    KafkaService kafkaService;

    @InjectMocks
    MessageService messageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMessage_Success() throws Exception
    {
        Message message = new Message();
        message.setFromAccountID(Long.valueOf(6));
        message.setToAccountID(Long.valueOf(5));
        message.setMessage("Testing");

        when(accountRepository.findAccountByAccountId(anyLong())).thenReturn(new Account());

        Boolean result = messageService.createMessage(message);

        assertEquals(result, true);
    }

    @Test
    public void testCreateMessage_Failed() throws Exception
    {
        Message message = new Message();
        message.setFromAccountID(Long.valueOf(6));
        message.setToAccountID(Long.valueOf(999));
        message.setMessage("Testing");

        Boolean result = messageService.createMessage(message);

        assertEquals(result, false);
    }
}

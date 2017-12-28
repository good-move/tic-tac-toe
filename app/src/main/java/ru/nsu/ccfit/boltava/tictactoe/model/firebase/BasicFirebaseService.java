package ru.nsu.ccfit.boltava.tictactoe.model.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashSet;
import java.util.Map;

import ru.nsu.ccfit.boltava.tictactoe.model.message.GameMessageFactory;
import ru.nsu.ccfit.boltava.tictactoe.model.message.IGameMessageHandler;
import ru.nsu.ccfit.boltava.tictactoe.model.message.Message;

/**
 * Created by alexey on 07.12.17.
 */

public class BasicFirebaseService extends FirebaseMessagingService {

    static private HashSet<IGameMessageHandler> messageHandlers = new HashSet<>();
    static private final Object lock = new Object();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.err.println("Received new message");
        System.err.println("Printing values");
        for (String value: remoteMessage.getData().values()) {
            System.err.println(value);
        }

        synchronized (lock) {
            Map<String, String> messageData = remoteMessage.getData();
            Message message = GameMessageFactory.create(messageData);
            for (IGameMessageHandler handler : messageHandlers) {
                message.handleBy(handler);
            }
        }
    }

    public static void addMessageHandler(IGameMessageHandler handler) {
        synchronized (lock) {
            messageHandlers.add(handler);
        }
    }

    public static void removeMessageHandler(IGameMessageHandler handler) {
        synchronized (lock) {
            messageHandlers.remove(handler);
        }
    }

}

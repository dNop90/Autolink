import { useAuth } from "../contexts/AuthContext";
import { useCookie } from "../contexts/CookieContext";
import { User } from "../data/User";
import { api } from "./api";

const WEBSOCKET_HOST = process.env.REACT_APP_WEBSOCKET_HOST;

/**
 * This is a websocket class
 * It used in the message system
 */
class MessageManager{
    socket: any;
    user: any;
    cookie: any;

    init(user: any, cookie: any)
    {
        this.socket = (window as any).io.connect(WEBSOCKET_HOST, { transports: [ 'websocket' ] });
        this.user = user;
        this.cookie = cookie;
    }

    disconnect()
    {
        if(this.socket)
        {
            this.socket.close();
            this.socket = null;
        }
    }

    /**
     * Toggle the chat to open or close
     * @param event The event from the element
     */
    ToggleShowChat(event: any)
    {
        const current = event.currentTarget;
        let div = current.parentNode.querySelector("div");
        div.classList.toggle("active");

        if(div.classList.contains("active"))
        {
            let ul = div.getElementsByTagName("ul")[0];
            ul.scrollTop = ul.scrollHeight;
        }
    }

    /**
     * Create new chat if user doesn't exist
     * @param fromAccountID The account ID of that user you are going to send the chat message to
     * @param fromUsername The account username of that user you are going to send the chat message to
     * @returns The chat message that found or created
     */
    createMessageChat(fromAccountID: number, fromUsername: string)
    {
        let MessageChatList = document.querySelector("#MessageChatList");
        let findExist = MessageChatList?.querySelector(`div[data-message-id="${fromAccountID}"]`);
        if(findExist != null)
        {
            return findExist;
        }


        //Create new chatbox
        let NewMessageChat = document.createElement('div');
        NewMessageChat.classList.add("Message-Chat");
        NewMessageChat.setAttribute("data-message-id", String(fromAccountID));

        let span = document.createElement('span');
        span.onclick = this.ToggleShowChat;
        span.innerText = fromUsername;

        NewMessageChat.innerHTML = `<i class="bi bi-x-lg"></i><div><ul></ul><input type="text"></input></div>`;
        NewMessageChat.insertBefore(span, NewMessageChat.querySelector("div"));

        MessageChatList?.insertBefore(NewMessageChat, MessageChatList.firstChild);


        //Find and create if not exist in the message list
        let findExistInMessageList = document.querySelector(`#MessageList a[message-user-id="${fromAccountID}"]`);
        if(findExistInMessageList == null)
        {
            let li_a = document.createElement('li');
            let a = document.createElement('a');
            a.setAttribute("message-user-id", String(fromAccountID));
            a.innerText = fromUsername;
            a.onclick = this.OnUserSelectedFromMessageList;
            a.href = "#";
            li_a.appendChild(a);
            document.querySelector("#MessageList ul")?.appendChild(li_a);
        }

        return NewMessageChat;
    }

    /**
     * Add a message to a chat
     * @param fromAccountID The account ID to search for
     * @param message The message
     * @param self Check if the message is user himself/herself
     * @returns 
     */
    addMessageToChat(fromAccountID: number, message: string, self: boolean)
    {
        let MessageChatList = document.querySelector("#MessageChatList");
        let findExist = MessageChatList?.querySelector(`div[data-message-id="${fromAccountID}"]`);
        if(findExist == null)
        {
            return;
        }

        //Add message to the chatbox
        let ul = findExist?.querySelector("ul");
        let li = document.createElement('li');
        if(self)
        {
            li.classList.add("message-self");
        }
        li.innerText = message;

        ul?.appendChild(li);
    }

    /**
     * Open specific user chat
     * When user selected a user to chat with in the message list
     * @param event The event
     */
    OnUserSelectedFromMessageList(event: any)
    {
        event.preventDefault();
        let current = event.currentTarget;

        let user_id = current.getAttribute("message-user-id");
        messageManager.createMessageChat(user_id, current.innerText);
    }
}

export const messageManager = new MessageManager();
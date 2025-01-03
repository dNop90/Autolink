import React, { useEffect } from 'react'
import { useAuth } from '../../contexts/AuthContext';
import './MessageSystem.css'
import { socket } from '../../services/websocket';
import { api } from '../../services/api';
import { useCookie } from '../../contexts/CookieContext';


function MessageSystem() {
    const authContext = useAuth();
    const user = authContext.user;
    const cookie = useCookie().cookieData;
    let websocket = socket;
    
    useEffect(() => {
        websocket.init();
        websocket.socket.on("message", (data: any) =>
        {
            data = JSON.parse(JSON.stringify(data));

            let fromAccountID = data.fromAccountID;
            let fromUsername = data.fromUsername;
            let toAccountID = data.toAccountID;
            let toUsername = data.toUsername;
            let message = data.message;

            let MessageChat = undefined;
            if(fromAccountID == user?.userid)
            {
                MessageChat = createMessageChat(toAccountID, toUsername);
                addMessageToChat(toAccountID, message, true);
            }
            else if(toAccountID == user?.userid)
            {
                MessageChat = createMessageChat(fromAccountID, fromUsername);
                addMessageToChat(fromAccountID, message, false);
            }
        });
        
    }, [user]);

    /**
     * Toggle the chat to open or close
     * @param event The event from the element
     */
    function ToggleShowChat(event: any)
    {
        const current = event.currentTarget;
        let div = current.parentNode.querySelector("div");
        div.classList.toggle("active");
    }

    /**
     * Create new chat if user doesn't exist
     * @param fromAccountID The account ID of that user you are going to send the chat message to
     * @param fromUsername The account username of that user you are going to send the chat message to
     * @returns The chat message that found or created
     */
    function createMessageChat(fromAccountID: number, fromUsername: string)
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
        span.onclick = ToggleShowChat;
        span.innerText = fromUsername;

        NewMessageChat.innerHTML = `<div><ul></ul><input type="text"></input></div>`;
        NewMessageChat.insertBefore(span, NewMessageChat.querySelector("div"));

        MessageChatList?.insertBefore(NewMessageChat, MessageChatList.firstChild);


        //Add event for the input to send message
        NewMessageChat.querySelector("input")?.addEventListener("keyup", InputSendChat);


        //Find and create if not exist in the message list
        let findExistInMessageList = MessageChatList?.querySelector(`#MessageList a[message-user-id="${fromAccountID}"]`);
        if(findExistInMessageList == null)
        {
            let li_a = document.createElement('li');
            let a = document.createElement('a');
            a.setAttribute("message-user-id", String(fromAccountID));
            a.innerText = fromUsername;
            a.onclick = OnUserSelectedFromMessageList;
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
    function addMessageToChat(fromAccountID: number, message: string, self: boolean)
    {
        let MessageChatList = document.querySelector("#MessageChatList");
        let findExist = MessageChatList?.querySelector(`div[data-message-id="${fromAccountID}"]`);
        if(findExist == null)
        {
            return;
        }

        //Add message to the chatbox
        let ul = MessageChatList?.querySelector("ul");
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
    function OnUserSelectedFromMessageList(event: any)
    {
        event.preventDefault();
        let current = event.currentTarget;

        let user_id = current.getAttribute("message-user-id");
        createMessageChat(user_id, "Test2");
    }

    /**
     * Use for send chat message for the input tag
     * @param event The event of keyup
     */
    async function InputSendChat(event: any)
    {
        if(event.keyCode === 13)
        {
            let current = event.currentTarget;
            let value = current.value;

            console.log(value);
    
            let target_accountID = current.parentNode.parentNode.getAttribute("data-message-id");
            api.message.send(cookie.token, user?.userid ? user.userid : -1, target_accountID, value);
    
            current.value = "";
        }
    }

    function userContent()
    {
        return (
            <div className='MessageBox'>
                <div id="MessageChatList">
                    
                </div>
                <div id='MessageList'>
                    <span onClick={ToggleShowChat}>Chat</span>
                    <div>
                        <ul>
                            
                        </ul>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <>
            {
                !user ? <></> :
                userContent()
            }
        </>
    );
}

export default MessageSystem
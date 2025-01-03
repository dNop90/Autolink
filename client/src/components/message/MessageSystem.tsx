import React, { useEffect } from 'react'
import { useAuth } from '../../contexts/AuthContext';
import './MessageSystem.css'
import { messageManager } from '../../services/MessageManager';
import { api } from '../../services/api';
import { useCookie } from '../../contexts/CookieContext';


function MessageSystem() {
    const authContext = useAuth();
    const user = authContext.user;
    const cookie = useCookie().cookieData;
    
    useEffect(() => {
        if(user)
        {
            messageManager.init(user, cookie);
            messageManager.socket.on("message", (data: any) =>
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
                    MessageChat = messageManager.createMessageChat(toAccountID, toUsername);
                    messageManager.addMessageToChat(toAccountID, message, true);
                }
                else if(toAccountID == user?.userid)
                {
                    MessageChat = messageManager.createMessageChat(fromAccountID, fromUsername);
                    messageManager.addMessageToChat(fromAccountID, message, false);
                }
            });

            WatchForChatListChange();
        }
        else
        {
            messageManager.disconnect();
        }
    }, [user]);

    
    /**
     * Watch for the chat list change
     * @returns 
     */
    function WatchForChatListChange(){
        let MessageChatList = document.getElementById("MessageChatList");
        if(!MessageChatList) return;

        let observer = new window.MutationObserver((e) => {
            let div = (e[0].addedNodes[0]) as HTMLElement;
            let input = div.querySelector("input");
            if(input)
            {
                input.addEventListener("keyup", InputSendChat);
            }
        });

        observer.observe(MessageChatList, {
            childList: true,
        });
    }

    /**
     * Use for send chat message for the input tag
     * @param event The event of keyup
     */
    function InputSendChat(event: any)
    {
        if(event.keyCode === 13)
        {
            let current = event.currentTarget;
            let value = current.value;
    
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
                    <span onClick={messageManager.ToggleShowChat}>Chat</span>
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
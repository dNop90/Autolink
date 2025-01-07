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

                //Add to message chat if message chat already exist
                if(fromAccountID === user?.userid)
                {
                    messageManager.addMessageToChat(toAccountID, message, true);
                }
                else if(toAccountID === user?.userid)
                {
                    messageManager.addMessageToChat(fromAccountID, message, false);
                }

                //If message chat didn't exist then we will create one
                if(fromAccountID === user?.userid)
                {
                    messageManager.createMessageChat(toAccountID, toUsername);
                }
                else if(toAccountID === user?.userid)
                {
                    messageManager.createMessageChat(fromAccountID, fromUsername);
                }
            });

            WatchForChatListChange();
            FillChatUsers();
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

        //When it create a new chat
        let observer = new window.MutationObserver((e) => {
            if(!e[0].addedNodes.length)
                return;

            let div = (e[0].addedNodes[0]) as HTMLElement;
            let input = div.querySelector("input");
            if(input)
            {
                input.addEventListener("keyup", InputSendChat);
            }

            let btnclose = div.querySelector("i.bi-x-lg");
            if(btnclose)
            {
                btnclose.addEventListener("click", CloseButtonEvent);
            }

            FillChatMessage(div);
        });

        observer.observe(MessageChatList, {
            childList: true,
        });
    }

    /**
     * Fill the new chat message with old messages
     * @param elem The element of the chat
     */
    function FillChatMessage(elem: HTMLElement)
    {
        (async () => {
            try
            {
                let fromID = Number(elem.getAttribute("data-message-id"));
                let res = await api.message.getOldMessages(cookie.token, fromID);

                for(let i = 0; i < res.length; i++)
                {
                    let message = res[i];
                    if(message.fromAccountID === user?.userid)
                    {
                        messageManager.addMessageToChat(message.toAccountID, message.message, true);
                    }
                    else
                    {
                        messageManager.addMessageToChat(message.fromAccountID, message.message, false);
                    }
                }
            }
            catch(e)
            {
                
            }
        })();
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

    /**
     * Close the chat button event
     * @param event 
     */
    function CloseButtonEvent(event: any)
    {
        let current = event.currentTarget;
        let chatpanel = current.parentNode;
        let MessageChatList = document.getElementById("MessageChatList");

        //Remove the chat
        MessageChatList?.removeChild(chatpanel);
    }

    /**
     * Fill the chatbox with previous user
     */
    function FillChatUsers()
    {
        (async () => {
            let ul = document.querySelector("#MessageList ul");
            if(!ul)
            {
                return;
            }

            try
            {
                let res = await api.message.getUsers(cookie.token);

                for(let i = 0; i < res.length; i++)
                {
                    let fromID = res[i][0];
                    let fromUsername = res[i][1];
                    let toID = res[i][2];
                    let toUsername = res[i][3];

                    if(fromID === user?.userid)
                    {
                        let findExistInMessageList = document.querySelector(`#MessageList a[message-user-id="${toID}"]`);
                        if(findExistInMessageList) continue;

                        let li_a = document.createElement('li');
                        let a = document.createElement('a');
                        a.setAttribute("message-user-id", String(toID));
                        a.innerText = toUsername;
                        a.onclick = messageManager.OnUserSelectedFromMessageList;
                        a.href = "#";
                        li_a.appendChild(a);
                        ul.appendChild(li_a);
                    }
                    else
                    {
                        let findExistInMessageList = document.querySelector(`#MessageList a[message-user-id="${fromID}"]`);
                        if(findExistInMessageList) continue;

                        let li_a = document.createElement('li');
                        let a = document.createElement('a');
                        a.setAttribute("message-user-id", String(fromID));
                        a.innerText = fromUsername;
                        a.onclick = messageManager.OnUserSelectedFromMessageList;
                        a.href = "#";
                        li_a.appendChild(a);
                        ul.appendChild(li_a);
                    }
                }
            }
            catch (e)
            {
                
            }
        })();
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
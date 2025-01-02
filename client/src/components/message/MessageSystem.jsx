import React, { useEffect } from 'react'
import { useAuth } from '../../contexts/AuthContext';
import './MessageSystem.css'


function MessageSystem() {
    const authContext = useAuth();
    const user = authContext.user;

    function ToggleShowChat(event)
    {
        let div = document.querySelector("#MessageList > div");
        div.classList.toggle("active");
    }

    function userContent()
    {
        return (
            <div className='MessageBox'>
                <div id='MessageList' onClick={ToggleShowChat}>
                    <span>Chat</span>
                    <div>
                        <ul>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
                            <li><a href="#">test</a></li>
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
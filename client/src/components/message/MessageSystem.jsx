import React, { useEffect } from 'react'

const WEBSOCKET_HOST = process.env.REACT_APP_WEBSOCKET_HOST;

function MessageSystem() {

    useEffect(() => {
        const socket = window.io;
        console.log(WEBSOCKET_HOST);
        socket.connect(WEBSOCKET_HOST);
    }, []);

    return (
        <>
            <div>Message testtt</div>
        </>
    )
}

export default MessageSystem
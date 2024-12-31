const WEBSOCKET_HOST = process.env.REACT_APP_WEBSOCKET_HOST;

/**
 * This is a websocket class
 * It used in the message system
 */
class websocket{
    socket: any;

    init()
    {
        this.socket = (window as any).io;
        this.socket.connect(WEBSOCKET_HOST, { transports: [ 'websocket' ] });
    }
}

export const socket = new websocket();
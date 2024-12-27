import axios from "axios";

/**
 * Handle all API related to user
 */
export class api_user {
    api_link: string;

    constructor(api_link : string)
    {
        this.api_link = api_link;
    }

    /**
     * Check the user token if it validate or not
     * @param token The token to send to the server
     * @returns only the response status
     */
    async tokenValidation(token: string)
    {
        const response = await axios.post(`${this.api_link}/token`, {}, {headers:{'Authorization': token}});
        return response.data;
    }

    /**
     * Login user api
     * @param username The user username 
     * @param password The user password
     * @returns The promise of axios response
     */
    async loginUser(username: string, password: string){
        const response = await axios.post(`${this.api_link}/login`, { username, password });
        return response.data;
    };

    async registerUser(username: string, email: string, password: string) {
        const response = await axios.post(`${this.api_link}/register`, { username, email, password});
        return response.data;
    }
}
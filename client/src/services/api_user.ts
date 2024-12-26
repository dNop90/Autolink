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
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

    /**
     * Register user api
     * @param username 
     * @param email 
     * @param password 
     * @returns promise of axios response
     */
    async registerUser(username: string, email: string, password: string) {
        const response = await axios.post(`${this.api_link}/register`, { username, email, password});
        return response.data;
    }

    async searchUser(token: string, username: string) {
        const response = await axios({
            method: 'POST',
            url: `${this.api_link}/search`,
            data: username,
            headers: {
                'Authorization' : token,
                "Content-Type": "text/plain"
            }
        });
        return response.data;
    }

    async promoteUser(token: string, username: string) {
        const response = await axios({
            method: 'PATCH',
            url: `${this.api_link}/promote`,
            data: username,
            headers: {
                'Authorization' : token,
                "Content-Type": "text/plain"
            }
        })
        return response.data;
    }

    async suspendUser(token: string, username: string, status: boolean) {
        const response = await axios({
            method: 'PATCH',
            url: `${this.api_link}/suspend/${status}`,
            data: username,
            headers: {
                'Authorization' : token,
                "Content-Type": "text/plain"
            }
        })
        return response.data;
    }

    async fetchProfile(token: string, username: string) {
        const response = await axios({
            method: 'GET',
            url: `${this.api_link}/info/${username}`,
            headers: {
                'Authorization' : token
            }
        })
        return response.data;
    }

    async updateProfile(token: string, username: string, email: string, firstName: string, lastName: string, phone: string) {
        const response = await axios({
            method: 'PATCH',
            url: `${this.api_link}/profile`,
            data: {
                username: username,
                email: email,
                firstName: firstName,
                lastName: lastName,
                phone: phone
            },
            headers: {
                'Authorization': token
            }
        })
        return response.data;
    }

    async updatePassword(token: string, username: string, currentPassword: string, newPassword: string) {
        const response = await axios({
            method: 'PATCH',
            url: `${this.api_link}/password`,
            data: {
                username: username,
                currentPassword: currentPassword,
                newPassword: newPassword
            },
            headers: {
                'Authorization' : token
            }
        })
        return response.data;
    }
}
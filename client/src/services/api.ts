import { api_user } from './api_user';


const API_LINK_USER = process.env.REACT_APP_API_USER;

/**
 * Store all API endpoints and classes
 */
export const api = {
  user : new api_user(API_LINK_USER as string)
} as const;


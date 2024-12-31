import { api_application } from './api_application';
import { api_user } from './api_user';


const API_LINK_USER = process.env.REACT_APP_API_USER;
const API_LINK_APPLICATION = process.env.REACT_APP_API_APPLICATION;
/**
 * Store all API endpoints and classes
 */
export const api = {
  user : new api_user(API_LINK_USER as string),
  application : new api_application(API_LINK_APPLICATION as string)
} as const;


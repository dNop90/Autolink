import { api_application } from './api_application';
import { api_message } from './api_message';
import { api_user } from './api_user';


const API_LINK_USER = process.env.REACT_APP_API_USER;
const API_LINK_APPLICATION = process.env.REACT_APP_API_APPLICATION;
const API_LINK_MESSAGE = process.env.REACT_APP_API_MESSAGE;

/**
 * Store all API endpoints and classes
 */
export const api = {
  user : new api_user(API_LINK_USER as string),
  application : new api_application(API_LINK_APPLICATION as string),
  message : new api_message(API_LINK_MESSAGE as string)
} as const;


import { api_user } from './api_user';
import { api_vehicle } from './api_vehicle';

const API_LINK_USER = process.env.REACT_APP_API_USER;
const API_LINK_VEHICLE = process.env.REACT_APP_API_VEHICLE;

/**
 * Store all API endpoints and classes
 */
export const api = {
  user : new api_user(API_LINK_USER as string),
  vehicle : new api_vehicle(API_LINK_VEHICLE as string)
} as const;


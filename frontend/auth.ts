import { configureAuth } from '@hilla/react-auth';
import {RegisteredUserEndpoint} from "Frontend/generated/endpoints";

// Configure auth to use `UserInfoService.getUserInfo`
const auth = configureAuth(RegisteredUserEndpoint.getUserInfo);

// Export auth provider and useAuth hook, which are automatically
// typed to the result of `UserInfoService.getUserInfo`
export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;

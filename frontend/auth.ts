import { configureAuth } from '@hilla/react-auth';
import {RegisteredUserEndpoint} from "Frontend/generated/endpoints";
import UserInfo from "Frontend/generated/com/cocroachden/planner/user/endpoint/UserInfo";

// Configure auth to use `UserInfoService.getUserInfo`
const auth = configureAuth(RegisteredUserEndpoint.getUserInfo, {
    getRoles(user: UserInfo): readonly string[] {
        return user.authorities
    }
});

// Export auth provider and useAuth hook, which are automatically
// typed to the result of `UserInfoService.getUserInfo`
export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;

import {useAuth} from "Frontend/auth";
import {Navigate} from "react-router-dom";
import {ReactNode} from "react";

export function ProtectedRoute({ element }: { element : ReactNode }) {
    const auth = useAuth()
    return auth.state.user ? element : <Navigate to={"/login"}/>
}

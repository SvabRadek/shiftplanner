import {RouterProvider} from 'react-router-dom';
import {
    ScheduleValidationCtxProvider
} from "Frontend/components/validation/ScheduleValidationCtxProvider";
import {AuthProvider} from "Frontend/auth";
import routes from "Frontend/routes";

export default function App() {
    return (
        <AuthProvider>
            <ScheduleValidationCtxProvider>
                <RouterProvider router={routes}/>
            </ScheduleValidationCtxProvider>
        </AuthProvider>
    )
}

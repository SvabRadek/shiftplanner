import router from 'Frontend/routes.js';
import {RouterProvider} from 'react-router-dom';
import {
    ScheduleValidationCtxProvider
} from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";
import {AuthProvider} from "Frontend/auth";

export default function App() {
    return (
        <AuthProvider>
            <ScheduleValidationCtxProvider>
                <RouterProvider router={router}/>
            </ScheduleValidationCtxProvider>
        </AuthProvider>
    )
}

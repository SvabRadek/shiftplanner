import MainLayout from 'Frontend/views/MainLayout.js';
import {createBrowserRouter, RouteObject} from 'react-router-dom';
import {ScheduleModeProvider} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import LoginView from "Frontend/views/login/LoginView";
import EmployeeView from "Frontend/views/employees/EmployeeView";
import {ScheduleView2} from "Frontend/views/schedule/ScheduleView2";
import {TestView} from "Frontend/views/test/TestView";
import {protectRoutes} from "@hilla/react-auth";
import {ProtectedRoute} from "Frontend/components/security/ProtectedRoute";

export const routes: RouteObject[] = protectRoutes([
    {
        element: <MainLayout/>,
        handle: {title: 'Planner'},
        children: [
            {
                path: '/',
                element: <ScheduleModeProvider><ProtectedRoute element={<ScheduleView2/>}/></ScheduleModeProvider>,
                handle: {title: 'Rozvrh', requiresLogin: true}
            },
            {
                path: '/zamestnanci',
                element: <ProtectedRoute element={<EmployeeView/>}/>,
                handle: {title: 'Zaměstnanci', requiresLogin: true}
            },
            {
                path: '/test',
                element: <TestView/>,
                handle: {title: 'Test'},
            }
        ],
    },
    {
        path: '/login', element: <LoginView/>,
    }
]);

export default createBrowserRouter(routes);

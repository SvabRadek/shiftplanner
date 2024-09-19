import EmployeeView from 'Frontend/views/employees/EmployeeView';
import MainLayout from 'Frontend/views/MainLayout.js';
import {createBrowserRouter, RouteObject} from 'react-router-dom';
import ScheduleView from "Frontend/views/schedule/ScheduleView";
import {ScheduleModeProvider} from "Frontend/views/schedule/ScheduleModeCtxProvider";
import LoginView from "Frontend/views/LoginView";

export const routes = [
    {
        element: <MainLayout/>,
        handle: {title: 'Planner'},
        children: [
            {
                path: '/',
                element: <ScheduleModeProvider><ScheduleView/></ScheduleModeProvider>,
                handle: {title: 'Rozvrh'}
            },
            {
                path: '/zamestnanci',
                element: <EmployeeView/>,
                handle: {title: 'Zaměstnanci'}
            }
        ],
    },
    {
        path: '/login', element: <LoginView/>,
    }

] as RouteObject[];

export default createBrowserRouter(routes);

import EmployeeView from 'Frontend/views/employees/EmployeeView';
import MainLayout from 'Frontend/views/MainLayout.js';
import { createBrowserRouter, RouteObject } from 'react-router-dom';
import ScheduleView from "Frontend/views/schedule/ScheduleView";
import { ScheduleModeProvider } from "Frontend/views/schedule/ScheduleModeCtxProvider";

export const routes = [
  {
    element: <MainLayout/>,
    handle: { title: 'Hilla CRM' },
    children: [
      {
        path: '/',
        element: <ScheduleModeProvider><ScheduleView/></ScheduleModeProvider>,
        handle: { title: 'Rozvrh' }
      },
      { path: '/zamestnanci', element: <EmployeeView/>, handle: { title: 'Zaměstnanci' } }
    ],
  },
] as RouteObject[];

export default createBrowserRouter(routes);

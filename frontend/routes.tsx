import EmployeeView from 'Frontend/views/contacts/EmployeeView';
import MainLayout from 'Frontend/views/MainLayout.js';
import { createBrowserRouter, RouteObject } from 'react-router-dom';
import { HomeView } from "Frontend/views/home/HomeView";
import ScheduleView from "Frontend/views/about/ScheduleView";

export const routes = [
  {
    element: <MainLayout />,
    handle: { title: 'Hilla CRM' },
    children: [
      { path: '/', element: <HomeView />, handle: { title: 'Domu' } },
      { path: '/zamestnanci', element: <EmployeeView />, handle: { title: 'Zamestnanci' } },
      { path: '/rozvrh', element: <ScheduleView />, handle: { title: 'Rozvrh' } },
    ],
  },
] as RouteObject[];

export default createBrowserRouter(routes);

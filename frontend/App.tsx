import router from 'Frontend/routes.js';
import { RouterProvider } from 'react-router-dom';
import {
  ScheduleValidationCtxProvider
} from "Frontend/views/schedule/components/validation/ScheduleValidationCtxProvider";

export default function App() {
  return (
    <ScheduleValidationCtxProvider>
      <RouterProvider router={router}/>
    </ScheduleValidationCtxProvider>
  )
}

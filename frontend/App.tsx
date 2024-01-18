import router from 'Frontend/routes.js';
import { RouterProvider } from 'react-router-dom';
import { RequestCtxProvider } from "Frontend/views/schedule/components/schedulegrid/RequestCtxProvider";

export default function App() {
  return (
    <RequestCtxProvider>
      <RouterProvider router={router}/>
    </RequestCtxProvider>
  )
}

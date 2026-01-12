import { userApi } from '@/api/userApi';
import { UseAppContext } from '@/ContextProvider';
import { useQuery } from '@tanstack/react-query'
import { createFileRoute, Navigate } from '@tanstack/react-router'

export const Route = createFileRoute('/')({
  component: App,
})

function App() {
 
  const {jwt} = UseAppContext();
  if(jwt.trim() && jwt != 'init')
  return <Navigate to='/home' />
  if(!jwt.trim())
    return <Navigate to='/login'/>
  return <div className="loading">loading...</div>

}

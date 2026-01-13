import '../styles/index.scss';
import { Navigate, createFileRoute } from '@tanstack/react-router'
import { UseAppContext } from '@/ContextProvider';

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

import { userApi } from '@/api/userApi'
import { useMutation } from '@tanstack/react-query'
import { createFileRoute } from '@tanstack/react-router'
import React from 'react';

export const Route = createFileRoute('/login')({
  component: RouteComponent,
})

function RouteComponent() {
    const {mutate} = useMutation({
        mutationFn: userApi.login,
        onSuccess:(res)=>{
            console.log(res);
        },
        onError:erro=>{
            console.error(erro);
        }
    });
    const [data, setData] = React.useState<LoginDto>({username:'', password:''});
    const sumbit = (e:React.FormEvent<HTMLFormElement>) =>{
        e.preventDefault();
        mutate(data);
    }
  return (
    <form className="login" onSubmit={sumbit}>
        <h2>Login</h2>
        <input
         type="text"
         placeholder='Username'
         value={data.username}
         onChange={e => setData(d => ({...d, username:e.target.value}))}
        />
        <input 
        type="text" 
        placeholder='Password'
        value={data.password}
        onChange={e => setData(d => ({...d, password:e.target.value}))}
        />
        <button type='submit'>Entrar</button>
    </form>
  )
}

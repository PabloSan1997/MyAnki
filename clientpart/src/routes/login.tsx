import { userApi } from '@/api/userApi'
import { UseAppContext } from '@/ContextProvider';
import { useMutation } from '@tanstack/react-query'
import { createFileRoute, Navigate } from '@tanstack/react-router'
import React from 'react';

export const Route = createFileRoute('/login')({
    component: RouteComponent,
})

function RouteComponent() {
    const { jwt, setJwt } = UseAppContext();
    const [mymessage, setMymessage] = React.useState('');
    const { mutate } = useMutation({
        mutationFn: userApi.login,
        onSuccess: (res) => {
            setJwt(res.jwt);
            setMymessage('');
        },
        onError: erro => {
            setMymessage(erro.message);
        }
    });
    const [data, setData] = React.useState<LoginDto>({ username: '', password: '' });
    const sumbit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        mutate(data);

    }
    if (jwt.trim() && jwt != 'init')
        return <Navigate to='/home' />
    return (
        <form className="login" onSubmit={sumbit}>
            <h2>Login</h2>
            <input
                type="text"
                placeholder='Username'
                value={data.username}
                onChange={e => setData(d => ({ ...d, username: e.target.value }))}
            />
            <input
                type="text"
                placeholder='Password'
                value={data.password}
                onChange={e => setData(d => ({ ...d, password: e.target.value }))}
            />
            <button type='submit'>Entrar</button>
            <p className="error">{mymessage}</p>
        </form>
    )
}

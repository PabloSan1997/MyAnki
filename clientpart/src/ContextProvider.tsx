import { useMutation, useQuery } from '@tanstack/react-query';
import React from 'react'
import { userApi } from './api/userApi';


const AppContext = React.createContext({jwt:''});

export function ContextProvider({ children }: { children: React.ReactNode }) {
    const [jwt, setJwt] = React.useState('init');
    const { mutate } = useMutation({
        mutationFn: userApi.refresh,
        onSuccess: res => {
            setJwt(res.jwt)
        },
        onError: () => {
            setJwt('');
        }
    });
    React.useEffect(() => {
        mutate();
    }, []);
    return (
        <AppContext.Provider value={{ jwt }}>
            {children}
        </AppContext.Provider>
    )
}



export const UseAppContext = () => React.useContext(AppContext);

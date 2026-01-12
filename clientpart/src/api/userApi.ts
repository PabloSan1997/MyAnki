import { apiProps } from "./apiProps"


async function viewRefresh(): Promise<string> {
    try {
        const d = await userApi.refresh();
        return d.jwt;
    } catch (error) {
        return '';
    }
}
export const userApi = {
    async login(data: LoginDto): Promise<{ jwt: string }> {
        const ft = await fetch(`${apiProps.urlbase}/user/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify(data)
        });
        if (!ft.ok) {
            const errordto = await ft.json() as ErrorDto;
            throw new Error(errordto.message)
        }
        return ft.json();
    },
    async refresh(): Promise<{ jwt: string }> {
        const ft = await fetch(`${apiProps.urlbase}/user/refresh`, {
            method: 'POST',
            credentials: 'include',
        });
        if (!ft.ok) {
            const errordto = await ft.json() as ErrorDto;
            throw new Error(errordto.message)
        }
        return ft.json();
    },
    async logout() {

        const ft = await fetch(`${apiProps.urlbase}/user/logout`, {
            method: 'POST',
            credentials: 'include'
        });
        if (ft.status == 401 || ft.ok)
            return;
        throw new Error('error to connect');
    },

    async theRefresh<T, C>(cb: (d: C) => Promise<T>, c: C, setJwt: (jwt: string) => void): Promise<T> {
        try {
            return await cb(c);
        } catch (error) {
            const refresh = await viewRefresh();
            if (!refresh.trim() && refresh != 'refresh') {
                setJwt('');
                throw new Error('Error a obtener datos');
            }
            console.log('refresh');
            setJwt(refresh);
            if (typeof c == 'string')
                return await cb(refresh as C)
            if (typeof c == 'object')
                return await cb({ ...c, jwt: refresh });
            return cb(c);
        }
    }
}
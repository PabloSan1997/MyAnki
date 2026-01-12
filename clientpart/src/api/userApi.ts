import { apiProps } from "./apiProps"



export const userApi = {
    async login(data:LoginDto):Promise<{jwt:string}>{
        const ft = await fetch(`${apiProps.urlbase}/user/login`, {
            method:'POST',
            headers:{
                'Content-Type':'application/json'
            },
            credentials:'include',
            body:JSON.stringify(data)
        });
        if(!ft.ok) {
            const errordto = await ft.json() as ErrorDto;
            throw new Error(errordto.message)
        }
        return ft.json();
    },
    async refresh():Promise<{jwt:string}>{
        const ft = await fetch(`${apiProps.urlbase}/user/refresh`, {
            method:'POST',
            credentials:'include',
        });
        if(!ft.ok) {
            const errordto = await ft.json() as ErrorDto;
            throw new Error(errordto.message)
        }
        return ft.json();
    }
}
import { apiProps } from "./apiProps";
import { userApi } from "./userApi";


const refreshtoken = userApi.refresh;

export const noteApi = {
    async findAll(jwt:string){
        const ft = await fetch(`${apiProps.urlbase}/notes`, {
            method:'GET',
            headers:{
                'Authorization':`Bearer ${jwt}`
            }
        });
        if(!ft.ok) throw new Error('Error al cargar datos');
        return ft.json();
    },
    async countData(jwt:string):Promise<NotesSummary>{
         const ft = await fetch(`${apiProps.urlbase}/notes/count`, {
            method:'GET',
            headers:{
                'Authorization':`Bearer ${jwt}`
            }
        });
        if(!ft.ok) throw new Error('Error al cargar datos');
        return ft.json();
    }
}
import { apiProps } from "./apiProps";


export const noteApi = {
    async findAll(jwt: string): Promise<Array<IFlashcard>> {
        const ft = await fetch(`${apiProps.urlbase}/notes`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwt}`
            }
        });
        if (!ft.ok) {
            const errorDto = await ft.json() as ErrorDto;
            if (errorDto.statusCode == 401 && errorDto.message == 'refresh')
                throw Error(errorDto.message);
        };
        return ft.json();
    },
    async countData(jwt: string): Promise<NotesSummary> {
        const ft = await fetch(`${apiProps.urlbase}/notes/count`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwt}`
            }
        });
        if (!ft.ok) {
            const errorDto = await ft.json() as ErrorDto;
            if (errorDto.statusCode == 401 && errorDto.message == 'refresh')
                throw Error(errorDto.message);
            throw new Error('Error al cargar datos');
        };
        return ft.json();
    },
    async update({ jwt, data }: { jwt: string, data: OptionDto }): Promise<IFlashcard> {
        const ft = await fetch(`${apiProps.urlbase}/notes`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(data)
        });
        if (!ft.ok) {
            const er = await ft.json() as ErrorDto;
            throw new Error(er.message)
        }
        return ft.json();
    },
    async save({ jwt, data }: { jwt: string, data: IFlashcardDto }): Promise<IFlashcard> {
        const ft = await fetch(`${apiProps.urlbase}/notes`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(data)
        });
        if (!ft.ok) {
            const er = await ft.json() as ErrorDto;
            throw new Error(er.message)
        }
        return ft.json();
    },
    async deleteById(data:{id:number, jwt:string}):Promise<void>{
         const ft = await fetch(`${apiProps.urlbase}/notes/${data.id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${data.jwt}`
            }
        });
        if (!ft.ok) {
            const errorDto = await ft.json() as ErrorDto;
            if (errorDto.statusCode == 401 && errorDto.message == 'refresh')
                throw Error(errorDto.message);
            
        };
        
    }
}

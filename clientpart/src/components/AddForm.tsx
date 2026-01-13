import React from "react";
import '../styles/addform.scss';

interface IAddForm {
    setShow: (a: boolean) => void;
    save: (a: IFlashcardDto) => void;
}

const initstate: IFlashcardDto = {
    front: "",
    back: "",
    note: ""
}

export function AddForm({ save, setShow }: IAddForm) {

    const [data, setData] = React.useState<IFlashcardDto>(initstate);

    const sub = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        save(data);
        setData(initstate);
    }
    return (
        <form className="addform" onSubmit={sub}>
            <h2>Agrega nota</h2>
            <label htmlFor="">Front</label>
            <input
                type="text"
                value={data.front}
                onChange={d => setData({ ...data, front: d.target.value })}
            />
            <label htmlFor="">Back</label>
            <input
                type="text"
                value={data.back}
                onChange={d => setData({ ...data, back: d.target.value })}
            />
            <label htmlFor="">Note</label>
            <input
                type="text"
                value={data.note}
                onChange={d => setData({ ...data, note: d.target.value })}
            />
            <div className="areabutton">
                <button type="submit">Agregar</button>
                <button type="button" onClick={() => setShow(false)}>Cancelar</button>
            </div>
        </form>
    );
}




interface IReviewNotes {
  notes: Array<IFlashcard>,
  updateNote: (data: OptionDto) => void;
  show: boolean,
  setShow: (a: boolean) => void;
  deleteById: (id: number) => void;
}

export function ReviewNotes({ notes, updateNote, show, setShow, deleteById }: IReviewNotes) {
  const length = notes.length;
  const note = length > 1 ? notes[length - 1] : notes[0];
  const deleteByIdView = () => {
    if (confirm("¿Seguro que deseas eliminar nota?"))
      deleteById(note.id);
  }
  if (length == 0)
    return <div className="areanotes"><p className="message">No se encontraron notas para repasar</p></div>
  return (
    <div className="areanotes">
      <span>{note.front}</span>
      {!show ? (
        <div className="areabutton"><button className="show" onClick={() => setShow(true)}>Mostrar</button></div>
      ) : (
        <>
          <span>{note.back}</span>
          <span>{note.note}</span>
          <div className="areabutton areasend">
            <button
              className="send"
              onClick={() => updateNote({ idnote: note.id, option: 'BIEN' })}
            >Bien</button>
            <button
              className="send"
              onClick={() => updateNote({ idnote: note.id, option: 'DIFICIL' })}
            >Dificil</button>
            <button className="send" onClick={deleteByIdView}>Eliminar</button>
          </div>
        </>
      )}
    </div>
  )
}

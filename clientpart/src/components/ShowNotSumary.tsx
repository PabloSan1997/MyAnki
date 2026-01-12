


export function ShowNotSumary({newnotes, goneovernotes, graduatednotes}:NotesSummary) {
  return (
    <div className="shownotsumary">
        <div className="fila"><span>Nuevas</span><span>Repasar</span><span>Programadas</span></div>
        <div className="fila"><span>{newnotes}</span><span>{goneovernotes}</span><span>{graduatednotes}</span></div>
    </div>
  )
}

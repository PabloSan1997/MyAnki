

export  function ReviewNotes({notes}:{notes:IFlashcard[]}) {
  return (
    <div className="notes">
        {JSON.stringify(notes)}
    </div>
  )
}

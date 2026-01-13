import { useMutation, useQuery } from '@tanstack/react-query';
import { Navigate, createFileRoute } from '@tanstack/react-router'
import React from 'react';
import { noteApi } from '@/api/noteApi';
import { userApi } from '@/api/userApi';
import { AddForm } from '@/components/AddForm';
import { MyHeader } from '@/components/MyHeader';
import { ReviewNotes } from '@/components/ReviewNotes';
import { ShowNotSumary } from '@/components/ShowNotSumary';
import { UseAppContext } from '@/ContextProvider'
import '../styles/home.scss';

export const Route = createFileRoute('/home')({
  component: RouteComponent,
})


function RouteComponent() {
  const { jwt, setJwt } = UseAppContext();
  const [show, setShow] = React.useState(false);
  const [showform, setShowform] = React.useState(false);
  const [messageform, setMessageform] = React.useState('');
  const { data: datacount, refetch: refrescount } = useQuery({
    queryKey: ['count', jwt],
    queryFn: () => userApi.theRefresh(noteApi.countData, jwt, setJwt),
    retry: 1,
    initialData: { newnotes: 0, graduatednotes: 0, goneovernotes: 0 },
    enabled: !!jwt.trim() && jwt != 'init'
  });
  const { data: datanotes, refetch: refreshnote } = useQuery({
    queryKey: ['notes', jwt],
    queryFn: async () => userApi.theRefresh(noteApi.findAll, jwt, setJwt),
    retry: 1,
    initialData: [],
    enabled: !!jwt.trim() && jwt != 'init'
  });
  const { mutate: updateData, isPending: isPendingUpdate } = useMutation({
    mutationFn: (data: { jwt: string, data: OptionDto }) => userApi.theRefresh(noteApi.update, data, setJwt),
    onSuccess: () => {
      refrescount();
      refreshnote();
      setShow(false);
    }
  });
  const { mutate: mutatesave } = useMutation({
    mutationFn: (data: { jwt: string, data: IFlashcardDto }) => userApi.theRefresh(noteApi.save, data, setJwt),
    onSuccess: () => {
      refrescount();
      refreshnote();
      setMessageform('');
    },
    onError:(err)=>{
      setMessageform(err.message);
    }
  });
  const { mutate: mutatedelete } = useMutation({
    mutationFn: (data: { jwt: string, id: number }) => userApi.theRefresh(noteApi.deleteById, data, setJwt),
    onSuccess: () => {
      refrescount();
      refreshnote();
    }
  });
  if (!jwt.trim())
    return <Navigate to='/login' />
  return (
    <>
      <MyHeader />
      <main className="container">
        <ShowNotSumary {...datacount} />
        {!isPendingUpdate ? <ReviewNotes
          notes={datanotes}
          updateNote={(d: OptionDto) => { updateData({ jwt, data: d }); }}
          show={show}
          setShow={setShow}
          deleteById={(d: number) => mutatedelete({ id: d, jwt })}
        />: <div className='loading'></div>}
      </main>
      {showform && <AddForm message={messageform} setMessage={setMessageform} setShow={setShowform} save={(d: IFlashcardDto) => mutatesave({ jwt, data: d })} />}
      <button className='showbutton' onClick={() => setShowform(true)}>+</button>
    </>
  );
}

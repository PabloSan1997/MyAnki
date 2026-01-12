import { noteApi } from '@/api/noteApi';
import { MyHeader } from '@/components/MyHeader';
import { ReviewNotes } from '@/components/ReviewNotes';
import { ShowNotSumary } from '@/components/ShowNotSumary';
import { UseAppContext } from '@/ContextProvider'
import { useQuery } from '@tanstack/react-query';
import { createFileRoute, Navigate } from '@tanstack/react-router'

export const Route = createFileRoute('/home')({
  component: RouteComponent,
})

function RouteComponent() {
  const { jwt } = UseAppContext();

  const { data: datacount, refetch: refrescount } = useQuery({
    queryKey: ['count', jwt],
    queryFn: () => noteApi.countData(jwt),
    retry: 2,
    initialData: { newnotes: 0, graduatednotes: 0, goneovernotes: 0 },
    enabled: !!jwt.trim() && jwt != 'init'
  });
  const {data:datanotes} = useQuery({
    queryKey:['notes', jwt],
    queryFn: () => noteApi.findAll(jwt),
    retry: 2,
    initialData: [],
    enabled: !!jwt.trim() && jwt != 'init'
  });

  if (!jwt.trim())
    return <Navigate to='/login' />
  return (
    <>
      <MyHeader />
      <main className="container">
        <ShowNotSumary {...datacount} />
        <ReviewNotes notes={datanotes}/>
      </main>
    </>
  );
}

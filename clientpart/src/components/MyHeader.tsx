
import { userApi } from "@/api/userApi";
import { UseAppContext } from "@/ContextProvider";
import { useMutation } from "@tanstack/react-query"



export function MyHeader() {
    const { setJwt } = UseAppContext();
    const { mutate } = useMutation({
        mutationFn: userApi.logout,
        onSuccess: () => {
            setJwt('');
        },
        onError: err => {
            alert(err.message);
            setJwt('');
        }
    });

    return (
        <header>
            <h1>Tarjetas</h1>
            <button onClick={() => mutate()}>Log out</button>
        </header>
    )
}

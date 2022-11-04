import {useEffect} from "react";
import {useAuth} from "../context/authProvider";
import {Navigate} from "react-router-dom";

export default function LogoutRoute() {

    const authentication = useAuth();
    useEffect(() => {
        authentication.onLogout();
    }, [authentication]);

    return <Navigate to="/"/>;
}

import {Navigate} from "react-router-dom";
import {useAuth} from "../context/useAuth";

interface PrivateRouteProps {
    children: JSX.Element;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({children}) => {
    const {token} = useAuth();
    return token ? children : <Navigate to="/login" replace/>;
};

export default PrivateRoute;

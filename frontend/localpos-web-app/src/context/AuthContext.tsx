import React, {createContext, ReactNode, useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";

interface JwtPayload {
    sub: string;
    roles: string[];
}

interface AuthContextType {
    token: string | null;
    user: JwtPayload | null;
    login: (token: string) => void;
    logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({
    token: null,
    user: null,
    login: () => {
    },
    logout: () => {
    },
});

interface AuthProviderProps {
    children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [token, setToken] = useState<string | null>(
        sessionStorage.getItem("jwt_token")
    );
    const [user, setUser] = useState<JwtPayload | null>(null);

    useEffect(() => {
        if (token) {
            try {
                const decoded = jwtDecode<JwtPayload>(token);
                setUser(decoded);
                sessionStorage.setItem("jwt_token", token);
            } catch (error) {
                setUser(null);
                sessionStorage.removeItem("jwt_token");
                setToken(null);
            }
        } else {
            setUser(null);
            sessionStorage.removeItem("jwt_token");
        }
    }, [token]);

    const login = (newToken: string) => {
        setToken(newToken);
    };

    const logout = () => {
        setToken(null);
    };

    return (
        <AuthContext.Provider value={{token, user, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

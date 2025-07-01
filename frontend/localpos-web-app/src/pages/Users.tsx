import React, {useEffect, useState} from "react";
import {
    Box,
    Button,
    CircularProgress,
    IconButton,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import AddIcon from "@mui/icons-material/Add";
import {useTranslation} from "react-i18next";
import axios from "axios";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import UserFormDialog from "../components/UserFormDialog";

const MySwal = withReactContent(Swal);

interface User {
    id: string;
    username: string;
    email: string;
    roles: { name: string }[];
    stores: { name: string }[];
    createdAt: string;
}

const Users: React.FC = () => {
    const {t} = useTranslation("users");
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [formDialog, setFormDialog] = useState<{ open: boolean; userToEdit: User | null }>({open: false, userToEdit: null});
    const apiUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";
    const token = sessionStorage.getItem("jwt_token");

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const response = await axios.get<User[]>(`${apiUrl}/api/v1/users`, {
                headers: {Authorization: `Bearer ${token}`},
            });
            setUsers(response.data);
        } catch {
            MySwal.fire({
                icon: "error",
                title: t("messages.errorLoading"),
            });
        } finally {
            setLoading(false);
        }
    };

    const deleteUser = async (id: string) => {
        const result = await MySwal.fire({
            title: t("dialogs.confirmDeleteTitle"),
            text: t("dialogs.confirmDeleteMessage"),
            icon: "warning",
            showCancelButton: true,
            confirmButtonText: t("buttons.delete"),
            cancelButtonText: t("buttons.cancel"),
        });
        if (result.isConfirmed) {
            try {
                await axios.delete(`${apiUrl}/api/v1/users/${id}`, {
                    headers: {Authorization: `Bearer ${token}`},
                });
                setUsers((prev) => prev.filter((u) => u.id !== id));
                MySwal.fire({icon: "success", title: t("messages.userDeleted")});
            } catch {
                MySwal.fire({icon: "error", title: t("messages.errorDeleting")});
            }
        }
    };

    const handleSaveUser = (savedUser: User) => {
        setUsers((prev) => {
            const exists = prev.find((u) => u.id === savedUser.id);
            if (exists) {
                return prev.map((u) => (u.id === savedUser.id ? savedUser : u));
            }
            return [...prev, savedUser];
        });
        MySwal.fire({icon: "success", title: savedUser.id ? t("messages.userUpdated") : t("messages.userCreated")});
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    return (
        <Box p={4}>
            <Typography variant="h5" mb={2}>{t("title")}</Typography>
            <Box mb={2} display="flex" justifyContent="flex-end">
                <Button variant="contained" startIcon={<AddIcon/>} onClick={() => setFormDialog({open: true, userToEdit: null})}>
                    {t("buttons.create")}
                </Button>
            </Box>
            {loading ? (
                <CircularProgress/>
            ) : (
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>{t("fields.username")}</TableCell>
                            <TableCell>{t("fields.email")}</TableCell>
                            <TableCell>{t("fields.roles")}</TableCell>
                            <TableCell>{t("fields.stores")}</TableCell>
                            <TableCell>{t("fields.actions")}</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users.map((user) => (
                            <TableRow key={user.id}>
                                <TableCell>{user.username}</TableCell>
                                <TableCell>{user.email}</TableCell>
                                <TableCell>{user.roles.map((r) => r.name).join(", ")}</TableCell>
                                <TableCell>{user.stores.map((s) => s.name).join(", ")}</TableCell>
                                <TableCell>
                                    <IconButton onClick={() => setFormDialog({open: true, userToEdit: user})}><EditIcon/></IconButton>
                                    <IconButton onClick={() => deleteUser(user.id)}><DeleteIcon/></IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            )}
            <UserFormDialog
                open={formDialog.open}
                onClose={() => setFormDialog({open: false, userToEdit: null})}
                onSaved={handleSaveUser}
                userToEdit={formDialog.userToEdit}
            />
        </Box>
    );
};

export default Users;

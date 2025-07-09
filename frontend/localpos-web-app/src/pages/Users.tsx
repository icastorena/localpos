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
import UserFormDialog from "../components/UserFormDialog";
import DarkSwal from "../utils/darkSwal.ts";

interface User {
    id: string;
    username: string;
    email: string;
    firstName: string;
    lastName: string;
    phone?: string;
    address?: string;
    isActive: boolean;
    roles: { name: string }[];
    stores: { name: string }[];
    createdAt: string;
    updatedAt: string;
}

const Users: React.FC = () => {
    const {t} = useTranslation("users");
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [formDialog, setFormDialog] = useState<{
        open: boolean;
        userToEdit: User | null;
    }>({
        open: false,
        userToEdit: null,
    });

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
            DarkSwal.fire({
                icon: "error",
                title: t("messages.errorLoading"),
            });
        } finally {
            setLoading(false);
        }
    };

    const deleteUser = async (id: string) => {
        const result = await DarkSwal.fire({
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
                DarkSwal.fire({icon: "success", title: t("messages.userDeleted")});
            } catch {
                DarkSwal.fire({icon: "error", title: t("messages.errorDeleting")});
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
        DarkSwal.fire({
            icon: "success",
            title: savedUser.id
                ? t("messages.userUpdated")
                : t("messages.userCreated"),
        });
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    return (
        <Box p={4}>
            <Typography variant="h5" mb={3} fontWeight="bold" color="text.primary">
                {t("title")}
            </Typography>

            <Box mb={3} display="flex" justifyContent="flex-end">
                <Button
                    variant="contained"
                    startIcon={<AddIcon/>}
                    onClick={() => setFormDialog({open: true, userToEdit: null})}
                    sx={{
                        borderRadius: 2,
                        textTransform: "none",
                        fontWeight: 500,
                    }}
                >
                    {t("buttons.create")}
                </Button>
            </Box>

            {loading ? (
                <Box display="flex" justifyContent="center" mt={6}>
                    <CircularProgress/>
                </Box>
            ) : (
                <Box
                    sx={{
                        overflowX: "auto",
                        borderRadius: 3,
                        boxShadow: 2,
                        bgcolor: "background.paper",
                    }}
                >
                    <Table sx={{minWidth: 650}}>
                        <TableHead>
                            <TableRow
                                sx={{
                                    backgroundColor: (theme) =>
                                        theme.palette.mode === "dark"
                                            ? theme.palette.grey[900]
                                            : theme.palette.grey[100],
                                    borderBottom: (theme) => `2px solid ${theme.palette.divider}`,
                                }}
                            >
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.username")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.email")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.firstName")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.lastName")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.phone")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.address")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.roles")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.stores")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.isActive")}</TableCell>
                                <TableCell
                                    sx={{fontWeight: "bold", color: "text.primary"}}>{t("fields.actions")}</TableCell>
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {users.map((user) => (
                                <TableRow
                                    key={user.id}
                                    hover
                                    sx={{
                                        backgroundColor: "background.default",
                                        "&:hover": {backgroundColor: "action.hover"},
                                        borderBottom: (theme) => `1px solid ${theme.palette.divider}`,
                                    }}
                                >
                                    <TableCell
                                        sx={{color: "text.primary", fontSize: "0.95rem"}}>{user.username}</TableCell>
                                    <TableCell
                                        sx={{color: "text.primary", fontSize: "0.95rem"}}>{user.email}</TableCell>
                                    <TableCell
                                        sx={{color: "text.primary", fontSize: "0.95rem"}}>{user.firstName}</TableCell>
                                    <TableCell
                                        sx={{color: "text.primary", fontSize: "0.95rem"}}>{user.lastName}</TableCell>
                                    <TableCell
                                        sx={{color: "text.primary", fontSize: "0.95rem"}}>{user.phone}</TableCell>
                                    <TableCell
                                        sx={{color: "text.primary", fontSize: "0.95rem"}}>{user.address}</TableCell>
                                    <TableCell sx={{color: "text.primary", fontSize: "0.95rem"}}>
                                        {user.roles.map((r) => t(`rolesDisplay.${r.name}`)).join(", ")}
                                    </TableCell>
                                    <TableCell sx={{color: "text.primary", fontSize: "0.95rem"}}>
                                        {user.stores.map((s) => s.name).join(", ")}
                                    </TableCell>
                                    <TableCell sx={{color: "text.primary", fontSize: "0.95rem"}}>
                                        {user.isActive ? t("fields.active") : t("fields.inactive")}
                                    </TableCell>
                                    <TableCell>
                                        <IconButton
                                            onClick={() => setFormDialog({open: true, userToEdit: user})}
                                            color="primary"
                                        >
                                            <EditIcon/>
                                        </IconButton>
                                        <IconButton
                                            onClick={() => deleteUser(user.id)}
                                            color="error"
                                        >
                                            <DeleteIcon/>
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </Box>
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

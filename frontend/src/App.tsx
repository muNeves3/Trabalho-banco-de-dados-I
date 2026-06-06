import React, { useState } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import './App.css';
import Cadastro from './Cadastro';
import Home from './Home';
import Login from './Login';
import Perfil from './Perfil';
import { buscarUsuarioPorCpf, cadastrarUsuario, loginUsuario } from './api/api';

type UserSession = {
  cpf: string;
  email: string;
};

function App() {
  const [user, setUser] = useState<UserSession | null>(null);

  const handleLogin = async (cpf: string, senha: string) => {
    const usuario = await buscarUsuarioPorCpf(cpf);
    await loginUsuario({ email: usuario.email, senhaHash: senha });
    setUser({ cpf, email: usuario.email });
  };

  const handleCadastro = async (cpf: string, email: string, senha: string) => {
    await cadastrarUsuario({ cpf, email, senhaHash: senha, nome: cpf });
    setUser({ cpf, email });
  };

  const handleUpdateUser = (email: string) => {
    setUser((prev) => prev ? { ...prev, email } : null);
  };

  const handleLogout = () => {
    setUser(null);
  };

  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route
        path="/login"
        element={user ? <Navigate to="/home" replace /> : <Login onLogin={handleLogin} />}
      />
      <Route
        path="/cadastro"
        element={user ? <Navigate to="/home" replace /> : <Cadastro onCadastro={handleCadastro} />}
      />
      <Route
        path="/home"
        element={user ? <Home user={user} onLogout={handleLogout} /> : <Navigate to="/login" replace />}
      />
      <Route
        path="/perfil"
        element={user ? <Perfil user={user} onUpdateUser={handleUpdateUser} onLogout={handleLogout} /> : <Navigate to="/login" replace />}
      />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}

export default App;

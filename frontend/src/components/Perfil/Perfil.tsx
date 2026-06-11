import React, { FormEvent, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { buscarUsuarioPorCpf, atualizarUsuario } from '../../api/api';
import './Perfil.css';

type UserSession = {
  cpf: string;
  email: string;
};

type PerfilProps = {
  user: UserSession;
  onUpdateUser: (email: string) => void;
  onLogout: () => void;
};

function Perfil({ user, onUpdateUser, onLogout }: PerfilProps) {
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const usuario = await buscarUsuarioPorCpf(user.cpf);
        setNome(usuario.nome || '');
        setEmail(usuario.email || '');
      } catch {
        setErrorMessage('Erro ao carregar dados do usuário.');
      }
    };
    fetchUser();
  }, [user.cpf]);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorMessage('');
    setSuccessMessage('');
    setLoading(true);

    try {
      const payload: { nome: string; email: string; senhaHash?: string } = { nome, email };
      if (senha) {
        payload.senhaHash = senha;
      }
      await atualizarUsuario(user.cpf, payload);
      setSuccessMessage('Perfil atualizado com sucesso!');
      onUpdateUser(email);
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao atualizar perfil.';
      setErrorMessage(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="perfil-page">
      <div className="perfil-card">
        <div className="perfil-header">
          <h1>Perfil de Usuário</h1>
          <button type="button" className="perfil-link-button" onClick={() => navigate('/home')}>
            Voltar
          </button>
        </div>

        <form onSubmit={handleSubmit} className="perfil-form">
          <label htmlFor="perfil-cpf">CPF (Somente Leitura)</label>
          <input id="perfil-cpf" type="text" value={user.cpf} readOnly disabled />

          <label htmlFor="perfil-nome">Nome</label>
          <input
            id="perfil-nome"
            type="text"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />

          <label htmlFor="perfil-email">Email</label>
          <input
            id="perfil-email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label htmlFor="perfil-senha">Nova Senha (deixe em branco para não alterar)</label>
          <input
            id="perfil-senha"
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
          />

          <button type="submit" disabled={loading}>
            {loading ? 'Salvando...' : 'Salvar Alterações'}
          </button>
        </form>

        <button type="button" className="perfil-logout-button" onClick={onLogout}>
          Sair da Conta
        </button>

        {successMessage && <p className="perfil-success">{successMessage}</p>}
        {errorMessage && <p className="perfil-error">{errorMessage}</p>}
      </div>
    </div>
  );
}

export default Perfil;

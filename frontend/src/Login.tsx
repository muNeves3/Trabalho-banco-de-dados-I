import React, { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';

type LoginProps = {
  onLogin: (cpf: string, senha: string) => Promise<void>;
};

function Login({ onLogin }: LoginProps) {
  const navigate = useNavigate();
  const [cpf, setCpf] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorMessage('');
    setLoading(true);

    try {
      await onLogin(cpf, senha);
      setSenha('');
      navigate('/home');
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao fazer login.';
      setErrorMessage(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>Login</h1>
        <form onSubmit={handleSubmit} className="form">
          <label htmlFor="login-cpf">CPF</label>
          <input
            id="login-cpf"
            type="text"
            value={cpf}
            onChange={(e) => setCpf(e.target.value)}
            required
          />

          <label htmlFor="login-senha">Senha</label>
          <input
            id="login-senha"
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <button className="link-button" onClick={() => navigate('/cadastro')}>
          Não tem conta? Cadastre-se
        </button>

        {errorMessage && <p className="error">{errorMessage}</p>}
      </div>
    </div>
  );
}

export default Login;

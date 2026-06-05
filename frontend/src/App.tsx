import React, { FormEvent, useState } from 'react';
import './App.css';

type Screen = 'login' | 'register' | 'home';

type UserSession = {
  cpf: string;
  email: string;
};

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

function App() {
  const [screen, setScreen] = useState<Screen>('login');
  const [loginCpf, setLoginCpf] = useState('');
  const [loginSenha, setLoginSenha] = useState('');
  const [registerCpf, setRegisterCpf] = useState('');
  const [registerEmail, setRegisterEmail] = useState('');
  const [registerSenha, setRegisterSenha] = useState('');
  const [user, setUser] = useState<UserSession | null>(null);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleLogin = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorMessage('');
    setLoading(true);

    try {
      const userResponse = await fetch(`${API_BASE_URL}/api/usuarios/${loginCpf}`);
      if (!userResponse.ok) {
        throw new Error('CPF não encontrado.');
      }

      const userData = await userResponse.json();
      const loginResponse = await fetch(`${API_BASE_URL}/api/usuarios/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: userData.email, senhaHash: loginSenha })
      });

      if (!loginResponse.ok) {
        const backendMessage = await loginResponse.text();
        throw new Error(backendMessage || 'Falha no login.');
      }

      setUser({ cpf: loginCpf, email: userData.email });
      setScreen('home');
      setLoginSenha('');
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao fazer login.';
      setErrorMessage(message);
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorMessage('');
    setLoading(true);

    try {
      const registerResponse = await fetch(`${API_BASE_URL}/api/usuarios`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          cpf: registerCpf,
          email: registerEmail,
          senhaHash: registerSenha,
          nome: registerCpf
        })
      });

      if (!registerResponse.ok) {
        const backendMessage = await registerResponse.text();
        throw new Error(backendMessage || 'Falha no cadastro.');
      }

      setUser({ cpf: registerCpf, email: registerEmail });
      setScreen('home');
      setRegisterSenha('');
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao cadastrar.';
      setErrorMessage(message);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    setUser(null);
    setLoginCpf('');
    setLoginSenha('');
    setRegisterCpf('');
    setRegisterEmail('');
    setRegisterSenha('');
    setErrorMessage('');
    setScreen('login');
  };

  return (
    <div className="container">
      {screen === 'login' && (
        <div className="card">
          <h1>Login</h1>
          <form onSubmit={handleLogin} className="form">
            <label htmlFor="login-cpf">CPF</label>
            <input
              id="login-cpf"
              type="text"
              value={loginCpf}
              onChange={(e) => setLoginCpf(e.target.value)}
              required
            />

            <label htmlFor="login-senha">Senha</label>
            <input
              id="login-senha"
              type="password"
              value={loginSenha}
              onChange={(e) => setLoginSenha(e.target.value)}
              required
            />

            <button type="submit" disabled={loading}>
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
          </form>

          <button className="link-button" onClick={() => setScreen('register')}>
            Não tem conta? Cadastre-se
          </button>

          {errorMessage && <p className="error">{errorMessage}</p>}
        </div>
      )}

      {screen === 'register' && (
        <div className="card">
          <h1>Cadastro</h1>
          <form onSubmit={handleRegister} className="form">
            <label htmlFor="register-cpf">CPF</label>
            <input
              id="register-cpf"
              type="text"
              value={registerCpf}
              onChange={(e) => setRegisterCpf(e.target.value)}
              required
            />

            <label htmlFor="register-email">Email</label>
            <input
              id="register-email"
              type="email"
              value={registerEmail}
              onChange={(e) => setRegisterEmail(e.target.value)}
              required
            />

            <label htmlFor="register-senha">Senha</label>
            <input
              id="register-senha"
              type="password"
              value={registerSenha}
              onChange={(e) => setRegisterSenha(e.target.value)}
              required
            />

            <button type="submit" disabled={loading}>
              {loading ? 'Cadastrando...' : 'Cadastrar'}
            </button>
          </form>

          <button className="link-button" onClick={() => setScreen('login')}>
            Já tem conta? Fazer login
          </button>

          {errorMessage && <p className="error">{errorMessage}</p>}
        </div>
      )}

      {screen === 'home' && (
        <div className="card">
          <h1>Tela Inicial</h1>
          <p>Bem-vindo!</p>
          {user && (
            <p>
              CPF: {user.cpf}
              <br />
              Email: {user.email}
            </p>
          )}
          <button onClick={handleLogout}>Sair</button>
        </div>
      )}
    </div>
  );
}

export default App;

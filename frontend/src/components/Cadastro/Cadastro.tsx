import React, { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Cadastro.css';

type CadastroProps = {
  onCadastro: (cpf: string, email: string, nome: string, senha: string) => Promise<void>;
};

function Cadastro({ onCadastro }: CadastroProps) {
  const navigate = useNavigate();
  const [cpf, setCpf] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [nome, setNome] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setErrorMessage('');
    setLoading(true);

    try {
      await onCadastro(cpf, email, nome, senha);
      setSenha('');
      navigate('/home');
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao cadastrar.';
      setErrorMessage(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="cadastro-page">
      <div className="cadastro-card">
        <h1>Cadastro</h1>
        <form onSubmit={handleSubmit} className="cadastro-form">
          <label htmlFor="register-cpf">CPF</label>
          <input
            id="register-cpf"
            type="text"
            value={cpf}
            onChange={(e) => setCpf(e.target.value)}
            required
          />

          <label htmlFor="register-email">Email</label>
          <input
            id="register-email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label htmlFor="register-nome">Nome</label>
          <input
            id="register-nome"
            type="text"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />  

          <label htmlFor="register-senha">Senha</label>
          <input
            id="register-senha"
            type="password"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? 'Cadastrando...' : 'Cadastrar'}
          </button>
        </form>

        <button type="button" className="cadastro-link-button" onClick={() => navigate('/login')}>
          Já tem conta? Fazer login
        </button>

        {errorMessage && <p className="cadastro-error">{errorMessage}</p>}
      </div>
    </div>
  );
}

export default Cadastro;

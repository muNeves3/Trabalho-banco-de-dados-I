import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { cadastrarDataset, DatasetPayload } from './api/api';

interface CadastroDatasetProps {
  cpfCriador: string;
}

export default function CadastroDataset({ cpfCriador }: CadastroDatasetProps) {
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  const [descricao, setDescricao] = useState('');
  const [fontes, setFontes] = useState('');
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState('');
  const [isError, setIsError] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMensagem('');
    setIsError(false);
    setLoading(true);

    const payload: DatasetPayload = {
      nome,
      descricao,
      criadorCpf: cpfCriador,
    };

    try {
      await cadastrarDataset(payload);
      setMensagem('Dataset criado com sucesso!');
      setNome('');
      setDescricao('');
    } catch (error: any) {
      setMensagem(error.message || 'Erro ao criar o Dataset');
      setIsError(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <h1>Novo Dataset</h1>
          <button type="button" className="link-button" onClick={() => navigate('/home')} style={{ marginTop: 0 }}>
            Voltar
          </button>
        </div>
        
        <form onSubmit={handleSubmit} className="form">
          <label htmlFor="dataset-nome">Nome do Dataset</label>
          <input 
            id="dataset-nome"
            type="text" 
            value={nome} 
            onChange={(e) => setNome(e.target.value)} 
            required 
          />

          <label htmlFor="dataset-descricao">Descrição</label>
          <input 
            id="dataset-descricao"
            type="text" 
            value={descricao} 
            onChange={(e) => setDescricao(e.target.value)} 
            required 
          />


          <button type="submit" disabled={loading}>
            {loading ? 'Salvando...' : 'Salvar Dataset'}
          </button>
        </form>

        {mensagem && (
          <p className={isError ? "error" : ""} style={!isError ? { color: 'green', marginTop: '12px', fontSize: '14px' } : {}}>
            {mensagem}
          </p>
        )}
      </div>
    </div>
  );
}
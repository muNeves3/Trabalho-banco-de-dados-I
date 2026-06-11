import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { cadastrarDataset, DatasetPayload } from '../../api/api';
import './CadastroDataset.css';

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
      fontes,
      criadorCpf: cpfCriador,
    };

    try {
      await cadastrarDataset(payload);
      setMensagem('Dataset criado com sucesso!');
      setNome('');
      setDescricao('');
      setFontes('');
    } catch (error) {
      const message = error instanceof Error ? error.message : 'Erro ao criar o Dataset';
      setMensagem(message);
      setIsError(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="dataset-page">
      <div className="dataset-card">
        <div className="dataset-header">
          <h1>Novo Dataset</h1>
          <button type="button" className="dataset-link-button" onClick={() => navigate('/home')}>
            Voltar
          </button>
        </div>

        <form onSubmit={handleSubmit} className="dataset-form">
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

          <label htmlFor="dataset-fontes">Fontes</label>
          <input
            id="dataset-fontes"
            type="text"
            value={fontes}
            onChange={(e) => setFontes(e.target.value)}
          />

          <button type="submit" disabled={loading}>
            {loading ? 'Salvando...' : 'Salvar Dataset'}
          </button>
        </form>

        {mensagem && <p className={isError ? 'dataset-error' : 'dataset-success'}>{mensagem}</p>}
      </div>
    </div>
  );
}

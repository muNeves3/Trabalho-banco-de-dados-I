import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { cadastrarDataset, adicionarFonte, DatasetPayload } from '../../api/api';
import './CadastroDataset.css';

interface CadastroDatasetProps {
  cpfCriador: string;
}

export default function CadastroDataset({ cpfCriador }: CadastroDatasetProps) {
  const navigate = useNavigate();
  const [nome, setNome] = useState('');
  const [descricao, setDescricao] = useState('');
  const [fontes, setFontes] = useState<string[]>([]);
  const [novaFonte, setNovaFonte] = useState('');
  const [loading, setLoading] = useState(false);
  const [mensagem, setMensagem] = useState('');
  const [isError, setIsError] = useState(false);

  const handleAdicionarFonte = () => {
    if (novaFonte.trim() && !fontes.includes(novaFonte.trim())) {
      setFontes([...fontes, novaFonte.trim()]);
      setNovaFonte('');
    }
  };

  const handleRemoverFonte = (fonte: string) => {
    setFontes(fontes.filter(f => f !== fonte));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMensagem('');
    setIsError(false);
    setLoading(true);

    try {
      const resultado = await cadastrarDataset({
        nome,
        descricao,
        criadorCpf: cpfCriador,
      });

      for (const fonte of fontes) {
        await adicionarFonte(resultado.id, fonte);
      }

      setMensagem(resultado.mensagem || 'Dataset criado com sucesso!');
      setNome('');
      setDescricao('');
      setFontes([]);
      setNovaFonte('');
    } catch (error: any) {
      setMensagem(error.message || 'Erro ao criar o Dataset');
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
          <button type="button" className="detalhe-btn-nova-versao" onClick={() => navigate('/home')}>
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

          <label>Fontes</label>
          <div style={{ display: 'flex', gap: '8px' }}>
            <input
              type="text"
              value={novaFonte}
              onChange={(e) => setNovaFonte(e.target.value)}
              placeholder="Nome da fonte"
              style={{ flex: 1 }}
            />
            <button type="button" onClick={handleAdicionarFonte} disabled={!novaFonte.trim()}>
              +
            </button>
          </div>
          {fontes.length > 0 && (
            <ul style={{ margin: '4px 0', paddingLeft: '20px', fontSize: '14px' }}>
              {fontes.map((f) => (
                <li key={f} style={{ marginBottom: '4px' }}>
                  {f}
                  <button
                    type="button"
                    className="dataset-link-button"
                    onClick={() => handleRemoverFonte(f)}
                    style={{ marginLeft: '8px', color: 'red', fontSize: '12px' }}
                  >
                    remover
                  </button>
                </li>
              ))}
            </ul>
          )}

          <button type="submit" disabled={loading}>
            {loading ? 'Salvando...' : 'Salvar Dataset'}
          </button>
        </form>

        {mensagem && (
          <p className={isError ? 'dataset-error' : 'dataset-success'}>
            {mensagem}
          </p>
        )}
      </div>
    </div>
  );
}
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { criarVersao, listarVersoes, VersaoDataset } from '../../api/api';
import './CadastroVersao.css';

type UserSession = {
  cpf: string;
  email: string;
};

type CadastroVersaoProps = {
  user: UserSession;
};

export default function CadastroVersao({ user }: CadastroVersaoProps) {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const datasetId = Number(id);

  const [versoes, setVersoes] = useState<VersaoDataset[]>([]);
  const [versaoBase, setVersaoBase] = useState<string>('');
  const [descModificacoes, setDescModificacoes] = useState('');
  const [arquivo, setArquivo] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [loadingVersoes, setLoadingVersoes] = useState(true);
  const [mensagem, setMensagem] = useState('');
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    const carregar = async () => {
      try {
        const vs = await listarVersoes(datasetId);
        setVersoes(vs);
      } catch (error) {
        setVersoes([]);
      } finally {
        setLoadingVersoes(false);
      }
    };
    carregar();
  }, [datasetId]);

  const proximaVersao = versoes.length > 0
    ? Math.max(...versoes.map(v => v.numeroVersao)) + 1
    : 1;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!arquivo) return;

    setMensagem('');
    setIsError(false);
    setLoading(true);

    try {
      const base = versaoBase ? Number(versaoBase) : undefined;
      await criarVersao(datasetId, proximaVersao, user.cpf, descModificacoes, arquivo, base);
      setMensagem('Versão criada com sucesso!');
      setDescModificacoes('');
      setArquivo(null);
      setVersaoBase('');

      const inputFile = document.getElementById('versao-arquivo') as HTMLInputElement;
      if (inputFile) inputFile.value = '';
    } catch (error) {
      const msg = error instanceof Error ? error.message : 'Erro ao criar versão.';
      setMensagem(msg);
      setIsError(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="versao-page">
      <div className="versao-card">
        <div className="versao-header">
          <h1>Nova Versão</h1>
          <button type="button" className="versao-link-button" onClick={() => navigate(`/datasets/${datasetId}`)}>
            Voltar
          </button>
        </div>

        <p className="versao-info">Dataset #{datasetId} — Versão {proximaVersao}</p>

        {loadingVersoes ? (
          <p>Carregando...</p>
        ) : (
          <form onSubmit={handleSubmit} className="versao-form">
            {versoes.length > 0 && (
              <>
                <label htmlFor="versao-base">Versão base</label>
                <select
                  id="versao-base"
                  value={versaoBase}
                  onChange={(e) => setVersaoBase(e.target.value)}
                >
                  <option value="">Nenhuma (versão original)</option>
                  {versoes.map((v) => (
                    <option key={v.numeroVersao} value={v.numeroVersao}>
                      Versão {v.numeroVersao}
                    </option>
                  ))}
                </select>
              </>
            )}

            <label htmlFor="versao-desc">Descrição das modificações</label>
            <textarea
              id="versao-desc"
              value={descModificacoes}
              onChange={(e) => setDescModificacoes(e.target.value)}
              rows={3}
              required
            />

            <label htmlFor="versao-arquivo">Arquivo CSV</label>
            <input
              id="versao-arquivo"
              type="file"
              accept=".csv"
              onChange={(e) => setArquivo(e.target.files?.[0] || null)}
              required
            />

            <button type="submit" disabled={loading || !arquivo}>
              {loading ? 'Enviando...' : 'Criar Versão'}
            </button>
          </form>
        )}

        {mensagem && <p className={isError ? 'versao-error' : 'versao-success'}>{mensagem}</p>}
      </div>
    </div>
  );
}